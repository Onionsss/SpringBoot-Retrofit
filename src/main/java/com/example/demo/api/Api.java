package com.example.demo.api;

import com.example.demo.annotation.HttpApi;
import com.example.demo.interceptor.LoggingInterceptor;
import retrofit2.Call;
import retrofit2.http.POST;

/**
 * Created by OnionMac on 2017/12/4.
 */
@HttpApi(interceptor = LoggingInterceptor.class
        ,timeOut = {100,100,100})
public interface Api {
    @POST("user/getCode.action")
    Call<String> getCity();
}
