package com.masha.yuhubrain;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.masha.yuhubrain.data.GameLevel;

import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<ViewHolder> {
    private List<GameLevel> levels;
    private Context context;
    private FirestoreRepository repository = new FirestoreRepository();
    public RecyclerAdapter(List<GameLevel> levels, Context context){
        this.levels = levels;
        this.context = context;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.menu_item_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.nameTextView.setText(levels.get(position).getName());
        holder.descriptionTextView.setText(levels.get(position).getDescription());
        repository.getImageIntoView(levels.get(position).getImageLink(),context, holder.imageView);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, GameActivity.class);
                intent.putExtra("levelName", levels.get(position).getId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return levels.size();
    }
}

class ViewHolder extends RecyclerView.ViewHolder{
    public View view;
    public TextView nameTextView;
    public TextView descriptionTextView;
    public ImageView imageView;
    public ViewHolder(@NonNull View itemView) {
        super(itemView);
        view = itemView;
        nameTextView = view.findViewById(R.id.levelNameTextView);
        descriptionTextView = view.findViewById(R.id.levelDescTextView);
        imageView = view.findViewById(R.id.levelImage);
    }
}
