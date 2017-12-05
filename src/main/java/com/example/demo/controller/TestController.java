package com.example.demo.controller;

import com.example.demo.annotation.HttpService;
import com.example.demo.api.Api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by OnionMac on 2017/12/4.
 */
@RestController
@RequestMapping("test")
public class TestController {

    @HttpService
    Api api;

    @RequestMapping("/test1")
    public void get(){
        Call<String> city = api.getCity();

        city.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                System.out.println("zhangqi11"+response.body()+response.isSuccessful());

            }

            @Override
            public void onFailure(Call<String> call, Throwable throwable) {
                System.out.println("zhangqi");
            }
        });

    }
}
