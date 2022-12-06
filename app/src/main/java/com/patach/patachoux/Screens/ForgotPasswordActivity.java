package com.patach.patachoux.Screens;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.patach.patachoux.R;

public class ForgotPasswordActivity extends AppCompatActivity {
    EditText email;
    ImageView imageView1;
    Button resetbutton;
    ViewGroup container;
    TextView goback,textView;
    ProgressBar progressBar;
    FirebaseAuth firebaseAuth;
    private Dialog loadingDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        email=findViewById(R.id.editText);
        resetbutton=findViewById(R.id.button);
        goback=findViewById(R.id.goback);
        textView=findViewById(R.id.textsuces);
        container=findViewById(R.id.linearLayout);
        progressBar=findViewById(R.id.progressBar);
        firebaseAuth=FirebaseAuth.getInstance();
        imageView1=findViewById(R.id.icomimage);

        /////loading dialog
        loadingDialog=new Dialog(this);
        loadingDialog.setContentView(R.layout.loading_progress_dialog);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.slider_background));
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);

        goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        resetbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String eemail =   email.getText().toString();

                if (TextUtils.isEmpty(eemail)){

                    Toast.makeText(ForgotPasswordActivity.this, "Enter Your Email", Toast.LENGTH_SHORT).show();
                }

                else {

                    textView.setVisibility(View.GONE);

                    loadingDialog.show();
                    // call the function send password of firebase this will send the passsword to your email address
                    firebaseAuth.sendPasswordResetEmail(eemail)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if (task.isSuccessful()){

                                        Toast.makeText(ForgotPasswordActivity.this, "Email Sent Successfully !", Toast.LENGTH_SHORT).show();
                                        imageView1.setVisibility(View.VISIBLE);
                                        imageView1.setImageResource(R.drawable.green_email);
                                        textView.setVisibility(View.VISIBLE);
                                        textView.setTextColor(Color.parseColor("#11A10c"));
                                        email.setText("");
                                        resetbutton.setEnabled(false);
                                        resetbutton.setBackgroundColor(Color.parseColor("#b5261c"));
                                        resetbutton.setTextColor(Color.parseColor("#cccccc"));

                                    }else {

                                        String error = task.getException().getMessage();
                                        Toast.makeText(ForgotPasswordActivity.this, error, Toast.LENGTH_SHORT).show();
                                        textView.setText(error);
                                        textView.setTextColor(getResources().getColor(R.color.colorPrimary));
                                        textView.setVisibility(View.VISIBLE);
                                    }
                                    loadingDialog.dismiss();

                                }
                            });


                }}
        });

    }

    public void finish(View view) {
        finish();
    }
}