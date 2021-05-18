package com.example.ecommercapp.Sign;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.example.ecommercapp.Admin.Admin_nav_menu;
import com.example.ecommercapp.R;
import com.example.ecommercapp.Seller.Seller_nav_menu;
import com.example.ecommercapp.User.User_nav_menu;

public class SplashScreen extends AppCompatActivity {
    SharedPreferences sharedpreferences;

    String userType;



    boolean isLoggedIn;



    String TAG="SplashScreen";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        //to check current user types
        sharedpreferences= getSharedPreferences("user_details", MODE_PRIVATE);

        isLoggedIn=sharedpreferences.getBoolean("isLoggedIn",false);


        Thread timer= new Thread(){
            public void run(){
                try{
                    sleep( 3000 );
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                finally {
                    //to check current user types
                    userType=sharedpreferences.getString("userType","");
                    Log.e(TAG,"userType" +userType);

//  Toast.makeText(SplashActivity.this,userType+"",Toast.LENGTH_SHORT).show();

                    //if user login then navigation process
                    if(isLoggedIn){

                        if(userType.equals("User")){

                            startActivity(new Intent(SplashScreen.this,User_nav_menu.class));
                            finish();

                        }else if(userType.equals("Seller")){

                            startActivity(new Intent(SplashScreen.this,Seller_nav_menu.class));
                            finish();
                        }else{

                            startActivity(new Intent(SplashScreen.this,Admin_nav_menu.class));
                            finish();
                        }



                        Log.e(TAG,"user login ");


                    } else {



                        startActivity(new Intent(SplashScreen.this,SignNavActivity.class));
                        finish();
                        Log.e(TAG,"no user login ");


                    }



                }
            }
        };
        timer.start();

    }
}