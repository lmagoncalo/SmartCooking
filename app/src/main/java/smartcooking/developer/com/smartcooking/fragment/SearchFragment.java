package smartcooking.developer.com.smartcooking.fragment;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import smartcooking.developer.com.smartcooking.R;
import smartcooking.developer.com.smartcooking.utils.MyAdapter;
import smartcooking.developer.com.smartcooking.utils.Recipe;

public class SearchFragment extends Fragment {

    EditText recipe_name_search;
    Button cleat_btn;
    MyAdapter adapter;

    public SearchFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        List<Recipe> recipeList = getRecipes();

        ListView list = getActivity().findViewById(R.id.list_recipes);
        recipe_name_search = getActivity().findViewById(R.id.name_search_edittext);

        cleat_btn = getActivity().findViewById(R.id.clear_search_btn);
        cleat_btn.setVisibility(View.GONE);
        cleat_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (recipe_name_search.getText().length() != 0) {
                    recipe_name_search.setText("");
                    InputMethodManager inputManager = (InputMethodManager)
                            getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

                    if (inputManager != null) {
                        if (getActivity().getCurrentFocus() != null) {
                            inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
                                    InputMethodManager.HIDE_NOT_ALWAYS);
                        }
                    }
                    recipe_name_search.clearFocus();
                }
            }
        });

        adapter = new MyAdapter(recipeList, getActivity(), getContext());
        list.setAdapter(adapter);

        // Enabling Search Filter
        recipe_name_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                if (!recipe_name_search.getText().toString().equals("")) { //if edittext include text
                    cleat_btn.setVisibility(View.VISIBLE);
                    String text = cs.toString().toLowerCase(Locale.getDefault());
                    adapter.filter(text);
                } else { //not include text
                    cleat_btn.setVisibility(View.GONE);
                    // When user changed the Text
                }
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            @Override
            public void afterTextChanged(Editable arg0) {
            }
        });

        recipe_name_search.setOnKeyListener(new OnKeyListener() {
            public boolean onKey(View view, int keyCode, KeyEvent keyevent) {
                //If the keyevent is a key-down event on the "enter" button
                if ((keyevent.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    InputMethodManager inputManager = (InputMethodManager)
                            getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

                    inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
                    recipe_name_search.clearFocus();
                    return true;
                }
                return false;
            }
        });
    }

    public List<Recipe> getRecipes() {
        List<Recipe> recipes = new ArrayList<>();
        recipes.add(new Recipe(1, "Tostas de Frango no Forno", 3, "https://smartcookingapp.files.wordpress.com/2015/10/receita_pgi.jpg"));
        recipes.add(new Recipe(2, "Salsichas com Queijo e Fiambre", 3, "https://smartcookingapp.files.wordpress.com/2015/10/sals.jpg"));
        recipes.add(new Recipe(3, "Esparguete com atum e Rucula", 2, "http://4.bp.blogspot.com/-OzBpI4GwzQM/VpbopX-mXkI/AAAAAAAAEtM/S-J30fCqndY/s1600/espaguete-atum-rucula.jpg"));
        recipes.add(new Recipe(4, "Bolo da Caneca", 1, "https://smartcookingapp.files.wordpress.com/2015/11/caneca.jpg"));
        recipes.add(new Recipe(5, "Tarte de Limão em Copo", 4, "https://smartcookingapp.files.wordpress.com/2015/11/tartelimaocopo.jpg"));
        recipes.add(new Recipe(6, "Sopa de Peixe", 4, "https://smartcookingapp.files.wordpress.com/2015/11/sopapeixe1.jpg"));
        recipes.add(new Recipe(7, "Alheira com Batata Frita e Ovo", 2, "https://smartcookingapp.files.wordpress.com/2015/11/alheira-com-batata-frita-grelos-cozidos-e-ovo-estrelado51.jpg"));
        recipes.add(new Recipe(8, "Salmão Grelhado", 3, "https://smartcookingapp.files.wordpress.com/2015/11/salmao-batata.jpg"));
        recipes.add(new Recipe(9, "Esparguete à Bolonhesa", 3, "https://smartcookingapp.files.wordpress.com/2015/11/esparguete_bolonhesa.jpg"));
        recipes.add(new Recipe(10, "Peixe à Algarvia", 4, "https://smartcookingapp.files.wordpress.com/2015/12/peixe_sopa_peixe.jpg"));

        return recipes;
    }
}
