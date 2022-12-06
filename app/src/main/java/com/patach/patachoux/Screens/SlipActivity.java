package com.patach.patachoux.Screens;

import static com.patach.patachoux.Cart.CartActivity.cartArrayList;
import static com.patach.patachoux.Cart.CartActivity.totalPrice;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.patach.patachoux.MainActivity;
import com.patach.patachoux.Notification.MyNotificationPublisher;
import com.patach.patachoux.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

public class SlipActivity extends AppCompatActivity {
    public static final String NOTIFICATION_CHANNEL_ID = "10001" ;
    private final static String default_notification_channel_id = "default" ;
    TextView text_cart,price_text;
    RecyclerView recyclerView;
    ArrayAdapter arrayAdapter;
    ConstraintLayout main_layout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slip);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        text_cart=findViewById(R.id.text_cart);
        recyclerView=findViewById(R.id.recylerview);
        price_text=findViewById(R.id.price_text);
        main_layout=findViewById(R.id.main_layout);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
    }

    @Override
    protected void onStart() {
        arrayAdapter=new ArrayAdapter();
        recyclerView.setAdapter(arrayAdapter);
        price_text.setText("Total Price : "+totalPrice+" $");
        super.onStart();
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
            View v= LayoutInflater.from(SlipActivity.this).inflate(R.layout.item_slip,parent,false);
            return  new ArrayAdapter.ImageViewHoler(v);
        }
        @Override
        public void onBindViewHolder(@NonNull final ArrayAdapter.ImageViewHoler holder, @SuppressLint("RecyclerView") int position) {

            holder.product_name.setText(cartArrayList.get(position).getProductName());
            holder.price.setText("$ "+cartArrayList.get(position).getProductPrice());
            holder.product_quantity.setText("Quantity :"+cartArrayList.get(position).getProductQuantity());
        }

        @Override
        public int getItemCount() {
            return cartArrayList.size();
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




    public  void checkPermission(){
        Dexter.withActivity(SlipActivity.this)
                .withPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {

                            main_layout.post(new Runnable() {
                                @Override
                                public void run() {
                                    // call the screen shot function
                                    Bitmap b = takeScreenShot(main_layout);
                                    try {
                                        if(b!=null){
                                            scheduleNotification(getNotification( "Thank for using our app your order will be delivered" ) , 5000 ) ;

                                            // call the save screen function
                                            SaveScreenShoot(b);
                                            Toast.makeText(SlipActivity.this,"Check your \nslip in internal storage",Toast.LENGTH_LONG).show();
                                               startActivity(new Intent(SlipActivity.this,MainActivity.class)
                                               .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                                               finish();


                                        }
                                    }
                                    catch (Exception e){
                                        e.printStackTrace();
                                    }
                                }
                            });







                        }

                        if (report.isAnyPermissionPermanentlyDenied()) {
                            showSettingsDialog();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
    }
    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.dialog_permission_title));
        builder.setMessage(getString(R.string.dialog_permission_message));
        builder.setPositiveButton(getString(R.string.go_to_settings), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                openSettings();
            }
        });
        builder.setNegativeButton(getString(android.R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
//
    }
    private void openSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package",SlipActivity.this.getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 101);
    }

    public void saveSlip(View view) {
        checkPermission();
    }
    // function to tak the screen shoot of the screen
    private Bitmap takeScreenShot(View v) {
        Bitmap screen = null;
        try {
            int width = v.getMeasuredWidth();
            int hight = v.getMeasuredHeight();
            screen = Bitmap.createBitmap(width, hight, Bitmap.Config.ARGB_8888);
            Canvas c = new Canvas(screen);
            v.draw(c);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return screen;
    }
    // function to save the slip in the gallery
    private void SaveScreenShoot(Bitmap b) {
        ByteArrayOutputStream bao = null;
        File file = null;
        try {
            bao= new ByteArrayOutputStream();
            b.compress(Bitmap.CompressFormat.PNG,40,bao);
            file=new File(Environment.getExternalStorageDirectory()+File.separator+" Order_slip.png");
            file.createNewFile();
            FileOutputStream f=new FileOutputStream(file);
            f.write(bao.toByteArray());
            f.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // schedule the notification
    private void scheduleNotification (Notification notification , int delay) {
        Intent notificationIntent = new Intent( this, MyNotificationPublisher. class ) ;
        notificationIntent.putExtra(MyNotificationPublisher. NOTIFICATION_ID , 1 ) ;
        notificationIntent.putExtra(MyNotificationPublisher. NOTIFICATION , notification) ;
        PendingIntent pendingIntent = PendingIntent. getBroadcast ( this, 0 , notificationIntent , PendingIntent. FLAG_UPDATE_CURRENT ) ;
        long futureInMillis = SystemClock. elapsedRealtime () + delay ;
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context. ALARM_SERVICE ) ;
        assert alarmManager != null;
        alarmManager.set(AlarmManager. ELAPSED_REALTIME_WAKEUP , futureInMillis , pendingIntent) ;
    }
    // notification function that generate the notification and display the info in the notification
    private Notification getNotification (String content) {
        Intent notificationIntent = new Intent(this , MainActivity. class ) ;
        PendingIntent resultIntent = PendingIntent. getActivity (this , 0 , notificationIntent , 0 ) ;
        NotificationCompat.Builder builder = new NotificationCompat.Builder( this, default_notification_channel_id ) ;
        builder.setContentTitle( "Order" ) ;
        builder.setContentText(content) ;
        builder.setSmallIcon(R.drawable. logo ) ;
        builder.setAutoCancel( true ) ;
        builder.setChannelId( NOTIFICATION_CHANNEL_ID ) ;
        builder.setContentIntent(resultIntent) ;
        return builder.build() ;

    }
}