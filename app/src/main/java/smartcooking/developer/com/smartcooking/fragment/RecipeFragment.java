package smartcooking.developer.com.smartcooking.fragment;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import smartcooking.developer.com.smartcooking.R;
import smartcooking.developer.com.smartcooking.db.Recipe.Recipe;

public class RecipeFragment extends Fragment {
    private static String RECIPE = "get_recipe";

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

        return result;
    }

}
