package smartcooking.developer.com.smartcooking.fragment;


import android.app.Fragment;
import android.app.FragmentTransaction;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import java.util.List;

import smartcooking.developer.com.smartcooking.R;
import smartcooking.developer.com.smartcooking.activity.MainActivity;
import smartcooking.developer.com.smartcooking.db.OperationsDb;
import smartcooking.developer.com.smartcooking.db.Recipe.Recipe;
import smartcooking.developer.com.smartcooking.utils.MyAdapter;

public class RecipeListFragment extends Fragment implements AdapterView.OnItemClickListener {
    private static String CATEGORY = "get_category";
    private static String INGREDIENTS = "get_ingredients";

    List<Recipe> recipeList;
    MyAdapter adapter;

    public RecipeListFragment() {
    }

    public static RecipeListFragment newInstance(int category) {
        RecipeListFragment fragment = new RecipeListFragment();
        Bundle args = new Bundle();
        args.putInt(CATEGORY, category);
        fragment.setArguments(args);
        return fragment;
    }

    public int getCategory() {
        if (getArguments() != null) {
            return getArguments().getInt(CATEGORY, -1);
        }
        return -1;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View result = inflater.inflate(R.layout.fragment_recipe_list, container, false);

        RecyclerView list = result.findViewById(R.id.list_recipes);

        SQLiteDatabase database = ((MainActivity) getActivity()).getDatabase();

        switch (getCategory()) {
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
            default:
                break;
        }

        adapter = new MyAdapter(recipeList, this);
        list.setAdapter(adapter);
        list.setHasFixedSize(true);
        list.setLayoutManager(new LinearLayoutManager(getActivity()));

        return result;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        openDetails(position);
    }

    private void openDetails(int index) {
        Recipe recipe = recipeList.get(index);
        RecipeFragment recipeFragment = RecipeFragment.newInstance(recipe);
        FragmentTransaction ft = getActivity().getFragmentManager().beginTransaction().addToBackStack("CATEGORIES").setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.replace(R.id.fragment, recipeFragment).commit();
    }
}
