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
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.Collections;
import java.util.List;

public class RVadapter extends RecyclerView.Adapter<RVadapter.Myviewholder> {
    private LayoutInflater inflater;
    Context context;
     private OnCallback onCallback;
    int previousposition = 0;
    List<listinfo> data = Collections.emptyList();

    public RVadapter(Context c,List<listinfo> data){
        inflater = LayoutInflater.from(c);
        context = c;
        this.data = data;
        //onCallback = (OnCallback) c;
    }



    @Override
    public Myviewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.custom_row, parent, false);
        Myviewholder myviewholder = new Myviewholder(view);
        return myviewholder;
    }

    @Override
    public void onBindViewHolder(Myviewholder holder, final int position) {

        final listinfo current = data.get(position);
        holder.title.setText(current.title);
        holder.yp.setText("Selling Price: "+String.valueOf(current.yourprice));
        holder.op.setText("MRP : "+String.valueOf(current.originalprice));
        Log.i("title", current.title);
        //holder.icon.setImageResource(current.icon);
        Glide.with(context)
                .load(current.url)
                .crossFade()
                .placeholder(R.drawable.holder)
                .into(holder.icon);



        holder.myview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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


       /* if(position > previousposition){
            AnimationUtils.(holder,true);
        }
        else{
            AnimationUtils.animate(holder,false);
        }
        previousposition=position;*/

       // AnimationUtils.scaleY(holder);


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

    class Myviewholder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView title,yp,op;
        ImageView icon;
        CardView cv;
        View myview;

        public Myviewholder(View itemView) {
            super(itemView);
            myview = itemView;
            cv = (CardView)itemView.findViewById(R.id.cv);
            title = (TextView) itemView.findViewById(R.id.CustTxt);
            icon = (ImageView) itemView.findViewById(R.id.custImg);
            yp = (TextView) itemView.findViewById(R.id.Cust_your_price);
            op = (TextView) itemView.findViewById(R.id.Cust_original_price);
           // config = (ImageView) itemView.findViewById(R.id.Cust_delete);
           // itemView.setOnClickListener(this);


        }

        @Override
        public void onClick(View view) {
            //Toast.makeText(view.getContext(), "position = " + getPosition(), Toast.LENGTH_SHORT).show();
            //onCallback.onSelectBook(getPosition());
        }
    }

    public interface OnCallback{
        public void onSelectBook(int index);
    }
}