package com.patach.patachoux.Screens;

import static com.patach.patachoux.Utils.Constant.getAdminId;
import static com.patach.patachoux.Utils.Constant.getUserId;
import static com.patach.patachoux.Utils.Constant.getUsername;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.patach.patachoux.Model.Cart;
import com.patach.patachoux.R;

import java.util.ArrayList;

public class OrderSubListActivity extends AppCompatActivity {
    TextView text_cart,price_text;
    RecyclerView recyclerView;
    ArrayAdapter arrayAdapter;
    private Dialog loadingDialog;
    ArrayList<Cart> orderList =new ArrayList<Cart>();
    int totalPrice=0;
    DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_sub_list);
        text_cart=findViewById(R.id.text_cart);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        /////loading dialog
        loadingDialog=new Dialog(this);
        loadingDialog.setContentView(R.layout.loading_progress_dialog);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.slider_background));
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        recyclerView=findViewById(R.id.recylerview);
        price_text=findViewById(R.id.price_text);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
    }

    @Override
    protected void onStart() {
        getProductsData();
        super.onStart();
    }
    public void getProductsData(){
        totalPrice=0;
        databaseReference = FirebaseDatabase.getInstance().getReference().child("User").child(getAdminId(this)).child(getUserId(this)).child("Order").child(getIntent().getStringExtra("orderId")).child("OrderItems");
        loadingDialog.show();
        orderList.clear();
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    orderList.add(new Cart(
                            postSnapshot.child("ProductName").getValue(String.class)
                            ,postSnapshot.child("ProductPrice").getValue(String.class)
                            ,postSnapshot.child("ProductQuantity").getValue(String.class)
                            ,postSnapshot.child("ProductImage").getValue(String.class),postSnapshot.child("ProductId").getValue(String.class)));
                    totalPrice=totalPrice+(Integer.valueOf(postSnapshot.child("ProductPrice").getValue(String.class))*Integer.valueOf(postSnapshot.child("ProductQuantity").getValue(String.class)));

                }
                price_text.setText("Total Price : "+totalPrice+" $");
                arrayAdapter =new ArrayAdapter();
                recyclerView.setAdapter(arrayAdapter);
                loadingDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void finish(View view) {
        finish();
    }


    public class ArrayAdapter extends RecyclerView.Adapter<ArrayAdapter.ImageViewHoler> {

        public ArrayAdapter(){

        }
        @NonNull
        @Override
        public ArrayAdapter.ImageViewHoler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v= LayoutInflater.from(OrderSubListActivity.this).inflate(R.layout.item_slip,parent,false);
            return  new ArrayAdapter.ImageViewHoler(v);
        }
        @Override
        public void onBindViewHolder(@NonNull final ArrayAdapter.ImageViewHoler holder, @SuppressLint("RecyclerView") int position) {

            holder.product_name.setText(orderList.get(position).getProductName());
            holder.price.setText("$ "+orderList.get(position).getProductPrice());
            holder.product_quantity.setText("Quantity :"+orderList.get(position).getProductQuantity());
        }

        @Override
        public int getItemCount() {
            return orderList.size();
        }

        public class ImageViewHoler extends RecyclerView.ViewHolder {

            TextView product_name,product_quantity,price;
            CardView cardView;
            public ImageViewHoler(@NonNull View itemView) {
                super(itemView);
                product_name=itemView.findViewById(R.id.product_name);
                cardView=itemView.findViewById(R.id.cardView);
                product_quantity=itemView.findViewById(R.id.product_quantity);
                price=itemView.findViewById(R.id.product_price);
            }
        }
    }
}