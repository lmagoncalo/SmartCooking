package smartcooking.developer.com.smartcooking.utils;


import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.JsonReader;
import android.view.View;
import android.widget.ProgressBar;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import smartcooking.developer.com.smartcooking.R;
import smartcooking.developer.com.smartcooking.db.DatabaseBaseHelper;
import smartcooking.developer.com.smartcooking.db.Ingredient.Ingredient;
import smartcooking.developer.com.smartcooking.db.OperationsDb;
import smartcooking.developer.com.smartcooking.db.Recipe.Recipe;
import smartcooking.developer.com.smartcooking.db.Relation.Relations;
import smartcooking.developer.com.smartcooking.fragment.MainFragment;

public class UpdateRecipesTask extends AsyncTask<Integer, Integer, String> {
    private final String baseUrl = "http://smartcooking-api.herokuapp.com/api/";

    private int APIVersion;

    /*
        We use a weak reference because it is not strong enough to keep the object in memory, so
        when the Activity stops existing it can be collected. Therefore no memory leaks will happen
    */
    private final WeakReference<Context> contextRef;
    private final WeakReference<Activity> activityRef;
    private final WeakReference<ProgressBar> progressBar;
    private final Fragment fragment;

    // this variable is used to check if there was any problem with any HTTP request (API)
    // if there's any error, the app won't make any more HTTP requests (API)
    private boolean error = false;

    private final List<Recipe> list_recipes;
    private final List<Ingredient> list_ingredients;
    private final List<Relations> list_relations;
    private Integer count = 1;

    /*
        the arguments are:
            1 --> "Context" from the activity for the Shared Preferences
            2 --> "ProgressBar" for the rotating animation while stuff is being done
            3 --> "Activity" for the "BottomNavigationBar" reference to make it visible, once the SplashScreen is completed
            4 --> "Fragment" to access the "FragmentManager" to replace the current fragment
    */
    public UpdateRecipesTask(Context context, ProgressBar progressBar, Activity activityRef, Fragment fragment) {
        this.list_recipes = new ArrayList<>();
        this.list_ingredients = new ArrayList<>();
        this.list_relations = new ArrayList<>();
        this.contextRef = new WeakReference<>(context);
        this.activityRef = new WeakReference<>(activityRef);
        this.progressBar = new WeakReference<>(progressBar);
        this.fragment = fragment;
    }

    @Override
    protected String doInBackground(Integer... params) {
        Context c = contextRef.get();
        String PREFS_NAME = "SmartCooking_PrefsName";
        SharedPreferences sharedPreferences = c.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        // increment the progress of the AsyncTask
        publishProgress(count++);

        // get the versino from the remote database (from the API)
        getVersion();

        publishProgress(count++);

        String version = sharedPreferences.getString(PREFS_NAME, "-1");
        int localVersion = 0;
        if (version != null) {
            localVersion = Integer.parseInt(version);
        }

        publishProgress(count++);

        // if there's a local version of the database, but there was some problem with the connection with the API
        // "Success": because we can still use the app with the local version of the database; opens MainFragment
        if (localVersion != -1 && error) {
            return "Success";
        } else if (localVersion == -1 && error) {
            // if there ISN'T a local version of the database and there was some problem with the connection with the API
            // "First_Time": displays error because there's nothing to work with, on the local database
            return "First_Time";
        }

        // if the localVersion is lower than the APIVersion
        if (localVersion < APIVersion) {
            // get recipes from the API
            getRecipes();

            publishProgress(count++);

            if (!error) {
                // if there wasn't any problem with the connection with the API so far

                // get the ingredients list from the API
                getIngredients();
            } else {
                // "Net_Error": displays error because there was a problem with the API connection
                return "Net_Error";
            }

            publishProgress(count++);


            if (!error) {
                // if there wasn't any problem with the connection with the API so far

                // get the relatinos list from the API
                getRelations();
            } else {
                // "Net_Error": displays error because there was a problem with the API connection
                return "Net_Error";
            }

            publishProgress(count++);


            if(!error){
                // if there wasn't any problem with the connection with the API so far

                if(updateDatabase()){
                    // saves the APIVersion as the new local database version
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(PREFS_NAME, Integer.toString(APIVersion));
                    editor.apply();
                }

            } else {
                return "Net_Error";
            }

            publishProgress(count++);

            return "Success";
        } else {
            return "Success";
        }
    }

    private void getVersion() {
        try {
            // URL endpoint to get the version
            URL urlEndpoint = new URL(baseUrl + "version/");

            // Create HTTP connection
            HttpURLConnection myConnection = (HttpURLConnection) urlEndpoint.openConnection();
            myConnection.setConnectTimeout(5000);

            if (myConnection.getResponseCode() == 200) {
                // If the result is success
                InputStream responseBody = myConnection.getInputStream();
                InputStreamReader responseBodyReader =
                        new InputStreamReader(responseBody, "UTF-8");

                // get JSON object from the response
                JsonReader jsonReader = new JsonReader(responseBodyReader);

                // read all the values from the JSON object
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
        } catch (SocketTimeoutException e) {
            error = true;
        } catch (IOException e) {
            e.printStackTrace();
            error = true;
        }
    }

    private void getRecipes() {
        try {
            // URL endpoint to get the recipes
            URL urlEndpoint = new URL(baseUrl + "recipes/");

            // Create HTTP connection
            HttpURLConnection myConnection = (HttpURLConnection) urlEndpoint.openConnection();

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
            error = true;
        }
    }

    private void parseJsonRecipes(JsonReader jsonReader) {
        try {
            // read all the values from the JSON object
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
                // add the new recipe to the list
                list_recipes.add(new_recipe);
            }
            jsonReader.endArray();
        } catch (IOException e) {
            error = true;
        }
    }

    private void getIngredients() {
        try {
            // URL endpoint to get the ingredients
            URL urlEndpoint = new URL(baseUrl + "ingredients/");

            // Create HTTP connection
            HttpURLConnection myConnection = (HttpURLConnection) urlEndpoint.openConnection();

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
            error = true;
        }
    }

    private void parseJsonIngredients(JsonReader jsonReader) {
        try {
            // read all the values from the JSON object
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
                // add the new ingredient to the list
                list_ingredients.add(new_ingredient);
            }
            jsonReader.endArray();
        } catch (IOException e) {
            error = true;
        }
    }

    private void getRelations() {
        try {
            // URL endpoint to get the relations
            URL urlEndpoint = new URL(baseUrl + "relations/");

            // Create HTTP connection
            HttpURLConnection myConnection = (HttpURLConnection) urlEndpoint.openConnection();

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
            error = true;
        }
    }

    private void parseJsonRelations(JsonReader jsonReader) {
        try {
            // read all the values from the JSON object
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
                // add the new relation to the list
                list_relations.add(new_relation);
            }
            jsonReader.endArray();
        } catch (IOException e) {
            error = true;
        }
    }

    private boolean updateDatabase() {

        // update the database; if there's any error, stop the updates

        SQLiteDatabase database = new DatabaseBaseHelper(contextRef.get()).getWritableDatabase();
        boolean flag = true;

        for (Recipe r : list_recipes) {
            if(!OperationsDb.recipeControlledInsert(r, database)){
                flag = false;
                break;
            }
        }

        if(flag){
            for (Ingredient i : list_ingredients) {
                if(OperationsDb.insertIngredient(i, database)==-1){
                    flag = false;
                    break;
                }
            }
        }

        if(flag){
            for (Relations rs : list_relations) {
                if(OperationsDb.insertRelation(rs, database)==-1){
                    flag = false;
                    break;
                }
            }
        }

        return flag;

    }

    @Override
    protected void onPostExecute(String result) {
        progressBar.get().setVisibility(View.GONE);
        switch (result) {
            case "Success":
                // if "success", initialize the MainFragment
                MainFragment mainFragment = new MainFragment();
                FragmentTransaction ft;
                if (fragment.getFragmentManager() != null) {
                    ft = fragment.getFragmentManager().beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                    ft.replace(R.id.fragment, mainFragment).commit();
                }
                BottomNavigationView navigation = activityRef.get().findViewById(R.id.navigation);
                navigation.setVisibility(View.VISIBLE);
                break;
            case "First_Time":
                // if "First_Time", displays an error and closes the app
                crash("É necessário estar ligado à Internet na primeira vez que abre a aplicação.");
                break;
            case "Net_Error":
                // if "Net_Error", displays an error and closes the app
                crash("Ocorreu algum erro inesperado. Confirme a sua ligação à Internet.");
                break;
        }
    }

    @Override
    protected void onPreExecute() {
        // setup the "progressBar"
        progressBar.get().setProgress(0);
        progressBar.get().setMax(6);
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);

        // to make the rotating animation of the progress bar
        progressBar.get().setProgress(values[0]);
    }

    private void crash(String tipo) {
        // create dialog
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(contextRef.get());

        // set title
        alertDialogBuilder.setTitle("Erro");

        // set dialog message
        alertDialogBuilder
                .setMessage(tipo)
                .setCancelable(false)
                .setPositiveButton("Fechar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // if this button is clicked, close the activity
                        activityRef.get().finish();
                    }
                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }
}
