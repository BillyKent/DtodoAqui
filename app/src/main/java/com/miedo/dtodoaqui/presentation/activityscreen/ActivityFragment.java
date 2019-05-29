package com.miedo.dtodoaqui.presentation.activityscreen;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.miedo.dtodoaqui.R;
import com.miedo.dtodoaqui.core.BaseFragment;

import butterknife.BindView;
import butterknife.ButterKnife;


public class ActivityFragment extends BaseFragment {

    @BindView(R.id.container)
    public TextView anotherView;

    public ActivityFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_activity, container, false);

        ButterKnife.bind(this, view);
        setUpStateView(view, anotherView);


        getStateView().showLoading();

        new Thread() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                getStateView().finishLoading();

            }
        }.start();


        return view;
    }


}
