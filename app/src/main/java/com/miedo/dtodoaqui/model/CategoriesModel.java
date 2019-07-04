package com.miedo.dtodoaqui.model;

import com.miedo.dtodoaqui.data.EstablishmentSearchTO;
import com.miedo.dtodoaqui.data.remote.DeTodoAquiAPI;
import com.miedo.dtodoaqui.data.remote.ServiceGenerator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoriesModel {

    public void GetCategories(Callback<Map<Integer, String>> callback) {
        // Creamos la instancia de la api
        DeTodoAquiAPI api = ServiceGenerator.createServiceScalar(DeTodoAquiAPI.class);

        // Obtenemos las categorias
        Call<ResponseBody> callCategories = api.getCategories();

        callCategories.enqueue(new retrofit2.Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                callback.onResult(fetchCategoriesResponse(response.body()));
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                callback.onFailure();
            }
        });
    }

    private Map<Integer, String> fetchCategoriesResponse(ResponseBody response) {
        Map<Integer, String> categories = null;
        try {
            JSONObject data = new JSONObject(response.string());
            JSONArray categoriesArray = data.getJSONArray("data");

            if (categoriesArray.length() > 0) {
                categories = new HashMap();

                for (int i = 0; i < categoriesArray.length(); i++) {
                    JSONObject categoryJsonObject = (JSONObject) categoriesArray.get(i);

                    String categoria = categoryJsonObject.getString("name");
                    categoria = categoria.substring(0, 1).toUpperCase() + categoria.substring(1);
                    if (!categories.containsKey(categoryJsonObject.getInt("id"))) {
                        categories.put(categoryJsonObject.getInt("id"), categoria);
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return categories;
    }

    public interface Callback<T> {
        public void onResult(T arg);

        public void onFailure();
    }
}
