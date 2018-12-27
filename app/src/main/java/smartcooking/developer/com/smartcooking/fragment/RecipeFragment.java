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

        SQLiteDatabase database = null;
        if (getActivity() != null) {
            database = ((MainActivity) getActivity()).getDatabase();
        }
        long id = getRecipe();
        if (getActivity() != null && database != null && id != -1) {
            recipe = OperationsDb.selectRecipeByID(id, database);

            ImageView iv = result.findViewById(R.id.recipe_image);

            Picasso.get().load(recipe.getImage()).into(iv);

            favorite = result.findViewById(R.id.recipe_fab_favorite);
            if (favorite != null) {

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
            }

            share = result.findViewById(R.id.recipe_fab_share);
            if (share != null) {
                share.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        /*Intent sendIntent = new Intent();
                        sendIntent.setAction(Intent.ACTION_SEND);
                        sendIntent
                                .putExtra(Intent.EXTRA_TEXT,);
                        sendIntent.setType("text/plain");
                        sendIntent.setPackage("com.facebook.orca");
                        try {
                            startActivity(sendIntent);
                        }
                        catch (android.content.ActivityNotFoundException ex) {
                            Toast.makeText(getContext(),"Please Install Facebook Messenger", Toast.LENGTH_LONG).show();
                        }*/
                        /*Intent sendIntent = new Intent();
                        sendIntent.setAction(Intent.ACTION_SEND);
                        sendIntent.putExtra(Intent.EXTRA_TEXT, "www.smartcooking.com/recipe?id=" + recipe.getId());
                        sendIntent.setType("text/plain");
                        sendIntent.setPackage("com.facebook.orca");
                        //sendIntent.setPackage("com.twitter.android");
                        sendIntent.setPackage("com.instagram.android");
                        //sendIntent.setPackage("com.pinterest");
                        startActivity(sendIntent);*/
                        String urlToShare = "https://" + getString(R.string.redirect_url) + "/recipe?id="+recipe.getId();
                        /*try {
                            Intent mIntentFacebook = new Intent();
                            mIntentFacebook.setClassName("com.facebook.katana", "com.facebook.composer.shareintent.ImplicitShareIntentHandlerDefaultAlias");
                            mIntentFacebook.setAction(Intent.ACTION_SEND);
                            mIntentFacebook.setType("text/plain");
                            mIntentFacebook.putExtra("android.intent.extra.TEXT", urlToShare);
                            startActivity(mIntentFacebook);
                        } catch (Exception e) {
                            e.printStackTrace();
                            Intent mIntentFacebookBrowser = new Intent(Intent.ACTION_SEND);
                            String mStringURL = "https://www.facebook.com/sharer/sharer.php?u=" + urlToShare;
                            mIntentFacebookBrowser = new Intent(Intent.ACTION_VIEW, Uri.parse(mStringURL));
                            startActivity(mIntentFacebookBrowser);
                        }*/

                        /*Intent fb = new Intent();
                        fb.setClassName("com.facebook.katana", "com.facebook.composer.shareintent.ImplicitShareIntentHandlerDefaultAlias");
                        fb.setAction(Intent.ACTION_SEND);
                        fb.setType("text/plain");
                        fb.putExtra(Intent.EXTRA_TEXT, urlToShare);*/

                        Intent fb = new Intent();
                        fb.setClassName("com.facebook.katana", "com.facebook.katana.activity.composer.ImplicitShareIntentHandler");
                        fb.setAction("android.intent.action.SEND");
                        fb.setType("text/plain");
                        fb.putExtra("android.intent.extra.TEXT", urlToShare);
                        //startActivity(fb);

                        Intent twitter = new Intent(Intent.ACTION_SEND);
                        twitter.setClassName("com.twitter.android","com.twitter.android.PostActivity");
                        twitter.putExtra(Intent.EXTRA_TEXT, "message");

                        Intent sendIntent = new Intent();
                        sendIntent.setAction(Intent.ACTION_SEND);
                        sendIntent.putExtra(Intent.EXTRA_TEXT, urlToShare);
                        sendIntent.setType("text/plain");

                        Intent intent = Intent.createChooser(fb, "Share Smartcooking");
                        intent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] {
                                sendIntent, twitter
});
                        startActivity(intent);

                    }
                });
            }

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

            if (collapsingToolbarLayout != null) {
                collapsingToolbarLayout.setTitle(recipe.getName());

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
