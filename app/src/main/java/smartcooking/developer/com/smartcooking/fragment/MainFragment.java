package smartcooking.developer.com.smartcooking.fragment;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
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
    private final ArrayList<LinearLayout> linearLayouts = new ArrayList<>(5);
    private final ArrayList<ImageButton> reduceButtons = new ArrayList<>(5);
    private int n_ingredients;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View result = inflater.inflate(R.layout.fragment_main, container, false);

        List<Ingredient> ingredients = OperationsDb.selectAllIngredients(((MainActivity) getActivity()).getDatabase());
        n_ingredients = 1;

        SpinAdapter adapter = new SpinAdapter(getContext(),
                android.R.layout.simple_spinner_item,
                ingredients);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinners.add(0, (Spinner) result.findViewById(R.id.ingr_spinner1));
        spinners.add(1, (Spinner) result.findViewById(R.id.ingr_spinner2));
        spinners.add(2,(Spinner)result.findViewById(R.id.ingr_spinner3));
        spinners.add(3,(Spinner)result.findViewById(R.id.ingr_spinner4));
        spinners.add(4, (Spinner) result.findViewById(R.id.ingr_spinner5));

        linearLayouts.add(0, (LinearLayout) result.findViewById(R.id.layout_spinner1));
        linearLayouts.add(1, (LinearLayout) result.findViewById(R.id.layout_spinner2));
        linearLayouts.add(2, (LinearLayout) result.findViewById(R.id.layout_spinner3));
        linearLayouts.add(3, (LinearLayout) result.findViewById(R.id.layout_spinner4));
        linearLayouts.add(4, (LinearLayout) result.findViewById(R.id.layout_spinner5));

        reduceButtons.add(0, (ImageButton) result.findViewById(R.id.reduce_btn1));
        reduceButtons.add(1, (ImageButton) result.findViewById(R.id.reduce_btn2));
        reduceButtons.add(2, (ImageButton) result.findViewById(R.id.reduce_btn3));
        reduceButtons.add(3, (ImageButton) result.findViewById(R.id.reduce_btn4));
        reduceButtons.add(4, (ImageButton) result.findViewById(R.id.reduce_btn5));

        for (int i = 0; i < 5; i++) {
            spinners.get(i).setAdapter(adapter);
        }

        reduceButtons.get(0).setVisibility(View.GONE);

        for (int i = 4; i >= n_ingredients; i--) {
            linearLayouts.get(i).setVisibility(View.GONE);
        }

        for (int i = 0; i < 5; i++) {
            reduceButtons.get(i).setTag(i);
            reduceButtons.get(i).setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    linearLayouts.get((Integer) v.getTag()).setVisibility(View.GONE);
                    n_ingredients--;
                }
            });
        }

        ImageButton add_button = result.findViewById(R.id.add_btn);
        add_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (n_ingredients < 5) {
                    linearLayouts.get(n_ingredients).setVisibility(View.VISIBLE);
                    n_ingredients++;
                }
                if (n_ingredients == 5) {
                    v.setVisibility(View.GONE);
                }
            }
        });

        Button search_button = result.findViewById(R.id.search_btn);
        search_button.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                ArrayList<Ingredient> selected_ingredients = new ArrayList<>(n_ingredients);

                for (int i = 0; i < n_ingredients; i++) {
                    selected_ingredients.add((Ingredient) spinners.get(i).getSelectedItem());
                }

                RecipeListFragment recipeListFragment = RecipeListFragment.newInstance_ingredients(selected_ingredients);
                FragmentTransaction ft = getActivity().getFragmentManager().beginTransaction().addToBackStack("MAIN").setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.replace(R.id.fragment, recipeListFragment).commit();
            }
        });

        Typeface type = ResourcesCompat.getFont(getContext(), R.font.montserrat);
        search_button.setTypeface(type);

        return result;
    }
}
