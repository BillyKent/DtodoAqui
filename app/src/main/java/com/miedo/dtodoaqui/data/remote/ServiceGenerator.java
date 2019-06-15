package com.miedo.dtodoaqui.data.remote;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class ServiceGenerator {
    private static final String API_URL_BASE = "http://35.226.8.87/";

    private static final OkHttpClient httpClient = new OkHttpClient.Builder()
            .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .connectTimeout(5, TimeUnit.SECONDS)
            .writeTimeout(5, TimeUnit.SECONDS)
            .readTimeout(5, TimeUnit.SECONDS)
            .build();


    // --------------------------------  Servicio con Gson ------------------------------------------
    private static final Retrofit.Builder builderGson = new Retrofit.Builder()
            .client(httpClient)
            .baseUrl(API_URL_BASE)
            .addConverterFactory(GsonConverterFactory.create());

    private static final Retrofit retrofitGson = builderGson.build();

    public static <S> S createServiceGson(Class<S> serviceClass) {
        return retrofitGson.create(serviceClass);
    }


    // ------------------------------- Servicio con Scalars ------------------------------------------
    private static final Retrofit.Builder builderScalar = new Retrofit.Builder()
            .client(httpClient)
            .baseUrl(API_URL_BASE)
            .addConverterFactory(ScalarsConverterFactory.create());

    private static final Retrofit retrofitScalar = builderScalar.build();

    public static <S> S createServiceScalar(Class<S> serviceClass) {
        return retrofitScalar.create(serviceClass);
    }


}