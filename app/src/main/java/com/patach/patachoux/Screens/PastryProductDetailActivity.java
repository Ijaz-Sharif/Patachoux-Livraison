package com.patach.patachoux.Screens;

import static com.patach.patachoux.Utils.Constant.getAdminId;
import static com.patach.patachoux.Utils.Constant.getUserId;
import static com.patach.patachoux.Utils.Constant.getUsername;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.patach.patachoux.MainActivity;
import com.patach.patachoux.R;
import com.squareup.picasso.Picasso;

public class PastryProductDetailActivity extends AppCompatActivity {
    int index =0;
    DatabaseReference myRef;
    ImageView image;
    EditText product_name,product_des,product_price,product_quantity;
    int quantity=1;
    private Dialog loadingDialog;
    String product="";
    Button btn_cart;
    String adminId="",userId="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pastry_product_detail);
        product_price=findViewById(R.id.product_price);
        product_des=findViewById(R.id.product_des);
        product_name=findViewById(R.id.product_name);
        image=findViewById(R.id.image);
        product_quantity=findViewById(R.id.product_quantity);
        btn_cart=findViewById(R.id.btn_cart1);

        adminId=getAdminId(this);
        userId=getUserId(this);

        /////loading dialog
        loadingDialog=new Dialog(this);
        loadingDialog.setContentView(R.layout.loading_progress_dialog);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.slider_background));
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        btn_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addDataToCart();
                Toast.makeText(PastryProductDetailActivity.this,"product added into cart",Toast.LENGTH_LONG).show();
            }
        });
        product=getIntent().getStringExtra("name");
    }

    @Override
    protected void onStart() {
        getProduct();
        super.onStart();
    }

    public void getProduct(){
        loadingDialog.show();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("User").child(adminId).child(userId).child("Cart");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener(){
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                    if(product.equals(dataSnapshot1.child("ProductName").getValue(String.class))) {
                        product_quantity.setText(dataSnapshot1.child("ProductQuantity").getValue().toString());
                        quantity =Integer.valueOf(dataSnapshot1.child("ProductQuantity").getValue().toString());
                        btn_cart.setText("update quantity");
                        break;
                    }
                }


                product_quantity.setText(quantity+"");
                Picasso.with(PastryProductDetailActivity.this)
                        .load(getIntent().getStringExtra("image"))
                        .placeholder(R.drawable.progress_animation)
                        .fit()
                        .centerCrop()
                        .into(image);
                product_name.setText(getIntent().getStringExtra("name"));
                product_price.setText("$ "+getIntent().getStringExtra("price"));
                product_des.setText(getIntent().getStringExtra("des"));
                loadingDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void addDataToCart() {
        DatabaseReference  databaseReference= FirebaseDatabase.getInstance().getReference().child("User")
                .child(adminId)
                .child(userId).child("Cart")
                .child(getIntent().getStringExtra("productId"));
        databaseReference.child("productId").setValue(getIntent().getStringExtra("productId"));
        databaseReference.child("ProductPrice").setValue(getIntent().getStringExtra("price"));
        databaseReference.child("ProductImage").setValue(getIntent().getStringExtra("image"));
        if(Integer.parseInt(product_quantity.getText().toString())==0){
            databaseReference.child("ProductQuantity").setValue(1+"");
        }
        else {
            databaseReference.child("ProductQuantity").setValue(product_quantity.getText().toString());
        }
        databaseReference.child("ProductName").setValue(product_name.getText().toString().trim());
      //  Toast.makeText(PastryProductDetailActivity.this,"product added in cart list",Toast.LENGTH_SHORT).show();
       // finish();
         startActivity(new Intent(PastryProductDetailActivity.this, MainActivity.class)
         .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
    }
    public void decreaseValue(View view) {
        if(quantity!=0){
           int  q= Integer.parseInt(product_quantity.getText().toString());
           quantity =q;
            quantity--;
            product_quantity.setText(quantity+"");
        }

    }

    public void increaseValue(View view) {
        int  q= Integer.parseInt(product_quantity.getText().toString());
        quantity =q;
        quantity++;
        product_quantity.setText(quantity+"");
    }

    public void finish(View view) {
        finish();
    }
}