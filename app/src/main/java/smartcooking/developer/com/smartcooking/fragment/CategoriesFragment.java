package smartcooking.developer.com.smartcooking.fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;

import smartcooking.developer.com.smartcooking.R;

public class CategoriesFragment extends Fragment {

    private final ArrayList<Button> buttons = new ArrayList<>(6);

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View result = inflater.inflate(R.layout.fragment_categories, container, false);

        // add the buttons for each category
        buttons.add(0, (Button) result.findViewById(R.id.meat_btn));
        buttons.add(1, (Button) result.findViewById(R.id.fish_btn));
        buttons.add(2, (Button) result.findViewById(R.id.vegan_btn));
        buttons.add(3, (Button) result.findViewById(R.id.dessert_btn));
        buttons.add(4, (Button) result.findViewById(R.id.snack_btn));
        buttons.add(5, (Button) result.findViewById(R.id.other_btn));

        // set a tag, from 0 to 5, for the new fragment to know which category was chosen
        for (int i = 0; i < 6; i++) {
            buttons.get(i).setTag(i);
            buttons.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    RecipeListFragment recipeListFragment = RecipeListFragment.newInstance_category((int) v.getTag());
                    FragmentTransaction ft;
                    if (getFragmentManager() != null) {
                        ft = getFragmentManager().beginTransaction().addToBackStack("CATEGORIES").setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                        ft.replace(R.id.fragment, recipeListFragment).commit();
                    }
                }
            });
        }

        return result;
    }
}
