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
import android.content.Intent;
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
import com.patach.patachoux.Model.Order;
import com.patach.patachoux.R;

import java.util.ArrayList;

public class UserOrderActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    DatabaseReference dRef;
    public static ArrayList<Order> orderIdList =new ArrayList<Order>();
    ArrayAdapter arrayAdapter;
    private Dialog loadingDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_order);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        /////loading dialog
        loadingDialog=new Dialog(this);
        loadingDialog.setContentView(R.layout.loading_progress_dialog);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.slider_background));
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        recyclerView=findViewById(R.id.recylerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

    }
    @Override
    public void onStart() {
        getProductsData();
        super.onStart();
    }
    public void getProductsData(){


        dRef=    FirebaseDatabase.getInstance().getReference().child("SubAdmin")
                .child(getAdminId(this)).child("Order");
        loadingDialog.show();
        orderIdList.clear();
        dRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    if(postSnapshot.child("UserId").getValue(String.class).equals(getUserId(UserOrderActivity.this)))
                    orderIdList.add(new Order(postSnapshot.child("OrderId").getValue(String.class),postSnapshot.child("Date").getValue(String.class)
                            ,postSnapshot.child("UserId").getValue(String.class)));
                }
                arrayAdapter=new ArrayAdapter();
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
            View v= LayoutInflater.from(UserOrderActivity.this).inflate(R.layout.item_order,parent,false);
            return  new ArrayAdapter.ImageViewHoler(v);
        }
        @Override
        public void onBindViewHolder(@NonNull final ArrayAdapter.ImageViewHoler holder, @SuppressLint("RecyclerView") int position) {

            holder.order_id.setText(orderIdList.get(position).getOrderId());
            holder.order_date.setText(orderIdList.get(position).getDate());
            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(UserOrderActivity.this,OrderSubListActivity.class)
                            .putExtra("orderId",orderIdList.get(position).getOrderId()));
                }
            });
        }

        @Override
        public int getItemCount() {
            return orderIdList.size();
        }

        public class ImageViewHoler extends RecyclerView.ViewHolder {
            TextView order_id,order_date;
            CardView cardView;
            public ImageViewHoler(@NonNull View itemView) {
                super(itemView);
                order_id=itemView.findViewById(R.id.order_id);
                order_date=itemView.findViewById(R.id.order_date);
                cardView=itemView.findViewById(R.id.cardView);
            }
        }
    }
}