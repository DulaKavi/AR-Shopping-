package com.example.ecommercapp.User.MyCart.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.ecommercapp.R;
import com.example.ecommercapp.User.Description.ProductDescriptionFragment;
import com.example.ecommercapp.User.MyCart.Model.CartModel;
import com.example.ecommercapp.User.Payment.PaymentFragment;
import com.example.ecommercapp.User.User_nav_menu;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class MyCartAdapter extends RecyclerView.Adapter<MyCartAdapter.MyCartViewHolder> {

    List<CartModel> cartModelList;
    Context context;


    public MyCartAdapter(List<CartModel> cartModelList, Context context) {
        this.cartModelList = cartModelList;
        this.context = context;
    }



    @NonNull
    @Override
    public MyCartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view ;
        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate( R.layout.user_cart_item_view,parent,false) ;

        final MyCartViewHolder viewHolder= new MyCartViewHolder(view);


        viewHolder.closeBtn.setOnClickListener(v -> {
//
            FirebaseDatabase     rootNode = FirebaseDatabase.getInstance();
            DatabaseReference reference = rootNode.getReference("MyCart");


            reference.orderByChild("cartId").equalTo(cartModelList.get(viewHolder.getAdapterPosition()).getCartId()+"")
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                        {
                            for (DataSnapshot ds : dataSnapshot.getChildren())
                            {
                                ds.getRef().removeValue();
                                dialogBox("Success !","Remove Cart");



                             //   context.startActivity(new Intent(context,User_nav_menu.class));

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError)
                        {
//                            Toast.makeText(context, "Failed to delete", Toast.LENGTH_SHORT).show();


                            dialogBox("Failed to delete","Remove Cart");
                        }
                    });


        });

        viewHolder.paymentBtn.setOnClickListener(v -> {


            FragmentTransaction fragmentTransaction = ((FragmentActivity) context).getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.container,new PaymentFragment(
                    cartModelList.get(viewHolder.getAdapterPosition())

            ))
                    .addToBackStack(null)
                    .commit();


        });





        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyCartViewHolder holder, int position) {

        RequestOptions requestOptions = new RequestOptions()
                .placeholder(R.drawable.loading);

        Glide.with(context)
                .load(cartModelList.get(position).getProductImageUrl())
                .apply(requestOptions)
                .into(holder.productImage);


        holder.productNameTxt.setText("Product Name : "+cartModelList.get(position).getProductName()+"");
        holder.productPriceTxt.setText("Unit Price Rs : "+cartModelList.get(position).getUnitPrice()+"");
        holder.productQtyTxt.setText("Qty : "+cartModelList.get(position).getQty()+"");
        holder.totalTxt.setText("Total Rs: "+cartModelList.get(position).getQty()*cartModelList.get(position).getUnitPrice()+"");


    }

    @Override
    public int getItemCount() {
        return cartModelList.size();
    }

    class MyCartViewHolder extends RecyclerView.ViewHolder{



        TextView productNameTxt;
        TextView productPriceTxt;
        TextView productQtyTxt;
        TextView totalTxt;
        ImageView closeBtn;
        ImageView productImage;

        Button paymentBtn;



        public MyCartViewHolder(@NonNull View itemView) {
            super(itemView);


            productImage=itemView.findViewById(R.id.productImage);
            productNameTxt=itemView.findViewById(R.id.productNameTxt);
            productPriceTxt=itemView.findViewById(R.id.productPriceTxt);
            productQtyTxt=itemView.findViewById(R.id.productQtyTxt);
            totalTxt=itemView.findViewById(R.id.totalTxt);
            closeBtn=itemView.findViewById(R.id.closeBtn);
            paymentBtn=itemView.findViewById(R.id.paymentBtn);

        }
    }


    public void dialogBox(String message,String title) {


        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
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
