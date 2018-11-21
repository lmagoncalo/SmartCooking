package smartcooking.developer.com.smartcooking.utils;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import smartcooking.developer.com.smartcooking.R;

class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private TextView name;
    private TextView difficulty;
    private ImageView image;

    private AdapterView.OnItemClickListener onItemClickListener;

    MyViewHolder(@NonNull View itemView, AdapterView.OnItemClickListener onItemClickListener) {
        super(itemView);
        name = itemView.findViewById(R.id.recipe_name);
        difficulty = itemView.findViewById(R.id.recipe_difficulty);
        image = itemView.findViewById(R.id.recipe_image);
        image.setOnClickListener(this);
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public void onClick(View view) {
        //passing the clicked position to the parent class
        onItemClickListener.onItemClick(null, view, getAdapterPosition(), view.getId());
    }

    TextView getName() {
        return name;
    }

    TextView getDifficulty() {
        return difficulty;
    }

    ImageView getImage() {
        return image;
    }
}
