package com.example.ecommercapp.Admin.ViewOrder;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ecommercapp.R;
import com.example.ecommercapp.Seller.Screen.ViewOrder.Model.OrderModel;
import com.example.ecommercapp.User.MyOrder.Adapter.MyOrderAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class AdminViewOrderFragment extends Fragment {

    private  String TAG="AdminViewOrderFragment";
    View rootView;
    RecyclerView recyclerView;
    List<OrderModel> orderModelList;
    //real time database reference
    DatabaseReference reference;
    FirebaseDatabase rootNode;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView=inflater.inflate(R.layout.admin_view_order, container, false);
        init();
        loadOrder();
        return rootView;
    }

    @Override
    public void onStart() {
        loadOrder();
        super.onStart();
    }

    @Override
    public void onResume() {
        loadOrder();
        super.onResume();
    }

    private void loadOrder() {

        orderModelList=new ArrayList<>();
        //init firebase
        rootNode = FirebaseDatabase.getInstance();
        reference = rootNode.getReference().child("MyOrders");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                orderModelList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    OrderModel orderModel=dataSnapshot.getValue(OrderModel.class);

                    orderModelList.add(orderModel);

                }


                MyOrderAdapter adapter= new MyOrderAdapter(orderModelList,getContext());
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
    }
}