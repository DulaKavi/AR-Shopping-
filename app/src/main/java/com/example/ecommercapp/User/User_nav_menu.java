package com.example.ecommercapp.User;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ecommercapp.R;
import com.example.ecommercapp.Sign.SplashScreen;
import com.example.ecommercapp.User.ArShop.ArPermissionFragment;
import com.example.ecommercapp.User.ArShop.ArShopFragment;
import com.example.ecommercapp.User.Home.UserHomeFragment;
import com.example.ecommercapp.User.MyCart.MyCartFragment;
import com.example.ecommercapp.User.MyOrder.MyOrderFragment;
import com.example.ecommercapp.User.Payment.PaymentFragment;
import com.example.ecommercapp.User.Profile.ProfileFragment;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class User_nav_menu extends AppCompatActivity {

    String TAG="User_nav_menu";


    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ImageView menu_icon;
    ImageView cart_icon;
    ActionBarDrawerToggle toggle;
    FrameLayout contentView;
    static final float END_SCALE = 0.7f;

    TextView cart_badge_text_view;



    //real time database reference
    DatabaseReference reference;
    FirebaseDatabase rootNode;





    int numCart=0;

    //session for get values
    SharedPreferences sharedpreferences;

    StorageReference storageReference;


    ImageView nav_profile;
    TextView navBar_name;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_nav_menu);

        init();
        navigationDrawer();

       // openFragmentDefault(new UserHomeFragment());
        openFragmentDefault(new UserHomeFragment());


      if(numCart==0){ cart_badge_text_view.setVisibility(View.GONE); }else{ cart_badge_text_view.setText(numCart+""); }

        cart_icon.setOnClickListener(v -> { openFragment(new MyCartFragment()); });



    }

    @Override
    protected void onStart() {
        LoadCartData();
        LoadUserData();
        super.onStart();
    }


    @Override
    protected void onResume() {
        LoadCartData();
        LoadUserData();
        super.onResume();


    }

    private void LoadUserData() {
        //loading image from the database
        storageReference= FirebaseStorage.getInstance().getReference();
        StorageReference profileRef=storageReference.child("Users/"+FirebaseAuth.getInstance().getUid()+"/Profile.jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(uri -> {

            try {
                Picasso.get().load(uri).into(nav_profile);
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
                navBar_name.setText("@"+name);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                Log.e(TAG, "User Authentication"+databaseError.getMessage());

            }
        };
        reference.addListenerForSingleValueEvent(eventListener);


    }





    private void LoadCartData() {
        //init firebase
        rootNode = FirebaseDatabase.getInstance();
        reference = rootNode.getReference().child("MyCart");

        reference.orderByChild("cartUserId").equalTo(FirebaseAuth.getInstance().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                numCart=0;

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    numCart++;

                }

                if(numCart==0){
                    cart_badge_text_view.setVisibility(View.GONE);
                }else{
                    cart_badge_text_view.setVisibility(View.VISIBLE);
                    cart_badge_text_view.setText(numCart+"");
                }


                Log.e(TAG,numCart+"");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Log.e(TAG,"Error :"+error.getMessage());

            }
        });
    }

    private void init() {




        //declare a session
        sharedpreferences=getSharedPreferences("user_details",MODE_PRIVATE);

        menu_icon=findViewById(R.id.menu_icon);
        cart_icon=findViewById(R.id.cart_icon);

        drawerLayout = findViewById(R.id.drawer_layout);
        cart_badge_text_view = findViewById(R.id.cart_badge_text_view);
        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        contentView = findViewById(R.id.container);


        navigationView=findViewById(R.id.navigation_view);
        View headerView=navigationView.getHeaderView( 0 );
        navBar_name=headerView.findViewById( R.id.navBar_name );
        nav_profile=headerView.findViewById( R.id.nav_profile );
    }


    private void navigationDrawer() {

        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(menuItem -> {
            switch (menuItem.getItemId()) {
                case R.id.nav_home:
                    navigationView.setCheckedItem(R.id.nav_home);
                    drawerLayout.closeDrawer(GravityCompat.START);
                    openFragment(new UserHomeFragment());
                    break;


                case R.id.nav_profile:
                    navigationView.setCheckedItem(R.id.nav_profile);
                    drawerLayout.closeDrawer(GravityCompat.START);
                    openFragment(new ProfileFragment());
                    break;

                case R.id.nav_cart:
                    navigationView.setCheckedItem(R.id.nav_cart);
                    drawerLayout.closeDrawer(GravityCompat.START);
                    openFragment(new MyCartFragment());
                    break;
                case R.id.nav_orders:
                    navigationView.setCheckedItem(R.id.nav_orders);
                    drawerLayout.closeDrawer(GravityCompat.START);
                    openFragment(new MyOrderFragment());
                    break;

                case R.id.nav_views:
                    navigationView.setCheckedItem(R.id.nav_views);
                    drawerLayout.closeDrawer(GravityCompat.START);
                    openFragment(new ArPermissionFragment());
                    break;
                case R.id.nav_logout:
                    navigationView.setCheckedItem(R.id.nav_logout);
                    drawerLayout.closeDrawer(GravityCompat.START);
                    AlertMessageLogout();


                    break;

            }
            return false;
        });

        menu_icon.setOnClickListener(v -> {
            if (drawerLayout.isDrawerVisible(GravityCompat.START))
                drawerLayout.closeDrawer(GravityCompat.START);
            else drawerLayout.openDrawer(GravityCompat.START);
        });

         animateNavigationDrawer();
    }



    private void AlertMessageLogout(){

        android.app.AlertDialog.Builder dialog = new android.app.AlertDialog.Builder(this);
        dialog.setTitle( "Task Status" )
                .setIcon(R.drawable.ic_baseline_warning_24)
                .setMessage("Do You want Logout")
                .setPositiveButton("Ok", (dialog1, which) -> LogOut())
                .setNegativeButton("Cancel", (dialoginterface, i) -> dialoginterface.cancel()).show();

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



    private void animateNavigationDrawer() {

        drawerLayout.setScrimColor(getResources().getColor(R.color.blue));
        drawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {
                //Scale the view based on current slide offset
                final float diffScaledOffset = slideOffset * (1 - END_SCALE);
                final float offsetScale = 1 - diffScaledOffset;
                contentView.setScaleX(offsetScale);
                contentView.setScaleY(offsetScale);

                //Translating the view accounting for the scaled width
                final float xOffset = drawerView.getWidth() * slideOffset;
                final float xOffsetDiff = contentView.getWidth() * diffScaledOffset / 2;
                final float xTranslaiton = xOffset - xOffsetDiff;
                contentView.setTranslationX(xTranslaiton);

            }

            @Override
            public void onDrawerOpened(@NonNull View drawerView) {

            }

            @Override
            public void onDrawerClosed(@NonNull View drawerView) {

            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public void openFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);

        transaction.commit();
    }

    private void openFragmentDefault(Fragment fragment) {

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.commit();

    }


}