package smartcooking.developer.com.smartcooking.utils;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.JsonReader;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import smartcooking.developer.com.smartcooking.db.Ingredient.Ingredient;
import smartcooking.developer.com.smartcooking.db.Recipe.Recipe;
import smartcooking.developer.com.smartcooking.db.Relation.Relations;

public class UpdateRecipesTask extends AsyncTask<String, String, String> {
    private String baseUrl = "https://smartcookies.localtunnel.me/api";

    private String TAG = "MyActivity";

    private int APIVersion;

    private WeakReference<Context> contextRef;

    private boolean error = false;

    private List<Recipe> list_recipes;
    private List<Ingredient> list_ingredients;
    private List<Relations> list_relations;

    public UpdateRecipesTask(Context context) {
        contextRef = new WeakReference<>(context);
        list_recipes = new ArrayList<>();
        list_ingredients = new ArrayList<>();
        list_relations = new ArrayList<>();
    }

    @Override
    protected void onPreExecute() {
    }

    @Override
    protected String doInBackground(String... params) {
        Context c = contextRef.get();
        String PREFS_NAME = "SmartCooking_PrefsName";
        SharedPreferences sharedPreferences = c.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        //getVersion();

        //String version = sharedPreferences.getString("Recipes_Version", "");
        int localVersion = 0;
        /*if (version != null) {
            //localVersion = Integer.parseInt(version);
            localVersion = 0;
        }else{
            error = true;
            return null;
        }*/

        if (localVersion < APIVersion) {
            /*if(!error)
                getRecipes();

            if(!error)
                getIngredients();

            if(!error)
                getRelations();

            if(!error){
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(PREFS_NAME, Integer.toString(APIVersion));
                editor.apply();
            }*/
        } else {
            return "Success";
        }

        return null;
    }

    private void getVersion() {
        try {
            URL urlEndpoint = new URL(baseUrl + "/version");

            // Create connection
            HttpsURLConnection myConnection = (HttpsURLConnection) urlEndpoint.openConnection();

            if (myConnection.getResponseCode() == 200) {
                // Success
                InputStream responseBody = myConnection.getInputStream();
                InputStreamReader responseBodyReader =
                        new InputStreamReader(responseBody, "UTF-8");
                JsonReader jsonReader = new JsonReader(responseBodyReader);

                jsonReader.beginObject();
                if (jsonReader.nextName().equals("version")) {
                    APIVersion = Integer.parseInt(jsonReader.nextString());
                }
                jsonReader.endObject();

                Log.d(TAG, "Version: " + APIVersion);
            } else {
                // Error handling code goes here
                error = true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void getRecipes() {
        try {
            URL urlEndpoint = new URL(baseUrl + "/recipes");

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
                            new_recipe.setId(Long.parseLong(jsonReader.nextString()));
                            break;
                        case "nome":
                            new_recipe.setName(jsonReader.nextString());
                            break;
                        case "dificuldade":
                            new_recipe.setDifficulty(Integer.parseInt(jsonReader.nextString()));
                            break;
                        case "tempo":
                            new_recipe.setTime(Integer.parseInt(jsonReader.nextString()));
                            break;
                        case "categoria":
                            new_recipe.setCategory(jsonReader.nextString());
                            break;
                        case "fornecedor":
                            new_recipe.setSupplier(jsonReader.nextString());
                            break;
                        case "imagem":
                            new_recipe.setImage(jsonReader.nextString());
                            break;
                        case "preparacao":
                            new_recipe.setPreparation(Arrays.asList(jsonReader.nextString().split("\\|")));
                            break;
                        case "ingredientes":
                            new_recipe.setIngredients(Arrays.asList(jsonReader.nextString().split("\\|")));
                            break;
                        default:
                            jsonReader.skipValue();
                    }
                }
                jsonReader.endObject();
                list_recipes.add(new_recipe);
            }
            jsonReader.endArray();
        } catch (IOException e) {
            error = true;
        }

        for (int i = 0; i < list_recipes.size(); i++) {
            System.out.println(list_recipes.get(i));
        }
    }

    private void getIngredients() {
        try {
            URL urlEndpoint = new URL(baseUrl + "/ingredients");

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
                            new_ingredient.setId(Long.parseLong(jsonReader.nextString()));
                            break;
                        case "nome":
                            new_ingredient.setName(jsonReader.nextString());
                            break;
                        default:
                            jsonReader.skipValue();
                    }
                }
                jsonReader.endObject();
                list_ingredients.add(new_ingredient);
            }
            jsonReader.endArray();
        } catch (IOException e) {
            error = true;
        }

        for (int i = 0; i < list_ingredients.size(); i++) {
            System.out.println(list_ingredients.get(i));
        }
    }

    private void getRelations() {
        try {
            URL urlEndpoint = new URL(baseUrl + "/relations");

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
                            new_relation.setId_recipe(Long.parseLong(jsonReader.nextString()));
                            break;
                        case "id_ingredients":
                            new_relation.setId_ingredient(Long.parseLong(jsonReader.nextString()));
                            break;
                        default:
                            jsonReader.skipValue();
                    }
                }
                jsonReader.endObject();
                list_relations.add(new_relation);
            }
            jsonReader.endArray();
        } catch (IOException e) {
            error = true;
        }

        for (int i = 0; i < list_relations.size(); i++) {
            System.out.println(list_relations.get(i));
        }
    }

    @Override
    protected void onPostExecute(String s) {

    }

    public boolean isError() {
        return error;
    }
}
