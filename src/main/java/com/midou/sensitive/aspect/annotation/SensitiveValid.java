package com.midou.sensitive.aspect.annotation;

import com.midou.sensitive.aspect.type.DesensitizeEnum;

import java.lang.annotation.*;

/**
 * @author: Yang Jundong
 * @date: 2021/3/12 0012 9:29
 * @description: JavaBean属性定义注解类,字段需要脱敏操作,只需要添加此注解
 */

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SensitiveValid {
    /** 设置脱敏类型,可以省略其他的属性 */
    DesensitizeEnum value() default DesensitizeEnum.BASIC;
    /** 字段脱敏左侧保留位数 */
    int prefix() default -1;
    /** 字段脱敏右侧保留位数 */
    int suffix() default -1;
    /** 特殊分隔符 */
    String separator() default "";
    /** 字段脱敏使用的符号 */
    String symbol() default "*";
}
