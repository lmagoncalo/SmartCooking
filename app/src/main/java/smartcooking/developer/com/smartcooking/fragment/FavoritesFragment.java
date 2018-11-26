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

public class FavoritesFragment extends Fragment implements AdapterView.OnItemClickListener {

    List<Recipe> recipeList;
    MyAdapter adapter;

    public FavoritesFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View result = inflater.inflate(R.layout.fragment_recipe_list, container, false);

        RecyclerView list = result.findViewById(R.id.list_recipes);

        SQLiteDatabase database = ((MainActivity) getActivity()).getDatabase();

        recipeList = OperationsDb.selectFavoriteRecipes(database);

        adapter = new MyAdapter(recipeList, this, getContext());
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
        RecipeFragment recipeFragment = RecipeFragment.newInstance(recipe.getId());
        FragmentTransaction ft = getActivity().getFragmentManager().beginTransaction().addToBackStack("FAVORITES").setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.replace(R.id.fragment, recipeFragment).commit();
    }

}
