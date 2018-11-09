package com.he.android_1.service;

import com.he.android_1.model.Result;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface LoginService {
    /**
     * 登录
     * @return
     */
//    @Headers({"Content-Type: application/json","Accept: application/json"})//需要添加头
//    @FormUrlEncoded
    @POST("userinfo/signIn")
    Call<Result> login(@Body RequestBody body);
}
