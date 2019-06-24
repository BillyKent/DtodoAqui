package com.miedo.dtodoaqui.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.miedo.dtodoaqui.data.EstablishmentReviewTO;
import com.miedo.dtodoaqui.data.EstablishmentTO;
import com.miedo.dtodoaqui.model.EstablishmentModel;
import com.miedo.dtodoaqui.model.ReviewsModel;

import java.util.List;

public class EstablishmentViewModel extends ViewModel {
    private MutableLiveData<EstablishmentTO> liveEstablishment = new MutableLiveData<>();
    private MutableLiveData<List<EstablishmentReviewTO>> liveReviews = new MutableLiveData<>();

    private EstablishmentModel establishmentModel = new EstablishmentModel();
    private ReviewsModel reviewModel = new ReviewsModel();

    public void GetEstablishment(int id){
        establishmentModel.Get(id, liveEstablishment);
    }

    public void GetReviewsFromEstablishment(int id){
        reviewModel.GetFromEstablishment(id,liveReviews);
    }

    public LiveData<List<EstablishmentReviewTO>> getReviews(){ return liveReviews; }

    public LiveData<EstablishmentTO> getEstablishment(){ return liveEstablishment; }

}
