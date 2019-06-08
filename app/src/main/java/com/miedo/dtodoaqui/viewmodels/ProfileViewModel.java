package com.miedo.dtodoaqui.viewmodels;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.miedo.dtodoaqui.data.ProfileTO;
import com.miedo.dtodoaqui.data.UserTO;
import com.miedo.dtodoaqui.model.ProfileModel;

public class ProfileViewModel extends ViewModel {
    public static final String TAG = ProfileViewModel.class.getSimpleName();

    private ProfileModel model;
    private ProfileTO currentProfile;

    final MutableLiveData<ProfileState> profileState = new MutableLiveData<>();

    public enum ProfileState {
        VERIFICANDO_PERFIL,
        ERROR_STATE,
        OBTENIENDO_PERFIL,
        CON_PERFIL,
        SIN_PERFIL
    }

    public ProfileViewModel() {
        profileState.setValue(ProfileState.VERIFICANDO_PERFIL);
        model = new ProfileModel();
    }


    public void verificarPerfil() { // primer paso
        if (currentProfile == null) {
            profileState.setValue(ProfileState.OBTENIENDO_PERFIL);
        } else {
            profileState.setValue(ProfileState.CON_PERFIL);
        }
    }


    public void obtenerPerfil(String jwtToken) {
        new Thread() {
            @Override
            public void run() {
                currentProfile = model.getProfile(jwtToken);
                if (currentProfile != null) { // Successful request
                    if (currentProfile.getId() == null) { // 204
                        profileState.postValue(ProfileState.SIN_PERFIL);
                    } else { // 200
                        profileState.postValue(ProfileState.CON_PERFIL);
                    }
                } else {
                    profileState.postValue(ProfileState.ERROR_STATE);
                }
            }
        }.start();
    }

    public ProfileTO getCurrentProfile() {
        return currentProfile;
    }

    public MutableLiveData<ProfileState> getProfileState() {
        return profileState;
    }


}

