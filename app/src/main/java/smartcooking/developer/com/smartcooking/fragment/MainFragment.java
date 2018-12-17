package smartcooking.developer.com.smartcooking.fragment;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import smartcooking.developer.com.smartcooking.R;
import smartcooking.developer.com.smartcooking.activity.MainActivity;
import smartcooking.developer.com.smartcooking.db.Ingredient.Ingredient;
import smartcooking.developer.com.smartcooking.db.OperationsDb;
import smartcooking.developer.com.smartcooking.utils.SpinAdapter;

public class MainFragment extends Fragment {

    private final ArrayList<Spinner> spinners = new ArrayList<>(5);
    private final ArrayList<Button> buttons = new ArrayList<>(5);
    private ArrayList<Integer> ingredients_spinners = new ArrayList<>(5);
    private int n_ingredients;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View result = inflater.inflate(R.layout.fragment_main, container, false);

        List<Ingredient> ingredients = null;
        SQLiteDatabase database;
        if (getActivity() != null) {
            database = ((MainActivity) getActivity()).getDatabase();
            ingredients = OperationsDb.selectAllIngredients(database);
        }

        n_ingredients = 1;

        SpinAdapter adapter = new SpinAdapter(getContext(),
                android.R.layout.simple_spinner_item,
                ingredients, getContext());

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinners.add(0, (Spinner) result.findViewById(R.id.ingr_spinner1));
        spinners.add(1, (Spinner) result.findViewById(R.id.ingr_spinner2));
        spinners.add(2,(Spinner)result.findViewById(R.id.ingr_spinner3));
        spinners.add(3,(Spinner)result.findViewById(R.id.ingr_spinner4));
        spinners.add(4, (Spinner) result.findViewById(R.id.ingr_spinner5));


        buttons.add(0, (Button) result.findViewById(R.id.main_btn1));
        buttons.add(1, (Button) result.findViewById(R.id.main_btn2));
        buttons.add(2, (Button) result.findViewById(R.id.main_btn3));
        buttons.add(3, (Button) result.findViewById(R.id.main_btn4));
        buttons.add(4, (Button) result.findViewById(R.id.main_btn5));

        for (int i = 0; i < 5; i++) {
            spinners.get(i).setAdapter(adapter);
        }

        buttons.get(0).setText("+");

        for (int i = 4; i >= n_ingredients; i--) {
            spinners.get(i).setVisibility(View.GONE);
            buttons.get(i).setVisibility(View.GONE);
        }

        for (int i = 0; i < 5; i++) {
            buttons.get(i).setTag(i);
            buttons.get(i).setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    int this_btn = (Integer) v.getTag();

                    // ADD
                    if (n_ingredients < 5 && this_btn == n_ingredients - 1) {
                        addIngredient();
                    }
                    // REMOVE
                    else {
                        for (int j = 0; j < n_ingredients - 1 && j != this_btn; j++) {
                            ingredients_spinners.add(spinners.get(j).getSelectedItemPosition());
                        }
                        removeIngredient(this_btn);
                    }
                }
            });
        }

        Button search_button = result.findViewById(R.id.search_btn);
        search_button.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                ArrayList<Ingredient> selected_ingredients = new ArrayList<>(n_ingredients);

                for (int i = 0; i < n_ingredients; i++) {
                    selected_ingredients.add((Ingredient) spinners.get(i).getSelectedItem());
                    //Log.e("INGREDIENTES"," " + selected_ingredients.get(i).getName());
                }

                RecipeListFragment recipeListFragment = RecipeListFragment.newInstance_ingredients(selected_ingredients);
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction().addToBackStack("MAIN").setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.replace(R.id.fragment, recipeListFragment).commit();
            }
        });

        return result;
    }

    // Remove um ingrediente
    private void removeIngredient(int this_btn) {
        ingredients_spinners.clear();
        for (int i = 0; i < n_ingredients; i++) {
            if (i != this_btn)
                ingredients_spinners.add(spinners.get(i).getSelectedItemPosition());
        }

        for (int i = 0; i < 5; i++) {
            spinners.get(i).setVisibility(View.GONE);
        }

        for (int i = 0; i < ingredients_spinners.size(); i++) {
            spinners.get(i).setVisibility(View.VISIBLE);
            spinners.get(i).setSelection(ingredients_spinners.get(i));
        }

        n_ingredients--;
        buttons.get(n_ingredients).setVisibility(View.GONE);
        buttons.get(n_ingredients - 1).setText("+");
    }

    // Adicionar um ingrediente
    private void addIngredient() {
        buttons.get(n_ingredients - 1).setText("-");

        n_ingredients++;

        buttons.get(n_ingredients - 1).setVisibility(View.VISIBLE);

        if (n_ingredients == 5) {
            buttons.get(n_ingredients - 1).setText("-");
        } else {
            buttons.get(n_ingredients - 1).setText("+");
        }

        spinners.get(n_ingredients - 1).setVisibility(View.VISIBLE);
    }
}
