package smartcooking.developer.com.smartcooking.fragment;


import android.content.res.Configuration;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import smartcooking.developer.com.smartcooking.R;
import smartcooking.developer.com.smartcooking.activity.MainActivity;
import smartcooking.developer.com.smartcooking.db.Ingredient.Ingredient;
import smartcooking.developer.com.smartcooking.db.OperationsDb;
import smartcooking.developer.com.smartcooking.db.Recipe.Recipe;
import smartcooking.developer.com.smartcooking.utils.MyAdapter;

public class RecipeListFragment extends Fragment implements AdapterView.OnItemClickListener {
    private static final String CATEGORY = "get_category";
    private static final String INGREDIENTS = "get_ingredients";

    private List<Recipe> recipeList;

    // 2 methods to initialize the fragment because this is used in category search as well as in ingredients search
    public static RecipeListFragment newInstance_category(int category) {
        RecipeListFragment fragment = new RecipeListFragment();
        Bundle args = new Bundle();
        args.putInt(CATEGORY, category);
        fragment.setArguments(args);
        return fragment;
    }

    public static RecipeListFragment newInstance_ingredients(ArrayList<Ingredient> ingredients) {
        RecipeListFragment fragment = new RecipeListFragment();
        Bundle args = new Bundle();
        args.putSerializable(INGREDIENTS, ingredients);
        fragment.setArguments(args);
        return fragment;
    }

    private int getCategory() {
        if (getArguments() != null) {
            return getArguments().getInt(CATEGORY, -1);
        }
        return -1;
    }

    @SuppressWarnings("unchecked")
    private ArrayList<Ingredient> getIngredients() {
        Object obj = null;
        if (getArguments() != null) {
            obj = getArguments().getSerializable(INGREDIENTS);
        }
        return (ArrayList<Ingredient>) obj;
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View result = inflater.inflate(R.layout.fragment_recipe_list, container, false);

        RecyclerView list = result.findViewById(R.id.list_recipes);

        SQLiteDatabase database;
        if (getActivity() != null) {
            database = ((MainActivity) getActivity()).getDatabase();

            switch (getCategory()) {
                // get the recipes of the selected category (if it was a search by category)
                case 0:
                    recipeList = OperationsDb.selectRecipeByCategory("Carne", database);
                    break;
                case 1:
                    recipeList = OperationsDb.selectRecipeByCategory("Peixe", database);
                    break;
                case 2:
                    recipeList = OperationsDb.selectRecipeByCategory("Vegetariano", database);
                    break;
                case 3:
                    recipeList = OperationsDb.selectRecipeByCategory("Sobremesa", database);
                    break;
                case 4:
                    recipeList = OperationsDb.selectRecipeByCategory("Snack", database);
                    break;
                case 5:
                    recipeList = OperationsDb.selectRecipeByCategory("Outros", database);
                    break;
                case -1:
                    // if the category is '-1', it means that this fragment was initialized for a search by ingredients

                    // get the reciped that used the selected ingredients
                    ArrayList<Ingredient> selected_ingredients = getIngredients();
                    recipeList = OperationsDb.selectRecipesByIngredients(selected_ingredients, database);
                    break;
                default:
                    break;
            }
        }

        MyAdapter adapter = new MyAdapter(recipeList, this, getContext());
        list.setAdapter(adapter);

        // set the RecyclerView to have a fixed size to improve performance
        list.setHasFixedSize(true);

        // different display, depending on the screen orientation
        int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // In landscape
            list.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        } else {
            // In portrait
            list.setLayoutManager(new LinearLayoutManager(getActivity()));
        }

        // to prevent the case when there's recipe to display
        if (adapter.getItemCount() == 0) {
            TextView empty = result.findViewById(R.id.empty_list);
            String s = "Ainda não há receitas aqui";
            empty.setText(s);
        }

        return result;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        // open the screen  of the details of the selected recipe
        openDetails(position);
    }

    private void openDetails(int index) {
        // initialize the "RecipeFragment" with the information about the selected recipe
        Recipe recipe = recipeList.get(index);
        RecipeFragment recipeFragment = RecipeFragment.newInstance(recipe.getId());
        FragmentTransaction ft;
        if (getFragmentManager() != null) {
            ft = getFragmentManager().beginTransaction().addToBackStack("LIST").setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            ft.replace(R.id.fragment, recipeFragment).commit();
        }
    }
}
