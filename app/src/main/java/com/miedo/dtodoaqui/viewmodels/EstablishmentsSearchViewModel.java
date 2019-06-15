package com.miedo.dtodoaqui.viewmodels;

import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.miedo.dtodoaqui.data.EstablishmentSearchTO;
import com.miedo.dtodoaqui.model.EstablishmentsSearchModel;

import java.util.List;

public class EstablishmentsSearchViewModel extends ViewModel {
    private MutableLiveData<List<EstablishmentSearchTO>> data = new MutableLiveData<List<EstablishmentSearchTO>>();
    private EstablishmentsSearchModel model = new EstablishmentsSearchModel();

    public EstablishmentsSearchViewModel() {
    }

    public void SearchEstablishments(final String keyword, final String location, final String category){
        model.SearchEstablishments(keyword,location,category,data);
    }

    public LiveData<List<EstablishmentSearchTO>> getSearchData() {
        return data;
    }

}
