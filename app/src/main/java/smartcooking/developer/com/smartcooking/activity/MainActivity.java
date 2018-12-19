package smartcooking.developer.com.smartcooking.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;

import smartcooking.developer.com.smartcooking.R;
import smartcooking.developer.com.smartcooking.db.DatabaseBaseHelper;
import smartcooking.developer.com.smartcooking.fragment.AboutFragment;
import smartcooking.developer.com.smartcooking.fragment.CategoriesFragment;
import smartcooking.developer.com.smartcooking.fragment.FavoritesFragment;
import smartcooking.developer.com.smartcooking.fragment.MainFragment;
import smartcooking.developer.com.smartcooking.fragment.RecipeFragment;
import smartcooking.developer.com.smartcooking.fragment.SearchFragment;
import smartcooking.developer.com.smartcooking.fragment.SplashFragment;

// TODO - Ecrã main - Landscape

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView navigation;
    private SQLiteDatabase database;

    private final BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            FragmentTransaction ft;
            int open = navigation.getSelectedItemId();

            //Clear the stack
            FragmentManager fm = getSupportFragmentManager();
            for (int i = 0; i < fm.getBackStackEntryCount(); ++i) {
                fm.popBackStack();
            }

            switch (item.getItemId()) {
                case R.id.navigation_home:
                    if (open != R.id.navigation_home) {
                        MainFragment mainFragment = new MainFragment();
                        ft = getSupportFragmentManager().beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                        ft.replace(R.id.fragment, mainFragment).commit();
                    }
                    return true;
                case R.id.navigation_search:
                    if (open != R.id.navigation_search) {
                        SearchFragment searchFragment = new SearchFragment();
                        ft = getSupportFragmentManager().beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                        ft.replace(R.id.fragment, searchFragment).commit();
                    }
                    return true;
                case R.id.navigation_categories:
                    if (open != R.id.navigation_categories) {
                        CategoriesFragment categoriesFragment = new CategoriesFragment();
                        ft = getSupportFragmentManager().beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                        ft.replace(R.id.fragment, categoriesFragment).commit();
                    }
                    return true;
                case R.id.navigation_favorites:
                    if (open != R.id.navigation_favorites) {
                        FavoritesFragment favoritesFragment = new FavoritesFragment();
                        ft = getSupportFragmentManager().beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                        ft.replace(R.id.fragment, favoritesFragment).commit();
                    }
                    return true;
                case R.id.navigation_about:
                    if (open != R.id.navigation_about) {
                        AboutFragment aboutFragment = new AboutFragment();
                        ft = getSupportFragmentManager().beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
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

        database = new DatabaseBaseHelper(this).getWritableDatabase();

        navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setVisibility(View.GONE);

        String PREFS_NAME = "SmartCooking_PrefsName";
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String version = sharedPreferences.getString(PREFS_NAME, "-1");

        if (getIntent().getData() != null && version != null && Integer.parseInt(version) != -1) {
            Uri uri = getIntent().getData();
            String id;
            if ((id = uri.getQueryParameter("id")) != null) {
                RecipeFragment recipeFragment = RecipeFragment.newInstance(Integer.parseInt(id));
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment, recipeFragment).commit();
                return;
            }
        }

        if (savedInstanceState == null) {
            SplashFragment splashFragment = new SplashFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment, splashFragment).commit();
        }
    }

    @Override
    public void onBackPressed() {
        Fragment f = this.getSupportFragmentManager().findFragmentById(R.id.fragment);
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
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        database = new DatabaseBaseHelper(this).getWritableDatabase();
        super.onResume();
    }

    @Override
    public void onDestroy() {
        database.close();
        super.onDestroy();
    }

    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    public void onRestoreInstanceState(Bundle inState) {
        if (navigation.getVisibility() != View.VISIBLE)
            navigation.setVisibility(View.VISIBLE);

        super.onRestoreInstanceState(inState);
    }

}
