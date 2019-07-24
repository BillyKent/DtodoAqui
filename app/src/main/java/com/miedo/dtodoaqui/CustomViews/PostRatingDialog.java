package com.miedo.dtodoaqui.CustomViews;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.button.MaterialButton;
import com.miedo.dtodoaqui.R;
import com.miedo.dtodoaqui.data.RatingTO;
import com.miedo.dtodoaqui.model.RatingsModel;
import com.miedo.dtodoaqui.utils.CallbackUtils;

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
    private RatingTO lastRating;
    private boolean hasRating = false;

    public PostRatingDialog(@NonNull Context context, int establishmentId, int userId, RatingTO lastRating) {
        super(context);
        this.establishmentId = establishmentId;
        this.userId = userId;
        this.lastRating = lastRating;
    }

    public PostRatingDialog(@NonNull Context context, int themeResId, int establishmentId, int userId, RatingTO lastRating) {
        super(context, themeResId);
        this.establishmentId = establishmentId;
        this.userId = userId;
        this.lastRating = lastRating;
    }

    protected PostRatingDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener, int establishmentId, int userId, RatingTO lastRating) {
        super(context, cancelable, cancelListener);
        this.establishmentId = establishmentId;
        this.userId = userId;
        this.lastRating = lastRating;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_rating_establishment);

        Window window = getWindow();
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        ButterKnife.bind(this);

        if (lastRating != null) {
            hasRating = true;
            messageParm.setRating(lastRating.getValue());
        }else{
            hasRating = false;
            messageParm.setRating(5);
        }


        postBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean publish = true;

                if (publish) {
                    if (!hasRating) {
                        ratingsModel.PostRating(establishmentId, userId, "listing", (int)messageParm.getRating(), 5, new CallbackUtils.SimpleCallback<Integer>() {
                            @Override
                            public void OnResult(Integer integer) {
                                Toast.makeText(getContext(), "Calificación publicada con éxito", Toast.LENGTH_SHORT).show();
                                dismiss();
                            }

                            @Override
                            public void OnFailure(String response) {
                                Toast.makeText(getContext(), "No se pudo publicar su calificación", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }else{
                        ratingsModel.PutRating(lastRating.getId(), (int)messageParm.getRating(), new CallbackUtils.SimpleCallback<Integer>() {
                            @Override
                            public void OnResult(Integer integer) {
                                Toast.makeText(getContext(), "Calificación actualizada con éxito", Toast.LENGTH_SHORT).show();
                                dismiss();
                            }

                            @Override
                            public void OnFailure(String response) {
                                Toast.makeText(getContext(), "No se pudo publicar su calificación", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                }

            }
        });
    }
}
