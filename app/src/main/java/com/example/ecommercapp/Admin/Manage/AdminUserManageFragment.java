package com.example.ecommercapp.Admin.Manage;

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

import com.example.ecommercapp.Admin.Manage.Adapter.UserManageAdapter;
import com.example.ecommercapp.R;
import com.example.ecommercapp.Seller.Screen.AddProduct.Model.ProductModel;
import com.example.ecommercapp.Seller.Screen.ViewOrder.Model.OrderModel;
import com.example.ecommercapp.Sign.Model.UserModel;
import com.example.ecommercapp.User.Home.Adapter.ItemAdapter;
import com.example.ecommercapp.User.MyOrder.Adapter.MyOrderAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class AdminUserManageFragment extends Fragment {

    private  String TAG="AdminUserManageFragment";

    View rootView;

    SearchView search_bar;
    RecyclerView recyclerView;


    List<UserModel> userModelList;


    //real time database reference
    DatabaseReference reference;
    FirebaseDatabase rootNode;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView=inflater.inflate(R.layout.admin_user_manage, container, false);
        init();


        search_bar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                LoadUser(s);
//                Toast.makeText(getContext(),s+"",
//                        Toast.LENGTH_SHORT).show();
                return false;
            }
        });
        return rootView;
    }

    @Override
    public void onStart() {
        LoadUser("");
        super.onStart();
    }

    private void LoadUser(String searchTxt) {

        rootNode = FirebaseDatabase.getInstance();
        reference = rootNode.getReference().child("UserData");
        userModelList.clear();

        reference.orderByChild("name").startAt(searchTxt.toUpperCase()).endAt(searchTxt + "\uf8ff").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                userModelList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {


                    UserModel userModel=dataSnapshot.getValue(UserModel.class);

                    if(! userModel.getUType().equals("Admin")){
                        userModelList.add(userModel);
                    }



                }


                UserManageAdapter  adapter= new UserManageAdapter(userModelList,getContext());
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

        userModelList=new ArrayList<>();
        search_bar=rootView.findViewById(R.id.search_bar);
        recyclerView=rootView.findViewById(R.id.recyclerView);
    }
}