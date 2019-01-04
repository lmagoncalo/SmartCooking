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
import smartcooking.developer.com.smartcooking.activity.MainActivity;
import smartcooking.developer.com.smartcooking.db.OperationsDb;
import smartcooking.developer.com.smartcooking.db.Recipe.Recipe;

public class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {
    private List<Recipe> recipes;
    private final List<Recipe> recipes_copy;
    private final AdapterView.OnItemClickListener onItemClickListener;
    private final Context c;

    public MyAdapter(List<Recipe> recipes, AdapterView.OnItemClickListener onItemClickListener, Context c) {
        // initialize with the recipes list, the listener and a context
        this.recipes = recipes;

        // to have a backup of all recipes after filtering the recipes by name
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
        // to create each element of the list

        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.row, parent, false);

        // Return a new holder instance
        return new MyViewHolder(contactView, onItemClickListener, c);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder viewHolder, int position) {
        // to put all the information on each "ViewHolder"

        final Recipe recipe = recipes.get(position);

        // Set the results into TextViews
        viewHolder.getName().setText(recipe.getName().toLowerCase(Locale.getDefault()));
        String difficulty;
        switch (recipe.getDifficulty()) {
            case 1:
                difficulty = "Fácil";
                break;
            case 2:
                difficulty = "Intermédio";
                break;

            case 3:
                difficulty = "Avançado";
                break;

            default:
                difficulty = "Lendário";
                break;
        }
        viewHolder.getDifficulty().setText(difficulty);

        // to display the images from the URL's
        Picasso.get().load(recipe.getImage()).into(viewHolder.getImage());
    }


    // Filter Class to filter by name
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());

        // clear the list of displayed recipes
        recipes.clear();

        if (charText.isEmpty()) {
            // when the user deletes the search "query", the list is created again from the backup
            recipes = new ArrayList<>(recipes_copy);
        } else {
            String[] search_array = charText.split(" ");
            for (Recipe r : recipes_copy) {
                for (String s : search_array) {
                    // if the recipe isn't already in the list to be displayed and the name of the recipe contains (at least partially) the query
                    if (!recipes.contains(r) && r.getName().toLowerCase(Locale.getDefault()).contains(s)) {
                        recipes.add(r);
                    }
                }
            }
        }
        notifyDataSetChanged();
    }

    public void removeRecipe(int position) {
        // remove recipe from favorite list

        // change the favorite on the SQLite database
        OperationsDb.changeRecipeFavorite(recipes.get(position), ((MainActivity) c).getDatabase());

        // remove the recipe from the list variable
        recipes.remove(position);

        // notify the item removed by position
        // to perform recycler view delete animations
        // NOTE: don't call notifyDataSetChanged()
        notifyItemRemoved(position);
    }

    public void restoreItem(Recipe item, int position) {
        // when undo from the SnackBar is called

        // adds to list variable
        recipes.add(position, item);

        // change the favorite on the SQLite database
        OperationsDb.changeRecipeFavorite(item, ((MainActivity) c).getDatabase());

        // notify item added by position
        notifyItemInserted(position);
    }

    //
    public Recipe getRecipe(int position) {
        return recipes.get(position);
    }
}
