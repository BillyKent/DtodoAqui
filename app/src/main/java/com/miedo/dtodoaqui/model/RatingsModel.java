package com.miedo.dtodoaqui.model;

import com.miedo.dtodoaqui.data.RatingTO;
import com.miedo.dtodoaqui.data.remote.DeTodoAquiAPI;
import com.miedo.dtodoaqui.data.remote.ServiceGenerator;
import com.miedo.dtodoaqui.utils.CallbackUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RatingsModel {

    public void GetRatings(int establishmentId, CallbackUtils.SimpleCallback<Map<Integer, RatingTO>> callback){
        DeTodoAquiAPI api = ServiceGenerator.createServiceScalar(DeTodoAquiAPI.class);

        Call<ResponseBody> callRating = api.getListingRatings(establishmentId);

        callRating.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                callback.OnResult(fetchUserRatingResponseBody(response.body()));
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                callback.OnFailure("ERROR");
            }
        });
    }

    public Map<Integer, RatingTO> GetSyncRatings(int establishmentId){
        DeTodoAquiAPI api = ServiceGenerator.createServiceScalar(DeTodoAquiAPI.class);

        Call<ResponseBody> callRating = api.getListingRatings(establishmentId);

        try {
            return fetchUserRatingResponseBody(callRating.execute().body());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new HashMap<>();
    }

    public int GetSyncPromRatings(int establishmentId){
        DeTodoAquiAPI api = ServiceGenerator.createServiceScalar(DeTodoAquiAPI.class);

        Call<ResponseBody> callRating = api.getListingRatings(establishmentId);

        try {
            Map<Integer, RatingTO> ratings = fetchUserRatingResponseBody(callRating.execute().body());
            int s = 0;
            int i = 0;
            for (Map.Entry<Integer, RatingTO> ratingTOEntry : ratings.entrySet()) {
                s += ratingTOEntry.getValue().getValue();
                i++;
            }
            i = i==0?1:i;
            return s / i;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }



    public void PostRating(int reviewId, int userId, String type, int value, int max, CallbackUtils.SimpleCallback<Integer> callback){
        DeTodoAquiAPI api = ServiceGenerator.createServiceScalar(DeTodoAquiAPI.class);

        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"),
                getRatingRequestBody(reviewId, userId, type, value, max)
        );

        Call<ResponseBody> callRating = api.postRating(requestBody);

        callRating.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                callback.OnResult(getRatingId(response.body()));
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                callback.OnFailure("ERROR");
            }
        });

    }

    public void PutRating(int ratingId, int value, CallbackUtils.SimpleCallback<Integer> callback){
        DeTodoAquiAPI api = ServiceGenerator.createServiceScalar(DeTodoAquiAPI.class);

        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"),
                getRatingRequestBodyPut(value)
        );

        Call<ResponseBody> callRating = api.putRating(ratingId,requestBody);

        callRating.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                callback.OnResult(getRatingId(response.body()));
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                callback.OnFailure("ERROR");
            }
        });

    }

    public Map<Integer, RatingTO> fetchUserRatingResponseBody(ResponseBody responseBody) {
        Map<Integer, RatingTO> retorno = new HashMap<>();
        try {
            JSONObject data = new JSONObject(responseBody.string());
            JSONArray reviewJson = data.getJSONArray("data");
            if(reviewJson != null && reviewJson.length() > 0){
                for (int i = 0; i < reviewJson.length() ; i++) {
                    JSONObject reviewJO = reviewJson.getJSONObject(i);
                    int userId = reviewJO.getInt("user_id");
                    int id = reviewJO.getInt("id");
                    int value = reviewJO.getInt("value");

                    retorno.put(userId, new RatingTO(id, value, userId));
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        return  retorno;
    }

    public String getRatingRequestBody(int reviewId, int userId, String type, int value, int max) {
        String retorno = "";

        JSONObject body = new JSONObject();
        JSONObject rat = new JSONObject();

        try {
            rat.put("review_id", reviewId);
            rat.put("user_id", userId);
            rat.put("type", type);
            rat.put("value", value);
            rat.put("max", max);

            body.put("rating", rat);

            retorno = body.toString();

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return retorno;
    }

    public String getRatingRequestBodyPut(int value) {
        String retorno = "";

        JSONObject body = new JSONObject();
        JSONObject rat = new JSONObject();

        try {

            rat.put("value", value);

            body.put("rating", rat);

            retorno = body.toString();

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return retorno;
    }

    public int getRatingId(ResponseBody responseBody){
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



}
