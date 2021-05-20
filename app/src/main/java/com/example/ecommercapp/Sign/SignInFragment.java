package com.example.ecommercapp.Sign;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.ecommercapp.Admin.Admin_nav_menu;
import com.example.ecommercapp.R;
import com.example.ecommercapp.Seller.Seller_nav_menu;
import com.example.ecommercapp.User.User_nav_menu;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class SignInFragment extends Fragment {

    View rootView;


    EditText emailEdit;

    EditText passwordEdit;


    Button loginBtn;



    private  String TAG="SignInFragment";

    ProgressDialog pd;
    //Firebase authentication
    public FirebaseAuth mAuth;
    FirebaseDatabase rootNode;
    DatabaseReference reference;

    TextView forget_password_Btn;

    //session preparing for Set the values
    SharedPreferences sharedpreferences;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView=inflater.inflate(R.layout.signin_fragment, container, false);

        init();


        loginBtn.setOnClickListener(v -> {

            if (TextUtils.isEmpty(emailEdit.getText().toString())) {
                emailEdit.setError("Email is required !");
                return;
            }
            if (TextUtils.isEmpty(passwordEdit.getText().toString())) {
                passwordEdit.setError("Password is required !");
                return;
            }

            if(passwordEdit.length()<8){
                passwordEdit.setError( "Password must be 8 characters" );
                return;
            }


            loginUser();
        });


        forget_password_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomSheetDialog bottomSheetDialog= new BottomSheetDialog(getContext(), R.style.BottomDialogThme);

                View bottomView= LayoutInflater.from(getContext()).inflate(R.layout.sign_in_forget_pasword, null, false);

                EditText emailEditForget=bottomView.findViewById(R.id.emailEditForget);
                Button sendBtn=bottomView.findViewById(R.id.sendBtn);




                bottomSheetDialog.setContentView(bottomView);
                bottomSheetDialog.show();

                sendBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (TextUtils.isEmpty(emailEditForget.getText().toString())) {
                            emailEditForget.setError("Email is required !");
                            return;
                        }

                        pd.setTitle("Waiting...");
                        pd.show();
                        sendBtn.setVisibility(View.INVISIBLE);

                        FirebaseAuth firebaseAuth = mAuth.getInstance();

                        firebaseAuth.sendPasswordResetEmail(emailEditForget.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                dialogBox("Check your Email  ","Forget Password !");
                                pd.dismiss();
                                sendBtn.setVisibility(View.VISIBLE);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {



                                dialogBox("Email failed to send ","Forget Password !");

                                pd.dismiss();
                                sendBtn.setVisibility(View.VISIBLE);
                            }
                        });

                       bottomSheetDialog.dismiss();
                    }
                });

            }
        });
        return rootView;
    }

    private void loginUser() {

        loginBtn.setVisibility(View.INVISIBLE);
        pd.setTitle("In Progressing");
        pd.show();


        mAuth.signInWithEmailAndPassword(emailEdit.getText().toString(),passwordEdit.getText().toString()).addOnSuccessListener(authResult -> {



//            Log.e(TAG, "User Authentication Success !"+mAuth.getCurrentUser().getUid());

            rootNode = FirebaseDatabase.getInstance();
            reference= FirebaseDatabase.getInstance().getReference();
            reference = rootNode.getReference("UserData").child(mAuth.getCurrentUser().getUid());

            ValueEventListener eventListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    String uStatus = dataSnapshot.child("ustatus").getValue(String.class);

                    if(uStatus.equals("Inactive")){
                        dialogBox("Your Account Currently Deactivate Please Contact Us !","User Authentication");
                        loginBtn.setVisibility(View.VISIBLE);
                        pd.dismiss();
                        return;
                    }



                    String uType = dataSnapshot.child("utype").getValue(String.class);
                    Log.e(TAG, "User Authentication Success !"+uType);


                    //set session to uer credential
                    sharedpreferences =getActivity().getSharedPreferences("user_details", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putBoolean("isLoggedIn",true);
                    editor.putString("userType",uType);
                    editor.commit();


                    if(uType.equals("User")){

                        startActivity(new Intent(getContext(), User_nav_menu.class));
                        getActivity().finish();

                    }else if(uType.equals("Seller")){

                        startActivity(new Intent(getContext(), Seller_nav_menu.class));
                        getActivity().finish();
                    }else{

                        startActivity(new Intent(getContext(), Admin_nav_menu.class));
                        getActivity().finish();
                    }

                    loginBtn.setVisibility(View.VISIBLE);
                    pd.dismiss();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                    Log.e(TAG, "User Authentication"+databaseError.getMessage());
                    loginBtn.setVisibility(View.VISIBLE);
                    pd.dismiss();
                }
            };
            reference.addListenerForSingleValueEvent(eventListener);


        }).addOnFailureListener(e -> {

            dialogBox(e.getMessage(),"User Authentication");
            loginBtn.setVisibility(View.VISIBLE);
            pd.dismiss();
        });
    }

    private void init() {

        pd= new ProgressDialog(getContext());


        mAuth = FirebaseAuth.getInstance();

        emailEdit=rootView.findViewById(R.id.emailEdit);
        passwordEdit=rootView.findViewById(R.id.passwordEdit);
        loginBtn=rootView.findViewById(R.id.loginBtn);
        forget_password_Btn=rootView.findViewById(R.id.forget_password_Btn);


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