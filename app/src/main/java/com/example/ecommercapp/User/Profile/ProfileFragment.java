package com.example.ecommercapp.User.Profile;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ecommercapp.R;
import com.example.ecommercapp.Seller.Seller_nav_menu;
import com.example.ecommercapp.Sign.Model.UserModel;
import com.example.ecommercapp.User.User_nav_menu;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.IOException;


public class ProfileFragment extends Fragment {


    private  String  TAG="ProfileFragment";

    View rootView;
    ImageView userImage;
    ImageView edit_icon;

    EditText fullNameEdit;
    EditText addressEdit;
    EditText contactEdit;
    TextView userType;
    Button updateBtn;

    private static final int PReqCode = 2 ;
    private static final int REQ_CODE = 1000;
    private Bitmap original_image;




    //to store the image for database

    StorageReference storageReference;


    //Firebase authentication
    FirebaseDatabase rootNode;
    DatabaseReference reference;


    String email;
    String uType;
    Uri imageUri;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView=inflater.inflate(R.layout.user_profile, container, false);
        init();

        edit_icon.setOnClickListener(v -> { checkAndRequestForPermission(); });
        updateBtn.setOnClickListener(v -> {



            if (TextUtils.isEmpty(fullNameEdit.getText().toString())) {
                fullNameEdit.setError("Full Name is required !");
                return;
            }


            if (TextUtils.isEmpty(addressEdit.getText().toString())) {
                addressEdit.setError("Address is required !");
                return;
            }


            if (TextUtils.isEmpty(contactEdit.getText().toString())) {
                contactEdit.setError("Contact is required !");
                return;
            }



            UserModel userModel= new UserModel();
            userModel.setUserId(FirebaseAuth.getInstance().getUid());
            userModel.setName(fullNameEdit.getText().toString()+"");
            userModel.setContact(contactEdit.getText().toString()+"");
            userModel.setUType(uType);
            userModel.setAddress(addressEdit.getText().toString()+"");
             userModel.setUImages(imageUri+"");
            userModel.setEmail(email+"");
            userModel.setUStatus("Active");

            DatabaseReference  reference=FirebaseDatabase.getInstance().getReference("UserData");
            reference.child(FirebaseAuth.getInstance().getUid()).setValue(userModel).addOnSuccessListener(aVoid -> {
                dialogBox("Update Success ","Profile");
            }).addOnFailureListener(e -> {
                  dialogBox(e.getMessage(),"Profile");
            });

        });


        return rootView;
    }


    @Override
    public void onStart() {
        LoadUserData();
        super.onStart();
    }

    @Override
    public void onResume() {
        LoadUserData();
        super.onResume();
    }

    private void LoadUserData() {
        //loading image from the database
        storageReference= FirebaseStorage.getInstance().getReference();
        StorageReference profileRef=storageReference.child("Users/"+FirebaseAuth.getInstance().getUid()+"/Profile.jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {

                try {
                    Picasso.get().load(uri).into(userImage);

                }catch (Exception e){

                }
            }
        });




        rootNode = FirebaseDatabase.getInstance();
        reference= FirebaseDatabase.getInstance().getReference();
        reference = rootNode.getReference("UserData").child(FirebaseAuth.getInstance().getUid());

        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

              String  name = dataSnapshot.child("name").getValue(String.class);

                    fullNameEdit.setText(name+"");

                email = dataSnapshot.child("email").getValue(String.class);
              String  address = dataSnapshot.child("address").getValue(String.class);

              addressEdit.setText(address+"");
              String    contact = dataSnapshot.child("contact").getValue(String.class);
                contactEdit.setText(contact+"");

                 uType = dataSnapshot.child("utype").getValue(String.class);

              userType.setText("@"+uType+"");

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                Log.e(TAG, "User Authentication"+databaseError.getMessage());

            }
        };
        reference.addListenerForSingleValueEvent(eventListener);


    }

    private void init() {

        userType=rootView.findViewById(R.id.userType);
        userImage=rootView.findViewById(R.id.userImage);
        edit_icon=rootView.findViewById(R.id.edit_icon);
        fullNameEdit=rootView.findViewById(R.id.fullNameEdit);
        addressEdit=rootView.findViewById(R.id.addressEdit);
        contactEdit=rootView.findViewById(R.id.contactEdit);
        updateBtn=rootView.findViewById(R.id.updateBtn);
    }



    private void checkAndRequestForPermission() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)) {

                Toast.makeText(getContext(),"Please accept for required permission", Toast.LENGTH_SHORT).show();

            }

            else
            {
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        PReqCode);
            }


            return;
        }else{
            Intent OpenGalleryIntent= new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(OpenGalleryIntent,REQ_CODE);
        }

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_CODE) {
            if (resultCode == Activity.RESULT_OK) {


                 imageUri = data.getData();
                try {
                    original_image = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageUri);


                    UploadToDatabase(imageUri);
//                    userImage.setImageBitmap(original_image);

                } catch (IOException e) {
                    Log.e("TAG", "Error : " + e);
                }

            }

        }

    }

    private void UploadToDatabase(Uri imageUri) {

        storageReference= FirebaseStorage.getInstance().getReference();
        //upload image to firebase storage
        final StorageReference fileRef=storageReference.child("Users/"+ FirebaseAuth.getInstance().getCurrentUser().getUid()+"/Profile.jpg");
        fileRef.putFile(imageUri).addOnSuccessListener(taskSnapshot -> {


            //retrive
            fileRef.getDownloadUrl().addOnSuccessListener(uri -> {


                Picasso.get().load(uri).into(userImage);

                UserModel userModel= new UserModel();
                userModel.setUserId(FirebaseAuth.getInstance().getUid());
                userModel.setName(fullNameEdit.getText().toString()+"");
                userModel.setContact(contactEdit.getText().toString()+"");
                userModel.setUType(uType);
                userModel.setAddress(addressEdit.getText().toString()+"");
                userModel.setUImages(uri+"");
                userModel.setEmail(email+"");
                userModel.setUStatus("Active");

                DatabaseReference  reference=FirebaseDatabase.getInstance().getReference("UserData");
                reference.child(FirebaseAuth.getInstance().getUid()).setValue(userModel).addOnSuccessListener(aVoid -> {
                   //dialogBox("Success fully Register ","User Register");
                }).addOnFailureListener(e -> {
                    //  dialogBox(e.getMessage(),"User Register");
                });




            }).addOnFailureListener(e -> dialogBox("Image Not Retrieve","Image Uploading"));

        }).addOnFailureListener(e -> dialogBox("Image Uploaded is Failed","Image Uploading"));


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