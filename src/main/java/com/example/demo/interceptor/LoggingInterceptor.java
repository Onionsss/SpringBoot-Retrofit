package com.example.demo.interceptor;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Created by OnionMac on 2017/12/4.
 */
public class LoggingInterceptor implements Interceptor {

    private static Logger logger = LoggerFactory.getLogger(LoggingInterceptor.class);

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        logger.info(String.format("发送请求：%s%n请求头：%s",
                request.url(), request.headers()));
        Response response = chain.proceed(request);
        return response;
    }
}