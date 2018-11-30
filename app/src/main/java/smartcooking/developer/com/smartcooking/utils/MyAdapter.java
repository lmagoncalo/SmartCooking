package smartcooking.developer.com.smartcooking.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import smartcooking.developer.com.smartcooking.R;
import smartcooking.developer.com.smartcooking.db.Recipe.Recipe;

public class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {
    private List<Recipe> recipes;
    private List<Recipe> recipes_copy;
    private AdapterView.OnItemClickListener onItemClickListener;
    private Context c;

    public MyAdapter(List<Recipe> recipes, AdapterView.OnItemClickListener onItemClickListener, Context c) {
        this.recipes = recipes;
        this.recipes_copy = new ArrayList<>(recipes);
        this.onItemClickListener = onItemClickListener;
        this.c = c;
    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int position) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.row, parent, false);

        // Return a new holder instance
        return new MyViewHolder(contactView, onItemClickListener, c);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder viewHolder, int position) {
        final Recipe recipe = recipes.get(position);

        // Set the results into TextViews
        viewHolder.getName().setText(recipe.getName().toLowerCase(Locale.getDefault()));
        String difficulty;
        switch (recipe.getDifficulty()) {
            case 1:
                difficulty = "Facil";
                break;
            case 2:
                difficulty = "Intermedio";
                break;

            case 3:
                difficulty = "Avançado";
                break;

            default:
                difficulty = "Lendário";
                break;
        }
        viewHolder.getDifficulty().setText(difficulty);

        Picasso.get().load(recipe.getImage()).into(viewHolder.getImage());
    }



    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        recipes.clear();

        if (charText.isEmpty()) {
            recipes = new ArrayList<>(recipes_copy);
        } else {
            String[] search_array = charText.split(" ");
            for (String s : search_array) {
                for (Recipe r : recipes_copy) {
                    if (r.getName().toLowerCase(Locale.getDefault()).contains(s) && !recipes.contains(r)) {
                        recipes.add(r);
                    }
                }
            }
        }
        notifyDataSetChanged();
    }

    public void removeRecipe(int postition){
        recipes.remove(postition);
        //TODO: é preciso fazer isto na "recipes_copy" ?
    }
}
