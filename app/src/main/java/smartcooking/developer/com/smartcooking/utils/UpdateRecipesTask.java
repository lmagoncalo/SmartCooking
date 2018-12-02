package smartcooking.developer.com.smartcooking.utils;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.design.widget.BottomNavigationView;
import android.util.JsonReader;
import android.view.View;
import android.widget.ProgressBar;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import smartcooking.developer.com.smartcooking.R;
import smartcooking.developer.com.smartcooking.db.DatabaseBaseHelper;
import smartcooking.developer.com.smartcooking.db.Ingredient.Ingredient;
import smartcooking.developer.com.smartcooking.db.OperationsDb;
import smartcooking.developer.com.smartcooking.db.Recipe.Recipe;
import smartcooking.developer.com.smartcooking.db.Relation.Relations;
import smartcooking.developer.com.smartcooking.fragment.MainFragment;

public class UpdateRecipesTask extends AsyncTask<Integer, Integer, String> {
    private String baseUrl = "https://smartcookies.localtunnel.me/api/";

    private String TAG = "MyActivity";

    private int APIVersion;

    private WeakReference<Context> contextRef;
    private Activity act;

    private boolean error = false;

    private List<Recipe> list_recipes;
    private List<Ingredient> list_ingredients;
    private List<Relations> list_relations;
    private ProgressBar progressBar;
    private Integer count = 1;

    public UpdateRecipesTask(Context context, ProgressBar progressBar, Activity act) {
        contextRef = new WeakReference<>(context);
        list_recipes = new ArrayList<>();
        list_ingredients = new ArrayList<>();
        list_relations = new ArrayList<>();
        this.progressBar = progressBar;
        this.act = act;
    }

    @Override
    protected String doInBackground(Integer... params) {
        Context c = contextRef.get();
        String PREFS_NAME = "SmartCooking_PrefsName";
        SharedPreferences sharedPreferences = c.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        getVersion();

        String version = sharedPreferences.getString("Recipes_Version", "-1");
        int localVersion;
        if (version != null) {
            localVersion = Integer.parseInt(version);
        } else {
            localVersion = -1;
        }


        if (localVersion < APIVersion) {
            if (!error) {
                getRecipes();
            }

            if (!error) {
                getIngredients();
            }


            if (!error) {
                getRelations();
            }


            if(!error){
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(PREFS_NAME, Integer.toString(APIVersion));
                editor.apply();

                updateDatabase();
            }

        } else {
            return "Success";
        }

        return null;
    }

    private void getVersion() {
        try {
            URL urlEndpoint = new URL(baseUrl + "version/");

            // Create connection
            HttpsURLConnection myConnection = (HttpsURLConnection) urlEndpoint.openConnection();

            if (myConnection.getResponseCode() == 200) {
                // Success
                InputStream responseBody = myConnection.getInputStream();
                InputStreamReader responseBodyReader =
                        new InputStreamReader(responseBody, "UTF-8");
                JsonReader jsonReader = new JsonReader(responseBodyReader);

                jsonReader.beginObject();
                while (jsonReader.hasNext()) {
                    switch (jsonReader.nextName()) {
                        case "version":
                            APIVersion = Integer.parseInt(jsonReader.nextString());
                            break;
                        case "id":
                            jsonReader.nextString();
                            break;
                    }
                }
                jsonReader.endObject();
            } else {
                // Error handling code goes here
                error = true;
            }
        } catch (IOException e) {
            e.printStackTrace();
            error = true;
        }
    }

    private void getRecipes() {
        try {
            URL urlEndpoint = new URL(baseUrl + "recipes/");

            // Create connection
            HttpsURLConnection myConnection = (HttpsURLConnection) urlEndpoint.openConnection();

            if (myConnection.getResponseCode() == 200) {
                // Success
                InputStream responseBody = myConnection.getInputStream();
                InputStreamReader responseBodyReader =
                        new InputStreamReader(responseBody, "UTF-8");
                JsonReader jsonReader = new JsonReader(responseBodyReader);

                parseJsonRecipes(jsonReader);
            } else {
                // Error handling code goes here
                error = true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void parseJsonRecipes(JsonReader jsonReader) {
        try {
            jsonReader.beginArray();
            while (jsonReader.hasNext()) {
                Recipe new_recipe = new Recipe();
                jsonReader.beginObject();
                while (jsonReader.hasNext()) {
                    switch (jsonReader.nextName()) {
                        case "id":
                            new_recipe.setId(jsonReader.nextInt());
                            break;
                        case "name":
                            new_recipe.setName(jsonReader.nextString());
                            break;
                        case "difficulty":
                            new_recipe.setDifficulty(jsonReader.nextInt());
                            break;
                        case "time":
                            new_recipe.setTime(jsonReader.nextInt());
                            break;
                        case "category":
                            new_recipe.setCategory(jsonReader.nextString());
                            break;
                        case "supplier":
                            new_recipe.setSupplier(jsonReader.nextString());
                            break;
                        case "image":
                            new_recipe.setImage(jsonReader.nextString());
                            break;
                        case "preparation":
                            new_recipe.setPreparation(Arrays.asList(jsonReader.nextString().split("\\|")));
                            break;
                        case "ingredients":
                            new_recipe.setIngredients(Arrays.asList(jsonReader.nextString().split("\\|")));
                            break;
                        case "favorite":
                            String fav = jsonReader.nextString();
                            if (fav.equals("False"))
                                new_recipe.setFavorite(false);
                            else
                                new_recipe.setFavorite(true);
                            break;
                        case "hash":
                            new_recipe.setHash(jsonReader.nextString());
                            break;
                        default:
                            jsonReader.skipValue();
                            break;
                    }
                }
                jsonReader.endObject();
                list_recipes.add(new_recipe);
            }
            jsonReader.endArray();
        } catch (IOException e) {
            error = true;
        }
    }

    private void getIngredients() {
        try {
            URL urlEndpoint = new URL(baseUrl + "ingredients/");

            // Create connection
            HttpsURLConnection myConnection = (HttpsURLConnection) urlEndpoint.openConnection();

            if (myConnection.getResponseCode() == 200) {
                // Success
                InputStream responseBody = myConnection.getInputStream();
                InputStreamReader responseBodyReader =
                        new InputStreamReader(responseBody, "UTF-8");
                JsonReader jsonReader = new JsonReader(responseBodyReader);

                parseJsonIngredients(jsonReader);
            } else {
                // Error handling code goes here
                error = true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void parseJsonIngredients(JsonReader jsonReader) {
        try {
            jsonReader.beginArray();
            while (jsonReader.hasNext()) {
                Ingredient new_ingredient = new Ingredient();
                jsonReader.beginObject();
                while (jsonReader.hasNext()) {
                    switch (jsonReader.nextName()) {
                        case "id":
                            new_ingredient.setId(jsonReader.nextLong());
                            break;
                        case "name":
                            new_ingredient.setName(jsonReader.nextString());
                            break;
                        default:
                            jsonReader.skipValue();
                            break;
                    }
                }
                jsonReader.endObject();
                list_ingredients.add(new_ingredient);
            }
            jsonReader.endArray();
        } catch (IOException e) {
            error = true;
        }
    }

    private void getRelations() {
        try {
            URL urlEndpoint = new URL(baseUrl + "relations/");

            // Create connection
            HttpsURLConnection myConnection = (HttpsURLConnection) urlEndpoint.openConnection();

            if (myConnection.getResponseCode() == 200) {
                // Success
                InputStream responseBody = myConnection.getInputStream();
                InputStreamReader responseBodyReader =
                        new InputStreamReader(responseBody, "UTF-8");
                JsonReader jsonReader = new JsonReader(responseBodyReader);

                parseJsonRelations(jsonReader);
            } else {
                // Error handling code goes here
                error = true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void parseJsonRelations(JsonReader jsonReader) {
        try {
            jsonReader.beginArray();
            while (jsonReader.hasNext()) {
                Relations new_relation = new Relations();
                jsonReader.beginObject();
                while (jsonReader.hasNext()) {
                    switch (jsonReader.nextName()) {
                        case "id_recipe":
                            new_relation.setId_recipe(jsonReader.nextLong());
                            break;
                        case "id_ingredient":
                            new_relation.setId_ingredient(jsonReader.nextLong());
                            break;
                        default:
                            jsonReader.skipValue();
                            break;
                    }
                }
                jsonReader.endObject();
                list_relations.add(new_relation);
            }
            jsonReader.endArray();
        } catch (IOException e) {
            error = true;
        }
    }

    private void updateDatabase() {
        SQLiteDatabase database = new DatabaseBaseHelper(contextRef.get()).getWritableDatabase();

        for (Recipe r : list_recipes) {
            OperationsDb.recipeControlledInsert(r, database);
        }

        for (Ingredient i : list_ingredients) {
            OperationsDb.insertIngredient(i, database);
        }

        for (Relations rs : list_relations) {
            OperationsDb.insertRelation(rs, database);
        }
    }

    @Override
    protected void onPostExecute(String result) {
        progressBar.setVisibility(View.GONE);
        if (!isError()) {
            MainFragment mainFragment = new MainFragment();
            act.getFragmentManager().beginTransaction().replace(R.id.fragment, mainFragment).commit();
            BottomNavigationView navigation = act.findViewById(R.id.navigation);
            navigation.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onPreExecute() {
        progressBar.setProgress(0);
        progressBar.setMax(6);
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        if (this.progressBar != null) {
            progressBar.setProgress(values[0]);
        }
    }

    public boolean isError() {
        return error;
    }
}
