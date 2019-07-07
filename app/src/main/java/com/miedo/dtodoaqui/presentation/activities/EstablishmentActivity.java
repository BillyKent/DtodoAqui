package com.miedo.dtodoaqui.presentation.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.leinardi.android.speeddial.SpeedDialActionItem;
import com.leinardi.android.speeddial.SpeedDialView;
import com.miedo.dtodoaqui.CustomViews.PostRatingDialog;
import com.miedo.dtodoaqui.CustomViews.PostReportDialog;
import com.miedo.dtodoaqui.R;
import com.miedo.dtodoaqui.adapters.EstablishmentReviewAdapter;
import com.miedo.dtodoaqui.customviews.PostReviewDialog;
import com.miedo.dtodoaqui.data.EstablishmentReviewTO;
import com.miedo.dtodoaqui.data.EstablishmentTO;
import com.miedo.dtodoaqui.data.local.SessionManager;
import com.miedo.dtodoaqui.viewmodels.EstablishmentViewModel;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.PicassoProvider;

import java.util.List;

import butterknife.BindDimen;
import butterknife.BindView;
import butterknife.ButterKnife;

public class EstablishmentActivity extends AppCompatActivity{

    private EstablishmentViewModel viewModel;

    private SupportMapFragment mapFragment;
    @BindView(R.id.establishmentCTL)
    CollapsingToolbarLayout establishmentToolbar;
    /*@BindView(R.id.establishmentCategoryTV)
    TextView establishmentCategory;*/
    /*@BindView(R.id.establishmentVerifiedTV)
    TextView establishmentVerified;*/
    @BindView(R.id.establishmentDecriptionTV)
    TextView establishmentDescription;
    @BindView(R.id.establishmentOpeningHoursTV)
    TextView establishmentOpeningHours;
    @BindView(R.id.establishmentPriceTV)
    TextView establishmentPrice;
    /*@BindView(R.id.establishmentAddressTV)
    TextView establishmentAddress;*/
    @BindView(R.id.establishmentBackgroundIV)
    ImageView establishmentBackground;
    @BindView(R.id.establishmentNameTV)
    TextView establishmentName;

    @BindView(R.id.establishmentReviewsRV)
    RecyclerView establishmentReviews;

    @BindView(R.id.postReviewEstablishmentFAB)
    SpeedDialView publishFab;

    private GoogleMap establishmentMap = null;
    private Marker establishmentMarker = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_establishment);
        ButterKnife.bind(this);

        //Mapa del establecimiento
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        //Viewmodel
        viewModel = ViewModelProviders.of(this).get(EstablishmentViewModel.class);
        viewModel.getEstablishment().observe(this, new Observer<EstablishmentTO>() {
            @Override
            public void onChanged(EstablishmentTO establishmentTO) {
                refreshEstablishment(establishmentTO);
            }
        });
        viewModel.getReviews().observe(this, new Observer<List<EstablishmentReviewTO>>() {
            @Override
            public void onChanged(List<EstablishmentReviewTO> establishmentReviewTOS) {
                refreshReviews(establishmentReviewTOS);
            }
        });

        //Configurar
        int id = getIntent().getExtras().getInt("establishment_id");
        viewModel.GetEstablishment(id);
        viewModel.GetReviewsFromEstablishment(id);

        establishmentReviews.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));

        publishFab.addActionItem(
                new SpeedDialActionItem.Builder(R.id.fab_establishment_review, R.drawable.ic_review_black_24dp)
                        .setLabel("Reseñar")
                        .create()
        );
        publishFab.addActionItem(
                new SpeedDialActionItem.Builder(R.id.fab_establishment_rating, R.drawable.ic_star_black_24dp)
                        .setLabel("Calificar")
                        .create()
        );
        publishFab.addActionItem(
                new SpeedDialActionItem.Builder(R.id.fab_establishment_report, R.drawable.ic_report_black_24dp)
                        .setLabel("Reportar")
                        .create()
        );
        publishFab.setOnActionSelectedListener(new SpeedDialView.OnActionSelectedListener() {
            @Override
            public boolean onActionSelected(SpeedDialActionItem actionItem) {
                if(SessionManager.getInstance(getApplicationContext()).isUserLogged()){
                    switch (actionItem.getId()) {
                        case R.id.fab_establishment_review:{
                            PostReviewDialog dialog = new PostReviewDialog(EstablishmentActivity.this, id, Integer.parseInt(SessionManager.getInstance(getApplicationContext()).getCurrentSession().getId().trim()));
                            dialog.show();
                            break;
                        }
                        case R.id.fab_establishment_rating:{
                            PostRatingDialog dialog = new PostRatingDialog(EstablishmentActivity.this, id, Integer.parseInt(SessionManager.getInstance(getApplicationContext()).getCurrentSession().getId().trim()), 5);
                            dialog.show();
                            break;
                        }
                        case R.id.fab_establishment_report:{
                            PostReportDialog dialog = new PostReportDialog(EstablishmentActivity.this, id, Integer.parseInt(SessionManager.getInstance(getApplicationContext()).getCurrentSession().getId().trim()));
                            dialog.show();
                            break;
                        }
                    }
                }else{
                    Toast.makeText(EstablishmentActivity.this, "Debe iniciar sesión primero", Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });

       /* publishFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(SessionManager.getInstance(getApplicationContext()).isUserLogged()){
                    Intent intent = new Intent(getApplicationContext(), PostReviewActivity.class);
                    intent.putExtra("establishment_id",id);
                    startActivity(intent);
                }else{
                    Toast.makeText(EstablishmentActivity.this, "Debe iniciar sesión primero", Toast.LENGTH_SHORT).show();
                }
            }
        });*/

    }

    private void refreshEstablishment(EstablishmentTO establishment){
        if(establishmentMap == null){
            mapFragment.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap googleMap) {
                    establishmentMap = googleMap;
                    googleMap.getUiSettings().setAllGesturesEnabled(false);
                    establishmentMarker = establishmentMap.addMarker(new MarkerOptions().position(establishment.getLatLng()).title(establishment.getName()));
                    establishmentMap.moveCamera(CameraUpdateFactory.newLatLngZoom(establishment.getLatLng(),15));
                }
            });
        }else{
            establishmentMarker.setPosition(establishment.getLatLng());
            establishmentMap.moveCamera(CameraUpdateFactory.newLatLng(establishment.getLatLng()));
        }

        Picasso.get().load(establishment.getSlug()).into(establishmentBackground);

        establishmentName.setText(establishment.getName());
        //establishmentAddress.setText(establishment.getAddress());
        //establishmentCategory.setText(establishment.getCategory());
        establishmentDescription.setText(establishment.getDescription());
        establishmentOpeningHours.setText(establishment.getOpeningHours());
        if(establishment.getPrice() != null && !establishment.getPrice().equals("null"))
        establishmentPrice.setText(establishment.getPrice());
        else
            establishmentPrice.setText("");
        establishmentName.setCompoundDrawablesWithIntrinsicBounds(0,0,establishment.isVerified()?R.drawable.ic_check_circle_black_24dp:R.drawable.ic_do_not_disturb_on_black_24dp,0);

    }

    @Override
    protected void onResume() {
        super.onResume();
        int id = getIntent().getExtras().getInt("establishment_id");
        viewModel.GetReviewsFromEstablishment(id);
    }

    private void refreshReviews(List<EstablishmentReviewTO> reviews){
        EstablishmentReviewAdapter adapter = new EstablishmentReviewAdapter(new EstablishmentReviewAdapter.OnClickViewHolder() {
            @Override
            public void clickViewHolder(EstablishmentReviewTO est) {

            }
        },reviews,this);

        establishmentReviews.setAdapter(adapter);
    }
}
