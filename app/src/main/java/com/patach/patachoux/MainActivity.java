package com.patach.patachoux;

import static com.patach.patachoux.Utils.Constant.getAdminId;
import static com.patach.patachoux.Utils.Constant.getUserId;
import static com.patach.patachoux.Utils.Constant.getUserPic;
import static com.patach.patachoux.Utils.Constant.getUseremail;
import static com.patach.patachoux.Utils.Constant.getUsername;
import static com.patach.patachoux.Utils.Constant.setUserLoginStatus;
import static com.patach.patachoux.Utils.Constant.setUseremial;
import static com.patach.patachoux.Utils.Constant.setUsername;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.patach.patachoux.Adapter.ViewPagerAdapter;
import com.patach.patachoux.CallBacks.CallListner;
import com.patach.patachoux.Cart.CartActivity;
import com.patach.patachoux.Fragments.BreadFragment;
import com.patach.patachoux.Fragments.PasteryFragment;
import com.patach.patachoux.Screens.LoginActivity;
import com.patach.patachoux.Screens.ProductDetailActivity;
import com.patach.patachoux.Screens.UpdateUserProfileActivity;
import com.patach.patachoux.Screens.UserOrderActivity;
import com.patach.patachoux.Service.NotificationService;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {
    ViewPager viewPager;
    TabLayout tabLayout;
    private DrawerLayout drawer;
    NavigationView navigationView;
    TextView user_name,user_mail;
    CircleImageView userImage;
    TextView cart_count;
    int count=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        tabLayout=findViewById(R.id.tab_layout);
        viewPager=findViewById(R.id.may_viewpager);

        tabLayout.setupWithViewPager(viewPager);
        navigationView =findViewById(R.id.nav_view);
        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toogle=new ActionBarDrawerToggle(this,drawer, (androidx.appcompat.widget.Toolbar) findViewById(R.id.toolbar),R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawer.addDrawerListener(toogle);
        toogle.syncState();
        View headerView=navigationView.getHeaderView(0);
        user_name=headerView.findViewById(R.id.name_user);
        user_mail=headerView.findViewById(R.id.user_email);
        userImage=headerView.findViewById(R.id.user_image);
        cart_count= findViewById(R.id.cart_count);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.logout:
                        setUsername(MainActivity.this,"");
                        setUserLoginStatus(MainActivity.this, false);
                        setUseremial(MainActivity.this,"");
                        FirebaseDatabase.getInstance().getReference("User").child(getAdminId(MainActivity.this)).
                                child(getUserId(MainActivity.this)).child("DeviceToken").setValue("empty");
                        startActivity(new Intent(MainActivity.this, LoginActivity.class)
                                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                        finish();
                        /// do some code here
                        break;
                    case R.id.home:
                        setUpViewPager(viewPager);
                        break;
                    case R.id.update_profile:
                        startActivity(new Intent(MainActivity.this, UpdateUserProfileActivity.class));
                        break;
                    case R.id.cart:
                        startActivity(new Intent(MainActivity.this, CartActivity.class));
                        break;
                    case R.id.order_history:
                        startActivity(new Intent(MainActivity.this, UserOrderActivity.class));

                        break;


                }
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });
    }

    @Override
    protected void onStart() {
        getCartCount();
        user_name.setText(getUsername(MainActivity.this));
        user_mail.setText(getUseremail(MainActivity.this));
        Picasso.with(this)
                .load(getUserPic(MainActivity.this))
                .placeholder(R.drawable.progress_animation)
                .fit()
                .centerCrop()
                .into(userImage);
       setUpViewPager(viewPager);


        super.onStart();
    }


    public void getCartCount(){

        DatabaseReference dRef=  FirebaseDatabase.getInstance().getReference().child("User").child(getAdminId(this)).child(getUserId(this)).child("Cart");

        dRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                count=0;
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    count++;
                }
                cart_count.setText(count+"");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
    private void setUpViewPager(ViewPager viewPager){
        ViewPagerAdapter viewPagerAdapter=new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragment(new BreadFragment(),"Bread");
        viewPagerAdapter.addFragment(new PasteryFragment(),"Pastry");
        viewPager.setAdapter(viewPagerAdapter);
    }

    public void openCartScreen(View view) {
        startActivity(new Intent(MainActivity.this, CartActivity.class));
    }

    @Override
    public void onBackPressed() {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Do you want to exit?");
        alertDialogBuilder.setPositiveButton("yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        MainActivity.super.onBackPressed();
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
}