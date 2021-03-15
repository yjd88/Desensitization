package com.midou.sensitive.common.utils;

import com.midou.sensitive.aspect.annotation.SensitiveValid;
import com.midou.sensitive.aspect.type.DesensitizeEnum;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.ReflectionUtils;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Map;

/**
 * @author: Yang Jundong
 * @date: 2021/3/12 0012 9:25
 * @description: 脱敏工具
 */
public class DesensitizationUtils {

    /**
     * 脱敏具体实现 通过common-lang3中的字符串处理实现;
     * @param sensitiveField 脱敏字段
     * @param sensitiveValid 脱敏注解
     * @return
     */
    public static String desensitization(String sensitiveField,SensitiveValid sensitiveValid){
        /* sensitiveField为空 则不进行处理 */
        if(StringUtils.isEmpty(sensitiveField)){
            return sensitiveField;
        }
        //获取sensitiveValid注解中的前面保留位数、末尾保留位数、脱敏使用的符号
        int prefix = sensitiveValid.prefix();
        int suffix = sensitiveValid.suffix();
        String symbol = sensitiveValid.symbol();
        String separator = sensitiveValid.separator();
        DesensitizeEnum type = sensitiveValid.value();
        String result = null;
        if (prefix<0){
            prefix = type.getRetainLeft();
        }
        if (suffix <0){
            suffix = type.getRetainRight();
        }
        if (StringUtils.isBlank(separator)){
            separator=type.getSeparator();
        }
        //处理普通方式
        if (StringUtils.isBlank(separator)){
            result = StringUtils.left(sensitiveField, prefix).concat(StringUtils.leftPad(StringUtils.right(sensitiveField, suffix), StringUtils.length(sensitiveField) - prefix, symbol));
            //处理带分隔符方式
        }else {
            String[] split = sensitiveField.split(separator);
            String leftStr = StringUtils.left(split[0], prefix);
            String rightStr = StringUtils.right(split[split.length-1], suffix);
            split[0] = StringUtils.rightPad(leftStr,StringUtils.length(split[0]),symbol);
            split[split.length-1] = StringUtils.leftPad(rightStr, StringUtils.length(split[split.length-1]),symbol);
            result = StringUtils.join(split, separator);

        }
        return result;
    }

    /**
     * 扫描对象属性,根据注解脱敏
     * 注意：对象中嵌套的对象不要太多,太深
     * @param obj 需要扫描对象
     */
    public static void format(Object obj) {
        DesensitizationUtils.formatMethod(obj);
    }

    /**
     * 递归遍历数据，找到最基本的对象
     * @param obj   需要反射对象
     */
    private static void formatMethod(Object obj) {
        if (obj == null || isPrimitive(obj.getClass())) { //判断是否是空或者是基本数据类型/String类型
            return;
        }
        if (obj.getClass().isArray()) { //判断是否是一个数组集合,如果是集合循环遍历
            for (Object object : (Object[]) obj) {
                formatMethod(object);
            }
        } else if (Collection.class.isAssignableFrom(obj.getClass())) {//判断是否是一个Collection集合,如果是集合循环遍历
            for (Object object : ((Collection) obj)) {
                formatMethod(object);
            }
        } else if (Map.class.isAssignableFrom(obj.getClass())) {//判断是否是一个Map集合,如果是Map集合循环遍历
            for (Object object : ((Map) obj).values()) {
                formatMethod(object);
            }
        } else {//如果是一个最基本的对象,进行反射格式化数据
            objFormat(obj);
        }
    }

    /**
     * 只有对象才格式化数据
     * @param obj
     */
    private static void objFormat(Object obj) {
        for (Field field : obj.getClass().getDeclaredFields()) {
            try {
                //ReflectionUtils是Spring中的反射工具,此方法将修改字段的private属性,让字段变为可以修改属性;主要是修改Field中的override属性为true
                ReflectionUtils.makeAccessible(field);
                if (isPrimitive(field.getType())) {
                    //获取字段的SensitiveValid注解属性(只要标注此属性的字段才进行脱敏)
                    SensitiveValid sensitiveValid = field.getAnnotation(SensitiveValid.class);
                    if (sensitiveValid != null && String.class.isAssignableFrom(field.getType())) {
                        //调用desensitization()方法进行脱敏操作
                        Object fieldValue = desensitization(String.valueOf(field.get(obj)),sensitiveValid);
                        //将脱敏后的数据在保存到对象的属性中,此时前面必须将字段属性设置为可修改属性 ReflectionUtils.makeAccessible(field);
                        ReflectionUtils.setField(field, obj, fieldValue);
                    }
                } else {
                    Object fieldValue = ReflectionUtils.getField(field, obj);
                    if (fieldValue == null) {
                        continue;
                    }
                    //如果field还是一个对象,那就需要formatMethod()方法继续处理
                    formatMethod(fieldValue);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 基本数据类型和String类型判断
     *
     * @param clz
     * @return
     */
    public static boolean isPrimitive(Class<?> clz) {
        /**
         * 1.源类.class.isAssignableFrom(目标类、子类或接口实现类.class)
         *  此方法主要用来判断 “参数类“ 是否是 ”源类“ 的子类、接口实现类，或者与 “源类” 相同，在此情况下返回 true
         * 2.Class.isPrimitive()
         *  此方法主要用来判断Class是否为原始类型（boolean、char、byte、short、int、long、float、double）
         */
        try {
            if (String.class.isAssignableFrom(clz) || clz.isPrimitive()) {
                return true;
            } else {
                return ((Class) clz.getField("TYPE").get(null)).isPrimitive();
            }
        } catch (Exception e) {
            return false;
        }
    }
}
