package smartcooking.developer.com.smartcooking.fragment;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
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
    private final ArrayList<Button> reduce_buttons = new ArrayList<>(5);
    private Button add_button;
    private final ArrayList<Integer> ingredients_spinners = new ArrayList<>(5);
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

            // gets all the ingredients from the database to put on the adapter for the spinner
            ingredients = OperationsDb.selectAllIngredients(database);
        }

        n_ingredients = 0;

        // initialize the adapter for the spinner with the list of ingredients from the database
        SpinAdapter adapter = new SpinAdapter(getContext(),
                android.R.layout.simple_spinner_item,
                ingredients, getContext());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // initiliaze the 5 possible ingredients and the corresponding "reduce_buttons"
        spinners.add(0, (Spinner) result.findViewById(R.id.ingr_spinner1));
        spinners.add(1, (Spinner) result.findViewById(R.id.ingr_spinner2));
        spinners.add(2,(Spinner)result.findViewById(R.id.ingr_spinner3));
        spinners.add(3,(Spinner)result.findViewById(R.id.ingr_spinner4));
        spinners.add(4, (Spinner) result.findViewById(R.id.ingr_spinner5));

        reduce_buttons.add(0, (Button) result.findViewById(R.id.main_btn_reduce1));
        reduce_buttons.add(1, (Button) result.findViewById(R.id.main_btn_reduce2));
        reduce_buttons.add(2, (Button) result.findViewById(R.id.main_btn_reduce3));
        reduce_buttons.add(3, (Button) result.findViewById(R.id.main_btn_reduce4));
        reduce_buttons.add(4, (Button) result.findViewById(R.id.main_btn_reduce5));

        // initialize the add button
        add_button = result.findViewById(R.id.main_btn_add);

        add_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // if the number of ingredients is less than 5
                if (n_ingredients < 5) {
                    n_ingredients++;

                    // makes the new spinner visible
                    spinners.get(n_ingredients - 1).setVisibility(View.VISIBLE);
                    // opens the dropdown list from the new spinner
                    spinners.get(n_ingredients - 1).performClick();
                    // makes the corresponding "reduce_button" visible
                    reduce_buttons.get(n_ingredients - 1).setVisibility(View.VISIBLE);
                    if (n_ingredients == 5) {
                        // if the number of ingredients is 5, the "add_button" is made invisible
                        add_button.setVisibility(View.GONE);
                    }
                }
            }
        });

        // fill all the spinners with the ingredients and makes them (along with the "reduce_buttons")
        for (int i = 0; i < 5; i++) {
            spinners.get(i).setAdapter(adapter);
            spinners.get(i).setVisibility(View.GONE);
            reduce_buttons.get(i).setVisibility(View.GONE);

            // this tag is used to know which button was pressed
            reduce_buttons.get(i).setTag(i);
            reduce_buttons.get(i).setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    int this_btn = (Integer) v.getTag();
                    // REMOVE the ingredient from the list
                    removeIngredient(this_btn);
                }
            });
        }

        Button search_button = result.findViewById(R.id.search_btn);
        search_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ArrayList<Ingredient> selected_ingredients = new ArrayList<>(n_ingredients);

                for (int i = 0; i < n_ingredients; i++) {
                    if(!selected_ingredients.contains((Ingredient) spinners.get(i).getSelectedItem())){
                        selected_ingredients.add((Ingredient) spinners.get(i).getSelectedItem());
                        //Log.e("INGREDIENTES"," " + selected_ingredients.get(i).getName());
                    }
                }

                // initialize the "RecipeListFragment" with the list of ingredients from the spinners
                RecipeListFragment recipeListFragment = RecipeListFragment.newInstance_ingredients(selected_ingredients);
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction().addToBackStack("MAIN").setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.replace(R.id.fragment, recipeListFragment).commit();
            }
        });

        return result;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // to make all the buttons squares
        final ViewTreeObserver observer = reduce_buttons.get(0).getViewTreeObserver();
        observer.addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        // the width of the "reduce_buttons" is being used as the height for the "add_button" and the spinners
                        int w = reduce_buttons.get(0).getWidth();
                        add_button.setHeight(w);
                        for (int i = 0; i < 5; i++) {
                            reduce_buttons.get(i).setHeight(w);
                            spinners.get(i).setMinimumHeight(w);

                        }
                    }
                });
    }

    // Remove an ingredient
    private void removeIngredient(int this_btn) {
        // clear the ingredient list
        ingredients_spinners.clear();

        // create the list only with the selected positions of the visible spinners
        for (int i = 0; i < n_ingredients; i++) {
            if (i != this_btn)
                ingredients_spinners.add(spinners.get(i).getSelectedItemPosition());
        }

        // makes only the right number of spinners visible and sets the right ingredients
        for (int i = 0; i < 5; i++) {
            spinners.get(i).setVisibility(View.GONE);
        }

        for (int i = 0; i < ingredients_spinners.size(); i++) {
            spinners.get(i).setVisibility(View.VISIBLE);
            spinners.get(i).setSelection(ingredients_spinners.get(i));
        }

        // decrement de number of ingredients
        n_ingredients--;
        // make the corresponding "reduce_button" invisible
        reduce_buttons.get(n_ingredients).setVisibility(View.GONE);

        // make the "add_button" visible
        add_button.setVisibility(View.VISIBLE);
    }
}
