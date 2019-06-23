package com.miedo.dtodoaqui.viewmodels;

import android.widget.ArrayAdapter;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.miedo.dtodoaqui.model.CategoriesModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RegisterEstablishmentViewModel extends ViewModel {


    private List<String> categories = new ArrayList<>();
    CategoriesModel categoriesModel = new CategoriesModel();

    public enum RegisterState {
        FETCHING_FORM_DATA,
        ERROR_FETCHING,
        READY_TO_REGISTER
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
                for (Map.Entry<Integer, String> entry : arg.entrySet()) {
                    categories.add(entry.getValue());
                }
                if (categories.size() > 0) {

                } else {

                }
            }

            @Override
            public void onFailure() {
                //Error
            }
        });

    }

    public MutableLiveData<StepOneState> getStepOneState() {
        return stepOneState;
    }
}
