package com.miedo.dtodoaqui.model;

import android.os.AsyncTask;

import androidx.lifecycle.MutableLiveData;

import com.miedo.dtodoaqui.data.EstablishmentReviewTO;
import com.miedo.dtodoaqui.data.ProfileTO;
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

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Body;

public class ReviewsModel {

    ProfileModel profileModel = new ProfileModel();

    //Test
    Map<Integer,String> userIds = new HashMap<>();
    List<EstablishmentReviewTO> reviews = null;

    public void GetFromEstablishment(int establishmentId, MutableLiveData<List<EstablishmentReviewTO>> mutableReviews){

        DeTodoAquiAPI api = ServiceGenerator.createServiceScalar(DeTodoAquiAPI.class);
        Call<ResponseBody> callEstablishment = api.getEstablishmentReviews(establishmentId);

        callEstablishment.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                new GetUserTask(mutableReviews,fetchEstablishmentReviewsResponse(response.body())).execute();
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                mutableReviews.setValue(null);
            }
        });

    }


    private List<EstablishmentReviewTO> fetchEstablishmentReviewsResponse(ResponseBody response){
        List<EstablishmentReviewTO> reviews = null;
        try {
            JSONObject data = new JSONObject(response.string());
            JSONArray establishmentsArray = data.getJSONArray("data");
            if(establishmentsArray.length() > 0){
                reviews = new ArrayList();

                for(int i = 0; i< establishmentsArray.length(); i++){
                    JSONObject establishmentJsonObject = (JSONObject) establishmentsArray.get(i);

                    int userId = establishmentJsonObject.getInt("user_id");
                    if(!userIds.containsKey(userId))
                        userIds.put(userId,"");

                    reviews.add(
                            new EstablishmentReviewTO(
                                    establishmentJsonObject.getInt("id"),
                                    String.valueOf(userId),
                                    establishmentJsonObject.getString("name"),
                                    establishmentJsonObject.getString("description")
                    ));
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return reviews;
    }

    private String getUsername(int id){
        DeTodoAquiAPI api = ServiceGenerator.createServiceScalar(DeTodoAquiAPI.class);
        Call<ResponseBody> callUser = api.getUserWithId(id);

        try {
            Response<ResponseBody> response = callUser.execute();
            return fetchUsernameResponse(response.body());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String fetchUsernameResponse(ResponseBody response){
        String username = null;
        try {
            JSONObject data = new JSONObject(response.string());
            JSONObject user = data.getJSONObject("data");
            if(user != null){
                username = user.getString("username");
            }

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return username;
    }

    private class GetUserTask extends AsyncTask<Void, Void, Void>{

        MutableLiveData<List<EstablishmentReviewTO>> mutableReviews = null;
        List<EstablishmentReviewTO> reviews = null;

        public GetUserTask(MutableLiveData<List<EstablishmentReviewTO>> mutableReviews, List<EstablishmentReviewTO> reviews){
            this.mutableReviews = mutableReviews;
            this.reviews = reviews;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            for(Map.Entry<Integer, String> entry : userIds.entrySet()){
                entry.setValue(getUsername(entry.getKey()));
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if(reviews != null){
                for(EstablishmentReviewTO review : reviews){
                    review.setUsername(userIds.get(Integer.parseInt(review.getUsername())));
                }
            }

            mutableReviews.setValue(reviews);

            super.onPostExecute(aVoid);
        }

        @Override
        protected void onCancelled() {
            mutableReviews.setValue(null);
            super.onCancelled();
        }
    }

}
