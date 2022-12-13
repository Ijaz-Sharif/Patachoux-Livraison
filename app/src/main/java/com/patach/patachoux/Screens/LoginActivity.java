package com.patach.patachoux.Screens;

import static com.patach.patachoux.Utils.Constant.setAdminId;
import static com.patach.patachoux.Utils.Constant.setSplierStatus;
import static com.patach.patachoux.Utils.Constant.setUserAddress;
import static com.patach.patachoux.Utils.Constant.setUserCity;
import static com.patach.patachoux.Utils.Constant.setUserCode;
import static com.patach.patachoux.Utils.Constant.setUserId;
import static com.patach.patachoux.Utils.Constant.setUserLoginStatus;
import static com.patach.patachoux.Utils.Constant.setUserNumber;
import static com.patach.patachoux.Utils.Constant.setUserPic;
import static com.patach.patachoux.Utils.Constant.setUserPostalCode;
import static com.patach.patachoux.Utils.Constant.setUseremial;
import static com.patach.patachoux.Utils.Constant.setUsername;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.patach.patachoux.CallBacks.CallListner;
import com.patach.patachoux.MainActivity;
import com.patach.patachoux.R;

public class LoginActivity extends AppCompatActivity {
    private EditText etLoginEmail, etLoginPassword;
    private FirebaseAuth firebaseAuth;


    DatabaseReference myRef;
    private Dialog loadingDialog;
    String token;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        /////loading dialog
        loadingDialog=new Dialog(this);
        loadingDialog.setContentView(R.layout.loading_progress_dialog);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.slider_background));
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        etLoginEmail =findViewById(R.id.et_login_email);
        etLoginPassword = findViewById(R.id.et_login_password);
        Button btnLogin = findViewById(R.id.btn_login);
        firebaseAuth = FirebaseAuth.getInstance();
        getDeviceToken();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
            @Override
            public void onClick(View view) {
                String email = etLoginEmail.getText().toString();
                String password = etLoginPassword.getText().toString();
                if (validate(email, password)) requestLogin(email, password);
            }
        });

        findViewById(R.id.tv_reset_password).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class));

            }
        });
    }
    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
    private boolean validate(String email, String password) {
        if (email.isEmpty()) etLoginEmail.setError("Enter email!");
        else if (!email.contains("@")||!email.contains(".")) etLoginEmail.setError("Enter valid email!");
        else if (password.isEmpty()) etLoginPassword.setError("Enter password!");
        else if (password.length()<6) etLoginPassword.setError("Password must be at least 6 characters!");
        else return true;
        return false;
    }

    private void requestLogin(String email, String password) {
        loadingDialog.show();
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (!task.isSuccessful()) {
                    loadingDialog.dismiss();
                    Toast.makeText(LoginActivity.this, "wrong mail or password" + task.getException(), Toast.LENGTH_LONG).show();
                } else if (task.isSuccessful()) {
                    getData(new CallListner() {
                        @Override
                        public void callback(boolean status) {
                            if(status){
                                 openHomeActivity();
                            }
                            else {
                          getData1(new CallListner() {
                              @Override
                              public void callback(boolean status) {
                                  if(status){
                                      openHomeActivity1();
                                  }
                              }
                          });
                            }
                        }
                    });

                }
            }
        });
    }

    private void openHomeActivity() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    private void getData(final CallListner callListner){
        final String user_m=etLoginEmail.getText().toString().trim();
        String id = firebaseAuth.getCurrentUser().getUid();
        myRef=  FirebaseDatabase.getInstance().getReference().child("User");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){

                    for(DataSnapshot dataSnapshot2:dataSnapshot1.getChildren()){
                        if(user_m.equals(dataSnapshot2.child("Mail").getValue(String.class))) {

                            setUserId(LoginActivity.this,dataSnapshot2.child("UserId").getValue(String.class));
                            setAdminId(LoginActivity.this,dataSnapshot2.child("AdminId").getValue(String.class));
                            setUsername(LoginActivity.this,dataSnapshot2.child("Name").getValue(String.class));
                            setUserAddress(LoginActivity.this,dataSnapshot2.child("Address").getValue(String.class));
                            setUserNumber(LoginActivity.this,dataSnapshot2.child("PhoneNumber").getValue(String.class));
                            setUserPic(LoginActivity.this,dataSnapshot2.child("UserImage").getValue(String.class));

                            setUserPostalCode(LoginActivity.this,dataSnapshot2.child("PostalCode").getValue(String.class));
                            setUserCity(LoginActivity.this,dataSnapshot2.child("City").getValue(String.class));
                            setUserCode(LoginActivity.this,dataSnapshot2.child("SecretCode").getValue(String.class));
                            setUserLoginStatus(LoginActivity.this, true);
                            setSplierStatus(LoginActivity.this,false);
                            setUseremial(LoginActivity.this,etLoginEmail.getText().toString().trim());



                            FirebaseDatabase.getInstance().getReference("User").child(dataSnapshot2.child("AdminId").getValue(String.class)).
                                    child(dataSnapshot2.child("UserId").getValue(String.class)).child("DeviceToken").setValue(token);
                            loadingDialog.dismiss();
                           callListner.callback(true);
                          // break;
                        }
                    }
                }
                callListner.callback(false);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                   callListner.callback(false);
            }
        });
    }


    private void openHomeActivity1() {
        startActivity(new Intent(this, SuplierMainActivity.class));
        finish();
    }


    private void getData1(final CallListner callListner){
        final String user_m=etLoginEmail.getText().toString().trim();
        myRef=  FirebaseDatabase.getInstance().getReference().child("Suplier");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                    for(DataSnapshot dataSnapshot2:dataSnapshot1.getChildren()){
                        if(user_m.equals(dataSnapshot2.child("Mail").getValue(String.class))) {
                            setUsername(LoginActivity.this,dataSnapshot2.child("FirstName").getValue(String.class)+dataSnapshot2.child("LastName").getValue(String.class));
                            setUserLoginStatus(LoginActivity.this, false);
                            setSplierStatus(LoginActivity.this,true);
                            String id =dataSnapshot2.child("AdminId").getValue(String.class);
                            setAdminId(LoginActivity.this,dataSnapshot2.child("AdminId").getValue(String.class));
                            setUserId(LoginActivity.this,dataSnapshot2.child("Id").getValue(String.class));
                            setUseremial(LoginActivity.this,etLoginEmail.getText().toString().trim());
                            FirebaseDatabase.getInstance().getReference("Suplier").child(dataSnapshot2.child("AdminId").getValue(String.class)).
                                    child(dataSnapshot2.child("Id").getValue(String.class)).child("DeviceToken").setValue(token);
                            loadingDialog.dismiss();
                            callListner.callback(true);
                        }
                    }

                }
                loadingDialog.dismiss();
                callListner.callback(false);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                callListner.callback(false);
            }
        });
    }
    @Override
    public void onBackPressed() {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Do you want to exit?");
        alertDialogBuilder.setPositiveButton("yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        LoginActivity.super.onBackPressed();
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


    public void getDeviceToken(){
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w("message", "Fetching FCM registration token failed", task.getException());
                            return;
                        }

                        // Get new FCM registration token
                        token = task.getResult();


                    }
                });

    }
}