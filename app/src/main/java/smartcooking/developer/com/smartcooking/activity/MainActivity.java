package smartcooking.developer.com.smartcooking.activity;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import smartcooking.developer.com.smartcooking.R;
import smartcooking.developer.com.smartcooking.db.DatabaseBaseHelper;
import smartcooking.developer.com.smartcooking.db.Ingredient.Ingredient;
import smartcooking.developer.com.smartcooking.db.OperationsDb;
import smartcooking.developer.com.smartcooking.db.Recipe.Recipe;
import smartcooking.developer.com.smartcooking.db.Relation.Relation;
import smartcooking.developer.com.smartcooking.fragment.AboutFragment;
import smartcooking.developer.com.smartcooking.fragment.CategoriesFragment;
import smartcooking.developer.com.smartcooking.fragment.FavoritesFragment;
import smartcooking.developer.com.smartcooking.fragment.MainFragment;
import smartcooking.developer.com.smartcooking.fragment.RecipeFragment;
import smartcooking.developer.com.smartcooking.fragment.SearchFragment;

// TODO - Ecrã de detalhes, ecrã main
// TODO - Criar os onResume e onDelete
// TODO - Responsiveness
// TODO - Meter um indice com o alfabeto para pesquisar ingredientes - Talvez
// TODO - Splashscreen com asynctask - Deixar para ultimo
// TODO - Slide to unfavorite

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView navigation;
    private SQLiteDatabase database;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            FragmentTransaction ft;
            int open = navigation.getSelectedItemId();

            //Clear the stack
            FragmentManager fm = getFragmentManager();
            for (int i = 0; i < fm.getBackStackEntryCount(); ++i) {
                fm.popBackStack();
            }

            switch (item.getItemId()) {
                case R.id.navigation_home:
                    if (open != R.id.navigation_home) {
                        MainFragment mainFragment = new MainFragment();
                        ft = getFragmentManager().beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                        ft.replace(R.id.fragment, mainFragment).commit();
                    }
                    return true;
                case R.id.navigation_search:
                    if (open != R.id.navigation_search) {
                        SearchFragment searchFragment = new SearchFragment();
                        ft = getFragmentManager().beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                        ft.replace(R.id.fragment, searchFragment).commit();
                    }
                    return true;
                case R.id.navigation_categories:
                    if (open != R.id.navigation_categories) {
                        CategoriesFragment categoriesFragment = new CategoriesFragment();
                        ft = getFragmentManager().beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                        ft.replace(R.id.fragment, categoriesFragment).commit();
                    }
                    return true;
                case R.id.navigation_favorites:
                    if (open != R.id.navigation_favorites) {
                        FavoritesFragment favoritesFragment = new FavoritesFragment();
                        ft = getFragmentManager().beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                        ft.replace(R.id.fragment, favoritesFragment).commit();
                    }
                    return true;
                case R.id.navigation_about:
                    if (open != R.id.navigation_about) {
                        AboutFragment aboutFragment = new AboutFragment();
                        ft = getFragmentManager().beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                        ft.replace(R.id.fragment, aboutFragment).commit();
                    }
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    @Override
    public void onBackPressed() {
        Fragment f = this.getFragmentManager().findFragmentById(R.id.fragment);
        if (f instanceof MainFragment || f instanceof SearchFragment || f instanceof CategoriesFragment || f instanceof FavoritesFragment || f instanceof AboutFragment) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Tem a certeza que quer sair?")
                    .setCancelable(true)

                    .setNegativeButton("Não", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    })
                    .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            MainActivity.super.onBackPressed();
                            database.close();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        } else {
            super.onBackPressed();
        }
    }

    public SQLiteDatabase getDatabase() {
        return database;
    }

    @Override
    public void onResume() {
        super.onResume();

        database = new DatabaseBaseHelper(this).getWritableDatabase();

        navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        Recipe r1 = new Recipe(1, "Tostas de Frango no Forno", 15, 3, "Carne", "SmartCooking", "Bota agua|Bota ao lume", "Agua|Lume", "https://smartcookingapp.files.wordpress.com/2015/10/receita_pgi.jpg");
        Recipe r2 = new Recipe(2, "Peixe", 15, 3, "Peixe", "SmartCooking", "Bota agua|Bota ao lume", "Agua|Lume", "https://smartcookingapp.files.wordpress.com/2015/10/receita_pgi.jpg");
        r2.setFavorite(true);
        Recipe r3 = new Recipe(3, "Mais peixe", 15, 3, "Peixe", "SmartCooking", "Bota agua|Bota ao lume", "Agua|Lume", "https://smartcookingapp.files.wordpress.com/2015/10/receita_pgi.jpg");
        OperationsDb.recipeControlledInsert(r1,database);
        OperationsDb.recipeControlledInsert(r2,database);
        OperationsDb.recipeControlledInsert(r3,database);
        /*Ingredient i1 = new Ingredient(1,"Carne");
        Ingredient i2 = new Ingredient(2,"Peixe");
        Ingredient i3 = new Ingredient(3,"Pão");
        OperationsDb.insertIngredient(i1,database);
        OperationsDb.insertIngredient(i2,database);
        OperationsDb.insertIngredient(i3,database);*/
        Relation rel = new Relation();
        rel.setID_ingredient(1);
        rel.setID_recipe(1);
        OperationsDb.insertRelation(rel, database);
        rel.setID_recipe(2);
        OperationsDb.insertRelation(rel, database);

        if (getIntent().getData() != null) {
            Uri uri = getIntent().getData();
            String id;
            if ((id = uri.getQueryParameter("id")) != null) {
                /*MainFragment mainFragment = new MainFragment();
                getFragmentManager().beginTransaction().replace(R.id.fragment, mainFragment).addToBackStack("MAIN").commit();*/

                RecipeFragment recipeFragment = RecipeFragment.newInstance(Integer.parseInt(id));
                getFragmentManager().beginTransaction().replace(R.id.fragment, recipeFragment).commit();
                return;
            }
        }

        Fragment f = this.getFragmentManager().findFragmentById(R.id.fragment);
        if (f==null) {
            MainFragment mainFragment = new MainFragment();
            getFragmentManager().beginTransaction().replace(R.id.fragment, mainFragment).commit();
        }

    }

    @Override
    public void onPause(){
        super.onPause();
        database.close();
    }

}
