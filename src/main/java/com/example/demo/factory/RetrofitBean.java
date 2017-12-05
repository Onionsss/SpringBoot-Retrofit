package com.example.demo.factory;

import retrofit2.Retrofit;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by OnionMac on 2017/12/4.
 */
public class RetrofitBean {
    private Retrofit retrofit;
    private Map<Class,Object> service = new HashMap<>();

    public Retrofit getRetrofit() {
        return retrofit;
    }

    public void setRetrofit(Retrofit retrofit) {
        this.retrofit = retrofit;
    }

    public Map<Class, Object> getService() {
        return service;
    }

    public void setService(Map<Class, Object> service) {
        this.service = service;
    }
}

