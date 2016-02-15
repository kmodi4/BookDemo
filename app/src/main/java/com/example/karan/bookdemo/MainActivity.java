package com.example.karan.bookdemo;

import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.NavigationView;
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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import jp.wasabeef.recyclerview.animators.ScaleInAnimator;

public class MainActivity extends AppCompatActivity implements  NavigationView.OnNavigationItemSelectedListener {

    private ViewFlipper mViewFlipper;
    private GestureDetector mGestureDetector;

    int[] resources = {
            R.drawable.img1, R.drawable.img2,
            R.drawable.img3, R.drawable.img4,
            R.drawable.img5, R.drawable.img6
    };


    RecyclerView recyclerView,rv1;
    Radpater radpater;
    TextView t1,t2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navg_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);

        }
        t1 = (TextView) findViewById(R.id.textView4);
        t2 = (TextView) findViewById(R.id.textView6);

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
        recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), R.drawable.divider));
        ScaleInAnimator animator = new ScaleInAnimator();
        animator.setAddDuration(1000);
        animator.setRemoveDuration(1000);
        recyclerView.setItemAnimator(animator);
        radpater = new Radpater(getApplicationContext(),getData());
        recyclerView.setAdapter(radpater);
        LinearLayoutManager layoutManager= new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL, false);
        //recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setLayoutManager(layoutManager);

        rv1 = (RecyclerView) findViewById(R.id.recycleview1);
        rv1.setAdapter(radpater);
        LinearLayoutManager layoutManager1= new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL, false);
        rv1.setLayoutManager(layoutManager1);

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

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        t1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // Toast.makeText(getBaseContext(), "coming soon", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(MainActivity.this,CatgView.class);
                TextView tv3 = (TextView)findViewById(R.id.textView3);
                String s1= tv3.getText().toString();
                i.putExtra("cat",s1);
                Toast.makeText(getBaseContext(), s1, Toast.LENGTH_SHORT).show();
                startActivity(i);

            }
        });
        t2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // Toast.makeText(getBaseContext(), "coming soon", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(MainActivity.this,RGridView.class);
                TextView tv3 = (TextView)findViewById(R.id.textView5);
                String s1= tv3.getText().toString();
                i.putExtra("cat",s1);
                Toast.makeText(getBaseContext(), s1, Toast.LENGTH_SHORT).show();
                startActivity(i);
            }
        });

    }

    public static List<listinfo> getData(){
        List<listinfo> data = new ArrayList<>();
        int[] icon = {R.drawable.b1,
                R.drawable.b2, R.drawable.b3, R.drawable.b4, R.drawable.b5,
                R.drawable.img7,R.drawable.img8,R.drawable.img9,
                R.drawable.img10,};
        String[] title = {"Book 1","Book 2","Book 3","Book 4","Book 5",
                "Book 6","Book 7","Book 8","Book 9"};



        for(int i=0;i<9;i++){
            listinfo current = new listinfo();
            current.icon = icon[i];
            current.title = title[i];
            data.add(current);
        }
        return  data;

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

    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        int id = menuItem.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) { Toast.makeText(getBaseContext(),"coming soon",Toast.LENGTH_SHORT).show();

        } else if (id == R.id.nav_slideshow) { Toast.makeText(getBaseContext(),"coming soon",Toast.LENGTH_SHORT).show();

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

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
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

        switch (id){
            case R.id.action_search:
                Toast.makeText(getBaseContext(),"coming soon",Toast.LENGTH_SHORT).show();
                return true;

            case R.id.login:
                //Toast.makeText(getBaseContext(),"coming soon feature",Toast.LENGTH_SHORT).show();
                Intent i = new Intent(MainActivity.this,login.class);
                startActivity(i);
                //finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
