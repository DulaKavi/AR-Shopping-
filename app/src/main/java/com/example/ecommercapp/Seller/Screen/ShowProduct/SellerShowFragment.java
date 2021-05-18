package com.example.ecommercapp.Seller.Screen.ShowProduct;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.example.ecommercapp.R;
import com.example.ecommercapp.Seller.Screen.AddProduct.Model.ProductModel;
import com.example.ecommercapp.Seller.Screen.ShowProduct.Adapter.SellerAdapter;
import com.example.ecommercapp.User.Home.Adapter.ItemAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class SellerShowFragment extends Fragment {

    View rootView;
    SearchView search_bar;
    RecyclerView recyclerView;



    //real time database reference
    DatabaseReference reference;
    FirebaseDatabase rootNode;


    List<ProductModel> productModelList;

    String TAG="SellerShowFragment";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView=inflater.inflate(R.layout.seller_show, container, false);

        init();


        search_bar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {

                loadData(s);

                return false;
            }
        });

        return rootView;
    }

    @Override
    public void onStart() {
        loadData("");
        super.onStart();
    }


    @Override
    public void onResume() {
        loadData("");
        super.onResume();
    }


    private void loadData(String searchText) {


        rootNode = FirebaseDatabase.getInstance();
        reference = rootNode.getReference().child("Products");
        productModelList.clear();

        reference.orderByChild("productName").startAt(searchText.toUpperCase()).endAt(searchText + "\uf8ff").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                productModelList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {


                    ProductModel productModel=dataSnapshot.getValue(ProductModel.class);

                    if(productModel.getProductStatus().equals("Approved")){
                        productModelList.add(productModel);
                        //pd.dismiss();
                    }

                }

                SellerAdapter adapter= new SellerAdapter(productModelList,getContext());
                StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL);

                recyclerView.setLayoutManager(staggeredGridLayoutManager);
                recyclerView.setAdapter(adapter);


            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {


                Log.e(TAG,"Error :"+error.getMessage());
            }
        });


    }

    private void init() {
        productModelList= new ArrayList<>();
        search_bar=rootView.findViewById(R.id.search_bar);
        recyclerView=rootView.findViewById(R.id.recyclerView);
    }
}