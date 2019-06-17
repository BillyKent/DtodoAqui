package com.miedo.dtodoaqui.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.miedo.dtodoaqui.data.ProfileTO;
import com.miedo.dtodoaqui.data.remote.DeTodoAquiAPI;
import com.miedo.dtodoaqui.data.remote.ServiceGenerator;
import com.miedo.dtodoaqui.utils.JSONUtils;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ModifyProfileViewModel extends ViewModel {

    private String jwt;

    public enum ModifyProfileState {
        CREATING,
        UPDATING,
        ERROR,
        SUCCESSFUL
    }

    private final MutableLiveData<ModifyProfileState> currentState = new MutableLiveData<>();

    public ModifyProfileViewModel() {

    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }

    public void createOrUpdateProfile(ProfileTO profileTO, Boolean toCreate) {
        if (toCreate) {
            currentState.setValue(ModifyProfileState.CREATING);
        } else {
            currentState.setValue(ModifyProfileState.UPDATING);
        }

        // Creamos la instancia de la api
        DeTodoAquiAPI api = ServiceGenerator.createServiceScalar(DeTodoAquiAPI.class);
        // Creamos el header
        String header = "Bearer " + jwt;

        // Creamos el requestBody
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"),
                JSONUtils.getProfileRequestBodyJSON(profileTO)
        );

        // Preparamos la peticion asincrona
        // La variable toCreate nos dice si debemos crear o modificar
        Call<ResponseBody> call =
                toCreate ? api.registerProfile(header, requestBody) :
                        api.updateProfile(header, requestBody);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) { // 201
                    currentState.setValue(ModifyProfileState.SUCCESSFUL);
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                currentState.setValue(ModifyProfileState.ERROR);
            }
        });

    }

    public MutableLiveData<ModifyProfileState> getCurrentState() {
        return currentState;
    }
}
