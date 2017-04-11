package com.app.ats.com.logintest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {


    EditText e;
    Button b;
RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    //initialize
        e=(EditText)findViewById(R.id.editText);
        b=(Button)findViewById(R.id.button);


        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               String mobile = null;
                try {
                   mobile= e.getText().toString();
                }catch (Exception e){}

                if(queue==null)
        queue= Volley.newRequestQueue(getApplicationContext());


             String   url = "http://vqtest.southeastasia.cloudapp.azure.com/VraiQueue/service/validateUser/";
                final String finalMobile = mobile;
                final String finalMobile1 = mobile;
                StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>()
                        {
                            public JSONObject jo;

                            @Override
                            public void onResponse(String response) {
                                // response
                                Log.d("Response", response);
Toast.makeText(getApplicationContext(),response,Toast.LENGTH_SHORT).show();

                                try {
                                     jo= new JSONObject(response);
                                } catch (JSONException e1) {
                                    e1.printStackTrace();
                                }

                                getSharedPreferences("mobile",MODE_PRIVATE).edit().clear().putString("mobile", finalMobile1).apply();

                                Intent i = new Intent(getApplicationContext(),Otp.class);
                                finish();
                                startActivity(i);




                            }
                        },
                        new Response.ErrorListener()
                        {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                // error
                                Toast.makeText(getApplicationContext(),"failure",Toast.LENGTH_SHORT).show();
                                Log.d("Error.Response", String.valueOf(error));
                            }
                        }
                ) {
                    @Override
                    protected Map<String, String> getParams()
                    {
                        Map<String, String>  params = new HashMap<String, String>();
                        params.put("mobile", finalMobile);
                        params.put("userType", "patient");

                        return params;
                    }
                };
                queue.add(postRequest);


            }
        });



    }
}
