package com.example.ecommercapp.Admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ecommercapp.Admin.Description.AdminProductDescriptionFragment;
import com.example.ecommercapp.Admin.Home.AdminHome;
import com.example.ecommercapp.Admin.Manage.AdminUserManageFragment;
import com.example.ecommercapp.Admin.ViewOrder.AdminViewOrderFragment;
import com.example.ecommercapp.Admin.ViewProducts.AdminProductFragment;
import com.example.ecommercapp.R;
import com.example.ecommercapp.Seller.Screen.Home.SellerHome;
import com.example.ecommercapp.Sign.SplashScreen;
import com.example.ecommercapp.User.Profile.ProfileFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class Admin_nav_menu extends AppCompatActivity {


    private  String  TAG="Admin_nav_menu";
    ImageView userImage;
    ImageView edit_icon;
    ImageView logoutBtn;
    TextView userName;
    //Firebase authentication
    FirebaseDatabase rootNode;
    DatabaseReference reference;

    //to store the image for database
    StorageReference storageReference;

    //session for get values
    SharedPreferences sharedpreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_nav_menu);
        init();


     // openFragmentDefault(new AdminHome());
      openFragmentDefault(new AdminHome());
   //   openFragmentDefault(new AdminProductDescriptionFragment());


        edit_icon.setOnClickListener(v -> {


            openFragment(new ProfileFragment());

        });

        logoutBtn.setOnClickListener(v -> {
            AlertMessageLogout();
        });
    }

    private void AlertMessageLogout(){

        android.app.AlertDialog.Builder dialog = new android.app.AlertDialog.Builder(this);
        dialog.setTitle( "Task Status" )
                .setIcon(R.drawable.ic_baseline_warning_24)
                .setMessage("Do You want Logout")
                .setPositiveButton("Ok", (dialog1, which) -> LogOut())
                .setNegativeButton("Cancel", (dialoginterface, i) -> dialoginterface.cancel()).show();

    }


    private void init() {

        sharedpreferences=getSharedPreferences("user_details",MODE_PRIVATE);
        edit_icon=findViewById(R.id.edit_icon);
        userImage=findViewById(R.id.userImage);
        userName=findViewById(R.id.userName);
        logoutBtn=findViewById(R.id.logoutBtn);

    }


    @Override
    protected void onStart() {

        LoadData();
        super.onStart();

    }

    private void LoadData() {

        storageReference= FirebaseStorage.getInstance().getReference();
        StorageReference profileRef=storageReference.child("Users/"+ FirebaseAuth.getInstance().getUid()+"/Profile.jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(uri -> {

            try {
                Picasso.get().load(uri).into(userImage);

            }catch (Exception e){

            }
        });




        rootNode = FirebaseDatabase.getInstance();
        reference= FirebaseDatabase.getInstance().getReference();
        reference = rootNode.getReference("UserData").child(FirebaseAuth.getInstance().getUid());

        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String  name = dataSnapshot.child("name").getValue(String.class);

                userName.setText(name+"");



            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                Log.e(TAG, "User Authentication"+databaseError.getMessage());

            }
        };
        reference.addListenerForSingleValueEvent(eventListener);

    }

    public void openFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.admin_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }


    private void openFragmentDefault(Fragment fragment) {

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.admin_container, fragment);
        transaction.commit();

    }


    private void LogOut() {

        //sessions clear when user log out
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putBoolean("isLoggedIn",false);
        editor.putString("userType","");
        editor.clear();
        editor.commit();


        startActivity(new Intent(this, SplashScreen.class));
        finish();
    }
}