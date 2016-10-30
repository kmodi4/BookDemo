package com.example.karan.bookdemo;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

public class MyProfile extends AppCompatActivity {

   private EditText et1,et3,et4,et5,et6;
    private ImageView iv;
    TextView et2;
    private SharedPreferences pf;
    private SharedPreferences.Editor pedit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.tb2);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN); SharedPreferences profile = getSharedPreferences("offlineprofile", Context.MODE_PRIVATE);
        pf = getSharedPreferences("offlineprofile",Context.MODE_PRIVATE);
        pedit = pf.edit();

        iv = (ImageView) findViewById(R.id.pfimage);
        String name = pf.getString("Name","");
        String email = pf.getString("EmailId","");
        String phno = pf.getString("Phoneno","");
        String username = pf.getString("username","");
        String url = pf.getString("url","");
        String adr = pf.getString("address","");
        String abt = pf.getString("about","");
        if (!url.equals("")){
            Glide.with(this).load(url).crossFade().into(iv);
        }

        

        et1 = (EditText) findViewById(R.id.pf1);
        et2 = (TextView) findViewById(R.id.pf2);
        et3 = (EditText) findViewById(R.id.pf3);
        et4 = (EditText) findViewById(R.id.pf4);
        et5 = (EditText) findViewById(R.id.pf5);
        et6 = (EditText) findViewById(R.id.pf6);

        et1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                et1.setCursorVisible(true);

            }
        });

        et1.setText(name);
        et2.setText(username);
        et3.setText(email);
        if (phno.equals("")){
            et4.setText("Phone No");
        }
        else {
            et4.setText(phno);
        }

        if (!adr.equals("")){
            et5.setText(adr);
        }
        if (!abt.equals(""))
            et6.setText(abt);




    }

    public void StoreDataPref(){

        pedit.putString("address",et5.getText().toString());
        pedit.putString("about",et6.getText().toString());
        pedit.apply();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_my_profile,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;

            case R.id.saveprifile:
                StoreDataPref();
                Toast.makeText(getApplicationContext(),"Stored Successfully",Toast.LENGTH_SHORT).show();
                return true;


        }

        return super.onOptionsItemSelected(item);
    }
}
