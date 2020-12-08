package com.example.memoria.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.memoria.R;
import com.example.memoria.activities.UsersMeasureActivity;
import com.example.memoria.activities.services;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

public class ThirdPageAdapter extends RecyclerView.Adapter<ThirdPageAdapter.ViewHolder> {

    private List<String> names;
    private int layout;
    private OnItemClickListener itemClickListener;
    private List<Double> thMax;
    private  List<services> services;
    private int[] positions;

    public ThirdPageAdapter(List<String> names, int layout, List<Double> thMax,  List<services> services, int[] positions) {
        this.names = names;
        this.layout = layout;
        this.thMax = thMax;
        this.services = services;
        this.positions = positions;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(names.get(position), thMax.get(position), services, positions[position]);
    }

    @Override
    public int getItemCount() {
        return names.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewName;
        public TextView serviceName;
        public ImageView serviceIcon;
        public ImageView verificationIcon;


        public ViewHolder(View itemView) {
            super(itemView);
            this.textViewName = (TextView) itemView.findViewById(R.id.textViewName);
            this.serviceIcon = (ImageView) itemView.findViewById(R.id.serviceIcon);
            this.serviceName = (TextView) itemView.findViewById(R.id.serviceName);
            this.verificationIcon = (ImageView) itemView.findViewById(R.id.verificationIcon);


        }

        public void bind(final String name, final double thMax,  List<services> services, int position){
            this.textViewName.setText(name);
            this.serviceName.setText(services.get(position).getName());
            this.serviceIcon.setImageResource(services.get(position).getIcon());
            this.verificationIcon.setImageResource(R.drawable.waiting);
            int throughputRelation = confirmation(thMax, services.get(position).throughput);
            if (throughputRelation == 0){
                verificationIcon.setImageResource(R.drawable.check);
            }else if (throughputRelation ==1){
                verificationIcon.setImageResource(R.drawable.x);
            }

        }
    }

    public interface OnItemClickListener{
        void onItemClick(String name, int position);
    }

    private static  int confirmation(double thMax, int thNecessary){
        if(thMax > thNecessary ){
            return 0;
        } else if (thMax < thNecessary){
            return 1;
        } else{
            return 2;
        }
    }

}
