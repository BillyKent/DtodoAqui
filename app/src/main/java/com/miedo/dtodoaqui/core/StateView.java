package com.miedo.dtodoaqui.core;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.miedo.dtodoaqui.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StateView {

    public View stateView;

    @BindView(R.id.state_title)
    TextView titleView;
    @BindView(R.id.state_description)
    TextView descriptionView;
    @BindView(R.id.state_icon)
    ImageView iconView;
    @BindView(R.id.state_button)
    Button buttonAction;
    @BindView(R.id.state_loading_progress_bar)
    ProgressBar progressBar;

    private int shortAnimationDuration = 1500;

    private final View target;

    public StateView(View target) {
        this.target = target;
        this.stateView = ((View) target.getParent()).findViewById(R.id.state_view);
        shortAnimationDuration = target.getContext().getResources().getInteger(android.R.integer.config_longAnimTime);
        ButterKnife.bind(this, stateView);
    }

    public void hideStateView() {

        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                finalAnimation(target, stateView);

                progressBar.setVisibility(View.GONE);
                buttonAction.setVisibility(View.GONE);
                descriptionView.setVisibility(View.GONE);
                titleView.setVisibility(View.GONE);
                iconView.setVisibility(View.GONE);

            }
        });

    }

    public void showLoadingTitle(String title) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                target.setVisibility(View.GONE);
                setLoadingTitleState(title);
                finalAnimation(stateView, target);

            }
        });
    }

    public void showTitleMessageIcon(String title, String description, int resIcon) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                setTitleMessageIconState(title, description, resIcon);

                finalAnimation(stateView, target);


            }
        });
    }

    public void showTitleMessageAction(String title, String description, View.OnClickListener listener) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                setTitleMessageActionState(title, description, listener);

                finalAnimation(stateView, target);


            }
        });
    }

    public void showTitleMessageButtonAction(String title, String description, String buttonText, View.OnClickListener listener) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                setTitleMessageButtonActionState(title, description, buttonText,listener);

                finalAnimation(stateView, target);


            }
        });
    }

    public void showTitleMessageIconAction(String title, String description, int resIcon, View.OnClickListener listener) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                setTitleMessageIconActionState(title, description, resIcon, listener);
                finalAnimation(stateView, target);

            }
        });
    }

    public void forceHideStateView() {

        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                forceAnimation(target, stateView);

                progressBar.setVisibility(View.GONE);
                buttonAction.setVisibility(View.GONE);
                descriptionView.setVisibility(View.GONE);
                titleView.setVisibility(View.GONE);
                iconView.setVisibility(View.GONE);

            }
        });

    }

    public void forceLoadingTitle(String title) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                target.setVisibility(View.GONE);
                setLoadingTitleState(title);
                forceAnimation(stateView, target);

            }
        });
    }

    public void forceTitleMessageIcon(String title, String description, int resIcon) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                setTitleMessageIconState(title, description, resIcon);

                forceAnimation(stateView, target);


            }
        });
    }

    public void forceTitleMessageAction(String title, String description, View.OnClickListener listener) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                setTitleMessageActionState(title, description, listener);

                forceAnimation(stateView, target);


            }
        });
    }

    public void forceTitleMessageIconAction(String title, String description, int resIcon, View.OnClickListener listener) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                setTitleMessageIconActionState(title, description, resIcon, listener);
                forceAnimation(stateView, target);
            }
        });
    }

    private void setLoadingTitleState(String title) {
        descriptionView.setVisibility(View.GONE);
        iconView.setVisibility(View.GONE);
        buttonAction.setVisibility(View.GONE);

        progressBar.setVisibility(View.VISIBLE);
        titleView.setVisibility(View.VISIBLE);
        titleView.setText(title);
    }

    private void setTitleMessageIconState(String title, String description, int resIcon) {
        progressBar.setVisibility(View.GONE);
        buttonAction.setVisibility(View.GONE);

        descriptionView.setVisibility(View.VISIBLE);
        titleView.setVisibility(View.VISIBLE);
        iconView.setVisibility(View.VISIBLE);

        titleView.setText(title);
        descriptionView.setText(description);
        iconView.setImageResource(resIcon);
    }

    private void setTitleMessageButtonActionState(String title, String description, String buttonText, View.OnClickListener listener) {

        progressBar.setVisibility(View.GONE);
        iconView.setVisibility(View.GONE);

        buttonAction.setVisibility(View.VISIBLE);
        descriptionView.setVisibility(View.VISIBLE);
        titleView.setVisibility(View.VISIBLE);

        titleView.setText(title);
        descriptionView.setText(description);
        buttonAction.setText(buttonText);
        buttonAction.setOnClickListener(listener);

    }

    private void setTitleMessageIconActionState(String title, String description, int resIcon, View.OnClickListener listener) {

        progressBar.setVisibility(View.GONE);

        buttonAction.setVisibility(View.VISIBLE);
        descriptionView.setVisibility(View.VISIBLE);
        titleView.setVisibility(View.VISIBLE);
        iconView.setVisibility(View.VISIBLE);

        titleView.setText(title);
        descriptionView.setText(description);
        iconView.setImageResource(resIcon);
        buttonAction.setOnClickListener(listener);
    }

    private void setTitleMessageActionState(String title, String description, View.OnClickListener listener) {
        progressBar.setVisibility(View.GONE);
        iconView.setVisibility(View.GONE);

        buttonAction.setVisibility(View.VISIBLE);
        descriptionView.setVisibility(View.VISIBLE);
        titleView.setVisibility(View.VISIBLE);

        titleView.setText(title);
        descriptionView.setText(description);
        buttonAction.setOnClickListener(listener);
    }

    private void forceAnimation(View toRevealView, View toHideView) {
        toHideView.setVisibility(View.GONE);
        toRevealView.setVisibility(View.VISIBLE);
    }

    private void finalAnimation(View toRevealView, View toHideView) {

        // hacemos aparecer la vista a revelar animando alpha de 0 a 1

        if (toRevealView.getVisibility() != View.VISIBLE) {
            toRevealView.setAlpha(0f);
            toRevealView.setVisibility(View.VISIBLE);
            toRevealView.animate()
                    .alpha(1f)
                    .setDuration(shortAnimationDuration)
                    .setListener(null);
        }

        if (toHideView.getVisibility() == View.VISIBLE) {
            // hacemos desaparecer la vista a ocultar animando su alfa de 1 a 0
            toHideView.setAlpha(1f);
            toHideView.animate()
                    .alpha(0f)
                    .setDuration(shortAnimationDuration)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            toHideView.setVisibility(View.GONE);
                        }
                    });
        }
    }

}
