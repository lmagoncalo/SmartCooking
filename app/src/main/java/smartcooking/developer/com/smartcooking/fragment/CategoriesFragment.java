package smartcooking.developer.com.smartcooking.fragment;


import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;

import smartcooking.developer.com.smartcooking.R;

public class CategoriesFragment extends Fragment {

    private ArrayList<Button> buttons = new ArrayList<>(6);
    private int i;

    public CategoriesFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_categories, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        buttons.add(0, (Button) getActivity().findViewById(R.id.meat_btn));
        buttons.add(1, (Button) getActivity().findViewById(R.id.fish_btn));
        buttons.add(2, (Button) getActivity().findViewById(R.id.vegan_btn));
        buttons.add(3, (Button) getActivity().findViewById(R.id.dessert_btn));
        buttons.add(4, (Button) getActivity().findViewById(R.id.snack_btn));
        buttons.add(5, (Button) getActivity().findViewById(R.id.other_btn));

        for (i = 0; i < 6; i++) {
            buttons.get(i).setTag(i);
            buttons.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    RecipeListFragment recipeListFragment = RecipeListFragment.newInstance((int) v.getTag());
                    FragmentTransaction ft = getFragmentManager().beginTransaction().addToBackStack("CATEGORIES").setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                    ft.replace(R.id.fragment, recipeListFragment).commit();
                }
            });
        }
    }

}
