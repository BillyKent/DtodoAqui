package com.miedo.dtodoaqui.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.miedo.dtodoaqui.data.EstablishmentTO;
import com.miedo.dtodoaqui.model.EstablishmentModel;

import java.util.List;

public class EstablishmentsSearchViewModel extends ViewModel {
    private MutableLiveData<List<EstablishmentTO>> data = new MutableLiveData<List<EstablishmentTO>>();
    private EstablishmentModel model = new EstablishmentModel();

    public EstablishmentsSearchViewModel() {
    }

    public void SearchEstablishments(final String keyword, final String location, final String category){
        model.Search(keyword,location,category,data);
    }

    public LiveData<List<EstablishmentTO>> getSearchData() {
        return data;
    }

}
