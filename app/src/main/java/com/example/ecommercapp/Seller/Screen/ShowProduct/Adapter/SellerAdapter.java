package com.example.ecommercapp.Seller.Screen.ShowProduct.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.ecommercapp.R;
import com.example.ecommercapp.Seller.Screen.AddProduct.Model.ProductModel;
import com.example.ecommercapp.Seller.Screen.Description.SellerProductDescription;

import java.util.List;

public class SellerAdapter extends  RecyclerView.Adapter <SellerAdapter.SellerViewHolder> {


    List<ProductModel> productModelList;
    Context context;


    public SellerAdapter(List<ProductModel> productModelList, Context context) {
        this.productModelList = productModelList;
        this.context = context;
    }

    @NonNull
    @Override
    public SellerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view ;
        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate( R.layout.user_item_card_layout,parent,false) ;
        final SellerViewHolder viewHolder= new SellerViewHolder(view);

        viewHolder.card.setOnClickListener(v -> {

            FragmentTransaction fragmentTransaction = ((FragmentActivity) context).getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.seller_container,new SellerProductDescription(
                    productModelList.get(viewHolder.getAdapterPosition())

            ))
                    .addToBackStack(null)
                    .commit();


        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull SellerViewHolder holder, int position) {

        RequestOptions requestOptions = new RequestOptions()
                .placeholder(R.drawable.loading);

        Glide.with(context)
                .load(productModelList.get(position).getProductImageUrl())
                .apply(requestOptions)
                .into(holder.imageView);


        holder.item_name_txt.setText(productModelList.get(position).getProductName()+"");
        holder.item_price_txt.setText("Rs : "+productModelList.get(position).getUnitPrice()+"");

    }

    @Override
    public int getItemCount() {
        return productModelList.size();
    }


    class SellerViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView item_name_txt;
        TextView item_price_txt;
        CardView card;

        public SellerViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView =itemView.findViewById(R.id.item_image);
            item_name_txt =itemView.findViewById(R.id.item_name_txt);
            item_price_txt =itemView.findViewById(R.id.item_price_txt);
            card =itemView.findViewById(R.id.card);
        }
    }
}
