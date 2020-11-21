package com.example.memoria.adapters;

import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.memoria.R;
import com.example.memoria.activities.MainActivity;
import com.example.memoria.activities.UsersMeasureActivity;
import com.example.memoria.models.Phone;
import com.example.memoria.models.WiFi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;

public class SecondPageAdapter extends RecyclerView.Adapter<SecondPageAdapter.ViewHolder> {
    private List<String> names;
    private String[] escrito;
    private OnItemClickListener itemClickListener;
    private Realm realm;
    public Context context;

    public SecondPageAdapter(List<String> names, OnItemClickListener listener) { //, OnItemClickListener listener
        this.names = names;
        this.escrito = new String[names.size()];
        this.itemClickListener = listener;
        this.context = context;
    }

    @NonNull
    @Override
    public SecondPageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_page2_analysis1, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull SecondPageAdapter.ViewHolder holder, int position) {
        holder.bind(names.get(position), itemClickListener);

    }

    @Override
    public int getItemCount() {
        return names.size();
    }

    public String[] getEscrito() {
        return escrito;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewUsers;
        public EditText editTextUserName;
        public TextView textViewInstruction;
        public Button buttonMeasure;
        public ImageView loading;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.textViewUsers = (TextView) itemView.findViewById(R.id.textViewUsers);
            this.editTextUserName = (EditText) itemView.findViewById(R.id.analysis1EditPersonName);
            this.textViewInstruction = (TextView) itemView.findViewById(R.id.textViewInstructionMeasure);
            this.buttonMeasure = (Button) itemView.findViewById(R.id.buttonMeasure);
            this.loading = (ImageView) itemView.findViewById(R.id.imageWaiting);
            editTextUserName.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    escrito[getAdapterPosition()] = s.toString();
                }
                @Override
                public void afterTextChanged(Editable s) {
                }
            });
        }

        public void bind(final String UserNumber, final OnItemClickListener listener){
            this.textViewUsers.setText(UserNumber);
            this.textViewInstruction.setText(" Para medir debe ponerse en la ubicación donde estará este usuario conectado y oprimir el botón 'medir'");

            //Botón para medir datos y guardarlos en DB

            buttonMeasure.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(editTextUserName.getText().toString(), 59, loading, buttonMeasure, getAdapterPosition());
                }
            });
        }
    }


    //Function for click in button medir
    public interface OnItemClickListener{
        void onItemClick(String name, int throughputMax, ImageView loading, Button medir, int position); //String name, int position
    }
}
