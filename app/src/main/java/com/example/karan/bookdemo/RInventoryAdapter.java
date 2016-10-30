package com.example.karan.bookdemo;


import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
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

public class RInventoryAdapter extends RecyclerView.Adapter<RInventoryAdapter.Myviewholder> {

    private LayoutInflater inflater;
    Context context;
    private OnCallback onCallback;
    int previousposition = 0;
    List<listinfo> data = Collections.emptyList();

    public RInventoryAdapter(Context context,List<listinfo> data) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data = data;
    }

    @Override
    public Myviewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.cust_inventory_row, parent, false);
        Myviewholder myviewholder = new Myviewholder(view);
        return myviewholder;
    }

    @Override
    public void onBindViewHolder(Myviewholder holder, int position) {
        final listinfo current = data.get(position);
        holder.title.setText(current.title);
        holder.yp.setText("Selling Price: "+String.valueOf(current.yourprice));
        holder.op.setText("MRP : "+String.valueOf(current.originalprice));
        Log.i("title", current.title);
       // Toast.makeText(context,"OnBind",Toast.LENGTH_SHORT).show();
        //holder.icon.setImageResource(current.icon);
        Glide.with(context)
                .load(current.url)
                .crossFade()
                .into(holder.icon);





        holder.config.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* Intent i = new Intent(context,EditInventory.class);
                i.putExtra("image",current.url);
                i.putExtra("title",current.title);
                i.putExtra("yourprice",current.yourprice);
                i.putExtra("originalprice",current.originalprice);
                i.putExtra("seller",current.seller);
                context.startActivity(i);*/

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

    class Myviewholder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView title,yp,op;
        ImageView icon,config;
        CardView cv;
        View myview;

        public Myviewholder(View itemView) {
            super(itemView);
            myview = itemView;
            cv = (CardView)itemView.findViewById(R.id.cv1);
            title = (TextView) itemView.findViewById(R.id.my_title);
            icon = (ImageView) itemView.findViewById(R.id.my_Img);
            yp = (TextView) itemView.findViewById(R.id.my_your_price);
            op = (TextView) itemView.findViewById(R.id.my_original_pricee);
            config = (ImageView) itemView.findViewById(R.id.my_delete);
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
