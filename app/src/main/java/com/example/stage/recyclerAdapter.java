package com.example.stage;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class recyclerAdapter extends RecyclerView.Adapter<recyclerAdapter.MyViewHolder> {

    private ArrayList<Fact> factList;


    public recyclerAdapter(ArrayList<Fact> factList) {
        this.factList = factList;
    }

    @NonNull
    @Override
    public recyclerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new MyViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(@NonNull recyclerAdapter.MyViewHolder holder, int position) {
        String text = factList.get(position).getText();
        String date = factList.get(position).getDate();
        holder.text.setText(text);
        holder.date.setText(date.substring(0, 10));
    }

    @Override
    public int getItemCount() {
        return factList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView text, date;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            text = itemView.findViewById(R.id.text);
            date = itemView.findViewById(R.id.date);

        }
    }
}
