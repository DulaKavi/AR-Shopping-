package com.example.ecommercapp.Admin.Description;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.service.controls.actions.FloatAction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.ecommercapp.R;
import com.example.ecommercapp.Seller.Screen.AddProduct.Model.ProductModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class AdminProductDescriptionFragment extends Fragment {


    public AdminProductDescriptionFragment() {
    }

    View rootView;
    TextView product_nameTxt;
    TextView product_descriptionTxt;
    TextView unit_priceTxt;
    TextView qtyTxt;
    TextView avbTxt;
    TextView statusTxt;
    ImageView product_image;
    FloatingActionButton approvedBtn;
    FloatingActionButton desApprovedBtn;
    FloatingActionButton deleteBtn;

    //real time database reference
    DatabaseReference reference;
    FirebaseDatabase rootNode;


    private  String TAG="AdminProductDescriptionFragment";


    ProductModel productModel;

    public AdminProductDescriptionFragment(ProductModel productModel) {
        this.productModel = productModel;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView=inflater.inflate(R.layout.admin_product_description, container, false);

        init();

        approvedBtn.setOnClickListener(v -> {

            productModel.setProductStatus("Approved");

            reference.child(String.valueOf(productModel.getProductId())).setValue(productModel).addOnSuccessListener(aVoid ->

                    dialogBox("Successfully","Product Status Change")

            ).addOnFailureListener(e ->

                    dialogBox("Failed","Product Status Change")

            );


        });


        desApprovedBtn.setOnClickListener(v -> {

            productModel.setProductStatus("InProgress");

            reference.child(String.valueOf(productModel.getProductId())).setValue(productModel).addOnSuccessListener(aVoid ->

                    dialogBox("Successfully","Product Status Change")

            ).addOnFailureListener(e ->

                    dialogBox("Failed","Product Status Change")

            );
        });



        deleteBtn.setOnClickListener(v -> {

           // dialogBox("Success !","Remove Cart"+String.valueOf(productModel.getProductId()));


            reference.child(String.valueOf(productModel.getProductId())).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot ds : snapshot.getChildren())
                    {
                        ds.getRef().removeValue();
                        dialogBox("Success !","Remove Product");





                        //   context.startActivity(new Intent(context,User_nav_menu.class));

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    dialogBox("Failed to delete","Remove Product");
                }
            });

        });

        return rootView;
    }

    @Override
    public void onStart() {
        LoadData();
        super.onStart();
    }


    @Override
    public void onResume() {

        LoadData();
        super.onResume();


    }
    private void LoadData() {

        RequestOptions requestOptions = new RequestOptions()
                .placeholder(R.drawable.loading);

        Glide.with(getContext())
                .load(productModel.getProductImageUrl())
                .apply(requestOptions)
                .into(product_image);



        product_nameTxt.setText(productModel.getProductName()+"");
        product_descriptionTxt.setText(productModel.getProductDescription()+"");
        unit_priceTxt.setText(productModel.getUnitPrice()+"");
        statusTxt.setText(productModel.getProductStatus()+"");



        if(productModel.getAvailableQuantity()>0){
            avbTxt.setText("Stock Available");
            avbTxt.setTextColor(Color.parseColor("#FF4CAF50"));
        }else{
            avbTxt.setText("Stock unavailable");
            avbTxt.setTextColor(Color.parseColor("#FFE32E2E"));

        }



    }

    private void init() {

        //init firebase
        rootNode = FirebaseDatabase.getInstance();
        reference = rootNode.getReference().child("Products");



        qtyTxt=rootView.findViewById(R.id.qtyTxt);
        avbTxt=rootView.findViewById(R.id.avbTxt);
        product_nameTxt=rootView.findViewById(R.id.product_nameTxt);
        product_descriptionTxt=rootView.findViewById(R.id.product_descriptionTxt);
        unit_priceTxt=rootView.findViewById(R.id.unit_priceTxt);
        product_image=rootView.findViewById(R.id.product_image);
        statusTxt=rootView.findViewById(R.id.statusTxt);
        desApprovedBtn=rootView.findViewById(R.id.desApprovedBtn);
        approvedBtn=rootView.findViewById(R.id.approvedBtn);
        deleteBtn=rootView.findViewById(R.id.deleteBtn);




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
}