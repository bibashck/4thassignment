package com.example.bibashkconlineshoppingapp.Interface;

import com.example.bibashkconlineshoppingapp.models.User;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface User_Interface {
    @POST("users/login")
    Call<ResponseBody> userLogin(@Body User user);


    @POST("users/signup")
    Call<ResponseBody> userSignup(@Body User user);
}
