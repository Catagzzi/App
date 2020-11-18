package com.example.memoria.adapters;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.memoria.R;

import java.util.List;

public class SpinnerAdapter extends RecyclerView.Adapter< SpinnerAdapter.ViewHolder > {
    private List<String> names;
    private int layout;
    private Context mContext;
    private  String[] bankname;

    public SpinnerAdapter(Context mContext, List<String> names, int layout, String[] bankname){
        this.names = names;
        this.layout = layout;
        this.mContext = mContext;
        this.bankname = bankname;
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
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        public void bind(Context mcontext, String[] bankname, List<String> names){
            instructions.setText("Elija el servicio que desea usar "+ names.get(getAdapterPosition()));
            ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(mcontext, android.R.layout.simple_spinner_item, bankname);
            myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerList.setAdapter(myAdapter);
            spinnerList.setAutofillHints("Hola");
        }
    }
}
