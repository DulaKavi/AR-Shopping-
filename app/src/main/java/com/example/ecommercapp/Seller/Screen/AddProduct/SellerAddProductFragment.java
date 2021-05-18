package com.example.ecommercapp.Seller.Screen.AddProduct;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.ecommercapp.R;
import com.example.ecommercapp.Seller.Screen.AddProduct.Model.ProductModel;
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

public class SellerAddProductFragment extends Fragment  implements AdapterView.OnItemSelectedListener{


    private  String TAG="SellerAddProductFragment";


    Spinner spinner_cat;
    View rootView;


//    private static final String[] paths = {"T-Shirts", "Frocks", "Watches", "Frocks", "Glasses", "Hats", "Jewelries", "Mobile Phones", "Laptops", "Headphones","Bags"};


private static final String[] paths = {"T-Shirts", "Frocks", "Watches", "Frocks", "Glasses", "Hats", "Mobile Phones", "Laptops", "Headphones","Bags"};


String product_cat;
EditText productNameEdit;
EditText unitPriceEdit;
EditText productDescription;
EditText stockEdit;
ImageView product_image;
Button addBtn;

    private static final int PReqCode = 2 ;
    private static final int REQ_CODE = 1000;


    private Uri imageUri;
    private Bitmap original_image;
    boolean isSelectedProductImage;



    long TableId=0;

    //real time database reference
    DatabaseReference reference;
    FirebaseDatabase rootNode;

    //to store the image for database
    StorageReference storageReference;



    //progress

    ProgressDialog progressDialog;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView=inflater.inflate(R.layout.seller_add_product_fragment, container, false);
        init();
        return rootView;
    }

    private void init() {
        progressDialog = new ProgressDialog(getContext());

        spinner_cat=rootView.findViewById(R.id.spinner_cat);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_item,paths);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_cat.setAdapter(adapter);
        spinner_cat.setOnItemSelectedListener(this);

        productNameEdit=rootView.findViewById(R.id.productNameEdit);
        unitPriceEdit=rootView.findViewById(R.id.unitPriceEdit);
        productDescription=rootView.findViewById(R.id.productDescription);
        addBtn=rootView.findViewById(R.id.addBtn);
        product_image=rootView.findViewById(R.id.product_image);
        stockEdit=rootView.findViewById(R.id.stockEdit);


        product_image.setOnClickListener(v -> {
            checkAndRequestForPermission();
        });


        addBtn.setOnClickListener(v -> {

            addToDatabase();
        });




//define the database variable
        reference=FirebaseDatabase.getInstance().getReference().child("Products");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists())
                    TableId=(snapshot.getChildrenCount());
                Log.d(TAG,"Table id "+ TableId);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }


    @Override
    public void onStart() {
        loadData();
        super.onStart();
    }

    @Override
    public void onResume() {
        loadData();
        super.onResume();
    }

    private void loadData() {
        //init firebase
        rootNode = FirebaseDatabase.getInstance();
        reference = rootNode.getReference().child("Products");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists())
                    TableId=(snapshot.getChildrenCount());

                Log.e(TAG,"Table ID :"+TableId);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void addToDatabase() {

        if(!isSelectedProductImage){
          //  Toast.makeText(getContext(),"Phase chose Image !",Toast.LENGTH_SHORT).show();
            dialogBox("Please chose Product image ! !","Product Image");
            return;
        }

//        if (TextUtils.isEmpty(productNameEdit.getText().toString())) {
//            productNameEdit.setError("Product name is required !");
//            return;
//        }
//
//        if (TextUtils.isEmpty(unitPriceEdit.getText().toString())) {
//            unitPriceEdit.setError("Unit Price is required !");
//            return;
//        }
//
//        if (TextUtils.isEmpty(productDescription.getText().toString())) {
//            productDescription.setError("Description is required !");
//            return;
//        }
//        if (TextUtils.isEmpty(stockEdit.getText().toString())) {
//            stockEdit.setError("Available Stock  is required !");
//            return;
//        }

        // Setting Message
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        addBtn.setVisibility(View.INVISIBLE);
        //upload product image
        storageReference= FirebaseStorage.getInstance().getReference();

        final StorageReference fileRef=storageReference.child("Products/"+TableId+1+".jpg");

        fileRef.putFile(imageUri).addOnSuccessListener(taskSnapshot -> {
            //retrieve
            fileRef.getDownloadUrl().addOnSuccessListener(uri -> {
                //to set picked data to current user data



//                Log.d(TAG,"Image upload : "+uri);


                //Set data model
                ProductModel productModel= new ProductModel();
                productModel.setProductId(Integer.parseInt(String.valueOf(TableId+1)));
                productModel.setProductName(productNameEdit.getText().toString());
                productModel.setAvailableQuantity(Integer.parseInt(stockEdit.getText().toString()));
                productModel.setProductDescription(productDescription.getText().toString());
                productModel.setUnitPrice(Double.parseDouble(unitPriceEdit.getText().toString()));
                productModel.setProductImageUrl(uri+"");
                productModel.setProductCategory(product_cat);
                productModel.setProductSellerID(FirebaseAuth.getInstance().getUid());
                productModel.setProductStatus("InProgress");


                reference.child(String.valueOf(TableId+1)).setValue(productModel).addOnSuccessListener(aVoid ->

                        dialogBox("Successfully","Product Add")

                ).addOnFailureListener(e ->

                        dialogBox("Failed","Product Add")

                );


            }).addOnFailureListener(e ->

                    Log.e(TAG,"Image Not Retrieve"+e.getMessage()));

        }).addOnFailureListener(e ->

                Log.e(TAG,"Image Uploaded is Failed"+e.getMessage())


        );

                  //  dialogBox("Image Uploaded is Failed","Image Uploaded");
        addBtn.setVisibility(View.VISIBLE);

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        product_cat = parent.getItemAtPosition(position).toString();

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

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

                    product_image.setImageBitmap(original_image);

                    isSelectedProductImage=true;
                } catch (IOException e) {
                    Log.e("TAG", "Error : " + e);
                }

            }

        }

    }


    public void dialogBox(String message,String title) {

        progressDialog.dismiss();
        addBtn.setVisibility(View.VISIBLE);

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