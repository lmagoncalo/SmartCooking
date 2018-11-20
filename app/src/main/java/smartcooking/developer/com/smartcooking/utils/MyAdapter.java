package smartcooking.developer.com.smartcooking.utils;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import smartcooking.developer.com.smartcooking.R;
import smartcooking.developer.com.smartcooking.fragment.RecipeFragment;

/**
 * Created by alex on 02/07/17.
 * Adapted by Luis Gonçalo on 20/11/18
 */

public class MyAdapter extends BaseAdapter {
    private final List<Recipe> recipes;
    private final Activity act;
    private final Context c;
    private ArrayList<Recipe> arraylist;

    public MyAdapter(List<Recipe> recipes, Activity act, Context c) {
        this.recipes = recipes;
        this.act = act;
        this.c = c;
        this.arraylist = new ArrayList<>();
        this.arraylist.addAll(recipes);
    }

    @Override
    public int getCount() {
        return recipes.size();
    }

    @Override
    public Object getItem(int position) {
        return recipes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        final ViewHolder holder;

        if (view == null) {
            holder = new ViewHolder();
            view = act.getLayoutInflater().inflate(R.layout.row, parent, false);

            // Locate the TextViews in listview_item.xml
            holder.nome = view.findViewById(R.id.recipe_name);
            holder.dificuldade = view.findViewById(R.id.recipe_difficulty);
            holder.imagem = view.findViewById(R.id.recipe_image);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        final Recipe recipe = recipes.get(position);

        // Set the results into TextViews
        holder.nome.setText(recipe.getName().toLowerCase(Locale.getDefault()));
        String dificuldade;
        switch (recipe.getDifficulty()) {
            case 1:
                dificuldade = "Facil";
                break;
            case 2:
                dificuldade = "Intermedio";
                break;

            case 3:
                dificuldade = "Avançado";
                break;

            default:
                dificuldade = "Lendário";
                break;
        }
        holder.dificuldade.setText(dificuldade);

        // Listen for ListView Item Click
        view.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                RecipeFragment recipeFragment = RecipeFragment.newInstance(recipe);
                FragmentTransaction ft = act.getFragmentManager().beginTransaction().addToBackStack("LIST").setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.replace(R.id.fragment, recipeFragment).commit();
            }
        });

        return view;
    }

    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        recipes.clear();

        if (charText.length() == 0) {
            recipes.addAll(arraylist);
        } else {
            String[] search_array = charText.split(" ");
            for (String s : search_array) {
                for (Recipe r : arraylist) {
                    if (r.getName().toLowerCase(Locale.getDefault()).contains(s) && !recipes.contains(r)) {
                        recipes.add(r);
                    }
                }
            }
        }
        notifyDataSetChanged();
    }

    public class ViewHolder {
        TextView nome;
        TextView dificuldade;
        ImageView imagem;
    }
}
