package com.example.bibashkconlineshoppingapp.Interface;

import com.example.bibashkconlineshoppingapp.models.Image;
import com.example.bibashkconlineshoppingapp.models.Item;

import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ItemInterface {

    @Multipart
    @POST("upload")
    Call<Image> uploadImage(@Part MultipartBody.Part body);

    @POST("items")
    Call<Void> addItem(@Body Item Body);

    @GET("items")
    Call<List<Item>> getAllItems();
}
