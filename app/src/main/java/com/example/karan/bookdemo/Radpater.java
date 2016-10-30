package com.example.karan.bookdemo;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

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
        View view=inflater.inflate(R.layout.horizn_row, parent, false);
        Myviewholder myviewholder = new Myviewholder(view);
        return myviewholder;
    }

    @Override
    public void onBindViewHolder(Myviewholder holder, final int position) {

        final listinfo current = data.get(position);
        holder.title.setText(current.title);
       // holder.title.append("\nPrice: "+current.yourprice);
        Log.i("title", current.title);
       // holder.icon.setImageResource(current.icon);
        Glide.with(context)
                .load(current.url)
                .crossFade()
                .placeholder(R.drawable.holder)
                .into(holder.img);
        holder.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(context, "Clicked at" + (position + 1), Toast.LENGTH_SHORT).show();
               // onDelete(position);
                Intent i = new Intent(context,Product_detail.class);
               // i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.putExtra("image",current.url);
                i.putExtra("title",current.title);
                i.putExtra("yourprice",current.yourprice);
                i.putExtra("originalprice",current.originalprice);
                i.putExtra("seller",current.seller);
                context.startActivity(i);
            }
        });

       /*if(position > previousposition){
            AnimationUtils.animate(holder,true);
        }
        else{
            AnimationUtils.animate(holder,false);
        }
        previousposition=position;*/


    }

    public void onDelete(int Position){
        if(Position != -1) {
            data.remove(Position);
            notifyItemRemoved(Position);
        }
    }

    public void Swapdata(List<listinfo> l1){
        if (data!=null) {
            data.clear();
            data.addAll(l1);

        }
        else
            data = l1;
        notifyDataSetChanged();
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
        ImageView img;
        CardView cv;

        public Myviewholder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv);
            title = (TextView) itemView.findViewById(R.id.textView2);
            img = (ImageView) itemView.findViewById(R.id.imageView3);


        }
    }
}
