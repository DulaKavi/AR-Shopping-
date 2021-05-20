package com.example.ecommercapp.Sign;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.example.ecommercapp.R;
import com.example.ecommercapp.Sign.Adapter.SignAdapter;
import com.google.android.material.tabs.TabLayout;

public class SignNavActivity extends AppCompatActivity {
    TabLayout tablayout;
    ViewPager viewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_nav_activity);


        tablayout = findViewById(R.id.tab_layout);
        viewPager = findViewById(R.id.pagerView);

        setUpViewPager(viewPager);
        tablayout.setupWithViewPager(viewPager);

        tablayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


    }

    private void setUpViewPager(ViewPager viewPager) {

        SignAdapter adapter = new SignAdapter(getSupportFragmentManager());
        adapter.addFragment(new SignInFragment(), "Sign In");
        adapter.addFragment(new SignUpFragment(), "Sign Up");
        viewPager.setAdapter(adapter);
    }
}