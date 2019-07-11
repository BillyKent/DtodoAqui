package com.miedo.dtodoaqui.model;

import com.miedo.dtodoaqui.data.remote.DeTodoAquiAPI;
import com.miedo.dtodoaqui.data.remote.ServiceGenerator;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileDetailModel {

    public void getDetailById(int id, CallBack<ArrayList<Integer>> callBack) {
        DeTodoAquiAPI api = ServiceGenerator.createServiceScalar(DeTodoAquiAPI.class);

        Call<ResponseBody> call = api.getProfileDetailById(id);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 200) {
                    callBack.onResult(fetchResponse(response.body()));
                } else {
                    callBack.onError();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                callBack.onError();
            }
        });

    }

    ArrayList<Integer> fetchResponse(ResponseBody responseBody) {
        try {
            JSONObject object = new JSONObject(responseBody.string());

            ArrayList<Integer> retorno = new ArrayList<>();
            retorno.add(object.getInt("listings"));
            retorno.add(object.getInt("ratings"));
            retorno.add(object.getInt("reviews"));


            return retorno;
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;

    }


    public interface CallBack<T> {

        void onResult(T t);

        void onError();
    }

}
