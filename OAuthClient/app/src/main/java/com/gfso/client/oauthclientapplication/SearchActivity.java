package com.gfso.client.oauthclientapplication;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.FrameLayout;

import com.gfso.client.oauthclientapplication.fragment.FilterDrawerFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchActivity extends AppCompatActivity {
    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    @BindView(R.id.drawer_content)
    FrameLayout mDrawerContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);

        initDrawer();

        mDrawerLayout.openDrawer(mDrawerContent);
    }

    private void initDrawer() {
        Fragment fragment = new FilterDrawerFragment();
        FragmentManager fragmentManager = this.getSupportFragmentManager();
        Bundle bundle = new Bundle();
        bundle.putString("departmentName","");
        fragment.setArguments(bundle);
        fragmentManager.beginTransaction().replace(R.id.drawer_content, fragment).commit();
    }
}
