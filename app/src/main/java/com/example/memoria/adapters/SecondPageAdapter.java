package com.example.memoria.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.memoria.R;

import java.util.List;

public class SecondPageAdapter extends RecyclerView.Adapter<SecondPageAdapter.ViewHolder> {

    private List<String> names;
    private int layout;
    private OnItemClickListener itemClickListener;

    public SecondPageAdapter(List<String> names, int layout, OnItemClickListener listener) {
        this.names = names;
        this.layout = layout;
        this.itemClickListener = listener;
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
        holder.bind(names.get(position), itemClickListener);
    }

    @Override
    public int getItemCount() {
        return names.size();
    }


    //ViewHolder class
    public static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView textViewUsers;
        public EditText editTextUserName;
        public TextView textViewInstruction;
        public Button buttonMeasure;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.textViewUsers = (TextView) itemView.findViewById(R.id.textViewUsers);
            this.editTextUserName = (EditText) itemView.findViewById(R.id.analysis1EditPersonName);
            this.textViewInstruction = (TextView) itemView.findViewById(R.id.textViewInstructionMeasure);
            this.buttonMeasure = (Button) itemView.findViewById(R.id.buttonMeasure);
        }

        public void bind(final String UserNumber, final OnItemClickListener listener){
            this.textViewUsers.setText(UserNumber);
            this.textViewInstruction.setText(" Para medir debe ponerse en la posición donde estará este usuario conectado y oprimir el botón 'medir'");
            /*itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(UserNumber, getAdapterPosition());
                }
            });*/
        }
    }


    //Function for click in textView
    public interface OnItemClickListener{
        void onItemClick(String name, int position);
    }
}
