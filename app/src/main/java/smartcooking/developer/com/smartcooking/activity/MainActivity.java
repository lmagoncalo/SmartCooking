package smartcooking.developer.com.smartcooking.activity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import smartcooking.developer.com.smartcooking.R;
import smartcooking.developer.com.smartcooking.db.DatabaseBaseHelper;
import smartcooking.developer.com.smartcooking.fragment.AboutFragment;
import smartcooking.developer.com.smartcooking.fragment.CategoriesFragment;
import smartcooking.developer.com.smartcooking.fragment.FavoritesFragment;
import smartcooking.developer.com.smartcooking.fragment.MainFragment;
import smartcooking.developer.com.smartcooking.fragment.SearchFragment;

// TODO - Ecrã de detalhes, ecrã main
// TODO - Carregar a imagem nas linhas
// TODO - Meter a pesquisa a andar para cima - Talvez
// TODO - Criar os onResume e onDelete
// TODO - Filtragem por ingredientes
// TODO - Responsiveness
// TODO - Erro não se sabe porquê - Carregar search, abrir uma receita, carregar novamente no search e depois numa receita
// TODO - Meter um indice com o alfabeto para pesquisar ingredientes - Talvez

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

        navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        database = new DatabaseBaseHelper(this).getWritableDatabase();

        /*Recipe r1 = new Recipe(1, "Tostas de Frango no Forno", 15, 3, "Carne", "SmartCooking", "Bota agua|Bota ao lume", "Agua|Lume", "https://smartcookingapp.files.wordpress.com/2015/10/receita_pgi.jpg");
        Recipe r2 = new Recipe(2, "Peixe", 15, 3, "Peixe", "SmartCooking", "Bota agua|Bota ao lume", "Agua|Lume", "https://smartcookingapp.files.wordpress.com/2015/10/receita_pgi.jpg");
        r2.setFavorite(true);
        Recipe r3 = new Recipe(3, "Mais peixe", 15, 3, "Peixe", "SmartCooking", "Bota agua|Bota ao lume", "Agua|Lume", "https://smartcookingapp.files.wordpress.com/2015/10/receita_pgi.jpg");
        OperationsDb.recipeControlledInsert(r1,database);
        OperationsDb.recipeControlledInsert(r2,database);
        OperationsDb.recipeControlledInsert(r3,database);
        Ingredient i1 = new Ingredient(1,"Carne");
        Ingredient i2 = new Ingredient(2,"Peixe");
        Ingredient i3 = new Ingredient(3,"Pão");
        OperationsDb.insertIngredient(i1,database);
        OperationsDb.insertIngredient(i2,database);
        OperationsDb.insertIngredient(i3,database);*/

        MainFragment mainFragment = new MainFragment();
        getFragmentManager().beginTransaction().replace(R.id.fragment, mainFragment).commit();
    }

    @Override
    public void onBackPressed() {
        //Toast.makeText(this, "Back", Toast.LENGTH_SHORT).show();
        super.onBackPressed();
    }

    public SQLiteDatabase getDatabase() {
        return database;
    }
}
