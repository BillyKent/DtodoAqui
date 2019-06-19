package com.miedo.dtodoaqui.viewmodels;

import android.app.Service;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.miedo.dtodoaqui.data.ProfileTO;
import com.miedo.dtodoaqui.data.UserTO;
import com.miedo.dtodoaqui.data.remote.DeTodoAquiAPI;
import com.miedo.dtodoaqui.data.remote.ServiceGenerator;
import com.miedo.dtodoaqui.model.ProfileModel;
import com.miedo.dtodoaqui.utils.JSONUtils;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileViewModel extends ViewModel {
    public static final String TAG = ProfileViewModel.class.getSimpleName();

    private ProfileModel model;
    private ProfileTO currentProfile;

    private Boolean toCreate = false;

    final MutableLiveData<ProfileState> profileState = new MutableLiveData<>();

    public enum ProfileState {
        ERROR_STATE,
        OBTENIENDO,
        CON_PERFIL,
        SIN_PERFIL
    }

    public ProfileViewModel() {
        model = new ProfileModel();
    }


    public void obtenerPerfil(String jwtToken) {
        profileState.setValue(ProfileState.OBTENIENDO);

        // Obtenemos la api
        DeTodoAquiAPI api = ServiceGenerator.createServiceScalar(DeTodoAquiAPI.class);
        // Creamos el header
        String header = "Bearer " + jwtToken;
        // Preparamos la peticion sincrona
        Call<ResponseBody> call = api.getProfile(header);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    if (response.isSuccessful()) {
                        if (response.code() == 204) { // vacio
                            toCreate = true;
                            profileState.setValue(ProfileState.SIN_PERFIL);
                        } else if (response.code() == 200) {
                            toCreate = false;
                            currentProfile = JSONUtils.getProfileFromJSONString(response.body().string());
                            profileState.setValue(ProfileState.CON_PERFIL);
                        }

                    } else {
                        if (response.code() == 404) {
                            profileState.setValue(ProfileState.ERROR_STATE);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.i(TAG, "Mostrando error en logged xd");
                profileState.setValue(ProfileState.ERROR_STATE);
            }
        });


    }

    public boolean isCurrentProfileActive() {
        return currentProfile != null;
    }

    public ProfileTO getCurrentProfile() {
        return currentProfile;
    }

    public MutableLiveData<ProfileState> getProfileState() {
        return profileState;
    }

    public Boolean getToCreate() {
        return toCreate;
    }

    public void setCurrentProfile(ProfileTO currentProfile) {
        this.currentProfile = currentProfile;
    }
}

