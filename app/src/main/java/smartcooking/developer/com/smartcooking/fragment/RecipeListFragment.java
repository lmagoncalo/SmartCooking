package smartcooking.developer.com.smartcooking.fragment;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import smartcooking.developer.com.smartcooking.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class RecipeListFragment extends Fragment {
    private static String CATEGORY = "get_category";

    public RecipeListFragment() {
    }

    public static RecipeListFragment newInstance(int category) {
        RecipeListFragment fragment = new RecipeListFragment();
        Bundle args = new Bundle();
        args.putInt(CATEGORY, category);
        fragment.setArguments(args);
        return fragment;
    }

    public int getCategory() {
        if (getArguments() != null) {
            return getArguments().getInt(CATEGORY, -1);
        }
        return -1;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_recipe_list, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        TextView textView_temp = getActivity().findViewById(R.id.textView_temp);
        String s = " " + getCategory() + " ";
        textView_temp.setText(s);
    }

}
