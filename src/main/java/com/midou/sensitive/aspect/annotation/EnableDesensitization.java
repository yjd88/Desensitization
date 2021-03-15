package com.midou.sensitive.aspect.annotation;

import java.lang.annotation.*;

/**
 * @author: Yang Jundong
 * @date: 2021/3/12 0012 10:27
 * @description: 在需要脱敏操作的方法上配置此注解;返回值拦截器,对返回结果进行脱敏操作
 */

@Target({ElementType.METHOD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface EnableDesensitization {
}
