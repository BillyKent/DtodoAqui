package com.miedo.dtodoaqui.customviews;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.miedo.dtodoaqui.R;

public class PostReviewDialog extends Dialog {
    public PostReviewDialog(@NonNull Context context) {
        super(context);
    }

    public PostReviewDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    protected PostReviewDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_post_review);
    }
}
