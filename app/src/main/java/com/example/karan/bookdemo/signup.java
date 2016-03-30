package com.example.karan.bookdemo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class signup extends AppCompatActivity {

    private EditText nam,pass,email,num;
    private static final String LOGIN_URL = "http://kmodi4.esy.es/BookDemo/registration.php";    //url of your php file
    RequestQueue mQueue11;
    private ProgressDialog pDialog;
    Button reg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar4);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            //getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            //getSupportActionBar().setTitle(getTitle());
        }
        nam = (EditText) findViewById(R.id.name);
        email = (EditText) findViewById(R.id.eid);
        pass = (EditText) findViewById(R.id.pass);
        num = (EditText) findViewById(R.id.num);
        reg = (Button) findViewById(R.id.reg);

        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startprogress();
                volleyconnect();
            }
        });




        MyVolley.init(this);
        mQueue11 = MyVolley.getRequestQueue();

    }

    public void startprogress(){
        pDialog = new ProgressDialog(signup.this);

        pDialog.setMessage("Attempting for Registration...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(true);
        pDialog.show();
    }

    public String getstr(EditText et){
        String st1 = et.getText().toString();
        return st1;
    }

    private void volleyconnect(){
        String name=getstr(nam);
        String password=getstr(pass);
        String emailid = getstr(email);
        String number =  (getstr(num));


        JSONObject jo = new JSONObject();
        try {
            jo.put("name",name);
            jo.put("password",password);
            jo.put("email",emailid);
            jo.put("phoneno",number);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest myReq = new JsonObjectRequest(Request.Method.POST,
                LOGIN_URL,
                jo,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        pDialog.dismiss();
                        try {
                            Toast.makeText(getApplicationContext(), response.getString("message"), Toast.LENGTH_SHORT).show();
                            int success = response.getInt("success");
                            if(success==1) {
                                Intent i = new Intent(signup.this, login.class);
                                //i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                //i.putExtra("name", username);
                                startActivity(i);
                                finish();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.dismiss();
                Toast.makeText(getApplicationContext(),"Unknow Error",Toast.LENGTH_SHORT).show();

            }

        });


        mQueue11.add(myReq);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_signup, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;


        }

        return super.onOptionsItemSelected(item);
    }
}
