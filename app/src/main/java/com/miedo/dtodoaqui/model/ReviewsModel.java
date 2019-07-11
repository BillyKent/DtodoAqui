package com.miedo.dtodoaqui.model;

import android.graphics.Bitmap;
import android.os.AsyncTask;

import androidx.lifecycle.MutableLiveData;

import com.miedo.dtodoaqui.data.EstablishmentReviewTO;
import com.miedo.dtodoaqui.data.ProfileTO;
import com.miedo.dtodoaqui.data.local.SessionManager;
import com.miedo.dtodoaqui.data.remote.DeTodoAquiAPI;
import com.miedo.dtodoaqui.data.remote.ServiceGenerator;
import com.miedo.dtodoaqui.utils.CallbackUtils;
import com.miedo.dtodoaqui.utils.JSONUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import okhttp3.MediaType;
import okhttp3.RequestBody;
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

    public void PostEstablishmentReview(String description, int establishmentId, String name, int userId, String jwt, List<Bitmap> images, ReviewsModel.CallBack callBack){
        DeTodoAquiAPI api = ServiceGenerator.createServiceScalar(DeTodoAquiAPI.class);

        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"),
                getProfileRequestBodyJSON(description,establishmentId,name,userId)
        );


        Call<ResponseBody> callEstablishment = api.postReview(requestBody);

        callEstablishment.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(images != null && images.size() > 0){
                    ImageModel imageModel = new ImageModel();
                    imageModel.PostImages(images, "review", fetchReviewIdResponse(response.body()), jwt, new CallbackUtils.SimpleCallback<String[]>() {
                        @Override
                        public void OnResult(String[] strings) {
                            callBack.OnResult();
                        }

                        @Override
                        public void OnFailure(String response) {
                            callBack.OnFailed();
                        }
                    });
                }else{
                    callBack.OnResult();
                }
                //callBack.OnResult();
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
               callBack.OnFailed();
            }
        });
    }

    public String getProfileRequestBodyJSON(String description, int establishmentId, String name, int userId) {
        String retorno = "";

        JSONObject body = new JSONObject();
        JSONObject prof = new JSONObject();

        try {
            prof.put("description", description);
            prof.put("is_published", true);
            prof.put("listing_id", establishmentId);
            prof.put("name", name);
            prof.put("user_id", userId);

            body.put("review", prof);

            retorno = body.toString();

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return retorno;
    }


    private int fetchReviewIdResponse(ResponseBody responseBody){
        List<EstablishmentReviewTO> reviews = new ArrayList();
        try {
            JSONObject data = new JSONObject(responseBody.string());
            JSONObject reviewJson = data.getJSONObject("data");
            if(reviewJson != null){
                return  reviewJson.getInt("id");
            }

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return -1;
    }

    private List<EstablishmentReviewTO> fetchEstablishmentReviewsResponse(ResponseBody response){
        List<EstablishmentReviewTO> reviews = new ArrayList();
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
                                    establishmentJsonObject.getString("description"),
                                    -1, new ArrayList<>()
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

    private float getRating(int id){
        return new Random().nextFloat() * 5.0f;
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
            new GetRatingTask(mutableReviews,reviews).execute();
            //mutableReviews.setValue(reviews);

            super.onPostExecute(aVoid);
        }

        @Override
        protected void onCancelled() {
            mutableReviews.setValue(null);
            super.onCancelled();
        }
    }

    private class GetRatingTask extends AsyncTask<Void, Void, Void>{

        MutableLiveData<List<EstablishmentReviewTO>> mutableReviews = null;
        List<EstablishmentReviewTO> reviews = null;

        public GetRatingTask(MutableLiveData<List<EstablishmentReviewTO>> mutableReviews, List<EstablishmentReviewTO> reviews){
            this.mutableReviews = mutableReviews;
            this.reviews = reviews;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            ImageModel imageModel = new ImageModel();

            for(EstablishmentReviewTO review : reviews){
                review.setRating(getRating(review.getId()));

                review.setImages(imageModel.GetSyncImages(review.getId()));
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            mutableReviews.setValue(reviews);
            super.onPostExecute(aVoid);
        }

        @Override
        protected void onCancelled() {
            mutableReviews.setValue(null);
            super.onCancelled();
        }
    }

    public interface CallBack{
        public void OnResult();
        public void OnFailed();
    }

}
