package smartcooking.developer.com.smartcooking.fragment;


import android.app.Fragment;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
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

    public static RecipeFragment newInstance(long id) {
        RecipeFragment fragment = new RecipeFragment();
        Bundle args = new Bundle();
        args.putLong(RECIPE, id);
        fragment.setArguments(args);
        return fragment;
    }

    public long getRecipe() {
        if (getArguments() != null) {
            return getArguments().getLong(RECIPE, -1);
        }
        return -1;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View result = inflater.inflate(R.layout.fragment_recipe, container, false);

        SQLiteDatabase database = ((MainActivity) getActivity()).getDatabase();
        long id = getRecipe();
        if (id != -1) {
            recipe = OperationsDb.selectRecipeByID(id, database);

            ImageView iv = result.findViewById(R.id.recipe_image);

            Picasso.get().load(recipe.getImage()).into(iv);

            favorite = result.findViewById(R.id.recipe_fab_favorite);

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

            TextView recipe_ingredients = result.findViewById(R.id.recipe_details_ingredients);
            TextView recipe_preparation = result.findViewById(R.id.recipe_details_preparation);

            recipe_ingredients.setText(recipe.getIngredientsString());
            recipe_preparation.setText(recipe.getPreparationString());

            Toolbar toolbar;
            CollapsingToolbarLayout collapsingToolbarLayout;

            toolbar = result.findViewById(R.id.toolbar);
            ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

            collapsingToolbarLayout = result.findViewById(R.id.collapsing_toolbar_layout);
            collapsingToolbarLayout.setTitle(recipe.getName());

            ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
            if (actionBar != null) {
                actionBar.setDisplayHomeAsUpEnabled(true);
                actionBar.setDisplayShowHomeEnabled(true);
            }

            AppBarLayout mAppBarLayout = result.findViewById(R.id.app_bar);
            mAppBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
                int scrollRange = -1;

                @Override
                public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                    if (scrollRange == -1) {
                        scrollRange = appBarLayout.getTotalScrollRange();
                    }
                }
            });

            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getActivity().getFragmentManager().popBackStackImmediate();
                }
            });

        }

        return result;
    }

}
