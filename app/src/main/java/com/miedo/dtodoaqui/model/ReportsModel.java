package com.miedo.dtodoaqui.model;

import com.miedo.dtodoaqui.data.ReportTO;
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

public class ReportsModel {

    public void PostReport(ReportTO report, CallbackUtils.SimpleCallback<ReportTO> callback){
        DeTodoAquiAPI api = ServiceGenerator.createServiceScalar(DeTodoAquiAPI.class);

        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"),
                getReportRequestBodyJSON(report)
        );

        Call<ResponseBody> callEstablishment = api.postReport(requestBody);

        callEstablishment.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                callback.OnResult(fetchReportResponseBody(response.body()));
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                callback.OnFailure("Error");
            }
        });
    }

    private ReportTO fetchReportResponseBody(ResponseBody body){
        ReportTO report = null;
        try {
            JSONObject data = new JSONObject(body.string());
            JSONObject reportJsonObject = data.getJSONObject("data");
            if (reportJsonObject != null) {
                report = new ReportTO(reportJsonObject.getInt("id"),
                        reportJsonObject.getBoolean("is_approved"),
                        reportJsonObject.getInt("listing_id"),
                        reportJsonObject.getString("message"),
                        reportJsonObject.getInt("user_id")
                );
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return report;
    }

    private String getReportRequestBodyJSON(ReportTO report) {
        String retorno = "";

        JSONObject body = new JSONObject();
        JSONObject rep = new JSONObject();

        try {
            rep.put("is_approved", report.isApproved());
            rep.put("listing_id", report.getEstablishmentId());
            rep.put("message", report.getMessage());
            rep.put("user_id", report.getUserId());

            body.put("report", rep);

            retorno = body.toString();

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return retorno;
    }
}
