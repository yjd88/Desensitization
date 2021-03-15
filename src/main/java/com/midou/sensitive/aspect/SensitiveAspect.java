package com.midou.sensitive.aspect;

import com.midou.sensitive.common.utils.DesensitizationUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

/**
 * @author: Yang Jundong
 * @date: 2021/3/12 0012 14:20
 * @description: 脱敏切面的实现
 */
@Aspect
@Component
public class SensitiveAspect {
    /**
     * 注解脱敏处理
     *   说明:
     *      @annotation(com.midou.sensitive.aspect.annotation.EnableDesensitization): 在方法上使用次注解,实现切面编程
     *          com.midou.sensitive.aspect.annotation. :表示注解的路径
     *      @within(com.midou.sensitive.aspect.annotation.EnableDesensitization) 在JavaBean中使用此注解,实现切面编程
     * @param joinPoint
     * @return
     * @throws Throwable
     */
    @Around("@annotation(com.midou.sensitive.aspect.annotation.EnableDesensitization) || @within(com.midou.sensitive.aspect.annotation.EnableDesensitization)")
    public Object sensitiveClass(ProceedingJoinPoint joinPoint) throws Throwable {
        return sensitiveFormat(joinPoint);
    }

    /**
     * 插拔式注解统一拦截器。link @EnableDesensitization 和 @SensitiveValid
     *
     * @param joinPoint
     * @return
     * @throws Throwable
     */
    public Object sensitiveFormat(ProceedingJoinPoint joinPoint) throws Throwable {
        Object obj = joinPoint.proceed();
        if (obj == null || DesensitizationUtils.isPrimitive(obj.getClass())) {
            return obj;
        }
        //使用脱敏工具类进行脱敏操作
        DesensitizationUtils.format(obj);
        return obj;
    }
}
