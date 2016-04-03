package com.example.karan.bookdemo;

import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.recyclerview.animators.ScaleInAnimator;
import jp.wasabeef.recyclerview.animators.ScaleInBottomAnimator;
import jp.wasabeef.recyclerview.animators.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.animators.adapters.ScaleInAnimationAdapter;

public class CatgView extends AppCompatActivity {

    RecyclerView recyclerView;
    RVadapter RVadapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catg_view);

        Toolbar toolbar = (Toolbar) findViewById(R.id.cattoolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        recyclerView = (RecyclerView) findViewById(R.id.catrecycle);
        recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), R.drawable.divider));
        ScaleInAnimator animator = new ScaleInAnimator();
        animator.setAddDuration(1000);
        animator.setRemoveDuration(1000);
        recyclerView.setItemAnimator(animator);
        RVadapter = new RVadapter(CatgView.this,getData());
        AlphaInAnimationAdapter alphaAdapter = new AlphaInAnimationAdapter(RVadapter);
        recyclerView.setAdapter(new ScaleInAnimationAdapter(alphaAdapter));
       // recyclerView.setAdapter(RVadapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setHasFixedSize(true);
    }

    public static List<listinfo> getData(){
        List<listinfo> data = new ArrayList<>();
        int[] icon = {R.drawable.b1,
                R.drawable.b2, R.drawable.b3, R.drawable.b4, R.drawable.b5,
                R.drawable.b6,R.drawable.b7,R.drawable.b8,
                R.drawable.b9,};
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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_catg_view, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);


                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
