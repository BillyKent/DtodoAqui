package com.miedo.dtodoaqui.model;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.miedo.dtodoaqui.data.ProfileTO;
import com.miedo.dtodoaqui.data.UserTO;
import com.miedo.dtodoaqui.data.remote.DeTodoAquiAPI;
import com.miedo.dtodoaqui.data.remote.ServiceGenerator;
import com.miedo.dtodoaqui.utils.JSONUtils;
import com.miedo.dtodoaqui.viewmodels.ProfileViewModel;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

public class ProfileModel {
    public static final String TAG = ProfileModel.class.getSimpleName();


    private MutableLiveData<ProfileViewModel.ProfileState> liveData;

    public ProfileModel() {
    }

    public ProfileTO getProfile(String token) {

        Log.i(TAG, "Token para obtener perfil : " + token);

        ProfileTO retorno = null;
        // Creamos la instancia de la api
        DeTodoAquiAPI api = ServiceGenerator.createServiceScalar(DeTodoAquiAPI.class);
        // Creamos el header
        String header = "Bearer " + token;
        // Preparamos la peticion sincrona
        Call<ResponseBody> call = api.getProfile(header);

        try {
            Response response = call.execute();

            if (response.isSuccessful()) {
                if (response.code() == 204) {
                    retorno = new ProfileTO();
                    Log.i(TAG, "Codigo 204 al obtener el perfil ");

                } else if (response.code() == 200) { // Devuelve el ProfileTO del perfil encontrado
                    String rpta = ((ResponseBody) response.body()).string();
                    retorno = JSONUtils.getProfileFromJSONString(rpta);
                    Log.i(TAG,"Perfil encontrado : "+retorno);
                }

            } else {
                return null;
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
        return retorno;

    }

}
