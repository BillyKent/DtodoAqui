package com.miedo.dtodoaqui.model;

import com.miedo.dtodoaqui.data.ClaimTO;
import com.miedo.dtodoaqui.data.remote.DeTodoAquiAPI;
import com.miedo.dtodoaqui.data.remote.ServiceGenerator;
import com.miedo.dtodoaqui.utils.CallbackUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ClaimsModel {

    public void PostReport(ClaimTO claim, CallbackUtils.SimpleCallback<ClaimTO> callback){
        DeTodoAquiAPI api = ServiceGenerator.createServiceScalar(DeTodoAquiAPI.class);

        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"),
                getClaimRequestBodyJSON(claim)
        );

        Call<ResponseBody> callEstablishment = api.postClaim(requestBody);

        callEstablishment.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                callback.OnResult(fetchClaimResponseBody(response.body()));
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                callback.OnFailure("Error");
            }
        });
    }

    private ClaimTO fetchClaimResponseBody(ResponseBody body){
        ClaimTO claim = null;
        try {
            JSONObject data = new JSONObject(body.string());
            JSONObject reportJsonObject = data.getJSONObject("data");
            if (reportJsonObject != null) {
                claim = new ClaimTO(reportJsonObject.getInt("id"),
                        reportJsonObject.getBoolean("is_approved"),
                        reportJsonObject.getInt("listing_id"),
                        reportJsonObject.getString("message"),
                        reportJsonObject.getInt("user_id")
                );
            }
        } catch (JSONException|IOException e) {
            e.printStackTrace();
        }
        return claim;
    }

    private String getClaimRequestBodyJSON(ClaimTO claim) {
        String retorno = "";

        JSONObject body = new JSONObject();
        JSONObject cla = new JSONObject();

        try {
            cla.put("is_approved", claim.isApproved());
            cla.put("listing_id", claim.getEstablishmentId());
            cla.put("message", claim.getMessage());
            cla.put("user_id", claim.getUserId());

            body.put("claim", cla);

            retorno = body.toString();

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return retorno;
    }
}
