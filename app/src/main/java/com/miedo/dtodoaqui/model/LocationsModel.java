package com.miedo.dtodoaqui.model;

import com.miedo.dtodoaqui.data.remote.DeTodoAquiAPI;
import com.miedo.dtodoaqui.data.remote.ServiceGenerator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LocationsModel {

    public void getLocations(Callback<HashMap<String, Integer>> callback) {

        DeTodoAquiAPI api = ServiceGenerator.createServiceScalar(DeTodoAquiAPI.class);
        Call<ResponseBody> call = api.getLocations();

        call.enqueue(new retrofit2.Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if (response.code() == 200) {
                    HashMap<String, Integer> rpta = fetchResponse(response.body());
                    callback.onResult(rpta);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                callback.onFailure();
            }
        });

    }

    private HashMap<String, Integer> fetchResponse(ResponseBody responseBody) {
        HashMap<String, Integer> retorno = new HashMap<>();

        try {
            JSONArray data = new JSONObject(responseBody.string()).getJSONArray("data");
            for (int i = 0; i < data.length(); i++) {

                retorno.put(
                        data.getJSONObject(i).getString("name"),
                        data.getJSONObject(i).getInt("user_id")
                );
            }

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return retorno;
    }


    public interface Callback<T> {
        public void onResult(T arg);

        public void onFailure();
    }
}
