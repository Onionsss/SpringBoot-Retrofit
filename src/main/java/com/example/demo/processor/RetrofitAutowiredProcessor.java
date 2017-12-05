package com.example.demo.processor;

import com.example.demo.annotation.HttpApi;
import com.example.demo.annotation.HttpService;
import com.example.demo.config.BaseConfig;
import com.example.demo.factory.RetrofitBeanFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessorAdapter;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;

/**
 * Created by OnionMac on 2017/12/4.
 */
@Component
@EnableConfigurationProperties
public class RetrofitAutowiredProcessor extends InstantiationAwareBeanPostProcessorAdapter {

    private Logger logger = LoggerFactory.getLogger(RetrofitAutowiredProcessor.class);

    @Autowired
    BaseConfig baseConfig;

    @Override
    public boolean postProcessAfterInstantiation(final Object bean, final String beanName) throws BeansException {
        ReflectionUtils.doWithFields(bean.getClass(), new ReflectionUtils.FieldCallback() {
            @Override
            public void doWith(Field field) throws IllegalArgumentException, IllegalAccessException {
                HttpService httpApi = field.getAnnotation(HttpService.class);
                if (httpApi == null) {
                    return;
                }
                createRetrofitService(bean, field, field.getType());
            }
        });
        return super.postProcessAfterInstantiation(bean, beanName);
    }

    private void createRetrofitService(Object bean, Field field, Class clazz) throws IllegalAccessException {
        //读取注解中的值
        HttpApi httpApi = (HttpApi) clazz.getAnnotation(HttpApi.class);
        String key = httpApi.value();
        if (key.equals("")) {
            return;
        }
        //根据注解的key获得配置文件中的url
        //Object value = propertyConfigurer.getProperty(key);
        //if (value == null) {
         //   return;
        //}
        //根据地址创建retrofit
        int[] timeout = httpApi.timeOut();
        logger.info(baseConfig.getBaseUrl());
        Object object = RetrofitBeanFactory.putBean(baseConfig.getBaseUrl(),timeout, clazz,httpApi.interceptor());
        if (object == null) {
            return;
        }
        field.setAccessible(true);
        field.set(bean, object);
    }
}
