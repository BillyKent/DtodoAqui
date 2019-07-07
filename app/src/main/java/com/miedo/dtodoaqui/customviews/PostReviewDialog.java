package com.miedo.dtodoaqui.customviews;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.miedo.dtodoaqui.R;
import com.miedo.dtodoaqui.core.BaseActivity;
import com.miedo.dtodoaqui.model.ReviewsModel;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PostReviewDialog extends BaseActivity {

    private final int RESULT_LOAD_IMAGE = 322;

    @BindView(R.id.establishment_postreview_tb)
    Toolbar toolbar;

    @BindView(R.id.establishment_postreview_title_et)
    TextInputEditText et_titulo;

    @BindView(R.id.establishment_postreview_message_et)
    TextInputEditText et_descripcion;

    @BindView(R.id.establishment_uploadimages_btn)
    MaterialButton button;

    ReviewsModel reviewsModel = new ReviewsModel();

    private int establishmentId;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_review_establishment);
        Window window = getWindow();
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        ButterKnife.bind(this);

        toolbar.inflateMenu(R.menu.menu_review);

        setSupportActionBar(toolbar);

        toolbar.setOnMenuItemClickListener(item -> {
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
                                Toast.makeText(getApplicationContext(), "Reseña publicada con éxito", Toast.LENGTH_SHORT).show();
                                finish();
                            }

                            @Override
                            public void OnFailed() {
                                Toast.makeText(getApplicationContext(), "No se pudo publicar su reseña", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
            return false;
        });

        button.setOnClickListener(v -> {
            Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(i, RESULT_LOAD_IMAGE);
        });
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);
    }
}
