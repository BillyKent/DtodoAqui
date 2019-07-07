package com.miedo.dtodoaqui.customviews;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.miedo.dtodoaqui.R;
import com.miedo.dtodoaqui.model.ReviewsModel;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PostReviewDialog extends Dialog {

    @BindView(R.id.et_titulo)
    public TextInputEditText et_titulo;

    @BindView(R.id.establishment_report_message_et)
    public TextInputEditText et_descripcion;

    @BindView(R.id.establishment_rating_post_btn)
    MaterialButton button;

    ReviewsModel reviewsModel = new ReviewsModel();

    private int establishmentId;
    private int userId;
    public PostReviewDialog(@NonNull Context context, int establishmentId, int userId) {
        super(context);
        this.establishmentId = establishmentId;
        this.userId = userId;
    }

    public PostReviewDialog(@NonNull Context context, int themeResId, int establishmentId, int userId) {
        super(context, themeResId);
        this.establishmentId = establishmentId;
        this.userId = userId;
    }

    protected PostReviewDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener, int establishmentId, int userId) {
        super(context, cancelable, cancelListener);
        this.establishmentId = establishmentId;
        this.userId = userId;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_review_establishment);

        Window window = getWindow();
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        ButterKnife.bind(this);

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
                                    Toast.makeText(getContext(), "Reseña publicada con éxito", Toast.LENGTH_SHORT).show();
                                    dismiss();
                                }

                                @Override
                                public void OnFailed() {
                                    Toast.makeText(getContext(), "No se pudo publicar su reseña", Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }
        });
    }
}
