package com.gfso.client.oauthclientapplication.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gfso.client.oauthclientapplication.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class GoodsOverviewFragment extends Fragment {
    @BindView(R.id.tv_goods_config)
    TextView tv_goods_config;

    AppCompatActivity activity = null;

    public GoodsOverviewFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_goods_overview, container, false);
        activity = (AppCompatActivity)this.getActivity();
        ButterKnife.bind(this, view);

        return view;
    }

}
