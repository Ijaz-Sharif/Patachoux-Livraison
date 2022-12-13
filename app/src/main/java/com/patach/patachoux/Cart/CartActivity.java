package com.patach.patachoux.Cart;

import static com.patach.patachoux.Utils.Constant.getAdminId;
import static com.patach.patachoux.Utils.Constant.getCurrentDate;
import static com.patach.patachoux.Utils.Constant.getUserAddress;
import static com.patach.patachoux.Utils.Constant.getUserId;
import static com.patach.patachoux.Utils.Constant.getUserNumber;
import static com.patach.patachoux.Utils.Constant.getUsername;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.patach.patachoux.Model.Cart;
import com.patach.patachoux.Model.RepeatOrder;
import com.patach.patachoux.R;
import com.patach.patachoux.Screens.SlipActivity;
import com.patach.patachoux.Service.NotificationService;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.UUID;

public class CartActivity extends AppCompatActivity {
    TextView text_cart,price_text;
    RecyclerView recyclerView;
    DatabaseReference dRef;
    public static ArrayList<Cart> cartArrayList =new ArrayList<Cart>();
    CartAdapter arrayAdapter;
    private Dialog loadingDialog;
    Button btn_checkOut;
    static public  int totalPrice=0;

    TextView mondeyTime,tuesdayTime,wednesdayTime,thursdayTime,fridayTime,saturdayTime,sundayTime;
    CheckBox isMondeyChecked,isTuesdayChecked,isWednesdayChecked,isThursdayChecked,isFridayChecked,isSaturdayChecked,isSundayChecked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        text_cart=findViewById(R.id.text_cart);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

//        mondeyTime=findViewById(R.id.mondeyTime);
//        tuesdayTime=findViewById(R.id.tuesdayTime);
//        wednesdayTime=findViewById(R.id.wednesdayTime);
//        thursdayTime=findViewById(R.id.thursdayTime);
//        fridayTime=findViewById(R.id.fridayTime);
//        saturdayTime=findViewById(R.id.saturdayTime);
//        sundayTime=findViewById(R.id.sundayTime);
//
//        isMondeyChecked=findViewById(R.id.isMondeyChecked);
//        isTuesdayChecked=findViewById(R.id.isTuesdayChecked);
//        isWednesdayChecked=findViewById(R.id.isWednesdayChecked);
//        isThursdayChecked=findViewById(R.id.isThursdayChecked);
//        isFridayChecked=findViewById(R.id.isFridayChecked);
//        isSaturdayChecked=findViewById(R.id.isSaturdayChecked);
//        isSundayChecked=findViewById(R.id.isSundayChecked);


//        isMondeyChecked.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if(isMondeyChecked.isChecked()){
//                    Calendar mcurrentTime = Calendar.getInstance();
//                    int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
//                    int minute = mcurrentTime.get(Calendar.MINUTE);
//                    TimePickerDialog mTimePicker;
//                    mTimePicker = new TimePickerDialog(CartActivity.this, new TimePickerDialog.OnTimeSetListener() {
//                        @Override
//                        public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
//                            if(selectedMinute==0){
//                                mondeyTime.setText( selectedHour + ":" + "00");
//                            }
//                            else {
//                                mondeyTime.setText( selectedHour + ":" + selectedMinute);
//                            }
//
//                        }
//                    }, hour, minute, true);//Yes 24 hour time
//                    mTimePicker.setTitle("Select Time");
//                    mTimePicker.show();
//                }
//                else {
//                    mondeyTime.setText("");
//                }
//            }
//        });
//        isWednesdayChecked.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if(isWednesdayChecked.isChecked()){
//                    Calendar mcurrentTime = Calendar.getInstance();
//                    int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
//                    int minute = mcurrentTime.get(Calendar.MINUTE);
//                    TimePickerDialog mTimePicker;
//                    mTimePicker = new TimePickerDialog(CartActivity.this, new TimePickerDialog.OnTimeSetListener() {
//                        @Override
//                        public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
//                            if(selectedMinute==0){
//                                wednesdayTime.setText( selectedHour + ":" + "00");
//                            }
//                            else {
//                                wednesdayTime.setText( selectedHour + ":" + selectedMinute);
//                            }
//
//                        }
//                    }, hour, minute, true);//Yes 24 hour time
//                    mTimePicker.setTitle("Select Time");
//                    mTimePicker.show();
//                }
//                else {
//                    wednesdayTime.setText("");
//                }
//            }
//        });
//        isTuesdayChecked.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if(isTuesdayChecked.isChecked()){
//                    Calendar mcurrentTime = Calendar.getInstance();
//                    int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
//                    int minute = mcurrentTime.get(Calendar.MINUTE);
//                    TimePickerDialog mTimePicker;
//                    mTimePicker = new TimePickerDialog(CartActivity.this, new TimePickerDialog.OnTimeSetListener() {
//                        @Override
//                        public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
//                            if(selectedMinute==0){
//                                tuesdayTime.setText( selectedHour + ":" + "00");
//                            }
//                            else {
//                                tuesdayTime.setText( selectedHour + ":" + selectedMinute);
//                            }
//
//                        }
//                    }, hour, minute, true);//Yes 24 hour time
//                    mTimePicker.setTitle("Select Time");
//                    mTimePicker.show();
//                }
//                else {
//                    tuesdayTime.setText("");
//                }
//            }
//        });
//        isThursdayChecked.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if(isThursdayChecked.isChecked()){
//                    Calendar mcurrentTime = Calendar.getInstance();
//                    int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
//                    int minute = mcurrentTime.get(Calendar.MINUTE);
//                    TimePickerDialog mTimePicker;
//                    mTimePicker = new TimePickerDialog(CartActivity.this, new TimePickerDialog.OnTimeSetListener() {
//                        @Override
//                        public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
//                            if(selectedMinute==0){
//                                thursdayTime.setText( selectedHour + ":" + "00");
//                            }
//                            else {
//                                thursdayTime.setText( selectedHour + ":" + selectedMinute);
//                            }
//
//                        }
//                    }, hour, minute, true);//Yes 24 hour time
//                    mTimePicker.setTitle("Select Time");
//                    mTimePicker.show();
//                }
//                else {
//                    thursdayTime.setText("");
//                }
//            }
//        });
//        isFridayChecked.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if(isFridayChecked.isChecked()){
//                    Calendar mcurrentTime = Calendar.getInstance();
//                    int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
//                    int minute = mcurrentTime.get(Calendar.MINUTE);
//                    TimePickerDialog mTimePicker;
//                    mTimePicker = new TimePickerDialog(CartActivity.this, new TimePickerDialog.OnTimeSetListener() {
//                        @Override
//                        public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
//                            if(selectedMinute==0){
//                                fridayTime.setText( selectedHour + ":" + "00");
//                            }
//                            else {
//                                fridayTime.setText( selectedHour + ":" + selectedMinute);
//                            }
//
//                        }
//                    }, hour, minute, true);//Yes 24 hour time
//                    mTimePicker.setTitle("Select Time");
//                    mTimePicker.show();
//                }
//                else {
//                    fridayTime.setText("");
//                }
//            }
//        });
//        isSaturdayChecked.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if(isSaturdayChecked.isChecked()){
//                    Calendar mcurrentTime = Calendar.getInstance();
//                    int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
//                    int minute = mcurrentTime.get(Calendar.MINUTE);
//                    TimePickerDialog mTimePicker;
//                    mTimePicker = new TimePickerDialog(CartActivity.this, new TimePickerDialog.OnTimeSetListener() {
//                        @Override
//                        public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
//                            if(selectedMinute==0){
//                                saturdayTime.setText( selectedHour + ":" + "00");
//                            }
//                            else {
//                                saturdayTime.setText( selectedHour + ":" + selectedMinute);
//                            }
//
//                        }
//                    }, hour, minute, true);//Yes 24 hour time
//                    mTimePicker.setTitle("Select Time");
//                    mTimePicker.show();
//                }
//                else {
//                    saturdayTime.setText("");
//                }
//            }
//        });
//        isSundayChecked.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if(isSundayChecked.isChecked()){
//                    Calendar mcurrentTime = Calendar.getInstance();
//                    int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
//                    int minute = mcurrentTime.get(Calendar.MINUTE);
//                    TimePickerDialog mTimePicker;
//                    mTimePicker = new TimePickerDialog(CartActivity.this, new TimePickerDialog.OnTimeSetListener() {
//                        @Override
//                        public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
//                            if(selectedMinute==0){
//                                sundayTime.setText( selectedHour + ":" + "00");
//                            }
//                            else {
//                                sundayTime.setText( selectedHour + ":" + selectedMinute);
//                            }
//
//                        }
//                    }, hour, minute, true);//Yes 24 hour time
//                    mTimePicker.setTitle("Select Time");
//                    mTimePicker.show();
//                }
//                else {
//                    sundayTime.setText("");
//                }
//            }
//        });





















        /////loading dialog
        loadingDialog=new Dialog(this);
        loadingDialog.setContentView(R.layout.loading_progress_dialog);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.slider_background));
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        recyclerView=findViewById(R.id.recylerview);
        price_text=findViewById(R.id.price_text);
        btn_checkOut=findViewById(R.id.btn_checkOut);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
    }

    @Override
    public void onStart() {
        NotificationService.getInstance().deviceTokenArrayList.clear();
        NotificationService.getInstance().getSuplierDeviceToken(this);
        NotificationService.getInstance().getAdminDeviceToken(this);
        getProductsData();
        super.onStart();
    }

    public void getProductsData(){
        totalPrice=0;
        dRef=  FirebaseDatabase.getInstance().getReference().child("User").child(getAdminId(CartActivity.this)).child(getUserId(CartActivity.this)).child("Cart");
        loadingDialog.show();
        cartArrayList.clear();
        dRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    cartArrayList.add(new Cart(postSnapshot.child("ProductName").getValue(String.class)
                            ,postSnapshot.child("ProductPrice").getValue(String.class)
                            ,postSnapshot.child("ProductQuantity").getValue(String.class)
                            ,postSnapshot.child("ProductImage").getValue(String.class)
                            ,postSnapshot.child("productId").getValue(String.class)));
                    totalPrice=totalPrice+(Integer.valueOf(postSnapshot.child("ProductPrice").getValue(String.class))*Integer.valueOf(postSnapshot.child("ProductQuantity").getValue(String.class)));

                }
                if(cartArrayList.size()==0){
                    text_cart.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                    price_text.setVisibility(View.GONE);
                    btn_checkOut.setVisibility(View.GONE);
                    //repeatOrderSpinner.setVisibility(View.GONE);
                }
                else {
                    price_text.setText("Total Price : "+totalPrice+" $");
                    arrayAdapter =new CartAdapter();
                    recyclerView.setAdapter(arrayAdapter);
                }

                loadingDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void saveData(){

        loadingDialog.show();
        NotificationService.getInstance().postRequest(CartActivity.this);
        try {
            String id =createOrderId();
            Toast.makeText(CartActivity.this, "sucessfull", Toast.LENGTH_SHORT).show();
            saveOrderRecordUSerSide(id);
            loadingDialog.dismiss();
            FirebaseDatabase.getInstance().getReference().child("User").child(getAdminId(CartActivity.this)).child(getUserId(CartActivity.this)).child("Cart").removeValue();
            startActivity(new Intent(CartActivity.this, SlipActivity.class));





        } catch (Exception e) {
            loadingDialog.dismiss();
            e.printStackTrace();
        }

    }
    public ArrayList<RepeatOrder> addOrderRepeatDays(){
        ArrayList<RepeatOrder> repeatOrderArrayList =new ArrayList<RepeatOrder>();
        if(isMondeyChecked.isChecked()){
             repeatOrderArrayList.add(new RepeatOrder("Monday",true,mondeyTime.getText().toString()));
        }
        else {
            repeatOrderArrayList.add(new RepeatOrder("Monday",false,"empty"));
        }
        if(isTuesdayChecked.isChecked()){
            repeatOrderArrayList.add(new RepeatOrder("Tuesday",true,tuesdayTime.getText().toString()));
        }
        else {
            repeatOrderArrayList.add(new RepeatOrder("Tuesday",false,"empty"));
        }
        if(isWednesdayChecked.isChecked()){
            repeatOrderArrayList.add(new RepeatOrder("Wednesday",true,wednesdayTime.getText().toString()));
        }
        else {
            repeatOrderArrayList.add(new RepeatOrder("Wednesday",false,"empty"));
        }
        if(isThursdayChecked.isChecked()){
            repeatOrderArrayList.add(new RepeatOrder("Thursday",true,thursdayTime.getText().toString()));
        }
        else {
            repeatOrderArrayList.add(new RepeatOrder("Thursday",false,"empty"));
        }
        if(isFridayChecked.isChecked()){
            repeatOrderArrayList.add(new RepeatOrder("Friday",true,fridayTime.getText().toString()));
        }
        else {
            repeatOrderArrayList.add(new RepeatOrder("Friday",false,"empty"));
        }
        if(isSaturdayChecked.isChecked()){
            repeatOrderArrayList.add(new RepeatOrder("Saturday",true,saturdayTime.getText().toString()));
        }
        else {
            repeatOrderArrayList.add(new RepeatOrder("Saturday",false,"empty"));
        }
        if(isSundayChecked.isChecked()){
            repeatOrderArrayList.add(new RepeatOrder("Sunday",true,sundayTime.getText().toString()));
        }
        else {
            repeatOrderArrayList.add(new RepeatOrder("Sunday",false,"empty"));
        }

        return repeatOrderArrayList;
    }
    public void saveOrderRecordUSerSide(String id){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("User")
                .child(getAdminId(this))
                .child(getUserId(this)).child("Order")
                .child(id.substring(0,8));
        databaseReference.child("OrderId").setValue(id.substring(0,8));
        databaseReference.child("Date").setValue(getCurrentDate());


        databaseReference.child("RepeatOrder").setValue(addOrderRepeatDays());


        databaseReference.child("UserId").setValue(getUserId(this));
        for(int i=0;i<cartArrayList.size();i++){
            databaseReference = FirebaseDatabase.getInstance().getReference().child("User")
                    .child(getAdminId(this))
                    .child(getUserId(this)).child("Order")
                    .child(id.substring(0,8)).child("OrderItems").child(cartArrayList.get(i).getProdcutId());
            databaseReference.child("ProductName").setValue(cartArrayList.get(i).getProductName());
            databaseReference.child("ProductPrice").setValue(cartArrayList.get(i).getProductPrice());
            databaseReference.child("ProductImage").setValue(cartArrayList.get(i).getProductImage());
            databaseReference.child("ProductQuantity").setValue(cartArrayList.get(i).getProductQuantity());
            databaseReference.child("ProductId").setValue(cartArrayList.get(i).getProdcutId());
        }
        saveOrderRecord(id);


    }
    public void saveOrderRecord(String id){
        DatabaseReference databaseReference1  = FirebaseDatabase.getInstance().getReference().child("SubAdmin")
                .child(getAdminId(this)).child("Order").child(id.substring(0,8));
        databaseReference1.child("Name").setValue(getUsername(CartActivity.this));
        databaseReference1.child("OrderId").setValue(id.substring(0,8));
        databaseReference1.child("Date").setValue(getCurrentDate());
        databaseReference1.child("Status").setValue("Start");


        databaseReference1.child("RepeatOrder").setValue(addOrderRepeatDays());


        databaseReference1.child("UserId").setValue(getUserId(this));
        databaseReference1.child("UserAddress").setValue(getUserAddress(CartActivity.this));
        databaseReference1.child("UserNumber").setValue(getUserNumber(CartActivity.this));
        databaseReference1.child("SuplierName").setValue("none");
        for(int i=0;i<cartArrayList.size();i++){
            databaseReference1 = FirebaseDatabase.getInstance().getReference().child("SubAdmin")
                    .child(getAdminId(this)).child("Order").child(id.substring(0,8)).child("OrderItems").child(cartArrayList.get(i).getProductName());
            databaseReference1.child("ProductName").setValue(cartArrayList.get(i).getProductName());
            databaseReference1.child("ProductPrice").setValue(cartArrayList.get(i).getProductPrice());
            databaseReference1.child("ProductImage").setValue(cartArrayList.get(i).getProductImage());
            databaseReference1.child("ProductQuantity").setValue(cartArrayList.get(i).getProductQuantity());
            databaseReference1.child("ProductId").setValue(cartArrayList.get(i).getProdcutId());
        }
       // databaseReference1.child("OrderItems").setValue(cartArrayList);
    }

    public void checkout(View view) {
        showAlert();
    }
    public void showAlert(){
        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(this);
        builder.setMessage("Confirm Your Order  ?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();


                       // saveData();


                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
        builder.setCancelable(false);
        AlertDialog alert = builder.create();
        alert.setTitle("Order Confirmation");
        alert.show();
    }

    public void finish(View view) {
        finish();
    }

    public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ImageViewHoler> {

        public CartAdapter(){

        }
        @NonNull
        @Override
        public ImageViewHoler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v= LayoutInflater.from(CartActivity.this).inflate(R.layout.item_cart,parent,false);
            return  new ImageViewHoler(v);
        }
        @Override
        public void onBindViewHolder(@NonNull final ImageViewHoler holder, @SuppressLint("RecyclerView") int position) {


            Picasso.with(CartActivity.this)
                    .load(cartArrayList.get(position).getProductImage())
                    .placeholder(R.drawable.progress_animation)
                    .fit()
                    .centerCrop()
                    .into(holder.imageView);
            holder.product_name.setText(cartArrayList.get(position).getProductName());
            holder.price.setText("$ "+cartArrayList.get(position).getProductPrice());
            holder.product_quantity.setText("Quantity :"+cartArrayList.get(position).getProductQuantity());
            holder.delete_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DeletionAlert(cartArrayList.get(position).getProdcutId());
                }
            });
        }

        @Override
        public int getItemCount() {
            return cartArrayList.size();
        }


        public class ImageViewHoler extends RecyclerView.ViewHolder {
            ImageView imageView,delete_item;
            TextView product_name,product_quantity,price;
            CardView cardView;
            public ImageViewHoler(@NonNull View itemView) {
                super(itemView);
                imageView=itemView.findViewById(R.id.image);
                product_name=itemView.findViewById(R.id.name);
                cardView=itemView.findViewById(R.id.cardView);
                product_quantity=itemView.findViewById(R.id.product_quantity);
                price=itemView.findViewById(R.id.price);
                delete_item=itemView.findViewById(R.id.delete_item);
            }
        }
    }
    public void DeletionAlert(String prodcutid){
        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to remove from cart list")
                .setCancelable(true)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();

                        FirebaseDatabase.getInstance().getReference().child("User").
                                child(getAdminId(CartActivity.this)).
                                child(getUserId(CartActivity.this)).child("Cart")
                                .child(prodcutid).removeValue();
                        getProductsData();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
        builder.setCancelable(false);
        AlertDialog alert = builder.create();
        alert.setTitle("Deletion Alert");
        alert.show();
    }
    public String createOrderId() throws Exception{
        return UUID.randomUUID().toString().replaceAll("-", "").toUpperCase();
    }
}