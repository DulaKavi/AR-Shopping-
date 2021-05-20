package com.example.ecommercapp.Sign;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.ecommercapp.R;
import com.example.ecommercapp.Seller.Seller_nav_menu;
import com.example.ecommercapp.Sign.Model.UserModel;
import com.example.ecommercapp.User.User_nav_menu;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpFragment extends Fragment  implements AdapterView.OnItemSelectedListener{


    private  String TAG="SignUpFragment";

    View rootView;

    EditText emailEdit;
    EditText nameEdit;
    EditText contactEdit;
    EditText passwordEdit;

    Spinner spinReg;


    Button regBtn;
    String uType;

    private static final String[] paths = {"User", "Seller"};


    ProgressDialog pd;


   //Firebase authentication
    public FirebaseAuth mAuth;
    FirebaseDatabase rootNode;
    DatabaseReference reference;



    //session preparing for Set the values
    SharedPreferences sharedpreferences;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView=inflater.inflate(R.layout.sign_up_fragment, container, false);
        init();


        regBtn.setOnClickListener(v -> {



            // ---------------------------------Validation   Part------------------------------------------


            if (TextUtils.isEmpty(emailEdit.getText().toString())) {
                emailEdit.setError("Email is required !");
                    return;
            }

            if (TextUtils.isEmpty(nameEdit.getText().toString())) {
                nameEdit.setError("Name is required !");
                return;
            }

            if (TextUtils.isEmpty(contactEdit.getText().toString())) {
                contactEdit.setError("Contact is required !");
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


             registerUser();
        });

        return rootView;
    }

    private void registerUser() {

        regBtn.setVisibility(View.INVISIBLE);
        pd.setTitle("In Progressing");
        pd.show();
        mAuth.createUserWithEmailAndPassword(emailEdit.getText().toString(),passwordEdit.getText().toString()).addOnCompleteListener(task -> {

            if(task.isSuccessful()){

                Log.e(TAG,"Register Success !");

                rootNode = FirebaseDatabase.getInstance();
                reference= FirebaseDatabase.getInstance().getReference();
                reference = rootNode.getReference("UserData");

                UserModel userModel= new UserModel();
                userModel.setUserId(FirebaseAuth.getInstance().getUid());
                userModel.setName(nameEdit.getText().toString());
                userModel.setContact(contactEdit.getText().toString());
                userModel.setUType(uType);
                userModel.setAddress("");
                userModel.setUImages("");
                userModel.setEmail(emailEdit.getText().toString()+"");
                userModel.setUStatus("Active");

                //create account using user id
                reference.child(FirebaseAuth.getInstance().getUid()).setValue(userModel).addOnSuccessListener(aVoid -> {

                    dialogBox("Success fully Register ","User Register");
                    pd.dismiss();
                    regBtn.setVisibility(View.VISIBLE);


                    //After register navigate to the particular screen

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
                    }



                }).addOnFailureListener(e -> {

                    dialogBox(e.getMessage(),"User Register");
                    pd.dismiss();
                    regBtn.setVisibility(View.VISIBLE);
                });

            }

        }).addOnFailureListener(e -> {

            Log.e(TAG,e.getMessage());


            dialogBox(e.getMessage(),"User Register");
            pd.dismiss();
            regBtn.setVisibility(View.VISIBLE);
        });
    }

    private void init() {

        mAuth = FirebaseAuth.getInstance();

        pd= new ProgressDialog(getContext());


        spinReg = (Spinner)rootView.findViewById(R.id.spinReg);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_item,paths);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinReg.setAdapter(adapter);
        spinReg.setOnItemSelectedListener(this);

        regBtn=rootView.findViewById(R.id.regBtn);
        emailEdit=rootView.findViewById(R.id.emailEdit);
        nameEdit=rootView.findViewById(R.id.nameEdit);
        contactEdit=rootView.findViewById(R.id.contactEdit);
        passwordEdit=rootView.findViewById(R.id.passwordEdit);

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        uType = parent.getItemAtPosition(position).toString();

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

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