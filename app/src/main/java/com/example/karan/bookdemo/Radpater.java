package com.example.karan.bookdemo;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Collections;
import java.util.List;


public class Radpater extends RecyclerView.Adapter<Radpater.Myviewholder> {

    private LayoutInflater inflater;
    Context context;

    int previousposition = 0;
    List<listinfo> data = Collections.emptyList();

    public Radpater(Context c,List<listinfo> data){
        inflater = LayoutInflater.from(c);
        context = c;
        this.data = data;
    }

    @Override
    public Myviewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.custom_row, parent, false);
        Myviewholder myviewholder = new Myviewholder(view);
        return myviewholder;
    }

    @Override
    public void onBindViewHolder(Myviewholder holder, final int position) {

        listinfo current = data.get(position);
        holder.title.setText(current.title);
        Log.i("title",current.title);
        holder.icon.setImageResource(current.icon);
        holder.icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "Clicked at" + (position + 1), Toast.LENGTH_SHORT).show();
                onDelete(position);
            }
        });

        if(position > previousposition){
            AnimationUtils.animate(holder,true);
        }
        else{
            AnimationUtils.animate(holder,false);
        }
        previousposition=position;


    }

    public void onDelete(int Position){
        if(Position != -1) {
            data.remove(Position);
            notifyItemRemoved(Position);
        }
    }



    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    class Myviewholder extends RecyclerView.ViewHolder{

        TextView title;
        ImageView icon;
        CardView cv;

        public Myviewholder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv);
            title = (TextView) itemView.findViewById(R.id.textView2);
            icon = (ImageView) itemView.findViewById(R.id.imageView3);


        }
    }
}
