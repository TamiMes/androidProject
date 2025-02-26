package Ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.androidproject_tamara_hen.R;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.MyViewHolder> {
    private ArrayList<Item> dataSet;
    private DatabaseReference databaseReference;
    private RecyclerViewListener listener;

    public ItemAdapter(ArrayList<Item> dataSet, RecyclerViewListener listener) {
        this.dataSet = dataSet;
        this.listener = listener;
        this.databaseReference = FirebaseDatabase.getInstance().getReference("carts/ratings");
    }

    public interface RecyclerViewListener {
        void onClick(View view, int position);

        boolean onLongClick(View view, int position);

        void onAddButtonClick(View view, int position);

        void onRemoveButtonClick(View view, int position);

        void onFavoriteButtonClick(View view, int position);
        void onRatingClick(View view, int position);
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textViewName, textCounter, tvDesc, tvPrice,tvrating;
        ImageView imageView;
        Button btnAdd, btnRemove;
        ImageButton ibFavorite;
        RatingBar rbRate;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.tvName);
            textCounter = itemView.findViewById(R.id.tvItemCounter);
            imageView = itemView.findViewById(R.id.ibItem);
            btnAdd = itemView.findViewById(R.id.btnAdd);
            btnRemove = itemView.findViewById(R.id.btnRemove);
            tvDesc = itemView.findViewById(R.id.tvItemDesc);
            tvPrice = itemView.findViewById(R.id.tvItemPrice);
            ibFavorite = itemView.findViewById(R.id.ibFavorite);
            rbRate = itemView.findViewById(R.id.ratingBar);
            tvrating = itemView.findViewById(R.id.tvRating);
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Item item = dataSet.get(position);
        holder.textViewName.setText(item.getName());
        Glide.with(holder.itemView).load(item.getImage()).into(holder.imageView);
        holder.textCounter.setText(String.valueOf(item.getAmount()));
        holder.tvDesc.setText(item.getDesc());
        holder.tvPrice.setText(String.valueOf(item.getPrice()));
        holder.ibFavorite.setImageResource(item.getFavorite() ? R.drawable.baseline_favorite_24 : R.drawable.baseline_favorite_border_24);
        holder.rbRate.setRating((float) item.getRating());
        holder.tvrating.setText(String.valueOf(item.getRating()));

        holder.itemView.setOnClickListener(v -> listener.onClick(v, position));
        holder.itemView.setOnLongClickListener(v -> {
            listener.onLongClick(v, position);
            return true;
        });
        holder.btnAdd.setOnClickListener(v -> listener.onAddButtonClick(holder.itemView, position));
        holder.btnRemove.setOnClickListener(v -> listener.onRemoveButtonClick(holder.itemView, position));
        holder.ibFavorite.setOnClickListener(v -> listener.onFavoriteButtonClick(holder.itemView, position));

        holder.rbRate.setOnClickListener(v -> listener.onRatingClick(holder.itemView, position));
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }
    public void filterList(ArrayList<Item> filterList) {

        this.dataSet = filterList;
    }
}