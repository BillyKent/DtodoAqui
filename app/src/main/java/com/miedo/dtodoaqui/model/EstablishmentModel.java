package com.miedo.dtodoaqui.model;

import android.os.AsyncTask;

import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.maps.model.LatLng;
import com.miedo.dtodoaqui.data.EstablishmentTO;
import com.miedo.dtodoaqui.data.remote.DeTodoAquiAPI;
import com.miedo.dtodoaqui.data.remote.ServiceGenerator;
import com.miedo.dtodoaqui.utils.CallbackUtils;

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

        categoriesModel.GetCategories(new CallbackUtils.SimpleCallback<Map<String, Integer>>() {
            @Override
            public void OnResult(Map<String, Integer> arg) {
                callEstablishment.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        //establishment.setValue(fetchEstablishmentResponse(response.body(), arg));
                        new GetRatingFromOne(fetchEstablishmentResponse(response.body(), arg), establishment).execute();
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        establishment.setValue(null);
                    }
                });
            }

            @Override
            public void OnFailure(String response) {
                establishment.setValue(null);
            }
        });
    }

    public void Search(String keyword, String location, String category, MutableLiveData<List<EstablishmentTO>> data) {
        // Creamos la instancia de la api
        DeTodoAquiAPI api = ServiceGenerator.createServiceScalar(DeTodoAquiAPI.class);
        Call<ResponseBody> callEstablishments = api.searchEstablishments(keyword, location, category);

        categoriesModel.GetCategories(new CallbackUtils.SimpleCallback<Map<String, Integer>>() {
            @Override
            public void OnResult(Map<String, Integer> arg) {
                callEstablishments.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        List<EstablishmentTO> establishments = fetchSearchResponse(response.body(), arg);

                        //Test

                        new GetRating(establishments, data).execute();

                        //data.setValue(establishments);
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        data.setValue(null);
                    }
                });
            }

            @Override
            public void OnFailure(String response) {
                data.setValue(null);
            }
        });
    }


    private List<EstablishmentTO> fetchSearchResponse(ResponseBody response, Map<String, Integer> categories) {
        List<EstablishmentTO> establishments = new ArrayList<>();
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
                            getCategoryName(establishmentJsonObject.getInt("category_id"), categories),
                            establishmentJsonObject.getString("description"),
                            new LatLng(establishmentJsonObject.getDouble("latitude"), establishmentJsonObject.getDouble("longitude")),
                            establishmentJsonObject.getBoolean("is_verified"),
                            establishmentJsonObject.getString("opening_hours"),
                            establishmentJsonObject.getString("price"),
                            "http://35.226.8.87/"+establishmentJsonObject.getString("slug"),
                            (float) establishmentJsonObject.getDouble("rating")
                    ));
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        } catch(NullPointerException e2){
            e2.printStackTrace();
        }
        return establishments;
    }

    private EstablishmentTO fetchEstablishmentResponse(ResponseBody response, Map<String, Integer> categories) {
        EstablishmentTO establishment = null;
        try {
            JSONObject data = new JSONObject(response.string());
            JSONObject establishmentJsonObject = data.getJSONObject("data");
            if (establishmentJsonObject != null) {
                establishment = new EstablishmentTO(establishmentJsonObject.getInt("id"),
                        establishmentJsonObject.getString("name"),
                        establishmentJsonObject.getString("address"),
                        getCategoryName(establishmentJsonObject.getInt("category_id"), categories),
                        establishmentJsonObject.getString("description"),
                        new LatLng(establishmentJsonObject.getDouble("latitude"), establishmentJsonObject.getDouble("longitude")),
                        establishmentJsonObject.getBoolean("is_verified"),
                        establishmentJsonObject.getString("opening_hours"),
                        establishmentJsonObject.getString("price"),
                        "http://35.226.8.87/"+establishmentJsonObject.getString("slug"),
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

    private String getCategoryName(int id, Map<String, Integer> categories){
        String name = "Sin categoría";
        for (Map.Entry<String, Integer> entry : categories.entrySet()) {
            if (entry.getValue().equals(id)) {
                name = entry.getKey();
                break;
            }
        }
        return name;
    }




    public class GetRating extends AsyncTask<Void, Void, Void> {

        List<EstablishmentTO> establishments;
        MutableLiveData<List<EstablishmentTO>> establishmentLive;

        public GetRating(List<EstablishmentTO> establishments, MutableLiveData<List<EstablishmentTO>> establishmentLive) {
            this.establishments = establishments;
            this.establishmentLive = establishmentLive;
        }

        @Override
        protected Void doInBackground(Void... voids) {

            RatingsModel ratingsModel = new RatingsModel();
            for (EstablishmentTO establishmentTO : establishments) {
                establishmentTO.setRating(ratingsModel.GetSyncPromRatings(establishmentTO.getId()));
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            establishmentLive.setValue(establishments);
            super.onPostExecute(aVoid);

        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            establishmentLive.setValue(null);
        }
    }

    public class GetRatingFromOne extends AsyncTask<Void, Void, Void> {

        EstablishmentTO establishment;
        MutableLiveData<EstablishmentTO> establishmentLive;

        public GetRatingFromOne(EstablishmentTO establishments, MutableLiveData<EstablishmentTO> establishmentLive) {
            this.establishment = establishments;
            this.establishmentLive = establishmentLive;
        }

        @Override
        protected Void doInBackground(Void... voids) {

            RatingsModel ratingsModel = new RatingsModel();
            establishment.setRating(ratingsModel.GetSyncPromRatings(establishment.getId()));
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            establishmentLive.setValue(establishment);
            super.onPostExecute(aVoid);

        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            establishmentLive.setValue(null);
        }
    }


}
