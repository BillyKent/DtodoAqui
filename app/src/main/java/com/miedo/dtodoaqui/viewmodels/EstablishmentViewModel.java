package com.miedo.dtodoaqui.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.miedo.dtodoaqui.data.EstablishmentTO;
import com.miedo.dtodoaqui.model.EstablishmentModel;

public class EstablishmentViewModel extends ViewModel {
    private MutableLiveData<EstablishmentTO> liveEstablishment = new MutableLiveData<>();
    private EstablishmentModel model = new EstablishmentModel();

    public void GetEstablishment(int id){
        model.Get(id, liveEstablishment);
    }

    public LiveData<EstablishmentTO> getEstablishment(){ return liveEstablishment; }

}
