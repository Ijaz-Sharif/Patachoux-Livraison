package com.patach.patachoux.Screens;

import static com.patach.patachoux.Utils.Constant.getAdminId;
import static com.patach.patachoux.Utils.Constant.getUserAddress;
import static com.patach.patachoux.Utils.Constant.getUserCity;
import static com.patach.patachoux.Utils.Constant.getUserCode;
import static com.patach.patachoux.Utils.Constant.getUserId;
import static com.patach.patachoux.Utils.Constant.getUserNumber;
import static com.patach.patachoux.Utils.Constant.getUserPic;
import static com.patach.patachoux.Utils.Constant.getUserPostalCode;
import static com.patach.patachoux.Utils.Constant.getUsername;
import static com.patach.patachoux.Utils.Constant.setUserAddress;
import static com.patach.patachoux.Utils.Constant.setUserCity;
import static com.patach.patachoux.Utils.Constant.setUserNumber;
import static com.patach.patachoux.Utils.Constant.setUserPic;
import static com.patach.patachoux.Utils.Constant.setUserPostalCode;
import static com.patach.patachoux.Utils.Constant.setUsername;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.patach.patachoux.R;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class UpdateUserProfileActivity extends AppCompatActivity {
    private EditText et_register_address,et_user_number, et_city,et_postal_code,et_code,et_user_name;
    DatabaseReference myRef;
    private Dialog loadingDialog;
    CircleImageView imageView;
    StorageReference mRef;
    private Uri imgUri =null;
    private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_user_profile);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        mRef= FirebaseStorage.getInstance().getReference("profile_images");
        firebaseAuth = FirebaseAuth.getInstance();
        et_user_name=findViewById(R.id.et_user_name);
        imageView=findViewById(R.id.userPic);
        et_user_number=findViewById(R.id.et_user_number);
        et_register_address=findViewById(R.id.et_register_address);
        et_postal_code=findViewById(R.id.et_postal_code);
        et_city=findViewById(R.id.et_city);
        et_code=findViewById(R.id.et_code);
        /////loading dialog
        loadingDialog=new Dialog(this);
        loadingDialog.setContentView(R.layout.loading_progress_dialog);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.slider_background));
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @Override
    protected void onStart() {
        et_user_number.setText(getUserNumber(this));
        et_register_address.setText(getUserAddress(this));
        et_postal_code.setText(getUserPostalCode(this));
        et_city.setText(getUserCity(this));
        et_code.setText(getUserCode(this));
        et_user_name.setText(getUsername(this));
        Picasso.with(this)
                .load(getUserPic(this))
                .placeholder(R.drawable.progress_animation)
                .fit()
                .centerCrop()
                .into(imageView);
        super.onStart();
    }
    // get the extension of file
    private String getFileEx(Uri uri){
        ContentResolver cr=UpdateUserProfileActivity.this.getContentResolver();
        MimeTypeMap mime=MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri));
    }
    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
    public void updateProfile(View view) {
        loadingDialog.show();
        myRef=  FirebaseDatabase.getInstance().getReference().child("User").child(getAdminId(UpdateUserProfileActivity.this)).child(getUserId(this));
        if(imgUri!=null){
            StorageReference storageReference = mRef.child(System.currentTimeMillis() + "." + getFileEx(imgUri));
            storageReference.putFile(imgUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
                            while (!urlTask.isSuccessful()) ;
                            Uri downloadUrl = urlTask.getResult();
                            myRef.child("Name").setValue(et_user_name.getText().toString());
                            myRef.child("Address").setValue(et_register_address.getText().toString());
                            myRef.child("PhoneNumber").setValue(et_user_number.getText().toString());
                            myRef.child("City").setValue(et_city.getText().toString());
                            myRef.child("PostalCode").setValue(et_postal_code.getText().toString());
                            myRef.child("SecretCode").setValue(et_code.getText().toString());
                            myRef.child("UserImage").setValue(downloadUrl.toString());
                            setUserPic(UpdateUserProfileActivity.this,downloadUrl.toString());
                            setUsername(UpdateUserProfileActivity.this,et_user_name.getText().toString());
                            setUserAddress(UpdateUserProfileActivity.this,et_register_address.getText().toString());
                            setUserNumber(UpdateUserProfileActivity.this,et_user_number.getText().toString());

                            setUserPostalCode(UpdateUserProfileActivity.this,et_postal_code.getText().toString());
                            setUserCity(UpdateUserProfileActivity.this,et_city.getText().toString());

                            loadingDialog.dismiss();
                            Toast.makeText(UpdateUserProfileActivity.this,"profile updated", Toast.LENGTH_LONG).show();

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            loadingDialog.dismiss();
                            Toast.makeText(UpdateUserProfileActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                        }
                    });

        }
        else {
            myRef.child("Name").setValue(et_user_name.getText().toString());
            myRef.child("Address").setValue(et_register_address.getText().toString());
            myRef.child("PhoneNumber").setValue(et_user_number.getText().toString());
            myRef.child("City").setValue(et_city.getText().toString());
            myRef.child("PostalCode").setValue(et_postal_code.getText().toString());
            myRef.child("SecretCode").setValue(et_code.getText().toString());
            setUsername(UpdateUserProfileActivity.this,et_user_name.getText().toString());

            setUserAddress(UpdateUserProfileActivity.this,et_register_address.getText().toString());
            setUserNumber(UpdateUserProfileActivity.this,et_user_number.getText().toString());

            setUserPostalCode(UpdateUserProfileActivity.this,et_postal_code.getText().toString());
            setUserCity(UpdateUserProfileActivity.this,et_city.getText().toString());

            Toast.makeText(UpdateUserProfileActivity.this,"profile updated", Toast.LENGTH_LONG).show();

            loadingDialog.dismiss();
        }



    }

    public void updatePicture(View view) {
        Intent intent=new Intent(Intent.ACTION_PICK,android.provider. MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent,1);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && null != data) {
            imgUri  = data.getData();
            imageView.setImageURI(imgUri);
        }

    }

    public void finish(View view) {
        finish();
    }
}