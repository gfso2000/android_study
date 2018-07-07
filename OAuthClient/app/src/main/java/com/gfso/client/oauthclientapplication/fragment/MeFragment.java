package com.gfso.client.oauthclientapplication.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gfso.client.oauthclientapplication.R;
import com.gfso.client.oauthclientapplication.fragment.activity.LoginActivity;
import com.gfso.client.oauthclientapplication.util.Content;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.app.Activity.RESULT_OK;

public class MeFragment extends Fragment {
    AppCompatActivity activity = null;
    ImageView photoView = null;

    @BindView(R.id.tb_user_name)
    TextView userIdView ;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_me, null);
        activity = (AppCompatActivity)this.getActivity();
        ButterKnife.bind(this, view);

        photoView = view.findViewById(R.id.tb_user_photo);
        photoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity , LoginActivity.class) ;
                startActivityForResult(intent , Content.LOGIN_REQUEST);
            }
        });
        return view;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == Content.LOGIN_REQUEST) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                Bundle res = data.getExtras();
                String result = res.getString(Content.LOGIN_USERID);
                userIdView.setText(result);
                Toast.makeText(activity,"login success:"+result,Toast.LENGTH_LONG);
            }
        }
    }
}