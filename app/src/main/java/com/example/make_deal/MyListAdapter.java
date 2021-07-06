package com.example.make_deal;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class MyListAdapter extends RecyclerView.Adapter<MyListAdapter.ViewHolder> {
    private ArrayList<MyListData> listdata;


    int a=0;

    // RecyclerView recyclerView;
    public MyListAdapter(ArrayList<MyListData> listdata) {
        this.listdata = listdata;
        a=listdata.size();

    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.list_item, parent, false);


        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {


        final MyListData myListData = listdata.get(position);
        holder.textView.setText(listdata.get(position).getDescription());
        holder.price.setText(listdata.get(position).getPrice());
        holder.time.setText(listdata.get(position).getTime()+" "+listdata.get(position).getDuration_type());
     //   holder.time_type.setText(listdata.get(position).getDuration_type());
        //   holder.imageView.setImageResource(listdata[position].getImgId());
        Picasso.with(holder.imageView.getContext()).load(listdata.get(position).getImgId()).resize(180, 180).into(holder.imageView);

//        picasso.load("https://firebasestorage.googleapis.com/v0/b/freelancer-android.appspot.com/o/images%2Fe2c00363-e7dd-49d3-87cd-f3d3f93b2124?alt=media&token=979b7089-32c6-4d6d-82b5-82c041194b93").resize(20, 20).into(holder.imageView);
        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(),"click on item: "+myListData.getImgId(),Toast.LENGTH_LONG).show();
    //            Intent intent=new Intent(v.getContext(),home.class);
      //          startActivity(intent);
                Context activity=holder.itemView.getContext();
//                Intent intent= null;
//                try {
//                    intent = new Intent(activity, Class.forName(myListData.getDestination().toString()));
//                } catch (ClassNotFoundException e) {
//                    e.printStackTrace();
//                }
                Intent intent=null;
                if(myListData.getDestination().equals("view_my_product")) {
                    intent=new Intent(activity,view_my_product.class);
                }
                else if(myListData.getDestination().equals("viewProduct_cart")) {
                    intent=new Intent(activity,viewProduct_cart.class);
                }
                else if(myListData.getDestination().equals("view_product")) {
                    intent=new Intent(activity,view_product.class);
                }
                else if(myListData.getDestination().equals("viewproduct_admin")) {
                    intent=new Intent(activity,viewproduct_admin.class);
                }

                intent.putExtra("url", myListData.getImgId());

                activity.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {

        return listdata.size();

    }



    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public TextView textView,time,price,time_type;
        public RelativeLayout relativeLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            this.imageView = (ImageView) itemView.findViewById(R.id.imageView);
            this.textView = (TextView) itemView.findViewById(R.id.textView);
            this.time = (TextView) itemView.findViewById(R.id.time);
            this.price = (TextView) itemView.findViewById(R.id.price);
            this.time_type=(TextView) itemView.findViewById(R.id.time_type);

            relativeLayout = (RelativeLayout)itemView.findViewById(R.id.relativeLayout);
        }
    }
}
