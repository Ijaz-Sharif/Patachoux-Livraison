package com.patach.patachoux.Fragments;

import static com.patach.patachoux.Utils.Constant.getAdminId;
import static com.patach.patachoux.Utils.Constant.getUserId;
import static com.patach.patachoux.Utils.Constant.getUsername;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.patach.patachoux.Model.Product;
import com.patach.patachoux.R;
import com.patach.patachoux.Screens.PastryProductDetailActivity;
import com.patach.patachoux.Screens.ProductDetailActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class PasteryFragment extends Fragment {
    RecyclerView recyclerView;
    DatabaseReference dRef;
    ArrayList<Product> productArrayList =new ArrayList<Product>();
    ArrayAdapter arrayAdapter;
    private Dialog loadingDialog;
    ArrayList<String> cartProducts = new ArrayList<String>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pastery, container, false);
        loadingDialog=new Dialog(getContext());
        loadingDialog.setContentView(R.layout.loading_progress_dialog);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.slider_background));
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        recyclerView=view.findViewById(R.id.recylerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        getCartData();
        return view;
    }
    @Override
    public void onStart() {
   //   getCartData();
        super.onStart();
    }
    public void getCartData(){
        loadingDialog.show();
        cartProducts.clear();
        DatabaseReference databaseReference =FirebaseDatabase.getInstance().getReference().child("User").child(getAdminId(getContext())).child(getUserId(getContext())).child("Cart");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                    cartProducts.add(dataSnapshot1.child("ProductName").getValue(String.class));
                }

                getProductsData();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void getProductsData(){
        productArrayList.clear();


        dRef=  FirebaseDatabase.getInstance().getReference("User").child(getAdminId(getContext())).child(getUserId(getContext())).child("Products").child("Pastry");
        dRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {

                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Products").child("Pastry").child( postSnapshot.child("ProductId").getValue(String.class));


                    databaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot productSnapshot) {

                            productArrayList.add(new Product(
                                    productSnapshot.child("ProductId").getValue(String.class),
                                    productSnapshot.child("ProductName").getValue(String.class)
                                    ,postSnapshot.child("ProductPrice").getValue(String.class)
                                    ,productSnapshot.child("ProductImage").getValue(String.class)
                                    ,productSnapshot.child("ProductDescription").getValue(String.class)

                            ));
                            arrayAdapter.notifyDataSetChanged();


                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });


                }
                arrayAdapter =new ArrayAdapter(productArrayList);
                recyclerView.setAdapter(arrayAdapter);
                loadingDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    public class ArrayAdapter extends RecyclerView.Adapter<ArrayAdapter.ImageViewHoler> {

        public ArrayAdapter(ArrayList<Product> itemArrayList1){
            productArrayList =itemArrayList1;
        }
        @NonNull
        @Override
        public ArrayAdapter.ImageViewHoler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v= LayoutInflater.from(getContext()).inflate(R.layout.item_product,parent,false);
            return  new ArrayAdapter.ImageViewHoler(v);
        }
        @Override
        public void onBindViewHolder(@NonNull final ArrayAdapter.ImageViewHoler holder, @SuppressLint("RecyclerView") int position) {


            Picasso.with(getContext())
                    .load(productArrayList.get(position).getProductPic())
                    .placeholder(R.drawable.progress_animation)
                    .fit()
                    .centerCrop()
                    .into(holder.imageView);
            holder.product_name.setText(productArrayList.get(position).getProductName());
            holder.price.setText(productArrayList.get(position).getProductPrice()+" $");
            holder.product_detail.setText(productArrayList.get(position).getProductDescription());


            if(cartProducts.contains(productArrayList.get(position).getProductName())){
                holder.btn_cart.setText("In Cart");
            }

            else {
                holder.btn_cart.setText("Add to Cart");
            }


            holder.btn_cart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(getContext(), PastryProductDetailActivity.class)
                            .putExtra("name",productArrayList.get(position).getProductName())
                            .putExtra("price",productArrayList.get(position).getProductPrice())
                            .putExtra("image",productArrayList.get(position).getProductPic())
                            .putExtra("des",productArrayList.get(position).getProductDescription())
                            .putExtra("productId",productArrayList.get(position).getProductId()));

                }
            });
        }

        @Override
        public int getItemCount() {
            return productArrayList.size();
        }

        public class ImageViewHoler extends RecyclerView.ViewHolder {
            ImageView imageView;
            TextView product_name,product_detail,price;
            CardView cardView;
            Button btn_cart;
            public ImageViewHoler(@NonNull View itemView) {
                super(itemView);
                imageView=itemView.findViewById(R.id.image);
                product_name=itemView.findViewById(R.id.name);
                cardView=itemView.findViewById(R.id.cardView);
                product_detail=itemView.findViewById(R.id.detail);
                price=itemView.findViewById(R.id.price);
                btn_cart=itemView.findViewById(R.id.btn_cart);
            }
        }
    }
}