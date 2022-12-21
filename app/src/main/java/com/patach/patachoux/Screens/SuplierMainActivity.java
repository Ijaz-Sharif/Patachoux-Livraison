package com.patach.patachoux.Screens;

import static com.patach.patachoux.Utils.Constant.getAdminId;
import static com.patach.patachoux.Utils.Constant.getUserId;
import static com.patach.patachoux.Utils.Constant.getUsername;
import static com.patach.patachoux.Utils.Constant.setSplierStatus;
import static com.patach.patachoux.Utils.Constant.setUserLoginStatus;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.patach.patachoux.Model.Order;
import com.patach.patachoux.R;

import java.util.ArrayList;

public class SuplierMainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    DatabaseReference dRef;
    public static ArrayList<Order> suplierOrderList =new ArrayList<Order>();
    ArrayAdapter arrayAdapter;
    private Dialog loadingDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suplier_main);
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
        suplierOrderList.clear();
        dRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    suplierOrderList.add(new Order(postSnapshot.child("OrderId").getValue(String.class),
                            postSnapshot.child("Date").getValue(String.class)
                            ,postSnapshot.child("Status").getValue(String.class) ,
                            postSnapshot.child("SuplierName").getValue(String.class)
                            , postSnapshot.child("Name").getValue(String.class), postSnapshot.child("UserAddress").getValue(String.class)
                            , postSnapshot.child("UserNumber").getValue(String.class)
                    ,postSnapshot.child("UserId").getValue(String.class)
                            ,postSnapshot.child("DeliveryOrderTime").getValue(String.class)
                            ,postSnapshot.child("DeliveryOrderDate").getValue(String.class)));
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
    public class ArrayAdapter extends RecyclerView.Adapter<ArrayAdapter.ImageViewHoler> {

        public ArrayAdapter(){

        }
        @NonNull
        @Override
        public ArrayAdapter.ImageViewHoler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v= LayoutInflater.from(SuplierMainActivity.this).inflate(R.layout.item_order_splier,parent,false);
            return  new ArrayAdapter.ImageViewHoler(v);
        }
        @Override
        public void onBindViewHolder(@NonNull final ArrayAdapter.ImageViewHoler holder, @SuppressLint("RecyclerView") int position) {

            holder.order_id.setText(suplierOrderList.get(position).getOrderId());
            holder.order_date.setText(suplierOrderList.get(position).getDate());
            holder.order_accepter.setText(suplierOrderList.get(position).getSuplierName());
            if(suplierOrderList.get(position).getStatus().equals("Start")){
                holder.image_status.setImageDrawable(getResources().getDrawable(R.drawable.green_image));
            }
            else if(suplierOrderList.get(position).getStatus().equals("InProgress")){
                holder.image_status.setImageDrawable(getResources().getDrawable(R.drawable.yellow_image));
            }
            else if(suplierOrderList.get(position).getStatus().equals("Complete")){
                holder.image_status.setImageDrawable(getResources().getDrawable(R.drawable.red_image));
            }
            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(suplierOrderList.get(position).getStatus().equals("InProgress")||suplierOrderList.get(position).getStatus().equals("Complete")){
                        if(getUsername(SuplierMainActivity.this).equals(suplierOrderList.get(position).getSuplierName())){
                            startActivity(new Intent(SuplierMainActivity.this, SuplierSubOrderActivity.class)
                                    .putExtra("index",position));
                        }
                    }
                    else {
                        startActivity(new Intent(SuplierMainActivity.this, SuplierSubOrderActivity.class)
                                .putExtra("index",position));
                    }


                }
            });
        }

        @Override
        public int getItemCount() {
            return suplierOrderList.size();
        }

        public class ImageViewHoler extends RecyclerView.ViewHolder {
            TextView order_id,order_date,order_accepter;
            ImageView image_status;
            CardView cardView;
            public ImageViewHoler(@NonNull View itemView) {
                super(itemView);
                image_status=itemView.findViewById(R.id.image_status);
                order_id=itemView.findViewById(R.id.order_id);
                order_date=itemView.findViewById(R.id.order_date);
                cardView=itemView.findViewById(R.id.cardView); order_accepter=itemView.findViewById(R.id.order_accepter);
            }
        }
    }

    @Override
    public void onBackPressed() {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Do you want to exit?");
        alertDialogBuilder.setPositiveButton("yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        SuplierMainActivity.super.onBackPressed();
                    }
                });
        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });



        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public void logoutSplier(View view) {
        setSplierStatus(this,false);
        setUserLoginStatus(this,false);
//        FirebaseDatabase.getInstance().getReference("Suplier").child(getAdminId(this)).
//                child(getUserId(this)).child("DeviceToken").setValue("empty");
        startActivity(new Intent(SuplierMainActivity.this,LoginActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
    }
}