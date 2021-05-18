package com.example.ecommercapp.User.Home;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
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
import com.example.ecommercapp.User.Home.Adapter.ItemAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class UserHomeFragment extends Fragment {


    private  String TAG="UserHomeFragment";
private static final int NUM_COLUMNS = 2;

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
RecyclerView recyclerView;
SearchView search_bar;

View rootView;


    //real time database reference
    DatabaseReference reference;
    FirebaseDatabase rootNode;




    List<ProductModel> productModelList;

    public UserHomeFragment() {
        productModelList=new ArrayList<>();

        productModelList.clear();

    }



    ProgressDialog pd;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView=inflater.inflate(R.layout.user_home_fragment, container, false);
        init();
        return rootView;
    }

    private void init() {
        pd= new ProgressDialog(getContext());
        search_bar=rootView.findViewById(R.id.search_bar);
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
        recyclerView=rootView.findViewById(R.id.recyclerView);

        search_bar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                LoadData(s);
//                Toast.makeText(getContext(),s+"",
//                        Toast.LENGTH_SHORT).show();
                return false;
            }
        });



        //load by category
        t_shirt_card.setOnClickListener(v -> {
            LoadDataCategoryWays("T-Shirts");
        });
        frocks_card.setOnClickListener(v -> {
            LoadDataCategoryWays("Frocks");
        });

        watches_card.setOnClickListener(v -> {
            LoadDataCategoryWays("Watches");
        });

        glasses_card.setOnClickListener(v -> {
                LoadDataCategoryWays("Glasses");
            });


        hats_card.setOnClickListener(v -> {
                LoadDataCategoryWays("Hats");
            });


//        jewelries_card.setOnClickListener(v -> {
//                LoadDataCategoryWays("Jewelries");
//            });


        bags_card.setOnClickListener(v -> {
                LoadDataCategoryWays("Bags");
            });
        mobile_card.setOnClickListener(v -> {
                LoadDataCategoryWays("Mobile Phones");
            });
        laptops_card.setOnClickListener(v -> {
                LoadDataCategoryWays("Laptops");
            });
        headphones_card.setOnClickListener(v -> {
                LoadDataCategoryWays("Headphones");
            });




//        LoadData("");
    }

    private void LoadDataCategoryWays(String cat) {
//        pd.setTitle("Loading...");
//        pd.show();
        //init array list
        productModelList= new ArrayList<>();


        //init firebase
        rootNode = FirebaseDatabase.getInstance();
        reference = rootNode.getReference().child("Products");
        productModelList.clear();

        reference.orderByChild("productCategory").equalTo(cat).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {


                    ProductModel productModel=dataSnapshot.getValue(ProductModel.class);


                    if(productModel.getProductStatus().equals("Approved")){
                        productModelList.add(productModel);

                    }

                }
                pd.dismiss();
                ItemAdapter adapter= new ItemAdapter(productModelList,getContext());
                StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(NUM_COLUMNS, LinearLayoutManager.VERTICAL);

                recyclerView.setLayoutManager(staggeredGridLayoutManager);
                recyclerView.setAdapter(adapter);


            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {


                Log.e(TAG,"Error :"+error.getMessage());
            }
        });

    }

    @Override
    public void onStart() {
        LoadData("");
        super.onStart();

    }

    private void LoadData(String searchText) {
//        pd.setTitle("Loading...");
//        pd.show();
        Log.e(TAG,"Function Calling ");

        //init firebase
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

                ItemAdapter adapter= new ItemAdapter(productModelList,getContext());
                StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(NUM_COLUMNS, LinearLayoutManager.VERTICAL);

                recyclerView.setLayoutManager(staggeredGridLayoutManager);
                recyclerView.setAdapter(adapter);


            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {


                Log.e(TAG,"Error :"+error.getMessage());
            }
        });
    }
}