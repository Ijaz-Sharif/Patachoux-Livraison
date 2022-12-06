package com.patach.patachoux.Screens;

import static com.patach.patachoux.Utils.Constant.getSplierStatus;
import static com.patach.patachoux.Utils.Constant.getUserLoginStatus;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.patach.patachoux.CallBacks.CallListner;
import com.patach.patachoux.Cart.CartActivity;
import com.patach.patachoux.MainActivity;
import com.patach.patachoux.R;
import com.patach.patachoux.Service.NotificationService;

public class SplashActivity extends AppCompatActivity {
       String token;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

//        getDeviceToken();
        Thread thread=new Thread(){
            @Override
            public void run() {
                try {
                    sleep(4000);
                    if(getSplierStatus(SplashActivity.this)){
                        startActivity(new Intent(SplashActivity.this, SuplierMainActivity.class));
                        finish();
                    }
                    else if(getUserLoginStatus(SplashActivity.this)){
                        startActivity(new Intent(SplashActivity.this, MainActivity.class));
                        finish();
                    }else {
                        startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                        finish();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        thread.start();
    }


}