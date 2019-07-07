package com.miedo.dtodoaqui.model;

import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.maps.model.LatLng;
import com.miedo.dtodoaqui.data.EstablishmentSearchTO;
import com.miedo.dtodoaqui.data.EstablishmentTO;
import com.miedo.dtodoaqui.data.remote.DeTodoAquiAPI;
import com.miedo.dtodoaqui.data.remote.ServiceGenerator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EstablishmentModel {

    CategoriesModel categoriesModel;

    public EstablishmentModel() {
        categoriesModel = new CategoriesModel();
    }

    public void Get(int id, MutableLiveData<EstablishmentTO> establishment) {
        DeTodoAquiAPI api = ServiceGenerator.createServiceScalar(DeTodoAquiAPI.class);
        Call<ResponseBody> callEstablishment = api.getEstablishment(id);

        categoriesModel.GetCategories(new CategoriesModel.Callback<Map<Integer, String>>() {
            @Override
            public void onResult(Map<Integer, String> arg) {
                callEstablishment.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        establishment.setValue(fetchEstablishmentResponse(response.body(), arg));
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        establishment.setValue(null);
                    }
                });
            }

            @Override
            public void onFailure() {
                establishment.setValue(null);
            }
        });
    }

    public void Search(String keyword, String location, String category, MutableLiveData<List<EstablishmentTO>> data) {
        // Creamos la instancia de la api
        DeTodoAquiAPI api = ServiceGenerator.createServiceScalar(DeTodoAquiAPI.class);
        Call<ResponseBody> callEstablishments = api.searchEstablishments(keyword, location, category);

        categoriesModel.GetCategories(new CategoriesModel.Callback<Map<Integer, String>>() {
            @Override
            public void onResult(Map<Integer, String> arg) {
                callEstablishments.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        List<EstablishmentTO> establishments = fetchSearchResponse(response.body(), arg);
                        data.setValue(establishments);
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        data.setValue(null);
                    }
                });
            }

            @Override
            public void onFailure() {
                data.setValue(null);
            }
        });
    }


    private List<EstablishmentTO> fetchSearchResponse(ResponseBody response, Map<Integer, String> categories) {
        List<EstablishmentTO> establishments = null;
        try {
            JSONObject data = new JSONObject(response.string());
            JSONArray establishmentsArray = data.getJSONArray("data");
            if (establishmentsArray.length() > 0) {
                establishments = new ArrayList();

                for (int i = 0; i < establishmentsArray.length(); i++) {
                    JSONObject establishmentJsonObject = (JSONObject) establishmentsArray.get(i);
                    establishments.add(new EstablishmentTO(establishmentJsonObject.getInt("id"),
                            establishmentJsonObject.getString("name"),
                            establishmentJsonObject.getString("address"),
                            categories.containsKey(establishmentJsonObject.getInt("category_id")) ? categories.get(establishmentJsonObject.getInt("category_id")) : "Sin categoría",
                            establishmentJsonObject.getString("description"),
                            new LatLng(establishmentJsonObject.getDouble("latitude"), establishmentJsonObject.getDouble("longitude")),
                            establishmentJsonObject.getBoolean("is_verified"),
                            establishmentJsonObject.getString("opening_hours"),
                            establishmentJsonObject.getString("price"),
                            "http://35.226.8.87/images/"+establishmentJsonObject.getString("slug"),
                            (float) establishmentJsonObject.getDouble("rating")
                    ));
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return establishments;
    }

    private EstablishmentTO fetchEstablishmentResponse(ResponseBody response, Map<Integer, String> categories) {
        EstablishmentTO establishment = null;
        try {
            JSONObject data = new JSONObject(response.string());
            JSONObject establishmentJsonObject = data.getJSONObject("data");
            if (establishmentJsonObject != null) {
                establishment = new EstablishmentTO(establishmentJsonObject.getInt("id"),
                        establishmentJsonObject.getString("name"),
                        establishmentJsonObject.getString("address"),
                        categories.containsKey(establishmentJsonObject.getInt("category_id")) ? categories.get(establishmentJsonObject.getInt("category_id")) : "Sin categoría",
                        establishmentJsonObject.getString("description"),
                        new LatLng(establishmentJsonObject.getDouble("latitude"), establishmentJsonObject.getDouble("longitude")),
                        establishmentJsonObject.getBoolean("is_verified"),
                        establishmentJsonObject.getString("opening_hours"),
                        establishmentJsonObject.getString("price"),
                        establishmentJsonObject.getString("slug"),
                        (float) establishmentJsonObject.getDouble("rating")
                );
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return establishment;
    }

    private void createEstablishment() {


    }

}
