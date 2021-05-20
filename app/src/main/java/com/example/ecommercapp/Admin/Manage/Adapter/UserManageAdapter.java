package com.example.ecommercapp.Admin.Manage.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.ecommercapp.R;
import com.example.ecommercapp.Sign.Model.UserModel;
import com.example.ecommercapp.User.Home.Adapter.ItemAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class UserManageAdapter  extends  RecyclerView.Adapter<UserManageAdapter.UserManageViewHolder>{

    List<UserModel> userModelList;
    Context context;

    public UserManageAdapter(List<UserModel> userModelList, Context context) {
        this.userModelList = userModelList;
        this.context = context;
    }

    @NonNull
    @Override
    public UserManageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view ;
        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate( R.layout.admin_user_card,parent,false) ;


        final UserManageViewHolder viewHolder= new UserManageViewHolder(view);


        viewHolder.activeBtn.setOnClickListener(v -> {

            UserModel userModel=  userModelList.get(viewHolder.getAdapterPosition());
            userModel.setUStatus("Active");

            DatabaseReference reference= FirebaseDatabase.getInstance().getReference("UserData");


            reference.child(userModel.getUserId()).setValue(userModel).addOnSuccessListener(aVoid -> {
                dialogBox("Success ","User Activation");
            }).addOnFailureListener(e -> {
                 dialogBox(e.getMessage(),"User Activation");
            });


        });

        viewHolder.inActiveButton.setOnClickListener(v -> {

            UserModel userModel=  userModelList.get(viewHolder.getAdapterPosition());
            userModel.setUStatus("Inactive");

            DatabaseReference reference= FirebaseDatabase.getInstance().getReference("UserData");


            reference.child(userModel.getUserId()).setValue(userModel).addOnSuccessListener(aVoid -> {
                dialogBox("Success ","User Activation");
            }).addOnFailureListener(e -> {
                dialogBox(e.getMessage(),"User Activation");
            });


        });

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull UserManageViewHolder holder, int position) {
        RequestOptions requestOptions = new RequestOptions()
                .placeholder(R.drawable.loading);

        Glide.with(context)
                .load(userModelList.get(position).getUImages())
                .apply(requestOptions)
                .into(holder.userImage);

        holder.userNameTxt.setText("Name : "+userModelList.get(position).getName()+"");
        holder.contactTxt.setText("Contact : "+userModelList.get(position).getContact()+"");
        holder.emailTxt.setText("Email : "+userModelList.get(position).getEmail()+"");
        holder.statusTxt.setText("Status : "+userModelList.get(position).getUStatus()+"");

    }

    @Override
    public int getItemCount() {
        return userModelList.size();
    }


    class  UserManageViewHolder extends RecyclerView.ViewHolder{

        TextView userNameTxt;
        TextView emailTxt;
        TextView contactTxt;
        TextView statusTxt;
        ImageView userImage;

        Button activeBtn;
        Button inActiveButton;


        public UserManageViewHolder(@NonNull View itemView) {

            super(itemView);

            userImage=itemView.findViewById(R.id.userImage);
            userNameTxt=itemView.findViewById(R.id.userNameTxt);
            emailTxt=itemView.findViewById(R.id.emailTxt);
            contactTxt=itemView.findViewById(R.id.contactTxt);
            statusTxt=itemView.findViewById(R.id.statusTxt);
            inActiveButton=itemView.findViewById(R.id.inActiveButton);
            activeBtn=itemView.findViewById(R.id.activeBtn);


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
