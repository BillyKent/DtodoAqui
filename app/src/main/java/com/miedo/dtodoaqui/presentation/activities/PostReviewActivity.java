package com.miedo.dtodoaqui.presentation.activities;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.miedo.dtodoaqui.R;
import com.miedo.dtodoaqui.core.BaseActivity;
import com.miedo.dtodoaqui.data.local.SessionManager;
import com.miedo.dtodoaqui.model.ReviewsModel;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PostReviewActivity extends BaseActivity {


    @BindView(R.id.et_titulo)
    public TextInputEditText et_titulo;

    @BindView(R.id.establishment_report_message_et)
    public TextInputEditText et_descripcion;

    @BindView(R.id.establishment_rating_post_btn)
    MaterialButton button;

    ReviewsModel reviewsModel = new ReviewsModel();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_review_establishment);
        ButterKnife.bind(this);

        //toolbar.setTitle("Publica una reseña");

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        int userId = Integer.parseInt(SessionManager.getInstance(getApplicationContext()).getCurrentSession().getId().trim());
        int establishmentId = getIntent().getExtras().getInt("establishment_id");

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean publish = true;
                if(et_titulo.getText().toString().isEmpty()){
                    et_titulo.setError("Debe ingresar un título");
                    publish = false;
                }
                if(et_descripcion.getText().toString().isEmpty()){
                    et_descripcion.setError("Debe ingresar una descripción");
                    publish = false;
                }
                if(publish){
                    reviewsModel.PostEstablishmentReview(et_descripcion.getText().toString(), establishmentId, et_titulo.getText().toString(), userId
                            , new ReviewsModel.CallBack() {
                                @Override
                                public void OnResult() {
                                    Toast.makeText(PostReviewActivity.this, "Reseña publicada con éxito", Toast.LENGTH_SHORT).show();
                                    finish();
                                }

                                @Override
                                public void OnFailed() {
                                    Toast.makeText(PostReviewActivity.this, "No se pudo publicar su reseña", Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_review, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                finish();
                return true;

            case R.id.send_option:
                showMessage("Enviando");
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

}
