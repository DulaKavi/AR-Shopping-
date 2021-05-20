package com.example.ecommercapp.Seller.Screen.Description;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.ecommercapp.R;
import com.example.ecommercapp.Seller.Screen.AddProduct.Model.ProductModel;


public class SellerProductDescription extends Fragment {

    public SellerProductDescription() {
    }

    ProductModel productModel;

    public SellerProductDescription(ProductModel productModel) {
        this.productModel=productModel;

    }

    private  String TAG="SellerProductDescription";

    View rootView;

    TextView product_nameTxt;
    TextView product_descriptionTxt;
    TextView unit_priceTxt;
    TextView qtyTxt;
    TextView avbTxt;
    ImageView product_image;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView=inflater.inflate(R.layout.seller_product_decription, container, false);

        init();
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



        if(productModel.getAvailableQuantity()>0){
            avbTxt.setText("Stock Available");
            avbTxt.setTextColor(Color.parseColor("#FF4CAF50"));
        }else{
            avbTxt.setText("Stock unavailable");
            avbTxt.setTextColor(Color.parseColor("#FFE32E2E"));

        }



    }

    private void init() {


        qtyTxt=rootView.findViewById(R.id.qtyTxt);
        avbTxt=rootView.findViewById(R.id.avbTxt);
        product_nameTxt=rootView.findViewById(R.id.product_nameTxt);
        product_descriptionTxt=rootView.findViewById(R.id.product_descriptionTxt);
        unit_priceTxt=rootView.findViewById(R.id.unit_priceTxt);
        product_image=rootView.findViewById(R.id.product_image);




    }
}