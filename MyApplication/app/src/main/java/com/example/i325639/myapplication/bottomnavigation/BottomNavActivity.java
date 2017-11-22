package com.example.i325639.myapplication.bottomnavigation;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.i325639.myapplication.R;

public class BottomNavActivity extends AppCompatActivity {

    private int navId;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            if(item.getItemId() == navId) {
                return true;
            }
            navId = item.getItemId();

            Fragment selectedFragment = getSupportFragmentManager().findFragmentByTag(item.getItemId()+"");
            if(selectedFragment == null) {
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        selectedFragment = BottomNavFragment1.newInstance(null, null);
                        break;
                    case R.id.navigation_dashboard:
                        selectedFragment = BottomNavFragment2.newInstance(null, null);
                        break;
                    case R.id.navigation_notifications:
                        selectedFragment = BottomNavFragment3.newInstance(null, null);
                        break;
                }
            }
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.frame_layout, selectedFragment, item.getItemId()+"");
            //addToBackStack() will cache the fragment, then we can use findFragmentByTag() to get it back, no need create a new fragment.
            transaction.addToBackStack(null);
            transaction.commit();
            return true;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_nav);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, BottomNavFragment1.newInstance(null, null));
        transaction.commit();
    }

    boolean doubleBackToExitPressedOnce = false;
    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            //super.onBackPressed();
            //if call onBackPressed(), since the fragments are added to stack, it will back to the previous fragment
            //but if use finishAndRemoveTask(), it will directly exit the current Activity, and back to the previous Activity.
            finishAndRemoveTask ();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }
}
