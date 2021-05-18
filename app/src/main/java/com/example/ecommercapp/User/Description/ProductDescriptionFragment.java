package com.example.ecommercapp.User.Description;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.ecommercapp.R;
import com.example.ecommercapp.Seller.Screen.AddProduct.Model.ProductModel;
import com.example.ecommercapp.User.MyCart.Model.CartModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProductDescriptionFragment extends Fragment {


    public ProductDescriptionFragment() { }


    private  String TAG="ProductDescriptionFragment";



    View rootView;
    TextView product_nameTxt;
    TextView product_descriptionTxt;
    TextView unit_priceTxt;
    TextView qtyTxt;
    TextView avbTxt;
    FloatingActionButton inQty;
    FloatingActionButton deQty;
    FloatingActionButton favBtn;
    Button addToCartBtn;
    ImageView product_image;

    int Qty=0;


    ProductModel productModel;



    FirebaseDatabase rootNode;
    DatabaseReference reference;

    long TableId=0;


    public ProductDescriptionFragment(ProductModel productModel) {
        this.productModel=productModel;
        Log.d(TAG,productModel.getProductImageUrl());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        rootView= inflater.inflate(R.layout.user_product_description_fragment, container, false);

        init();

        return rootView;
    }

    private void init() {


//define the database variable
        reference=FirebaseDatabase.getInstance().getReference().child("MyCart");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists())
                    TableId=(snapshot.getChildrenCount());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        qtyTxt=rootView.findViewById(R.id.qtyTxt);
        avbTxt=rootView.findViewById(R.id.avbTxt);
        product_nameTxt=rootView.findViewById(R.id.product_nameTxt);
        product_descriptionTxt=rootView.findViewById(R.id.product_descriptionTxt);
        addToCartBtn=rootView.findViewById(R.id.addToCartBtn);
        favBtn=rootView.findViewById(R.id.favBtn);
        unit_priceTxt=rootView.findViewById(R.id.unit_priceTxt);
        inQty=rootView.findViewById(R.id.inQty);
        deQty=rootView.findViewById(R.id.deQty);
        product_image=rootView.findViewById(R.id.product_image);

        inQty.setOnClickListener(v -> {
            Qty++;
            qtyTxt.setText(Qty+"");
        });

        deQty.setOnClickListener(v -> {
            if(Qty<=0) return;
           -- Qty;
            qtyTxt.setText(Qty+"");
        });



        addToCartBtn.setOnClickListener(v -> {



            if(productModel.getAvailableQuantity()== 0){

                dialogBox("This product not available !","Add to cart");

                return;
            }

            if(Qty==0){

                dialogBox("Please add Qty !","Add to cart");

                return;
            }



            addToCartDB();

        });
    }

    private void addToCartDB() {

        rootNode = FirebaseDatabase.getInstance();
        reference= FirebaseDatabase.getInstance().getReference();
        reference = rootNode.getReference("MyCart");


        CartModel cartModel= new CartModel();


        cartModel.setCartUserId(FirebaseAuth.getInstance().getUid());
        cartModel.setProductName(productModel.getProductName());
        cartModel.setProductImageUrl(productModel.getProductImageUrl());
        cartModel.setUnitPrice(productModel.getUnitPrice());
        cartModel.setQty(Qty);
        cartModel.setSellerId(productModel.getProductSellerID());
        cartModel.setCartId(TableId+"");


       reference.child(String.valueOf(TableId+1)).setValue(cartModel).addOnSuccessListener(aVoid -> {
           dialogBox("Success !","Add to cart");
       }).addOnFailureListener(e -> {
           dialogBox("Failed !","Add to cart");
       });


    }

    @Override
    public void onStart() {
        LoadData();
        super.onStart();
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



        if(productModel.getAvailableQuantity()>0){
            avbTxt.setText("Stock Available");
            avbTxt.setTextColor(Color.parseColor("#FF4CAF50"));
        }else{
            avbTxt.setText("Stock unavailable");
            avbTxt.setTextColor(Color.parseColor("#FFE32E2E"));

        }

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