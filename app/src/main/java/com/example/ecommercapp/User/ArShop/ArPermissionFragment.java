package com.example.ecommercapp.User.ArShop;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ecommercapp.R;


public class ArPermissionFragment extends Fragment {

    CardView t_shirt_card;
    CardView frocks_card;
    CardView watches_card;
    CardView glasses_card;
    CardView hats_card;
    CardView jewelries_card;
    CardView bags_card;
    CardView mobile_card;
    CardView laptops_card;
    CardView headphones_card;
    View rootView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView=inflater.inflate(R.layout.user_ar_peremistion, container, false);
        init();

        //load by category
        t_shirt_card.setOnClickListener(v -> {
            dialogBox("Shirts under constructions","Ar View ");

        });
        frocks_card.setOnClickListener(v -> {
            dialogBox("Frocks  under constructions","Ar View ");


        });

        watches_card.setOnClickListener(v -> {
            openFragmentDefault(new ArShopFragment());


        });

        glasses_card.setOnClickListener(v -> {

            dialogBox("Glasses under constructions","Ar View ");

        });


        hats_card.setOnClickListener(v -> {
            dialogBox("Hats under constructions","Ar View ");

        });

//
//        jewelries_card.setOnClickListener(v -> {
//            dialogBox("Jewelries under constructions","Ar View ");
//
//        });


        bags_card.setOnClickListener(v -> {
            dialogBox("Bags under constructions","Ar View ");

        });
        mobile_card.setOnClickListener(v -> {
            dialogBox("Mobile  Phones under constructions","Ar View ");

        });
        laptops_card.setOnClickListener(v -> {
            dialogBox("Laptops under constructions","Ar View ");

        });
        headphones_card.setOnClickListener(v -> {
            dialogBox("Headphones under constructions","Ar View ");
        });



        return rootView;
    }

    private void init() {
        t_shirt_card=rootView.findViewById(R.id.t_shirt_card);
        frocks_card=rootView.findViewById(R.id.frocks_card);
        glasses_card=rootView.findViewById(R.id.glasses_card);
        watches_card=rootView.findViewById(R.id.watches_card);
        headphones_card=rootView.findViewById(R.id.headphones_card);
        laptops_card=rootView.findViewById(R.id.laptops_card);
        hats_card=rootView.findViewById(R.id.hats_card);
      //  jewelries_card=rootView.findViewById(R.id.jewelries_card);
        mobile_card=rootView.findViewById(R.id.mobile_card);
        bags_card=rootView.findViewById(R.id.bags_card);
    }



    public void dialogBox(String message,String title) {


        AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Toast.makeText(getApplicationContext(), "You clicked on OK", Toast.LENGTH_SHORT).show();
            }
        });

        alertDialog.show();


    }

    private void openFragmentDefault(Fragment fragment) {

        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();

    }
}