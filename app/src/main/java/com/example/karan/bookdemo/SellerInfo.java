package com.example.karan.bookdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class SellerInfo extends AppCompatActivity {

    RequestQueue mqueue;
    String name,email,phoneNo;
    int id;
    Bundle b;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_info);
        MyVolley.init(SellerInfo.this);
        mqueue = MyVolley.getRequestQueue();
        b = getIntent().getExtras();
        if(b!=null){
            if(b.containsKey("id")){
                name = b.getString("name");
                id = b.getInt("id");
            }
        }
    }

    public void VolleyConnnect(){

        JSONObject jo = new JSONObject();

        try {
            jo.put("name",name);
            jo.put("id",id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest mreq = new JsonObjectRequest(Request.Method.POST, "", jo, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                try {
                    email = jsonObject.getString("email");
                    phoneNo = jsonObject.getString("phoneno");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_seller_info, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
