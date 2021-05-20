package com.example.ecommercapp.User.ArShop;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.ecommercapp.R;


public class ArShopFragment extends Fragment {

    View rootView;
    Button button;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView=inflater.inflate(R.layout.user_ar_shop, container, false);
        button=rootView.findViewById(R.id.button);



        button.setOnClickListener(v -> {
            try {

                Intent intent = getActivity().getPackageManager().getLaunchIntentForPackage("com.ptc.vuforia.engine");
                startActivity(intent);

            }catch (Exception e){

            }


        });

        return rootView;
    }
}