package Ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidproject_tamara_hen.R;

import java.util.ArrayList;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.MyViewHolder> {
    private ArrayList<Item> dataSet;
    public ItemAdapter(ArrayList<Item> dataSet) {
        this.dataSet = dataSet;
    }

    private RecyclerViewListener listener;

    public ItemAdapter(ArrayList<Item> dataSet, RecyclerViewListener listener) {
        this.dataSet = dataSet;
        this.listener = listener;
    }

    public interface RecyclerViewListener{
        void onClick(View  view, int position);
        boolean onLongClick(View view, int position);
        void onAddButtonClick(View view, int position);
        void onRemoveButtonClick(View view, int position);
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textViewName;
        TextView textCounter;
        ImageView imageView;
        Button btnAdd;
        Button btnRemove;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.tvName);
            textCounter = itemView.findViewById(R.id.tvItemCounter);
            imageView = itemView.findViewById(R.id.ibItem);
            btnAdd = itemView.findViewById(R.id.btnAdd);
            btnRemove = itemView.findViewById(R.id.btnRemove);

        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);

        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.textViewName.setText(dataSet.get(position).getName());
        holder.imageView.setImageResource(dataSet.get(position).getImage());
        holder.textCounter.setText(String.valueOf(dataSet.get(position).getAmount()));
//        holder.textCounter.setText(0);
        holder.itemView.setOnClickListener(v -> listener.onClick(v, position));
        holder.itemView.setOnLongClickListener(v -> {
            listener.onLongClick(v, position);
            return true;
        });
        holder.btnAdd.setOnClickListener(v -> listener.onAddButtonClick(holder.itemView, position));
        holder.btnRemove.setOnClickListener(v -> listener.onRemoveButtonClick(holder.itemView, position));
    }


    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    public void filterList(ArrayList<Item> filterList) {
        //Below line is to add our filtered list in our course array list.
        this.dataSet = filterList;
    }


}

