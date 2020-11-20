package com.example.memoria.adapters;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.memoria.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SpinnerAdapter extends RecyclerView.Adapter< SpinnerAdapter.ViewHolder > {
    private List<String> names;
    private int layout;
    private Context mContext;
    private  String[] bankname;
    static ViewHolder INSTANCE;
    public static int[] positions;

    public SpinnerAdapter(Context mContext, List<String> names, int layout, String[] bankname){
        this.names = names;
        this.layout = layout;
        this.mContext = mContext;
        this.bankname = bankname;
        this.positions = new int[names.size()];//new ArrayList<Integer>(Collections.nCopies(names.size(),0));

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(mContext, bankname, names);

    }

    public int[] getPositions() {
        return positions;
    }

    @Override
    public int getItemCount() {
        return names.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public Spinner spinnerList;
        public TextView instructions;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.spinnerList = (Spinner) itemView.findViewById(R.id.spinnerServices);
            this.instructions = (TextView) itemView.findViewById(R.id.textViewServices);
            spinnerList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                int previous = -1;
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if(previous != position ) {//&& previous!=-1
                     //   Log.v("Example", "Item Selected: " + parent.getItemAtPosition(position).toString());
                        // Do something
                        int i = spinnerList.getSelectedItemPosition();
                        //positions.set(getAdapterPosition(),i);
                        positions[getAdapterPosition()]=i;
                        //positions.add(i);
                    }
                    previous = position;
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });


        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        public void bind(Context mcontext, String[] bankname, List<String> names ){
            instructions.setText("Elija el servicio que desea usar "+ names.get(getAdapterPosition()));
            ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(mcontext, android.R.layout.simple_spinner_item, bankname);
            myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerList.setAdapter(myAdapter);


        }

    }




}
