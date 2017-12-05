package com.example.demo.annotation;

import com.example.demo.interceptor.LoggingInterceptor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by OnionMac on 2017/12/4.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface HttpApi {
    String value() default "";//通过key获得配置文件中的值
    int[] timeOut() default {50,50,50};
    Class[] interceptor() default {LoggingInterceptor.class};
}
