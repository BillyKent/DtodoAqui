package com.miedo.dtodoaqui.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Base64;

import com.miedo.dtodoaqui.data.local.SessionManager;
import com.miedo.dtodoaqui.data.remote.DeTodoAquiAPI;
import com.miedo.dtodoaqui.data.remote.ServiceGenerator;
import com.miedo.dtodoaqui.utils.CallbackUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ImageModel {


    public List<String> GetSyncImages(int reviewId){
        DeTodoAquiAPI api = ServiceGenerator.createServiceScalar(DeTodoAquiAPI.class);
        Call<ResponseBody> callImages = api.getImagesFromReview(reviewId);

        try {
            ResponseBody responseBody = callImages.execute().body();
            return  fetchImagesNamesResponseBody(responseBody);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new ArrayList<>();
    }

    public void GetImages(int reviewId, CallbackUtils.SimpleCallback<List<String>> callback){
        DeTodoAquiAPI api = ServiceGenerator.createServiceScalar(DeTodoAquiAPI.class);
        Call<ResponseBody> callImages = api.getImagesFromReview(reviewId);

        callImages.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                callback.OnResult(fetchImagesNamesResponseBody(response.body()));
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                callback.OnFailure("ERROR");
            }
        });
    }

    private List<String> fetchImagesNamesResponseBody(ResponseBody responseBody){
        List<String> names = new ArrayList<>();
        try {
            JSONObject data = new JSONObject(responseBody.string());
            JSONArray imagesArray = data.getJSONArray("data");
            if (imagesArray.length() > 0) {
                names = new ArrayList();

                for (int i = 0; i < imagesArray.length(); i++) {
                    JSONObject imagesJsonObj = (JSONObject) imagesArray.get(i);
                    names.add("http://35.226.8.87/"+imagesJsonObj.getString("image_name"));
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        } catch(NullPointerException e2){
            e2.printStackTrace();
        }
        return names;
    }

    public void PostImages(List<Bitmap> images, String entityName, int entityId, String jwt, CallbackUtils.SimpleCallback<String[]> callback) {
        String encoded[] = new String[images.size()];
        for (int i = 0; i < images.size(); i++) {
            encoded[i] = bitmapToBase64(images.get(i));
        }
        postImages(encoded, entityName, entityId, jwt, callback);
    }


    private void postImages(String[] imagesB64, String entityName, int entityId, String jwt, CallbackUtils.SimpleCallback<String[]> callback){
        new postImagesAsyncTask(imagesB64, entityName, entityId, jwt, callback).execute();
    }

    private String bitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
        byte[] byteArray = outputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    private String getImageRequestBodyJSON(String imageB64, String entityName, int entityId) {
        String retorno = "";

        JSONObject body = new JSONObject();
        JSONObject img = new JSONObject();

        try {
            img.put("image_base64", "data:image/png;base64," + imageB64.replace("\n", ""));
            img.put("entity_id", entityId);
            img.put("entity_name", entityName);

            body.put("image", img);

            retorno = body.toString();

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return retorno;
    }

    private String fetchImageNameResponseBody(String string) throws JSONException {

        String retorno = null;

        retorno = new JSONObject(string).getJSONObject("data").getString("image_name");

        return retorno;

    }

    private class postImagesAsyncTask extends AsyncTask<Void, Void, Void> {

        private String[] imagesB64;
        private String[] names;
        private String entityName;
        private int entityId;
        private String jwt;
        private CallbackUtils.SimpleCallback<String[]> simpleCallback;

        postImagesAsyncTask(String[] imagesB64, String entityName, int entityId, String jwt, CallbackUtils.SimpleCallback<String[]> simpleCallback){
            this.imagesB64 = imagesB64;
            this.entityName = entityName;
            this.entityId = entityId;
            this.jwt = jwt;
            this.simpleCallback = simpleCallback;
            names = new String[imagesB64.length];
        }
        @Override
        protected Void doInBackground(Void... voids) {

            DeTodoAquiAPI api = ServiceGenerator.createServiceScalar(DeTodoAquiAPI.class);

            for (int i=0; i < imagesB64.length; i++) {
                String imageB64 = imagesB64[i];

                RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), getImageRequestBodyJSON(imageB64,entityName,entityId));

                Call<ResponseBody> call = api.uploadImage("Bearer " + jwt, requestBody);

                try {
                    ResponseBody responseBody = call.execute().body();
                    names[i] = fetchImageNameResponseBody(responseBody.string());
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            simpleCallback.OnResult(names);
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            simpleCallback.OnFailure("Error");
        }
    }
}
