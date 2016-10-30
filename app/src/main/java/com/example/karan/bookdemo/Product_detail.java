package com.example.karan.bookdemo;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonArrayRequest;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import dmax.dialog.SpotsDialog;

import static com.example.karan.bookdemo.R.id.pedition;

public class Product_detail extends AppCompatActivity implements MyServer {

    private ImageView iv;
    private TextView tv,op,yp,sellerid,dis;
    private TextView pisbn,pcond,pauth,ppub,ppages,pdesc,pedi;
    int discount;
    int yourprice;
    int originalprice,pages;
    String sellername,title;
    SharedPreferences sharedPreferences;
    boolean status;
    private String isbn,author,edition,condition,publisher,desc;
    private RequestQueue mqueue;
    private android.app.AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.tb);
        iv = (ImageView) findViewById(R.id.imageView4);
        tv = (TextView) findViewById(R.id.bktitle);
        op = (TextView) findViewById(R.id.op);
        yp = (TextView) findViewById(R.id.yp);
        sellerid = (TextView) findViewById(R.id.sellerid);
        dis = (TextView) findViewById(R.id.dis);

        pisbn = (TextView) findViewById(R.id.pisbn);
        pauth = (TextView) findViewById(R.id.pauthor);
        ppub = (TextView) findViewById(R.id.ppub);
        ppages = (TextView) findViewById(R.id.ppages);
        pcond = (TextView) findViewById(R.id.pcondition);
        pdesc = (TextView) findViewById(R.id.pdesc);
        pedi = (TextView) findViewById(pedition);

        MyVolley.init(this.getApplicationContext());
        mqueue = MyVolley.getRequestQueue();
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            //getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            //getSupportActionBar().setTitle(getTitle());
        }

        Bundle b = getIntent().getExtras();
        sharedPreferences = getSharedPreferences("Login", Context.MODE_PRIVATE);
        status = sharedPreferences.getBoolean("LStatus",false);
        if (b != null) {
            final String url = b.getString("image");
            title = b.getString("title");
            //Toast.makeText(getApplicationContext(),url,Toast.LENGTH_SHORT).show();
            if(b.containsKey("yourprice")) {
                yourprice = b.getInt("yourprice");
                originalprice = b.getInt("originalprice");
                sellername = b.getString("seller");
                discount = ((originalprice - yourprice) * 100) / originalprice;

                sellerid.setText(sellername);
                yp.setText(String.valueOf(yourprice));
                op.setText(String.valueOf(originalprice));
                dis.setText(String.valueOf(discount) + "%");
            }
            Glide.with(Product_detail.this).load(url).crossFade().into(iv);



            tv.setText(title);
        }
        LinearLayout ll = (LinearLayout) findViewById(R.id.sellerview);
        ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(status) {
                    Intent i = new Intent(Product_detail.this, SellerProfile.class);
                    i.putExtra("seller", sellername);
                    startActivity(i);
                }
                else {
                    Toast.makeText(getApplicationContext(),"Login to get Access",Toast.LENGTH_SHORT).show();
                }
            }
        });

        /*dialog = new SpotsDialog(this, R.style.Custom2);
        dialog.show();*/
        VolleySpecs();
    }

    public void myDialog() {
        AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(this);
        myAlertDialog.setTitle("Contact Option");
        myAlertDialog.setMessage("Select  From");
        myAlertDialog.setPositiveButton("Call", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent i1 = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "8460529123"));
                if (ActivityCompat.checkSelfPermission(Product_detail.this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                startActivity(i1);

            }
        });
        myAlertDialog.setNegativeButton("Chat", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
               Toast.makeText(getApplicationContext(),"Chat Option",Toast.LENGTH_SHORT).show();
                Intent i2 = new Intent(Product_detail.this,ChatList.class);
                i2.putExtra("seller",sellername);
                startActivity(i2);
            }
        });
        myAlertDialog.show();
    }

    public void contactmethod(View view){
        if (status) {
            myDialog();
        }
        else {
            Toast.makeText(getApplicationContext(),"Login to get Access",Toast.LENGTH_SHORT).show();
        }
    }

    public void StopProgress(){
        if (dialog!=null && dialog.isShowing()){
            dialog.dismiss();
        }
    }



    public void VolleySpecs(){
        String updateUrl = MyServerUrl+"Specs.php";
        JSONObject jo = new JSONObject();
        JSONArray ja = new JSONArray();


        try {
            jo.put("seller",sellername);
            jo.put("title",title);
            ja.put(jo);
            Log.e("JsonUpdate:",ja.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonArrayRequest mreq1 = new JsonArrayRequest(Request.Method.POST, updateUrl, ja, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

              // StopProgress();

                if (response!=null){
                        for (int i=0; i< response.length(); i++){
                            JSONObject jo;
                            try {
                                jo = response.getJSONObject(i);
                                isbn = jo.getString("isbn");
                                author = jo.getString("author");
                                edition = jo.getString("edition");
                                condition = jo.getString("condition");
                                publisher = jo.getString("publisher");
                                pages = jo.getInt("pages");
                                desc = jo.getString("desc");


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                    pisbn.setText(isbn);
                    pauth.setText(author);
                    ppub.setText(publisher);
                    pcond.setText(condition);
                    ppages.setText(String.valueOf(pages));
                    pdesc.setText(desc);
                    pedi.setText(edition);


                }
                else {
                    Toast.makeText(getApplicationContext(),"can't get details",Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // StopProgress();

                Toast.makeText(getApplicationContext(),"can't get details",Toast.LENGTH_SHORT).show();
            }
        });
        mqueue.add(mreq1);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_product_detail, menu);
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
