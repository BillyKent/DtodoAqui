package com.miedo.dtodoaqui.CustomViews;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.RatingBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.button.MaterialButton;
import com.miedo.dtodoaqui.R;
import com.miedo.dtodoaqui.model.RatingsModel;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PostRatingDialog extends Dialog {

    @BindView(R.id.establishment_rating_rating_rb)
    RatingBar messageParm;
    @BindView(R.id.establishment_rating_btn)
    MaterialButton postBtn;

    RatingsModel ratingsModel = new RatingsModel();

    private int establishmentId;
    private int userId;
    public PostRatingDialog(@NonNull Context context, int establishmentId, int userId, float lastRating) {
        super(context);
        this.establishmentId = establishmentId;
        this.userId = userId;
    }

    public PostRatingDialog(@NonNull Context context, int themeResId, int establishmentId, int userId, float lastRating) {
        super(context, themeResId);
        this.establishmentId = establishmentId;
        this.userId = userId;
    }

    protected PostRatingDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener, int establishmentId, int userId, float lastRating) {
        super(context, cancelable, cancelListener);
        this.establishmentId = establishmentId;
        this.userId = userId;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_rating_establishment);

        Window window = getWindow();
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        ButterKnife.bind(this);

        postBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean publish = true;

                if (publish) {
                    dismiss();
                    /*reportsModel.PostReport(new ReportTO(0,false, establishmentId,messageParm.getText().toString(), userId)
                            , new CallbackUtils.SimpleCallback<ReportTO>() {
                                @Override
                                public void OnResult(ReportTO reportTO) {
                                    Toast.makeText(getContext(), "Denuncia publicada con éxito", Toast.LENGTH_SHORT).show();
                                    dismiss();
                                }

                                @Override
                                public void OnFailure(String response) {
                                    Toast.makeText(getContext(), "No se pudo publicar su denuncia", Toast.LENGTH_SHORT).show();
                                }

                            });
                */
                }

            }
        });
    }
}
