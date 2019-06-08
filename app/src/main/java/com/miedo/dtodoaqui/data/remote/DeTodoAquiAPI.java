package com.miedo.dtodoaqui.data.remote;

import com.google.android.gms.common.util.JsonUtils;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface DeTodoAquiAPI {

    // Login
    @POST("/api/sign_in")
    public Call<ResponseBody> loginUser(@Body RequestBody body);

    // Registro usuario
    @POST("/api/users")
    public Call<ResponseBody> registerUser(@Body RequestBody body);






}
