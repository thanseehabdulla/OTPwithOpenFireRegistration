package com.app.ats.com.logintest;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.app.ats.com.logintest.openfire.Broadcast;
import com.app.ats.com.logintest.openfire.ConnectXmpp;

import java.util.HashMap;
import java.util.Map;

import de.greenrobot.event.EventBus;

/**
 * Created by abdulla on 7/4/17.
 */

public class Otp extends AppCompatActivity {

    Button b;
    EditText e;
RequestQueue queue;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

    //initialize
        b=(Button)findViewById(R.id.button);
        e=(EditText)findViewById(R.id.editText);


        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              String otp = null;
            try{
                 otp = e.getText().toString();
            }catch (Exception e){}

if(queue==null)
    queue= Volley.newRequestQueue(getApplicationContext());

              String  url = "http://vqtest.southeastasia.cloudapp.azure.com/VraiQueue/service/validateOTP";
                final String finalOtp = otp;
                StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>()
                        {
                            @Override
                            public void onResponse(String response) {
                                // response
                                Log.d("Response", response);


                                if(response.equals("true")) {
                                    Toast.makeText(getApplicationContext(), "true", Toast.LENGTH_SHORT).show();

                                    Intent intent = new Intent(getBaseContext(),ConnectXmpp.class );
                                    intent.putExtra("user","9847444159");
                                    intent.putExtra("pwd","vrai");
                                    intent.putExtra("code","0");
                                    startService(intent);
                                }
                                else
                                    Toast.makeText(getApplicationContext(),"false",Toast.LENGTH_SHORT).show();


                            }
                        },
                        new Response.ErrorListener()
                        {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                // error
                                Log.d("Error.Response", String.valueOf(error));

                                Toast.makeText(getApplicationContext(),"failure",Toast.LENGTH_SHORT).show();
                            }
                        }
                ) {
                    @Override
                    protected Map<String, String> getParams()
                    {
                        Map<String, String>  params = new HashMap<String, String>();
                      String mobile=getSharedPreferences("mobile",MODE_PRIVATE).getString("mobile","0");
                        params.put("mobile","9567969610");
                        params.put("otp", finalOtp);
                        params.put("user_type", "2");
                        return params;
                    }
                };
                queue.add(postRequest);



            }
        });



    }
    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
    public void onEvent(Broadcast event){
        NotificationManager notif=(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notify= null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
            notify = new Notification.Builder
                    (getApplicationContext()).setContentTitle("XMPP Notify").setContentText(event.getMessage()).
                    setContentTitle("Testing").setSmallIcon(R.mipmap.ic_launcher).build();
        }

        notify.flags |= Notification.FLAG_AUTO_CANCEL;
        notif.notify(0, notify);



    }


    @Override
    public void onResume(){

        super.onResume();
        EventBus.getDefault().registerSticky(this);
        // Set title bar
    }


    @Override
    public void onPause() {

        EventBus.getDefault().unregister(this);
        super.onPause();
    }





}
