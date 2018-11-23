package smartcooking.developer.com.smartcooking.fragment;

import android.app.Fragment;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import smartcooking.developer.com.smartcooking.R;
import smartcooking.developer.com.smartcooking.activity.MainActivity;
import smartcooking.developer.com.smartcooking.db.OperationsDb;

public class MainFragment extends Fragment {

    private ArrayList<Spinner> spinners = new ArrayList<>(5);

    public MainFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View result = inflater.inflate(R.layout.fragment_main, container, false);

        List<String> ingredientsArray = OperationsDb.selectAllIngredientsStrings(((MainActivity) getActivity()).getDatabase());

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                getActivity(), android.R.layout.simple_spinner_item, ingredientsArray);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinners.add(0, (Spinner) result.findViewById(R.id.ingr_spinner1));
        /*spinners.add(1,(Spinner)result.findViewById(R.id.ingr_spinner2));
        spinners.add(2,(Spinner)result.findViewById(R.id.ingr_spinner3));
        spinners.add(3,(Spinner)result.findViewById(R.id.ingr_spinner4));
        spinners.add(4,(Spinner)result.findViewById(R.id.ingr_spinner5));*/

        for (int i = 0; i < 1; i++) {
            spinners.get(i).setAdapter(adapter);
            spinners.get(i).setPrompt("Ingrediente");
        }

        Button search_button = result.findViewById(R.id.search_btn);

        Typeface type = ResourcesCompat.getFont(getContext(), R.font.montserrat);
        search_button.setTypeface(type);


        /*String selected = sItems.getSelectedItem().toString();
        if (selected.equals("what ever the option was")) {
        }*/

        return result;
    }
}
