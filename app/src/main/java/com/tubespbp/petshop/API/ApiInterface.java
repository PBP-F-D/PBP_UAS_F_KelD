package com.tubespbp.petshop.API;

import com.tubespbp.petshop.API.User.UserResponse;
import com.tubespbp.petshop.ui.profile.model.User;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiInterface {
    @GET("detailuser")
    Call<UserResponse> getUser(@Header("Authorization") String authToken);

    @FormUrlEncoded
    @POST("login")
    Call<UserResponse> login(@Field("email") String email,
                             @Field("password") String password);

    @POST("register")
    @FormUrlEncoded
    Call<UserResponse> register(@Field("name") String name,
                                @Field("email") String email,
                                @Field("password") String password,
                                @Field("country") String country,
                                @Field("city") String city,
                                @Field("phone") String phone,
                                @Field("img_user") String photo);

    @GET("authToken")
    Call<UserResponse> getToken(@Header("Authorization") String authToken);

    @GET("logout")
    Call<UserResponse> logout(@Header("Authorization") String authToken);

    @POST("editUser/{id}")
    @FormUrlEncoded
    Call<UserResponse> updateUser(@Path("id") String id,
                                  @Field("name") String name,
                                  @Field("email") String email,
                                  @Field("country") String country,
                                  @Field("city") String city,
                                  @Field("phone") String phone,
                                  @Field("img_user") String img_user,
                                  @Header("Authorization") String authToken);
}
