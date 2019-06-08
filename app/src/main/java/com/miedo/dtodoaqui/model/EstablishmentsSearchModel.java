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


    public void TestSearchEstablishments(String keyword, String location, String category, MutableLiveData<List<EstablishmentSearchTO>> data){
        List<EstablishmentSearchTO> establishments = new ArrayList<>();
        if(keyword.equals("1")){
            establishments.add(new EstablishmentSearchTO(1,"La tiendita de don pepe", "Av. holi 124", "https://www.chiquipedia.com/imagenes/imagenes-amor02.jpg",5));
            establishments.add(new EstablishmentSearchTO(2,"Pollería", "Av. holi 124", "https://www.chiquipedia.com/imagenes/animo01.jpg",4));
            establishments.add(new EstablishmentSearchTO(3,"Esta es una prueba", "Av. holi 124", "https://www.chiquipedia.com/imagenes/imagenes-san-valentin18.jpg",3.5f));
        }else{
            establishments.add(new EstablishmentSearchTO(4,"Casa de Coco", "Av. holi 124", "https://www.chiquipedia.com/imagenes/imagenes-animo10.jpg",1));
            establishments.add(new EstablishmentSearchTO(5,"Patio de SMAAAAAAAAASH", "Av. holi 124", "https://www.chiquipedia.com/imagenes/imagenes-frases05.jpg",4.5f));
        }

        data.setValue(establishments);
    }

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
