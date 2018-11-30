package smartcooking.developer.com.smartcooking.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import smartcooking.developer.com.smartcooking.R;

public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
    private TextView name;
    private TextView difficulty;
    private ImageView image;
    private Context c;
    private RelativeLayout viewForeground, viewBackground;

    private AdapterView.OnItemClickListener onItemClickListener;

    MyViewHolder(@NonNull View itemView, AdapterView.OnItemClickListener onItemClickListener, Context c) {
        super(itemView);
        name = itemView.findViewById(R.id.recipe_name);
        difficulty = itemView.findViewById(R.id.recipe_difficulty);
        image = itemView.findViewById(R.id.recipe_image);
        image.setOnClickListener(this);
        image.setOnLongClickListener(this);
        this.onItemClickListener = onItemClickListener;
        this.c = c;
        this.viewForeground = itemView.findViewById(R.id.view_foreground);
        this.viewBackground = itemView.findViewById(R.id.view_background);
    }

    @Override
    public void onClick(View view) {
        //passing the clicked position to the parent class
        onItemClickListener.onItemClick(null, view, getAdapterPosition(), view.getId());
        //Toast.makeText(c, "Cenas: " + view.getId(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onLongClick(View view) {
        // Handle long click
        // Return true to indicate the click was handled
        new ImagePreviewer().show(c, (ImageView) view);
        return true;
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

    RelativeLayout getViewForeground() {
        return viewForeground;
    }
}
