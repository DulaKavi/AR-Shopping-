package com.example.ecommercapp.Admin.Home;

import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ecommercapp.Admin.Manage.AdminUserManageFragment;
import com.example.ecommercapp.Admin.ViewOrder.AdminViewOrderFragment;
import com.example.ecommercapp.Admin.ViewProducts.AdminProductFragment;
import com.example.ecommercapp.R;
import com.example.ecommercapp.Seller.Screen.AddProduct.SellerAddProductFragment;
import com.example.ecommercapp.Seller.Screen.ShowProduct.SellerShowFragment;
import com.example.ecommercapp.Seller.Screen.ViewOrder.SellerViewOrderFragment;

public class AdminHome extends Fragment {
    View rootView;


    CardView user_mange_card;
    CardView view_order_card;
    CardView view_product_card;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView=inflater.inflate(R.layout.admin_home, container, false);
        init();


        user_mange_card.setOnClickListener(v -> openFragment(new AdminUserManageFragment()));
        view_product_card.setOnClickListener(v -> openFragment(new AdminProductFragment()));
        view_order_card.setOnClickListener(v -> openFragment(new AdminViewOrderFragment()));


        return rootView;
    }

    private void init() {

        user_mange_card=rootView.findViewById(R.id.user_mange_card);
        view_order_card=rootView.findViewById(R.id.view_order_card);
        view_product_card=rootView.findViewById(R.id.view_product_card);
    }


    public void openFragment(Fragment fragment) {
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.admin_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}