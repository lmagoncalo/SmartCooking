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
    private final TextView name;
    private final TextView difficulty;
    private final ImageView image;
    private final Context c;
    private final RelativeLayout viewForeground, viewBackground;

    private final AdapterView.OnItemClickListener onItemClickListener;

    MyViewHolder(@NonNull final View itemView, AdapterView.OnItemClickListener _onItemClickListener, Context _c) {
        // set all the information about the recipe

        super(itemView);
        name = itemView.findViewById(R.id.recipe_name);
        difficulty = itemView.findViewById(R.id.recipe_difficulty);
        image = itemView.findViewById(R.id.recipe_image);
        image.setOnClickListener(this);
        image.setOnLongClickListener(this);
        this.onItemClickListener = _onItemClickListener;
        this.c = _c;
        this.viewForeground = itemView.findViewById(R.id.view_foreground);
        this.viewBackground = itemView.findViewById(R.id.view_background);

        // the same clickListener for the text. Without this, we could only click on the image
        name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickListener.onItemClick(null, view, getAdapterPosition(), view.getId());
            }
        });

        difficulty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickListener.onItemClick(null, view, getAdapterPosition(), view.getId());
            }
        });
    }

    @Override
    public void onClick(View view) {
        //passing the clicked position to the parent class
        onItemClickListener.onItemClick(null, view, getAdapterPosition(), view.getId());
    }

    @Override
    public boolean onLongClick(View view) {
        // Handle long click
        // Return true to indicate the click was handled

        // displays the image in bigger size
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

    // returns the foreground of each element during swipe
    RelativeLayout getViewForeground() {
        return viewForeground;
    }

    // returns the background of each element during swipe
    RelativeLayout getViewBackground() {
        return viewBackground;
    }
}
