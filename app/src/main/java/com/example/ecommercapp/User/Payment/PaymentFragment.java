package com.example.ecommercapp.User.Payment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

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
import com.example.ecommercapp.User.MyCart.Model.CartModel;
import com.example.ecommercapp.User.Profile.ProfileFragment;
import com.example.ecommercapp.User.User_nav_menu;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;




public class PaymentFragment extends Fragment {


    private  String  TAG="PaymentFragment";

    public PaymentFragment() {
    }
    CartModel cartModel;
    public PaymentFragment(CartModel cartModel) {
            this.cartModel=cartModel;
    }


    View rootView;


    EditText addressEdit;
    EditText cityEdit;
    EditText countyEdit;


    TextView totalTxt;
    Button payButton;


    //Firebase authentication
    FirebaseDatabase rootNode;
    DatabaseReference reference;


    String email;
    String address;
    String contact;
    String name;

    long OrderTableId=0;

    FirebaseDatabase rootNodeOrder;
    DatabaseReference referenceOrder;


    private  static  int PAYHERE_REQUEST=123;


    ProgressDialog pd;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        rootView=inflater.inflate(R.layout.user_payment, container, false);
        init();

        payButton.setOnClickListener(v -> {

            PaymentWithPayHere();
        });


        return rootView;
    }


    @Override
    public void onStart() {
        LoadUserData();
        super.onStart();
    }

    private void LoadUserData() {


        totalTxt.setText("Rs : "+cartModel.getQty()*cartModel.getUnitPrice() +"");



        //load order table id
        referenceOrder=FirebaseDatabase.getInstance().getReference().child("MyOrders");
        referenceOrder.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists())
                    OrderTableId=(snapshot.getChildrenCount());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




        rootNode = FirebaseDatabase.getInstance();
        reference= FirebaseDatabase.getInstance().getReference();
        reference = rootNode.getReference("UserData").child(FirebaseAuth.getInstance().getUid());

        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                 name = dataSnapshot.child("name").getValue(String.class);
                 email = dataSnapshot.child("email").getValue(String.class);
                 address = dataSnapshot.child("address").getValue(String.class);
                 contact = dataSnapshot.child("contact").getValue(String.class);


                //user data credential for before payment
                if(email.isEmpty()){

                    dialogBoxUserData("Please Update Email Address","Payment Screen");
                }


                if(address.isEmpty()){

                    dialogBoxUserData("Please Update Your Address","Payment Screen");
                }

                if(contact.isEmpty()){

                    dialogBoxUserData("Please Update Your Contact","Payment Screen");
                }


                String uType = dataSnapshot.child("utype").getValue(String.class);



            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                Log.e(TAG, "User Authentication"+databaseError.getMessage());

            }
        };
        reference.addListenerForSingleValueEvent(eventListener);

    }

    private void PaymentWithPayHere() {

        if (TextUtils.isEmpty(addressEdit.getText().toString())) {
            addressEdit.setError("Address is required !");
                return;
            }



        if (TextUtils.isEmpty(addressEdit.getText().toString())) {
            addressEdit.setError("Address is required !");
            return;
        }


        if (TextUtils.isEmpty(cityEdit.getText().toString())) {
            cityEdit.setError("City is required !");
            return;
        }

        if (TextUtils.isEmpty(countyEdit.getText().toString())) {
            countyEdit.setError("Country is required !");
            return;
        }


        Log.e(TAG, "User Authentication Success !"+email);



       double total=cartModel.getQty()*cartModel.getUnitPrice();

//        InitRequest req = new InitRequest();
//        req.setMerchantId("1217193");       // Your Merchant PayHere ID
//        req.setMerchantSecret("4aDkTSC2SHx8X6CDRVLchu8cQRn34Xpv14uUgfTKuhVM"); // Your Merchant secret (Add your app at Settings > Domains & Credentials, to get this))
//        req.setCurrency("LKR");             // Currency code LKR/USD/GBP/EUR/AUD
//        req.setAmount(total);             // Final Amount to be charged
//        req.setOrderId(String.valueOf(OrderTableId+1));        // Unique Reference ID
//        req.setItemsDescription(cartModel.getProductName()+"");  // Item description title
//
//        req.getCustomer().setFirstName(name+"");
//        req.getCustomer().setLastName("");
//        req.getCustomer().setEmail(email+"");
//        req.getCustomer().setPhone(contact+"");
//        req.getCustomer().getAddress().setAddress(address+"");
//        req.getCustomer().getAddress().setCity("");
//        req.getCustomer().getAddress().setCountry("Sri Lanka");
//
////Optional Params
//        req.getCustomer().getDeliveryAddress().setAddress(addressEdit.getText().toString()+"");
//        req.getCustomer().getDeliveryAddress().setCity(cityEdit.getText().toString()+"");
//        req.getCustomer().getDeliveryAddress().setCountry(countyEdit.getText().toString());
//
//        Intent intent = new Intent(getContext(), PHMainActivity.class);
//        intent.putExtra(PHConstants.INTENT_EXTRA_DATA, req);
//        PHConfigs.setBaseUrl(PHConfigs.SANDBOX_URL);
//        startActivityForResult(intent, PAYHERE_REQUEST);

    }

    private void init() {
        pd= new ProgressDialog(getContext());


        addressEdit=rootView.findViewById(R.id.addressEdit);
        cityEdit=rootView.findViewById(R.id.cityEdit);
        countyEdit=rootView.findViewById(R.id.countyEdit);
        totalTxt=rootView.findViewById(R.id.totalTxt);
        payButton=rootView.findViewById(R.id.payButton);


    }


    public void dialogBoxUserData(String message,String title) {


        AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Toast.makeText(getApplicationContext(), "You clicked on OK", Toast.LENGTH_SHORT).show();

                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.container, new ProfileFragment());
                transaction.commit();


            }
        });

        alertDialog.show();


    }




    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == PAYHERE_REQUEST && data != null && data.hasExtra(PHConstants.INTENT_EXTRA_RESULT)) {
//            PHResponse<StatusResponse> response = (PHResponse<StatusResponse>) data.getSerializableExtra(PHConstants.INTENT_EXTRA_RESULT);
//            if (resultCode == Activity.RESULT_OK) {
//                String msg;
//                if (response != null)
//                    if (response.isSuccess()) {
//                     //   dialogBox( "Payment Complete","PaymentMethod");
//                        addToDatabase();
//                    }
//                    else
//                          dialogBox( "Result:" + response.toString(),"PaymentMethod");
//                else
//                    dialogBox("Result: no response","PaymentMethod");
//            } else if (resultCode == Activity.RESULT_CANCELED) {
//                if (response != null) {
//                    dialogBox("User canceled the request","PaymentMethod");
//                    Log.d(TAG, response.toString());
//                }
//                else
//                dialogBox("User canceled the request","PaymentMethod");
//            }
//        }

    }

    private void addToDatabase() {
        pd.setTitle("In Progressing");
        pd.show();
        payButton.setVisibility(View.INVISIBLE);
        //add to real time database

        FirebaseDatabase     rootNode = FirebaseDatabase.getInstance();
        DatabaseReference reference = rootNode.getReference("MyOrders");


        reference.child(String.valueOf(OrderTableId+1)).setValue(cartModel).addOnSuccessListener(aVoid -> {


            // remove the particular cart
            FirebaseDatabase     rootNodeCart = FirebaseDatabase.getInstance();
            DatabaseReference referenceCart = rootNodeCart.getReference("MyCart");


            referenceCart.orderByChild("cartId").equalTo(cartModel.getCartId()+"")
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                        {
                            for (DataSnapshot ds : dataSnapshot.getChildren())
                            {
                                ds.getRef().removeValue();
                                //   context.startActivity(new Intent(context,User_nav_menu.class));
                                dialogBox( "Payment Complete","PaymentMethod");
                                pd.dismiss();
                                payButton.setVisibility(View.VISIBLE);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError)
                        {
//                            Toast.makeText(context, "Failed to delete", Toast.LENGTH_SHORT).show();


                            dialogBox("Order add Failed","Add Order");
                            pd.dismiss();
                            payButton.setVisibility(View.VISIBLE);
                        }
                    });





        }).addOnFailureListener(e -> {


            dialogBox(e.getMessage(),"Add Order");

            pd.dismiss();
            payButton.setVisibility(View.VISIBLE);

        });




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