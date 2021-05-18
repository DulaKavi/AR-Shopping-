package com.example.ecommercapp.User.MyCart;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.example.ecommercapp.R;
import com.example.ecommercapp.User.MyCart.Model.CartModel;
import com.example.ecommercapp.User.MyCart.Adapter.MyCartAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class MyCartFragment extends Fragment {

RecyclerView recyclerView;
SearchView search_bar;


View rootView;


    //real time database reference
    DatabaseReference reference;
    FirebaseDatabase rootNode;

    String TAG="MyCartFragment";

List<CartModel> cartModelList;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView=inflater.inflate(R.layout.user_my_cart, container, false);

        init();

        return rootView;
    }

    @Override
    public void onStart() {
        LoadCartData();
        super.onStart();
    }

    private void LoadCartData() {
        cartModelList= new ArrayList<>();
        //init firebase
        rootNode = FirebaseDatabase.getInstance();
        reference = rootNode.getReference().child("MyCart");

        reference.orderByChild("cartUserId").equalTo(FirebaseAuth.getInstance().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                cartModelList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {


                    CartModel cartModel=dataSnapshot.getValue(CartModel.class);

                    cartModelList.add(cartModel);

                }


                MyCartAdapter  adapter= new MyCartAdapter(cartModelList,getContext());

                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

                recyclerView.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Log.e(TAG,"Error :"+error.getMessage());

            }
        });
    }



    private void init() {
        recyclerView=rootView.findViewById(R.id.recyclerView);
//        search_bar=rootView.findViewById(R.id.search_bar);

    }
}