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

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileViewModel extends ViewModel {
    public static final String TAG = ProfileViewModel.class.getSimpleName();

    private UserTO currentUser;
    private ProfileModel model;
    private ProfileTO currentProfile;

    private String userID;

    private Boolean toCreate = false;

    final MutableLiveData<ProfileState> profileState = new MutableLiveData<>();

    public enum ProfileState {
        ERROR_STATE,
        OBTENIENDO,
        CON_PERFIL,
        SIN_PERFIL,
        INVALID_CREDENTIALS
    }

    public ProfileViewModel() {
        model = new ProfileModel();
    }


    public void obtenerPerfil() {
        profileState.setValue(ProfileState.OBTENIENDO);

        // Obtenemos la api
        DeTodoAquiAPI api = ServiceGenerator.createServiceScalar(DeTodoAquiAPI.class);
        // Creamos el header
        String header = "Bearer " + currentUser.getJwt();
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
                            userID = JSONUtils.getUserIDFromJSON(response.body().string());

                            profileState.setValue(ProfileState.CON_PERFIL);
                        }

                    } else {
                        if (response.code() == 404) {
                            reloadToken();
                        } else {
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

    public void reloadToken() {
        // Obtenemos la api
        DeTodoAquiAPI api = ServiceGenerator.createServiceScalar(DeTodoAquiAPI.class);
        // Armamos un JSON en forma de string para el body de la peticion
        String requestString = JSONUtils.getLoginRequestJSON(currentUser.getUsername(), currentUser.getPassword());
        // Creamos el requestBody
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), requestString);
        // Preparamos la peticion sincrona
        Call<ResponseBody> call = api.loginUser(requestBody);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String newToken = JSONUtils.getJWT(response.body().string());
                        currentUser.setJwt(newToken);
                        obtenerPerfil();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    profileState.setValue(ProfileState.INVALID_CREDENTIALS);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
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
        profileState.setValue(ProfileState.CON_PERFIL);
    }

    public void setCurrentUser(UserTO currentUser) {
        this.currentUser = currentUser;
    }

    public UserTO getCurrentUser() {
        return currentUser;
    }

    public String getUserID() {
        return userID;
    }
}

