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

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView navigation;
    private SQLiteDatabase database;

    // Listener when "BottomNavigationView" tab changes
    private final BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            FragmentTransaction ft;

            // get the selected tab
            int open = navigation.getSelectedItemId();

            // Clear the stack of fragments
            FragmentManager fm = getSupportFragmentManager();
            for (int i = 0; i < fm.getBackStackEntryCount(); ++i) {
                fm.popBackStack();
            }

            // Replaces the fragment if it is different from the previous
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

        // initialize database
        database = new DatabaseBaseHelper(this).getWritableDatabase();

        navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        // For the "BottomNavigationView" to be invisible during the splash screen
        navigation.setVisibility(View.GONE);

        // To prevent the case when a user opens a link (from share) before initializing the app for the first time
        String PREFS_NAME = "SmartCooking_PrefsName";
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String version = sharedPreferences.getString(PREFS_NAME, "-1");

        // after opening a link (from share) only executes if there's something in the database
        if (getIntent().getData() != null && version != null && Integer.parseInt(version) != -1) {
            Uri uri = getIntent().getData();
            String id;
            if ((id = uri.getQueryParameter("id")) != null) {
                if (navigation.getVisibility() != View.VISIBLE)
                    navigation.setVisibility(View.VISIBLE);
                RecipeFragment recipeFragment = RecipeFragment.newInstance(Integer.parseInt(id));
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment, recipeFragment).commit();
                return;
            }
        }

        /*
           inside this 'if' because when the screen rotates the "onCreate" is executed again and
           the splashScreen would be created every time that happens
        */
        if (savedInstanceState == null) {
            SplashFragment splashFragment = new SplashFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment, splashFragment).commit();
        }
    }

    @Override
    public void onBackPressed() {
        Fragment f = this.getSupportFragmentManager().findFragmentById(R.id.fragment);

        // if the current fragment is one the 5 main ones (from the "BottomNavigationView" tabs)
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
        // for fragments to access the database
        return database;
    }

    /*
    @Override
    public void onResume() {
        database = new DatabaseBaseHelper(this).getWritableDatabase();
        super.onResume();
    }*/

    @Override
    public void onDestroy() {
        // close the database only when the app is closed
        database.close();
        super.onDestroy();
    }

    public void onSaveInstanceState(Bundle outState) {
        // saves all the information when a orientation change is made
        super.onSaveInstanceState(outState);
    }

    public void onRestoreInstanceState(Bundle inState) {
        // since "onCreate" is called after every orientation change, the "BottomNavigationView" would be invisible
        if (navigation.getVisibility() != View.VISIBLE)
            navigation.setVisibility(View.VISIBLE);

        // loads all the previously saved information when a orientation change is made
        super.onRestoreInstanceState(inState);
    }

}
