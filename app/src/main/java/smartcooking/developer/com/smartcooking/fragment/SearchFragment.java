package smartcooking.developer.com.smartcooking.fragment;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;

import java.util.List;
import java.util.Locale;

import smartcooking.developer.com.smartcooking.R;
import smartcooking.developer.com.smartcooking.activity.MainActivity;
import smartcooking.developer.com.smartcooking.db.OperationsDb;
import smartcooking.developer.com.smartcooking.db.Recipe.Recipe;
import smartcooking.developer.com.smartcooking.utils.MyAdapter;

public class SearchFragment extends Fragment implements AdapterView.OnItemClickListener {

    private EditText recipe_name_search;
    private Button cleat_btn;
    private MyAdapter adapter;
    private List<Recipe> recipeList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View result = inflater.inflate(R.layout.fragment_search, container, false);

        SQLiteDatabase database = ((MainActivity) getActivity()).getDatabase();
        recipeList = OperationsDb.selectAllRecipes(database);

        RecyclerView list = result.findViewById(R.id.list_recipes_search);
        recipe_name_search = result.findViewById(R.id.name_search_edittext);

        cleat_btn = result.findViewById(R.id.clear_search_btn);
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

        adapter = new MyAdapter(recipeList, this, getContext());
        list.setAdapter(adapter);
        list.setHasFixedSize(true);
        list.setLayoutManager(new LinearLayoutManager(getActivity()));

        // Enabling Search Filter
        recipe_name_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                if (!recipe_name_search.getText().toString().isEmpty()) { //if edittext include text
                    cleat_btn.setVisibility(View.VISIBLE);
                } else { //not include text
                    cleat_btn.setVisibility(View.GONE);
                    // When user changed the Text
                }
                String text = cs.toString().toLowerCase(Locale.getDefault());
                adapter.filter(text);
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

        return result;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Recipe recipe = recipeList.get(position);
        RecipeFragment recipeFragment = RecipeFragment.newInstance(recipe.getId());
        FragmentTransaction ft = getActivity().getFragmentManager().beginTransaction().addToBackStack("SEARCH").setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.replace(R.id.fragment, recipeFragment).commit();
    }

    @Override
    public void onResume() {
        super.onResume();

        SQLiteDatabase database = ((MainActivity) getActivity()).getDatabase();
        recipeList = OperationsDb.selectAllRecipes(database);

        String text = recipe_name_search.getText().toString().toLowerCase(Locale.getDefault());

        RecyclerView list = getActivity().findViewById(R.id.list_recipes_search);
        adapter = new MyAdapter(recipeList, this, getContext());
        list.setAdapter(adapter);
        list.setHasFixedSize(true);
        list.setLayoutManager(new LinearLayoutManager(getActivity()));

        if (!text.isEmpty())
            adapter.filter(text);
    }
}
