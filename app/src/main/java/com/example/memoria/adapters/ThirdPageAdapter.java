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
import java.util.ArrayList;
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
        holder.bind(names, thMax, services, positions, position);
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

        public void bind( List<String> names, List<Double> thMax,  List<services> services, int[] position, int spinner){
            this.textViewName.setText(names.get(spinner));
            this.serviceName.setText(services.get(position[spinner]).getName());
            this.serviceIcon.setImageResource(services.get(position[spinner]).getIcon());
            this.verificationIcon.setImageResource(R.drawable.waiting);
            int throughputRelation = state(thMax,services,position);
            if (throughputRelation == 0){
                verificationIcon.setImageResource(R.drawable.check);
            }else if (throughputRelation ==1){
                verificationIcon.setImageResource(R.drawable.x);
            }

        }
    }

    public interface OnItemClickListener{
        void onItemClick(String name, double position);
    }


    private static  int state( List<Double> thMax,  List<services> services, int[] position){
        int ok = 0;
        List<Double> listThNec = new ArrayList<Double>();
        for (int i=0; i<thMax.size(); i++ ){
            double thNec = services.get(position[i]).throughput;
            listThNec.add(thNec);
        }
        double finalPercentage = 0;
        for (int i=0; i<thMax.size(); i++ ){
            double percentage = (listThNec.get(i)/thMax.get(i))*100.0;
            finalPercentage = finalPercentage + percentage;
        }
        if (finalPercentage > 100.0){
            ok = 1;
        }
        return ok;
    }

}
