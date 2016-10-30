package com.example.karan.bookdemo;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.support.design.widget.NavigationView;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;


import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonArrayRequest;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import jp.wasabeef.recyclerview.animators.ScaleInAnimator;
import jp.wasabeef.recyclerview.animators.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.animators.adapters.SlideInRightAnimationAdapter;

public class MainActivity extends AppCompatActivity implements  NavigationView.OnNavigationItemSelectedListener,MyServer {

    private ViewFlipper mViewFlipper;
    private GestureDetector mGestureDetector;
    private  NavigationView navigationView;
    private SharedPreferences sharedPreferences;
    private RequestQueue mqueue;
    private SharedPreferences.Editor editor;
    private static Boolean status = false;
    private ProgressDialog pdialog;
    Menu menu;
    List<listinfo> data;
    String user;


    int[] resources = {
            R.drawable.img1, R.drawable.img2, R.drawable.img3,
            R.drawable.img4, R.drawable.img5, R.drawable.img6
    };

    RecyclerView recyclerView,rv1;
    Radpater radpater;
    TextView t1,t2,hn,he;
    Bundle b;
    public static final String url = MyServerUrl+"getAllBooks.php";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navg_view_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }
        setTitle("Book");
        t1 = (TextView) findViewById(R.id.textView4);
        t2 = (TextView) findViewById(R.id.textView6);
        hn = (TextView) findViewById(R.id.headername1);
        he= (TextView) findViewById(R.id.headeremail1);
        sharedPreferences = getSharedPreferences("Login", Context.MODE_PRIVATE);
        status = sharedPreferences.getBoolean("LStatus",false);
        //hn.setText("hey");

        b = getIntent().getExtras();

        data = new ArrayList<>();
        MyVolley.init(this);
        mqueue = MyVolley.getRequestQueue();
        mViewFlipper = (ViewFlipper) findViewById(R.id.viewFlipper);


        // Add all the images to the ViewFlipper
        for (int i = 0; i < resources.length; i++) {
            ImageView imageView = new ImageView(this);
            imageView.setImageResource(resources[i]);
            mViewFlipper.addView(imageView);
        }

        // Set in/out flipping animations
        mViewFlipper.setInAnimation(this, android.R.anim.fade_in);
        mViewFlipper.setOutAnimation(this, android.R.anim.fade_out);
        CustomGestureDetector customGestureDetector = new CustomGestureDetector();
        mGestureDetector = new GestureDetector(this, customGestureDetector);
        mViewFlipper.setAutoStart(true);
        mViewFlipper.setFlipInterval(4000);


        recyclerView = (RecyclerView) findViewById(R.id.recycleview);
        //recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), R.drawable.divider));
        ScaleInAnimator animator = new ScaleInAnimator();
        animator.setAddDuration(1000);
        animator.setRemoveDuration(1000);
        recyclerView.setItemAnimator(animator);
        radpater = new Radpater(MainActivity.this,data);
        AlphaInAnimationAdapter alphaAdapter = new AlphaInAnimationAdapter(radpater);
        recyclerView.setAdapter(new SlideInRightAnimationAdapter(alphaAdapter));
        //recyclerView.setAdapter(radpater);
        LinearLayoutManager layoutManager= new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL, false);
        //recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        //recyclerView.setLayoutManager(layoutManager);
        recyclerView.setLayoutManager(new WrapContentLinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
       // recyclerView.setHasFixedSize(true);


        rv1 = (RecyclerView) findViewById(R.id.recycleview1);
        rv1.setAdapter(new SlideInRightAnimationAdapter(alphaAdapter));
        LinearLayoutManager layoutManager1= new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL, false);
       // rv1.setLayoutManager(layoutManager1);
        rv1.setLayoutManager(new WrapContentLinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
       /* radpater = new Radpater(MainActivity.this,data);
        recyclerView.setAdapter(radpater);
        rv1.setAdapter(radpater);*/
       /* if(mqueue.getCache().get(url).data.length !=0) {

        }*/
        //getBookData();
        try {
            ViewConfiguration config = ViewConfiguration.get(this);
            Field menuKeyField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
            if(menuKeyField != null) {
                menuKeyField.setAccessible(true);
                menuKeyField.setBoolean(config, false);
            }
        } catch (Exception ex) {
            // Ignore
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);



        if(status){
            newMenu();
            showKart(true);
        }


        t1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // Toast.makeText(getBaseContext(), "coming soon", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(MainActivity.this,CatgView.class);
                TextView tv3 = (TextView)findViewById(R.id.textView3);
                String s1= tv3.getText().toString();
                i.putExtra("cat", s1);
                i.putParcelableArrayListExtra("object",(ArrayList)data);
                Toast.makeText(getBaseContext(), s1, Toast.LENGTH_SHORT).show();
                startActivity(i);
                //finish();

            }
        });
        t2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // Toast.makeText(getBaseContext(), "coming soon", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(MainActivity.this,RGridView.class);
                TextView tv3 = (TextView)findViewById(R.id.textView5);
                String s1= tv3.getText().toString();
                i.putExtra("cat", s1);
                i.putParcelableArrayListExtra("object",(ArrayList)data);
                Toast.makeText(getBaseContext(), s1, Toast.LENGTH_SHORT).show();
                startActivity(i);
               // finish();
            }
        });
             getBookData();

    }

    @Override
    protected void onResume() {
        super.onResume();
        status = sharedPreferences.getBoolean("LStatus",false);
        SharedPreferences s1 = getSharedPreferences("update",Context.MODE_PRIVATE);
        SharedPreferences.Editor e1 = s1.edit();
        Log.e("ResumeStatus:", String.valueOf(status));
        if (mqueue!=null){
            if (mqueue.getCache()!=null){
                if(s1.getBoolean("update",false)) {
                    e1.clear();
                    e1.apply();
                    mqueue.getCache().invalidate(url, true);
                    startprogress();
                    getBookData();
                }
            }

        }
      /*  if(status){
            newMenu();
            showKart(true);
        }
        else {
            oldmenu();
            showKart(false);
        }*/
    }

    public void showKart(Boolean b){
        if(menu!=null){

            MenuItem login = menu.findItem(R.id.login);
            login.setVisible(!b);
           // kart.setVisible(b);
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        mqueue.getCache().invalidate(url,true);
    }

    public void newMenu(){
        navigationView.getMenu().clear();
        navigationView.inflateMenu(R.menu.loginmenu);

       SharedPreferences sharedPreferences2 = getSharedPreferences("UserDetail",Context.MODE_PRIVATE);
        SharedPreferences sharedPreferences3 = getSharedPreferences("Login", Context.MODE_PRIVATE);
        final String url = sharedPreferences3.getString("url","");
        user = sharedPreferences2.getString("username","User");
        View headerLayout = navigationView.getHeaderView(0);
        if(headerLayout!=null) {
            ((TextView) headerLayout.findViewById(R.id.headername1)).setText(user);
            if (!(url.equals(""))) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            final Bitmap bitmap = Glide.with(MainActivity.this).load(url).asBitmap().into(-1,-1).get();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    View headerLayout = navigationView.getHeaderView(0);
                                    if(headerLayout!=null)
                                        ((ImageView) headerLayout.findViewById(R.id.headerimage)).setImageBitmap(bitmap);
                                }
                            });

                        } catch (InterruptedException | ExecutionException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }

        }

    }

    public void oldmenu(){
        navigationView.getMenu().clear();
        navigationView.inflateMenu(R.menu.activity_main_drawer);

        View headerLayout = navigationView.getHeaderView(0);
        if(headerLayout!=null) {
            ((TextView) headerLayout.findViewById(R.id.headername1)).setText("Guest User");
            ((ImageView) headerLayout.findViewById(R.id.headerimage)).setImageResource(R.drawable.circularuser);
        }
    }

    public long ServerResponce(){
        return mqueue.getCache().get(url).serverDate;
    }



    @Override
    public void onNewIntent(Intent intent) {
        status = sharedPreferences.getBoolean("LStatus", false);
        Log.e("Status:", String.valueOf(status));
        if (status) {
            b=intent.getExtras();
            if (b != null) {
                //String sbu1 = "";
                String nm;
                if (b.containsKey("name")) {
                    nm = b.getString("name");

                    Toast.makeText(getApplicationContext(), nm, Toast.LENGTH_SHORT).show();

                    newMenu();
                    showKart(true);
                }
                else if (b.containsKey("refresh")){
                 //   mqueue.getCache().invalidate(url,true);
                  //  getBookData();
                }
                }


            } else {
                oldmenu();
                showKart(false);
            }
       // mqueue.getCache().invalidate(url,true);

    }

    public void startprogress(){
        pdialog = new ProgressDialog(MainActivity.this);
        pdialog.setMessage("Loading");
        pdialog.setCancelable(false);
        pdialog.setIndeterminate(false);
        pdialog.show();
    }

    public void stopprogress(){
        if (pdialog!=null && pdialog.isShowing()){
            pdialog.dismiss();
        }
    }

   /* public void showprogress(){
        findViewById(R.id.avloadingIndicatorView).setVisibility(View.VISIBLE);
    }

    public void hideprogress(){
        findViewById(R.id.avloadingIndicatorView).setVisibility(View.GONE);
    }*/

    public void getBookData(){

       // startprogress();
       // showprogress();

       data.clear();
        JsonArrayRequest mreq = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
               stopprogress();
              //  hideprogress();
                          for(int i=0;i<response.length();i++){
                              JSONObject jo;
                              try {
                                  jo = response.getJSONObject(i);
                                  listinfo current = new listinfo();
                                  current.title = jo.getString("title");
                                  current.url = jo.getString("imgUrl");
                                  current.originalprice = jo.getInt("originalprice");
                                  current.yourprice = jo.getInt("yourprice");
                                  current.seller = jo.getString("Username");
                                  data.add(current);

                              } catch (JSONException e) {
                                  e.printStackTrace();
                              }
                          }
               // radpater.notifyItemRangeInserted(0,data.size());
                Radpater rt = new Radpater(MainActivity.this,data);

                recyclerView.swapAdapter(rt,false);

                rv1.swapAdapter(rt,false);
                Log.e("bookResp","true");



            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                stopprogress();
               // hideprogress();
               Toast.makeText(getApplicationContext(),"Error Occured",Toast.LENGTH_SHORT).show();

            }
        });
        mqueue.add(mreq);
    }

    public static long getMinutesDifference(long timeStart,long timeStop){
        long diff = timeStop - timeStart;
        long diffMinutes = diff / (60 * 1000);

        return  diffMinutes;
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }



    class NavMenuClass{
        Menu menu;
        ArrayList items;

        public NavMenuClass(Menu menu,ArrayList items){

            this.items = items;
            this.menu = menu;

        }

        public Menu getMenu(){
            return menu;
        }

        public ArrayList getItems(){
            return items;
        }

    }

    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        int id = menuItem.getItemId();

      SharedPreferences sp = getSharedPreferences("UserDetail",Context.MODE_PRIVATE);
        SharedPreferences.Editor se;
          if (id == R.id.sell_book){
            Intent i = new Intent(MainActivity.this,sellbook.class);
            startActivity(i);
        }

        else if (id == R.id.nearby){
            Intent i = new Intent(MainActivity.this,gps.class);
            startActivity(i);
        }
        else if (id == R.id.lo){
            Boolean gl = sharedPreferences.getBoolean("g+",false);
              SharedPreferences sharedPreferences3 = getSharedPreferences("gcmDetails", Context.MODE_PRIVATE);
              SharedPreferences.Editor editor3 = sharedPreferences3.edit();
              SharedPreferences gcmPref  = getSharedPreferences("gcmDetails", Context.MODE_PRIVATE);
              SharedPreferences.Editor gcmedit = gcmPref.edit();
              SharedPreferences profile = getSharedPreferences("offlineprofile",Context.MODE_PRIVATE);
              SharedPreferences.Editor pf = profile.edit();
            if(status){
                //new login().signOut();
                editor = sharedPreferences.edit();
                se = sp.edit();
                se.putString("username","");
                se.apply();
                editor.putBoolean("gout",true);
                editor.putBoolean("LStatus",false);
                editor.putString("url","");
                gcmedit.clear();
                gcmedit.apply();
                pf.clear();
                pf.apply();
                editor.apply();
                editor.putString("Regid","");
                oldmenu();
                showKart(false);
                Toast.makeText(getApplicationContext(),"LogOut SuccessFully",Toast.LENGTH_SHORT).show();
            }

        }
        else if (id == R.id.myProfile){
            Intent i = new Intent(MainActivity.this,MyProfile.class);
              i.putExtra("username",user);
            startActivity(i);
        }

        else if (id == R.id.EditProfile){
              Intent i = new Intent(MainActivity.this,UpdateBooks.class);
              startActivity(i);
          }

        else if (id == R.id.navlogin){
              status = sharedPreferences.getBoolean("LStatus",false);
              if(status){
                  Toast.makeText(getBaseContext(),"Already Logged in",Toast.LENGTH_SHORT).show();
              }
              else {
                  editor = sharedPreferences.edit();
                  Log.e("mainG+",String.valueOf(sharedPreferences.getBoolean("g+",false)));
                  if(sharedPreferences.getBoolean("g+",false)){
                      // editor.putBoolean("gout", true);
                      editor.putBoolean("g+",false);
                  }
                  else {
                      //editor.putBoolean("gout",false);

                  }
                  //Log.e("loging+",String.valueOf(sharedPreferences.getBoolean("g+",false)));

                  editor.apply();
                  Intent i = new Intent(MainActivity.this, login.class);
                  startActivity(i);
              }
          }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return false;
    }

    class CustomGestureDetector extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

            // Swipe left (next)
            if (e1.getX() > e2.getX()) {
                mViewFlipper.setInAnimation(MainActivity.this, R.anim.left_in);
                mViewFlipper.setOutAnimation(MainActivity.this, R.anim.left_out);


                mViewFlipper.showNext();
            }

            // Swipe right (previous)
            if (e1.getX() < e2.getX()) {
                mViewFlipper.setInAnimation(MainActivity.this, R.anim.right_in);
                mViewFlipper.setOutAnimation(MainActivity.this, R.anim.right_out);
                mViewFlipper.showPrevious();
            }
            Log.i("motion1", String.valueOf(e1.getX()));
            Log.i("motion2",String.valueOf(e2.getX()));

            return super.onFling(e1, e2, velocityX, velocityY);
        }
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mGestureDetector.onTouchEvent(event);

        return super.onTouchEvent(event);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        this.menu = menu;
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        Intent i;

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        switch (id){
            case R.id.action_search:
                //Toast.makeText(getBaseContext(),"coming soon",Toast.LENGTH_SHORT).show();
                i = new Intent(MainActivity.this,SearchActivity.class);
                i.putParcelableArrayListExtra("object",(ArrayList)data);
                startActivity(i);
                return true;

            case R.id.login:
                //Toast.makeText(getBaseContext(),"coming soon feature",Toast.LENGTH_SHORT).show();
                status = sharedPreferences.getBoolean("LStatus",false);
                if(status){
                    Toast.makeText(getBaseContext(),"Already Logged in",Toast.LENGTH_SHORT).show();
                }
                else {
                    editor = sharedPreferences.edit();
                    Log.e("mainG+",String.valueOf(sharedPreferences.getBoolean("g+",false)));
                    if(sharedPreferences.getBoolean("g+",false)){
                       // editor.putBoolean("gout", true);
                        editor.putBoolean("g+",false);
                    }
                    else {
                        //editor.putBoolean("gout",false);

                    }
                    //Log.e("loging+",String.valueOf(sharedPreferences.getBoolean("g+",false)));

                    editor.apply();
                    i = new Intent(MainActivity.this, login.class);
                    startActivity(i);
                }
                //finish();
                return true;




        }

        return super.onOptionsItemSelected(item);
    }

    class WrapContentLinearLayoutManager extends LinearLayoutManager{

        public WrapContentLinearLayoutManager(Context context, int orientation, boolean reverseLayout) {
            super(context, orientation, reverseLayout);
        }

        @Override
        public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
            try {
                super.onLayoutChildren(recycler, state);
            }catch (IndexOutOfBoundsException e){
                Log.e("erLL",e.getMessage());
            }


        }
    }


}
