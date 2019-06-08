package com.miedo.dtodoaqui.data.remote;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface DeTodoAquiAPI {

    // Login
    @POST("/api/sign_in")
    public Call<ResponseBody> loginUser(@Body RequestBody body);

    // Registro usuario
    @POST("/api/users")
    public Call<ResponseBody> registerUser(@Body RequestBody body);

    // Buscar establecimientos
    @GET("/api/search")
    public Call<ResponseBody> searchEstablishments(@Query("keyword") String keyword, @Query("location") String location, @Query("category") String category);

    //  Obtener profile
    @GET("/api/my_profile")
    public Call<ResponseBody> getProfile(@Header("Authorization") String bearer);
}
