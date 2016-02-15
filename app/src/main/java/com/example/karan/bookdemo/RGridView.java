package com.example.karan.bookdemo;

import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.recyclerview.animators.ScaleInAnimator;

public class RGridView extends AppCompatActivity {

    RecyclerView recyclerView;
    Radpater adpater;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rgrid_view);

        Toolbar toolbar = (Toolbar) findViewById(R.id.gridtoolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        recyclerView = (RecyclerView) findViewById(R.id.gridrecycle);
        recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), R.drawable.divider));
        ScaleInAnimator animator = new ScaleInAnimator();
        animator.setAddDuration(1000);
        animator.setRemoveDuration(1000);
        recyclerView.setItemAnimator(animator);
        adpater = new Radpater(getApplicationContext(),getData());
        recyclerView.setAdapter(adpater);
        //recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_rgrid_view, menu);
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
