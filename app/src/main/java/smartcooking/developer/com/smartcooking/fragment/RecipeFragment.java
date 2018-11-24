package smartcooking.developer.com.smartcooking.fragment;


import android.app.Fragment;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import smartcooking.developer.com.smartcooking.R;
import smartcooking.developer.com.smartcooking.activity.MainActivity;
import smartcooking.developer.com.smartcooking.db.OperationsDb;
import smartcooking.developer.com.smartcooking.db.Recipe.Recipe;

public class RecipeFragment extends Fragment {
    private static String RECIPE = "get_recipe";

    FloatingActionButton favorite;
    Recipe recipe;

    public RecipeFragment() {
    }

    public static RecipeFragment newInstance(Recipe recipe) {
        RecipeFragment fragment = new RecipeFragment();
        Bundle args = new Bundle();
        args.putSerializable(RECIPE, recipe);
        fragment.setArguments(args);
        return fragment;
    }

    public Recipe getRecipe() {
        if (getArguments() != null) {
            return (Recipe) getArguments().getSerializable(RECIPE);
        }
        return null;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View result = inflater.inflate(R.layout.fragment_recipe, container, false);

        recipe = getRecipe();

        ImageView iv = result.findViewById(R.id.recipe_image);

        Picasso.get().load(recipe.getImage()).into(iv);

        favorite = result.findViewById(R.id.fab);

        if (recipe.isFavorite()) {
            favorite.setImageResource(R.drawable.ic_fav_on);
        } else {
            favorite.setImageResource(R.drawable.ic_fav_off);
        }

        favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (OperationsDb.changeRecipeFavorite(recipe, ((MainActivity) getActivity()).getDatabase())) {
                    recipe = OperationsDb.selectRecipeByID(recipe.getId(), ((MainActivity) getActivity()).getDatabase());
                    if (recipe != null && recipe.isFavorite()) {
                        favorite.setImageResource(R.drawable.ic_fav_on);
                    } else {
                        favorite.setImageResource(R.drawable.ic_fav_off);
                    }
                }
            }
        });

        TextView recipe_name = result.findViewById(R.id.recipe_details_name);
        TextView recipe_ingredients = result.findViewById(R.id.recipe_details_ingredients);
        TextView recipe_preparation = result.findViewById(R.id.recipe_details_preparation);

        recipe_name.setText(recipe.getName());
        recipe_ingredients.setText(recipe.getIngredientsString());
        recipe_preparation.setText(recipe.getPreparationString());


        Toolbar toolbar = result.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ActionBar actionbar = ((AppCompatActivity) getActivity()).getSupportActionBar();

        if (actionbar != null) {
            actionbar.setDisplayHomeAsUpEnabled(true);
            actionbar.setDisplayShowHomeEnabled(true);
        }

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getFragmentManager().popBackStackImmediate();
            }
        });

        return result;
    }

}
