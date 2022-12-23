package com.patach.patachoux.Service;

import static com.patach.patachoux.Utils.Constant.*;
import static com.patach.patachoux.Utils.Constant.getAdminId;
import static com.patach.patachoux.Utils.Constant.getUsername;
import static com.patach.patachoux.Utils.Constant.setAdminId;
import static com.patach.patachoux.Utils.Constant.setSplierStatus;
import static com.patach.patachoux.Utils.Constant.setUserId;
import static com.patach.patachoux.Utils.Constant.setUserLoginStatus;
import static com.patach.patachoux.Utils.Constant.setUseremial;
import static com.patach.patachoux.Utils.Constant.setUsername;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyLog;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.NetworkError;
import com.android.volley.error.NoConnectionError;
import com.android.volley.error.ParseError;
import com.android.volley.error.ServerError;
import com.android.volley.error.TimeoutError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonObjectRequest;
import com.android.volley.request.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.patach.patachoux.CallBacks.CallListner;
import com.patach.patachoux.Screens.LoginActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class NotificationService {
    private static final NotificationService instance = new NotificationService();
    public static NotificationService getInstance() {
        return instance;
    }
String url ="https://fcm.googleapis.com/fcm/send";
    String serverKey="Key=AAAAL0Zv1Xg:APA91bFbcTihrHz5Ozd2dO31KvnF4Yf1cILBxwG8E8RuSdrAi6IQI0gk98VqFmfZyrbRg8McP7Ci5lpHQ00oX7tpMa3QUawtm1eNZK2HUDs-k4Ak86s6JYL7dPqry1IAgkHbwv6PFT2X";



    public ArrayList<String> deviceTokenArrayList = new ArrayList<String>();

   

    String deviceToken="";
    public  void postRequest( final Context context ){
        JSONObject jsonObject = new JSONObject();
        JSONObject bodyJson = new JSONObject();
        try {
            jsonObject.put("title","New Order");
            jsonObject.put("body",getUsername(context)+" has place the order");
            jsonObject.put("check","suplier");
            for(int i=0;i<deviceTokenArrayList.size();i++){
                bodyJson.put("to",deviceTokenArrayList.get(i));
                bodyJson.put("data",jsonObject);

                JsonObjectRequest jsonOblect = new JsonObjectRequest(Request.Method.POST, url, bodyJson, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("error",response.toString());
                        //Toast.makeText(context, "Response:  " + response.toString(), Toast.LENGTH_SHORT).show();

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, "error:  " + error.toString(), Toast.LENGTH_SHORT).show();



                    }
                }) {
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        params.put("Authorization", serverKey);
                        params.put("Content-Type","application/json");
                        return params;
                    }
                };


                RequestQueue a = Volley.newRequestQueue(context);

                a.add(jsonOblect);

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }



    }

    public  void completeOrder( final Context context , final CallListner callListner){
        JSONObject jsonObject = new JSONObject();
        JSONObject bodyJson = new JSONObject();
        try {
            jsonObject.put("title","Order Completed");
            jsonObject.put("body",getUsername(context)+" has complete the order");
            jsonObject.put("check","user");
            bodyJson.put("to",deviceToken);
            bodyJson.put("data",jsonObject);
            final String requestBody = bodyJson.toString();

            JsonObjectRequest jsonOblect = new JsonObjectRequest(Request.Method.POST, url, bodyJson, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {

                    // Toast.makeText(context, "Response:  " + response.toString(), Toast.LENGTH_SHORT).show();
                    callListner.callback(true);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    //   Toast.makeText(context, "error:  " + error.toString(), Toast.LENGTH_SHORT).show();
                    callListner.callback(true);


                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("Authorization", serverKey);
                    params.put("Content-Type","application/json");
                    return params;
                }
            };


            RequestQueue a = Volley.newRequestQueue(context);

            a.add(jsonOblect);


        } catch (JSONException e) {
            e.printStackTrace();
        }



    }


    public String ids(){
        char quotes ='"';
        String deviceTokenList ="";
        for(int i=0;i<deviceTokenArrayList.size();i++){
            if(i==deviceTokenArrayList.size()-1){
                deviceTokenList =deviceTokenList+quotes+deviceTokenArrayList.get(i)+quotes;
            }
            else {
                deviceTokenList =deviceTokenList+quotes+deviceTokenArrayList.get(i)+quotes+",";
            }

        }
        return  deviceTokenList;
    }





    public void getDeviceToken(Context c,String userId ,CallListner callListner){
        DatabaseReference myRef1=   FirebaseDatabase.getInstance().getReference("User").child(getAdminId(c)).child(userId);
        myRef1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!dataSnapshot.child("DeviceToken").getValue(String.class).equals("empty")){
                    deviceToken =dataSnapshot.child("DeviceToken").getValue(String.class);
                }

                    callListner.callback(true);



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }


    public void getAdminDeviceToken(Context c){
        DatabaseReference myRef1=  FirebaseDatabase.getInstance().getReference().child("SubAdmin").child(getAdminId(c));
        myRef1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!dataSnapshot.child("DeviceToken").getValue(String.class).equals("empty")){
                    deviceTokenArrayList.add(dataSnapshot.child("DeviceToken").getValue(String.class));
                }


//


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }
public void getSuplierDeviceToken(Context c){

    DatabaseReference myRef=  FirebaseDatabase.getInstance().getReference().child("Suplier").child(getAdminId(c));
    myRef.addListenerForSingleValueEvent(new ValueEventListener() {
        @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                  if(!dataSnapshot1.child("DeviceToken").getValue(String.class).equals("empty")){
                      deviceTokenArrayList.add(dataSnapshot1.child("DeviceToken").getValue(String.class));

                  }
            }



        }


        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }

    });
}













}
