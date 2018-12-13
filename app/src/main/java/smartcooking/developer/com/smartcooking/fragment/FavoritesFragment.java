package smartcooking.developer.com.smartcooking.fragment;


import android.content.res.Configuration;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import java.util.List;

import smartcooking.developer.com.smartcooking.R;
import smartcooking.developer.com.smartcooking.activity.MainActivity;
import smartcooking.developer.com.smartcooking.db.OperationsDb;
import smartcooking.developer.com.smartcooking.db.Recipe.Recipe;
import smartcooking.developer.com.smartcooking.utils.MyAdapter;
import smartcooking.developer.com.smartcooking.utils.MyViewHolder;
import smartcooking.developer.com.smartcooking.utils.RecyclerItemTouchHelper;

public class FavoritesFragment extends Fragment implements AdapterView.OnItemClickListener, RecyclerItemTouchHelper.RecyclerItemTouchHelperListener {

    private List<Recipe> recipeList;
    private MyAdapter adapter;
    private TextView empty;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View result = inflater.inflate(R.layout.fragment_recipe_list, container, false);

        RecyclerView list = result.findViewById(R.id.list_recipes);

        SQLiteDatabase database;
        if (getActivity() != null) {
            database = ((MainActivity) getActivity()).getDatabase();
            recipeList = OperationsDb.selectFavoriteRecipes(database);
        }

        adapter = new MyAdapter(recipeList, this, getContext());
        list.setAdapter(adapter);
        list.setHasFixedSize(true);

        int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // In landscape
            list.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        } else {
            // In portrait
            list.setLayoutManager(new LinearLayoutManager(getActivity()));
        }

        empty = result.findViewById(R.id.empty_list);

        if (adapter.getItemCount() == 0) {
            String s = "Ainda não há receitas aqui";
            empty.setText(s);
        }

        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(list);

        return result;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        openDetails(position);
    }

    private void openDetails(int index) {
        Recipe recipe = recipeList.get(index);
        RecipeFragment recipeFragment = RecipeFragment.newInstance(recipe.getId());
        FragmentTransaction ft;
        if (getFragmentManager() != null) {
            ft = getFragmentManager().beginTransaction().addToBackStack("FAVORITES").setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            ft.replace(R.id.fragment, recipeFragment).commit();
        }
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder) {
        if (viewHolder instanceof MyViewHolder) {
            // backup of removed item for undo purpose
            final Recipe deletedRecipe = recipeList.get(viewHolder.getAdapterPosition());
            final int deletedIndex = viewHolder.getAdapterPosition();

            // remove the item from recycler view
            adapter.removeRecipe(viewHolder.getAdapterPosition());

            if (adapter.getItemCount() == 0) {
                String s = "Ainda não há receitas aqui";
                empty.setText(s);
            }

            // showing snack bar with Undo option
            if (getActivity() != null) {
                Snackbar snackbar = Snackbar
                        .make(getActivity().findViewById(R.id.list_layout), deletedRecipe.getName() + " deixou de ser favorito!", Snackbar.LENGTH_LONG);
                snackbar.setAction("DESFAZER", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        // undo is selected, restore the deleted item
                        adapter.restoreItem(deletedRecipe, deletedIndex);
                        empty.setText(null);
                    }
                });
                snackbar.setActionTextColor(Color.YELLOW);
                snackbar.show();
            }
        }
    }
}
