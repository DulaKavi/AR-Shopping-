package com.example.ecommercapp.User.MyOrder.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.ecommercapp.R;
import com.example.ecommercapp.Seller.Screen.ViewOrder.Model.OrderModel;
import com.example.ecommercapp.User.MyCart.Adapter.MyCartAdapter;

import java.util.List;

public class MyOrderAdapter  extends  RecyclerView.Adapter<MyOrderAdapter.MyOrderViewHolder>{

    List<OrderModel> orderModelList;
    Context context;

    public MyOrderAdapter(List<OrderModel> orderModelList, Context context) {
        this.orderModelList = orderModelList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyOrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view ;
        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate( R.layout.user_order_card_item,parent,false) ;

        final MyOrderViewHolder viewHolder= new MyOrderViewHolder(view);



        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyOrderViewHolder holder, int position) {

        RequestOptions requestOptions = new RequestOptions()
                .placeholder(R.drawable.loading);

        Glide.with(context)
                .load(orderModelList.get(position).getProductImageUrl())
                .apply(requestOptions)
                .into(holder.productImage);


        holder.productNameTxt.setText("Product Name : "+orderModelList.get(position).getProductName()+"");
        holder.productPriceTxt.setText("Unit Price Rs : "+orderModelList.get(position).getUnitPrice()+"");
        holder.productQtyTxt.setText("Qty : "+orderModelList.get(position).getQty()+"");
        holder.totalTxt.setText("Total Rs: "+orderModelList.get(position).getQty()*orderModelList.get(position).getUnitPrice()+"");
    }

    @Override
    public int getItemCount() {
        return orderModelList.size();
    }

    class MyOrderViewHolder extends RecyclerView.ViewHolder {
        TextView productNameTxt;
        TextView productPriceTxt;
        TextView productQtyTxt;
        TextView totalTxt;
        ImageView productImage;



        public MyOrderViewHolder(@NonNull View itemView) {
            super(itemView);

            productImage=itemView.findViewById(R.id.productImage);
            productNameTxt=itemView.findViewById(R.id.productNameTxt);
            productPriceTxt=itemView.findViewById(R.id.productPriceTxt);
            productQtyTxt=itemView.findViewById(R.id.productQtyTxt);
            totalTxt=itemView.findViewById(R.id.totalTxt);


        }
    }

}
