package com.example.demo.factory;

import com.example.demo.interceptor.LoggingInterceptor;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import org.springframework.beans.BeansException;
import org.springframework.util.StringUtils;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by OnionMac on 2017/12/4.
 */
public class RetrofitBeanFactory {
    //key:请求地址 value:当前请求地址下class所对应的service（key:class value:service）
    private static Map<String, RetrofitBean> resolvableDependencies = new HashMap(16);
    private static final int readTimeOut = 15;
    private static final int writeTimeOut = 15;
    private static final int connTimeOut = 15;

    /**
     * 获得service服务实体
     *
     * @param requiredType
     * @return
     * @throws BeansException
     */
    public static Object getBean(Class requiredType) throws BeansException {
        for (Map.Entry<String, RetrofitBean> entrySet : resolvableDependencies.entrySet()) {
            RetrofitBean retrofitBean = entrySet.getValue();
            for (Map.Entry<Class, Object> serviceSet : retrofitBean.getService().entrySet()) {
                if (requiredType == serviceSet.getKey()) {
                    return serviceSet.getValue();
                }
            }
        }
        return null;
    }

    /**
     * 创建service服务实体
     *
     * @param baseUrl
     * @param serviceClass
     */
    public static Object putBean(String baseUrl,int[] timeout, Class serviceClass, Class... interceptorClass) {
        if (StringUtils.isEmpty(baseUrl)) {
            return null;
        }
        RetrofitBean retrofitBean = resolvableDependencies.get(baseUrl);
        //如果为空设置一个
        if (retrofitBean == null) {
            retrofitBean = new RetrofitBean();
            OkHttpClient.Builder clientBuilder = new OkHttpClient().newBuilder()
                    .connectTimeout(timeout[0], TimeUnit.SECONDS)
                    .writeTimeout(timeout[1], TimeUnit.SECONDS)
                    .readTimeout(timeout[2], TimeUnit.SECONDS)
                    .addInterceptor(new LoggingInterceptor());
            if (interceptorClass != null && interceptorClass.length > 0) {
                for (Class clazz : interceptorClass) {
                    if (Interceptor.class.isAssignableFrom(clazz)) {
                        try {
                            clientBuilder.addInterceptor((Interceptor) clazz.newInstance());
                        } catch (InstantiationException e) {
                            e.printStackTrace();
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .client(clientBuilder.build())
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            retrofitBean.setRetrofit(retrofit);
            resolvableDependencies.put(baseUrl, retrofitBean);
        }
        Retrofit retrofit = retrofitBean.getRetrofit();
        Object bean = retrofit.create(serviceClass);
        retrofitBean.getService().put(serviceClass, bean);
        return bean;
    }

    public static Map<String, RetrofitBean> getResolvableDependencies() {
        return resolvableDependencies;
    }
}
