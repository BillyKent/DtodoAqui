package com.miedo.dtodoaqui.model;

import androidx.lifecycle.MutableLiveData;

import com.miedo.dtodoaqui.data.EstablishmentSearchTO;
import com.miedo.dtodoaqui.data.remote.DeTodoAquiAPI;
import com.miedo.dtodoaqui.data.remote.ServiceGenerator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EstablishmentsSearchModel {


    public void SearchEstablishments(String keyword, String location, String category, MutableLiveData<List<EstablishmentSearchTO>> data){

        // Creamos la instancia de la api
        DeTodoAquiAPI api = ServiceGenerator.createServiceScalar(DeTodoAquiAPI.class);
        // Realizamos la peticion asincrona
        Call<ResponseBody> call = api.searchEstablishments(keyword,location,category);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    List<EstablishmentSearchTO> establishments = fetchSearchResponse(response.body());
                    data.setValue(establishments);
                } else {
                    data.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                data.setValue(null);
            }
        });
    }

    private List<EstablishmentSearchTO> fetchSearchResponse(ResponseBody response){
        List<EstablishmentSearchTO> establishments = null;
        try {
            JSONObject data = new JSONObject(response.string());
            JSONArray establishmentsArray = data.getJSONArray("data");
            if(establishmentsArray.length() > 0){
                establishments = new ArrayList();

                for(int i = 0; i< establishmentsArray.length(); i++){
                    JSONObject establishmentJsonObject = (JSONObject) establishmentsArray.get(i);
                    establishments.add(new EstablishmentSearchTO(establishmentJsonObject.getInt("id"),
                            establishmentJsonObject.getString("name"),
                            establishmentJsonObject.getString("address"),
                            establishmentJsonObject.getString("urlImage"),
                            (float) establishmentJsonObject.getDouble("rating")));
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }


        return establishments;
    }
}
