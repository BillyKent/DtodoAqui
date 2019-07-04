package com.miedo.dtodoaqui.viewmodels;

import android.widget.ArrayAdapter;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.libraries.places.api.model.Place;
import com.miedo.dtodoaqui.model.CategoriesModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RegisterEstablishmentViewModel extends ViewModel {

    private List<Integer> indices = new ArrayList<>();
    private List<String> categories = new ArrayList<>();
    CategoriesModel categoriesModel = new CategoriesModel();

    public enum RegisterState {
        FETCHING_FORM_DATA,
        ERROR_FETCHING,
        READY_TO_REGISTER,
        NEXT_STEP,
        NORMAL
    }

    private final MutableLiveData<RegisterState> registerState = new MutableLiveData<>();


    public enum StepOneState {

    }

    private final MutableLiveData<StepOneState> stepOneState = new MutableLiveData<>();

    public enum StepTwoState {

    }

    public enum SteepThreeState {

    }

    public enum StepFourState {

    }

    public RegisterEstablishmentViewModel() {
        registerState.setValue(RegisterState.FETCHING_FORM_DATA);
        fetchCategories();
    }

    // ------------------------------------- Fetch data ----------------------------------------------
    public void fetchCategories() {
        categoriesModel.GetCategories(new CategoriesModel.Callback<Map<Integer, String>>() {
            @Override
            public void onResult(Map<Integer, String> arg) {
                categories.clear();
                indices.clear();
                for (Map.Entry<Integer, String> entry : arg.entrySet()) {
                    categories.add(entry.getValue());
                    indices.add(entry.getKey());
                }
                if (categories.size() > 0) {
                    registerState.setValue(RegisterState.READY_TO_REGISTER);
                } else {
                    registerState.setValue(RegisterState.ERROR_FETCHING);
                }
            }

            @Override
            public void onFailure() {
                registerState.setValue(RegisterState.ERROR_FETCHING);
            }
        });

    }

    public MutableLiveData<StepOneState> getStepOneState() {
        return stepOneState;
    }

    public MutableLiveData<RegisterState> getRegisterState() {
        return registerState;
    }

    public List<String> getCategories() {
        return categories;
    }

    public List<Integer> getIndices() {
        return indices;
    }


    private Integer categoryId = -1;
    private Place selectedPlace = null;

    public void setSelectedPlace(Place selectedPlace) {
        this.selectedPlace = selectedPlace;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }
}
