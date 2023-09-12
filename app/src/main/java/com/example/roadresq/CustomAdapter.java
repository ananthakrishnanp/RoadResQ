package com.example.roadresq;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.myViewHolder> {

    Context context;
    ArrayList<Complaint> arrayList;

    private onItemClickListener onItemClickListener;

    public CustomAdapter(Context context, ArrayList<Complaint> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    public  interface onItemClickListener{
        public void onItemClick(int position);
    }

    public void setOnItemClickListener(CustomAdapter.onItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(context);
        View itemView=inflater.inflate(R.layout.item_list,parent,false);
        return new myViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {



        // Set complaint information in the ViewHolder
        holder.txtSub.setText(arrayList.get(holder.getAdapterPosition()).getSubject());
        holder.txtDate.setText(arrayList.get(holder.getAdapterPosition()).getDate());
        holder.txtTime.setText(arrayList.get(holder.getAdapterPosition()).getTime());
        holder.txtStatus.setText(arrayList.get(holder.getAdapterPosition()).getStatus());

        if (holder.txtStatus.getText().toString().equalsIgnoreCase("Under Review")) {
            Drawable iconDrawable = ResourcesCompat.getDrawable(context.getResources(), R.drawable.baseline_playlist_add_check_24, null);
            DrawableCompat.setTint(iconDrawable, Color.WHITE);
            holder.imageView.setImageDrawable(null); // Clear old image
            holder.imageView.setImageDrawable(iconDrawable); // Set new image
        } else if (holder.txtStatus.getText().toString().equalsIgnoreCase("Resolved")) {
            Drawable iconDrawable = ResourcesCompat.getDrawable(context.getResources(), R.drawable.round_check_circle_24, null);
            DrawableCompat.setTint(iconDrawable, Color.parseColor("#82DE7C"));
            holder.imageView.setImageDrawable(null); // Clear old image
            holder.imageView.setImageDrawable(iconDrawable); // Set new image
        }


        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onItemClickListener!=null)
                {
                    onItemClickListener.onItemClick(holder.getAdapterPosition());
                }
            }
        });


    }

    @Override
    public int getItemCount() {

        return arrayList.size();
    }

    public class myViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView txtSub,txtDate,txtTime,txtStatus;

        CardView cardView;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            txtSub=itemView.findViewById(R.id.listsub);
            txtDate=itemView.findViewById(R.id.listdate);
            txtTime=itemView.findViewById(R.id.listtime);
            txtStatus=itemView.findViewById(R.id.status);
            imageView=itemView.findViewById(R.id.statusimg);
            cardView=itemView.findViewById(R.id.cardview);

        }
    }
}
