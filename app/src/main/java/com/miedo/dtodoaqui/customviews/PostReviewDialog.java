package com.miedo.dtodoaqui.CustomViews;

import android.app.Dialog;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputEditText;
import com.miedo.dtodoaqui.R;
import com.miedo.dtodoaqui.core.BaseActivity;
import com.miedo.dtodoaqui.data.local.SessionManager;
import com.miedo.dtodoaqui.model.ReviewsModel;

import java.util.ArrayList;
import java.util.List;

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

    @BindView(R.id.establishment_imageslayout_ll)
    LinearLayout imagesLayout;

    //Test
    @BindView(R.id.establishment_imageprev_1_iv)
    ImageView imagePreview1;
    @BindView(R.id.establishment_imageprev_2_iv)
    ImageView imagePreview2;
    @BindView(R.id.establishment_imageprev_3_iv)
    ImageView imagePreview3;
    @BindView(R.id.establishment_imageprev_4_iv)
    ImageView imagePreview4;
    @BindView(R.id.establishment_imageprev_5_iv)
    ImageView imagePreview5;
    @BindView(R.id.establishment_imageprev_6_iv)
    ImageView imagePreview6;

    ReviewsModel reviewsModel = new ReviewsModel();

    List<ImageView> imagesPreview = new ArrayList<>();

    boolean publishing = false;

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

        //Test
        imagesPreview.add(imagePreview1);
        imagesPreview.add(imagePreview2);
        imagesPreview.add(imagePreview3);
        imagesPreview.add(imagePreview4);
        imagesPreview.add(imagePreview5);
        imagesPreview.add(imagePreview6);

        //setSupportActionBar(toolbar);

        //toolbar.inflateMenu(R.menu.menu_review);

        establishmentId = getIntent().getExtras().getInt("establishment_id");
        userId = Integer.parseInt(SessionManager.getInstance(getApplicationContext()).getCurrentSession().getId());

        toolbar.setOnMenuItemClickListener(item -> {
            if(!publishing){
                publishing = true;
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
                    et_titulo.setEnabled(false);
                    et_descripcion.setEnabled(false);
                    button.setEnabled(false);

                    reviewsModel.PostEstablishmentReview(et_descripcion.getText().toString(), establishmentId, et_titulo.getText().toString(), userId, SessionManager.getInstance(getApplicationContext()).getCurrentSession().getJwt(),imagesEncodedList
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
                }else{
                    publishing = false;
                    et_titulo.setEnabled(true);
                    et_descripcion.setEnabled(true);
                    button.setEnabled(true);
                }
            }
            return false;
        });

        button.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent,"Select Picture"), RESULT_LOAD_IMAGE);
        });

    }
    List<Bitmap> imagesEncodedList;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            // When an Image is picked
            if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK
                    && null != data) {
                // Get the Image from data

                String[] filePathColumn = { MediaStore.Images.Media.DATA };
                imagesEncodedList = new ArrayList<>();
                if(data.getData()!=null){

                    imagesEncodedList.add(MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData()));

                } else {
                    if (data.getClipData() != null) {
                        ClipData mClipData = data.getClipData();
                        for (int i = 0; i < mClipData.getItemCount(); i++) {
                            imagesEncodedList.add(MediaStore.Images.Media.getBitmap(getContentResolver(), mClipData.getItemAt(i).getUri()));
                        }
                    }
                }

                setImages(imagesEncodedList);
            } else {
                Toast.makeText(this, "No seleccionó ninguna imagen",
                        Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Algo salió mal", Toast.LENGTH_LONG)
                    .show();
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void setImages(List<Bitmap> bitmaps){

        if(bitmaps.size() > 6){
            Toast.makeText(this, "Solo se tomará en cuenta las 6 primeras imágenes.", Toast.LENGTH_SHORT).show();
            bitmaps = bitmaps.subList(0,6);
        }

        for (ImageView imageView : imagesPreview) {
            imageView.setImageBitmap(null);
        }

        int i = 0;
        for (Bitmap bitmap : bitmaps) {
            imagesPreview.get(i).setImageBitmap(bitmap);
            i++;
        }

        imagesLayout.setVisibility(View.VISIBLE);
    }
}
