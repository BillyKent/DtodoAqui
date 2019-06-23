package com.miedo.dtodoaqui.model;

import com.miedo.dtodoaqui.data.remote.DeTodoAquiAPI;
import com.miedo.dtodoaqui.data.remote.ServiceGenerator;

import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LocationsModel {

    public void getLocations(Callback<Map<Integer, String>> callback) {

        DeTodoAquiAPI api = ServiceGenerator.createServiceScalar(DeTodoAquiAPI.class);
        Call<ResponseBody> call = api.getLocations();

        call.enqueue(new retrofit2.Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });

    }

    private Map<Integer, String> fetchResponse() {
        HashMap<Integer, String> retorno = null;


        return retorno;
    }


    public interface Callback<T> {
        public void onResult(T arg);

        public void onFailure();
    }
}
