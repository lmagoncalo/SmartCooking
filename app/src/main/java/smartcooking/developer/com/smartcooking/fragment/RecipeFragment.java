package smartcooking.developer.com.smartcooking.fragment;


import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import net.opacapp.multilinecollapsingtoolbar.CollapsingToolbarLayout;

import smartcooking.developer.com.smartcooking.R;
import smartcooking.developer.com.smartcooking.activity.MainActivity;
import smartcooking.developer.com.smartcooking.db.OperationsDb;
import smartcooking.developer.com.smartcooking.db.Recipe.Recipe;

public class RecipeFragment extends Fragment {
    private static final String RECIPE = "get_recipe";

    private FloatingActionButton favorite;
    private FloatingActionButton share;
    private Recipe recipe;

    public static RecipeFragment newInstance(long id) {
        // create new instance for this fragment and set the ID of the chosen recipe
        RecipeFragment fragment = new RecipeFragment();
        Bundle args = new Bundle();
        args.putLong(RECIPE, id);
        fragment.setArguments(args);
        return fragment;
    }

    private long getRecipe() {
        if (getArguments() != null) {
            return getArguments().getLong(RECIPE, -1);
        }
        return -1;
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View result = inflater.inflate(R.layout.fragment_recipe, container, false);

        // get reference of database from the activity
        SQLiteDatabase database = null;
        if (getActivity() != null) {
            database = ((MainActivity) getActivity()).getDatabase();
        }

        long id = getRecipe();
        if (getActivity() != null && database != null && id != -1) {
            // get recipe from database
            recipe = OperationsDb.selectRecipeByID(id, database);

            // loads the recipe image from the URL
            ImageView iv = result.findViewById(R.id.recipe_image);
            Picasso.get().load(recipe.getImage()).into(iv);

            favorite = result.findViewById(R.id.recipe_fab_favorite);
            if (favorite != null) {

                // change de icon if the recipe is favorite or not
                if (recipe.isFavorite()) {
                    favorite.setImageResource(R.drawable.ic_fav_on);
                } else {
                    favorite.setImageResource(R.drawable.ic_fav_off);
                }

                // change de icon if the recipe is favorite or not, and the recipe on the database
                favorite.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (OperationsDb.changeRecipeFavorite(recipe, ((MainActivity) getActivity()).getDatabase())) {
                            // only changes the icon, if the update of the database was done ok
                            recipe = OperationsDb.selectRecipeByID(recipe.getId(), ((MainActivity) getActivity()).getDatabase());
                            if (recipe != null && recipe.isFavorite()) {
                                favorite.setImageResource(R.drawable.ic_fav_on);
                            } else {
                                favorite.setImageResource(R.drawable.ic_fav_off);
                            }
                        }
                    }
                });
            }

            share = result.findViewById(R.id.recipe_fab_share);
            if (share != null) {
                share.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        /* CÓDIGO BASEADO EM:   https://stackoverflow.com/questions/8771333/android-share-intent-for-facebook-share-text-and-link */

                        String urlToShare = getString(R.string.redirect_url_protocol) + "://" + getString(R.string.redirect_url) + "/recipe?id="+recipe.getId();

                        Intent facebook = new Intent(Intent.ACTION_SEND);
                        facebook.setClassName("com.facebook.katana", "com.facebook.katana.activity.composer.ImplicitShareIntentHandler");
                        facebook.setType("text/plain");
                        facebook.putExtra(Intent.EXTRA_TEXT, urlToShare);

                        Intent twitter = new Intent(Intent.ACTION_SEND);
                        twitter.setClassName("com.twitter.android","com.twitter.android.PostActivity");
                        twitter.putExtra(Intent.EXTRA_TEXT, urlToShare);

                        Intent sendIntent = new Intent();
                        sendIntent.setAction(Intent.ACTION_SEND);
                        sendIntent.setType("text/plain");
                        sendIntent.putExtra(Intent.EXTRA_TEXT, urlToShare);

                        Intent intent = Intent.createChooser(facebook, "Share Smartcooking");
                        intent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] {
                                sendIntent, twitter
                        });


                        startActivity(intent);

                    }
                });
            }

            // display all the information about the recipe
            TextView recipe_ingredients = result.findViewById(R.id.recipe_details_ingredients);
            TextView recipe_preparation = result.findViewById(R.id.recipe_details_preparation);
            TextView recipe_time = result.findViewById(R.id.recipe_time_text);
            TextView recipe_difficulty = result.findViewById(R.id.recipe_difficulty_text);

            recipe_ingredients.setText(recipe.getIngredientsString());
            recipe_preparation.setText(recipe.getPreparationString());
            String time = recipe.getTime() + " min";
            recipe_time.setText(time);
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
            recipe_difficulty.setText(difficulty);



            Toolbar toolbar;
            CollapsingToolbarLayout collapsingToolbarLayout;

            toolbar = result.findViewById(R.id.toolbar);
            ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

            collapsingToolbarLayout = result.findViewById(R.id.collapsing_toolbar_layout);

            // if in landscape, there's no "CollapsingToolbarLayout"
            if (collapsingToolbarLayout != null) {
                collapsingToolbarLayout.setTitle(recipe.getName());

                // toolbar text margins, for this text to be align with the rest of the text
                int horizontal_dim = Math.round(getActivity().getResources().getDimension(R.dimen.activity_horizontal_margin));
                int vertical_dim = Math.round(getActivity().getResources().getDimension(R.dimen.activity_vertical_margin));
                collapsingToolbarLayout.setExpandedTitleMarginStart(horizontal_dim);
                collapsingToolbarLayout.setExpandedTitleMarginBottom(vertical_dim);


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

                // when click on the back button of the toolbar, pop the fragment stack to go back to the previous fragment
                toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getActivity().getSupportFragmentManager().popBackStackImmediate();
                    }
                });
            } else {
                TextView recipe_title = result.findViewById(R.id.recipe_details_title);
                recipe_title.setText(recipe.getName());
            }

        }

        return result;
    }

}
