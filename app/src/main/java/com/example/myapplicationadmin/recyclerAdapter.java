package com.example.myapplicationadmin;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class recyclerAdapter extends RecyclerView.Adapter<recyclerAdapter.viewHolder>
{

    ArrayList<pojo> items;

     OnEntryClickListener mOnEntryClickListener;

    static class viewHolder extends RecyclerView.ViewHolder  {


        public ImageView imRec;
        public TextView tvRec;

        public viewHolder(@NonNull View itemView) {
            super(itemView);

            imRec = itemView.findViewById(R.id.imageRecycler);
            tvRec = itemView.findViewById(R.id.tvRecycler);

        }


    }


    public interface OnEntryClickListener {
        void onEntryClick(View view, int position);
    }

//    public void setOnEntryClickListener(OnEntryClickListener onEntryClickListener) {
//        mOnEntryClickListener = onEntryClickListener;
//    }


    public recyclerAdapter(ArrayList<pojo> items,OnEntryClickListener listener) {
        this.items = items;
        this.mOnEntryClickListener=listener;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler,parent,false);
        viewHolder vh = new viewHolder(v);

        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, final int position) {


        holder.imRec.setImageBitmap(convert(items.get(position).getmImage()));
        holder.tvRec.setText(items.get(position).getmName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("onclick","onlcik");
                    mOnEntryClickListener.onEntryClick(v, position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    public static Bitmap convert(String base64Str) throws IllegalArgumentException
    {
        byte[] decodedBytes = Base64.decode(
                base64Str.substring(base64Str.indexOf(",")  + 1),
                Base64.DEFAULT
        );

        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }


}
