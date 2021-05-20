package com.example.ecommercapp.Seller.Screen.Home;

import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ecommercapp.R;
import com.example.ecommercapp.Seller.Screen.AddProduct.SellerAddProductFragment;
import com.example.ecommercapp.Seller.Screen.ShowProduct.SellerShowFragment;
import com.example.ecommercapp.Seller.Screen.ViewOrder.SellerViewOrderFragment;

public class SellerHome extends Fragment {

View rootView;


CardView add_product_card;
CardView view_order_card;
CardView view_product_card;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView=inflater.inflate(R.layout.seller_home, container, false);

        init();


        add_product_card.setOnClickListener(v -> openFragment(new SellerAddProductFragment()));

        view_product_card.setOnClickListener(v -> openFragment(new SellerShowFragment()));

        view_order_card.setOnClickListener(v -> openFragment(new SellerViewOrderFragment()));


        return rootView;
    }

    private void init() {
        add_product_card=rootView.findViewById(R.id.add_product_card);
        view_order_card=rootView.findViewById(R.id.view_order_card);
        view_product_card=rootView.findViewById(R.id.view_product_card);
    }


    public void openFragment(Fragment fragment) {
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.seller_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}