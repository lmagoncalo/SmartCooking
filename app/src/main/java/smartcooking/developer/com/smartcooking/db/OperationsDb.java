package smartcooking.developer.com.smartcooking.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Locale;

import smartcooking.developer.com.smartcooking.db.Ingredient.Ingredient;
import smartcooking.developer.com.smartcooking.db.Ingredient.IngredientsCursorWrapper;
import smartcooking.developer.com.smartcooking.db.Recipe.Recipe;
import smartcooking.developer.com.smartcooking.db.Recipe.RecipesCursorWrapper;
import smartcooking.developer.com.smartcooking.db.Relation.Relations;
// import smartcooking.developer.com.smartcooking.db.Relation.RelationsCursorWrapper;

public class OperationsDb {
    /*** CLASSE QUE VAI TER TODAS AS OPERAÇÕES DA BASE DE DADOS, PARA ISTO NÃO SE MISTURAR COM O RESTO DO CÓDIGO DA APP ***/

    public static boolean recipeControlledInsert(Recipe new_recipe, SQLiteDatabase mDatabase) {
        Recipe old_recipe = recipeExists(new_recipe, mDatabase);
        if (old_recipe != null) {
            // if there's a recipe with the same ID
            if (!old_recipe.getHash().equals(new_recipe.getHash())) {
                // if the hash is different, updates the recipe
                return updateRecipe(new_recipe, mDatabase);
            }
            return true;
        } else {
            // if the recipe doesn't exist in the database, inserts the new database
            return insertRecipe(new_recipe, mDatabase)!=-1;
        }
    }


    private static Recipe recipeExists(Recipe recipe, SQLiteDatabase mDatabase) {
        // here, we don't put the ID as an argument of "selectionArgs" because it would be converted to a String
        // and the ID is a 'serial'

        // check if there's any recipe with the same ID as the argument recipe

        Cursor c = mDatabase.query(DatabaseScheme.RecipesTable.NAME,
                null,
                String.format(Locale.getDefault(), DatabaseScheme.RecipesTable.Cols.ID + " = %d", recipe.getId()),
                null,
                null,
                null,
                null);

        RecipesCursorWrapper cursor = new RecipesCursorWrapper(c);

        // this method returns 'false' if the cursor is empty
        if (!cursor.moveToFirst()) {
            cursor.close();
            c.close();
            return null;
        }

        Recipe r = cursor.getRecipe();

        cursor.close();
        c.close();

        return r;
    }

    private static long insertRecipe(Recipe recipe, SQLiteDatabase mDatabase) {
        ContentValues values = getContentValuesRecipes(recipe);
        return mDatabase.insert(DatabaseScheme.RecipesTable.NAME, null, values);

        // the "insert" returns '-1' if there was any error
    }

    private static boolean updateRecipe(Recipe recipe, SQLiteDatabase mDatabase) {
        // here, we don't put the ID as an argument of "selectionArgs" because it would be converted to a String
        // and the ID is a 'serial'

        ContentValues values = getContentValuesRecipes(recipe);
        return mDatabase.update(DatabaseScheme.RecipesTable.NAME, values, String.format(Locale.getDefault(), DatabaseScheme.RecipesTable.Cols.ID + " = %d", recipe.getId()), null) != 0;

        // the "update" returns the number of affected rows, which means that if it returns '0', there might be some problem
    }

    public static boolean changeRecipeFavorite(Recipe recipe, SQLiteDatabase mDatabase) {
        recipe.setFavorite(!recipe.isFavorite());
        return updateRecipe(recipe, mDatabase);
    }

    public static long insertIngredient(Ingredient ingredient, SQLiteDatabase mDatabase) {
        ContentValues values = getContentValuesIngredients(ingredient);
        return mDatabase.insert(DatabaseScheme.IngredientsTable.NAME, null, values);

        // the "insert" returns '-1' if there was any error
    }

    /*public static boolean updateIngredient(Ingredient ingredient, SQLiteDatabase mDatabase) {
        // here, we don't put the ID as an argument of "selectionArgs" because it would be converted to a String
        // and the ID is a 'serial'

        ContentValues values = getContentValuesIngredients(ingredient);
        return mDatabase.update(DatabaseScheme.IngredientsTable.NAME, values, String.format(Locale.getDefault(), DatabaseScheme.IngredientsTable.Cols.ID + " = %d", ingredient.getId()), null) != 0;

        // the "update" returns the number of affected rows, which means that if it returns '0', there might be some problem
    }*/

    public static long insertRelation(Relations relations, SQLiteDatabase mDatabase) {
        ContentValues values = getContentValuesRelations(relations);
        return mDatabase.insert(DatabaseScheme.RelationsTable.NAME, null, values);

        // the "insert" returns '-1' if there was any error
    }

    public static ArrayList<Recipe> selectRecipeByCategory(String category, SQLiteDatabase mDatabase) {
        ArrayList<Recipe> list = new ArrayList<>();

        // selects all the recipes from a specific category

        Cursor c = mDatabase.query(DatabaseScheme.RecipesTable.NAME,
                null,
                "LOWER(" + DatabaseScheme.RecipesTable.Cols.CATEGORY + ") = ?",
                new String[]{category.toLowerCase()},
                null,
                null,
                DatabaseScheme.RecipesTable.Cols.ID + " ASC");

        RecipesCursorWrapper cursor = new RecipesCursorWrapper(c);

        // this method returns 'false' if the cursor is empty
        if (!cursor.moveToFirst()) {
            cursor.close();
            c.close();
            return list;
        }

        // while theres more elements in the cursor
        while (!cursor.isAfterLast()) {
            Recipe recipe = cursor.getRecipe();
            list.add(recipe);
            cursor.moveToNext();
        }

        cursor.close();
        c.close();

        return list;
    }

    public static ArrayList<Recipe> selectAllRecipes(SQLiteDatabase mDatabase) {
        ArrayList<Recipe> list = new ArrayList<>();

        // select all recipes

        Cursor c = mDatabase.query(DatabaseScheme.RecipesTable.NAME,
                null,
                null,
                null,
                null,
                null,
                DatabaseScheme.RecipesTable.Cols.ID + " ASC");

        RecipesCursorWrapper cursor = new RecipesCursorWrapper(c);

        // this method returns 'false' if the cursor is empty
        if (!cursor.moveToFirst()) {
            cursor.close();
            c.close();
            return list;
        }

        // while theres more elements in the cursor
        while (!cursor.isAfterLast()) {
            Recipe recipe = cursor.getRecipe();
            list.add(recipe);
            cursor.moveToNext();


            //System.out.println("update public.\"Recipes\" set hash = '" + byteArray2Hex(recipe.toString().getBytes()) + "' where id= " + recipe.getId() +";");

        }

        cursor.close();
        c.close();

        return list;
    }


    /*private static final char[] hex = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
    private static String byteArray2Hex(byte[] bytes) {
        StringBuilder sb = new StringBuilder(bytes.length * 2);
        for (final byte b : bytes) {
            sb.append(hex[(b & 0xF0) >> 4]);
            sb.append(hex[b & 0x0F]);
        }
        return sb.toString();
    }*/

    /*public static ArrayList<Relations> selectAllRelations(SQLiteDatabase mDatabase) {
        ArrayList<Relations> list = new ArrayList<>();

        Cursor c = mDatabase.query(DatabaseScheme.RelationsTable.NAME,
                null,
                null,
                null,
                null,
                null,
                null);

        RelationsCursorWrapper cursor = new RelationsCursorWrapper(c);

        if (!cursor.moveToFirst()) {
            cursor.close();
            c.close();
            return list;
        }

        while (!cursor.isAfterLast()) {
            Relations relations = cursor.getRelations();
            list.add(relations);
            cursor.moveToNext();
        }

        cursor.close();
        c.close();

        return list;
    }*/

    public static ArrayList<Ingredient> selectAllIngredients(SQLiteDatabase mDatabase) {
        ArrayList<Ingredient> list = new ArrayList<>();

        // selects all ingredients from the database
        Cursor c = mDatabase.query(DatabaseScheme.IngredientsTable.NAME,
                null,
                null,
                null,
                null,
                null,
                DatabaseScheme.IngredientsTable.Cols.ID + " ASC");

        IngredientsCursorWrapper cursor = new IngredientsCursorWrapper(c);

        // this method returns 'false' if the cursor is empty
        if (!cursor.moveToFirst()) {
            cursor.close();
            c.close();
            return list;
        }

        // while theres more elements in the cursor
        while (!cursor.isAfterLast()) {
            Ingredient ingredient = cursor.getIngredient();
            list.add(ingredient);
            cursor.moveToNext();
        }

        cursor.close();
        c.close();

        return list;
    }

    /*public static ArrayList<String> selectAllIngredientsStrings(SQLiteDatabase mDatabase) {
        ArrayList<String> list = new ArrayList<>();

        Cursor c = mDatabase.query(DatabaseScheme.IngredientsTable.NAME,
                null,
                null,
                null,
                null,
                null,
                DatabaseScheme.IngredientsTable.Cols.NAME + " ASC");

        IngredientsCursorWrapper cursor = new IngredientsCursorWrapper(c);

        if (!cursor.moveToFirst()) {
            cursor.close();
            c.close();
            return list;
        }

        while (!cursor.isAfterLast()) {
            Ingredient ingredient = cursor.getIngredient();
            list.add(ingredient.getName());
            cursor.moveToNext();
        }

        cursor.close();
        c.close();

        return list;
    }*/

    public static ArrayList<Recipe> selectFavoriteRecipes(SQLiteDatabase mDatabase) {
        ArrayList<Recipe> list = new ArrayList<>();

        // select favorite recipes from the database

        Cursor c = mDatabase.query(DatabaseScheme.RecipesTable.NAME,
                null,
                DatabaseScheme.RecipesTable.Cols.FAVORITE + " = 1",
                null,
                null,
                null,
                DatabaseScheme.RecipesTable.Cols.ID + " ASC");

        RecipesCursorWrapper cursor = new RecipesCursorWrapper(c);

        // this method returns 'false' if the cursor is empty
        if (!cursor.moveToFirst()) {
            cursor.close();
            c.close();
            return list;
        }

        // while theres more elements in the cursor
        while (!cursor.isAfterLast()) {
            Recipe recipe = cursor.getRecipe();
            list.add(recipe);
            cursor.moveToNext();
        }


        cursor.close();
        c.close();

        return list;
    }


    private static ContentValues getContentValuesRecipes(Recipe recipe) {

        // ContentValues work as a HashMap:  KEY (column name)  +  VALUE (value of that column)

        ContentValues values = new ContentValues();
        values.put(DatabaseScheme.RecipesTable.Cols.ID, recipe.getId());
        values.put(DatabaseScheme.RecipesTable.Cols.NAME, recipe.getName());
        values.put(DatabaseScheme.RecipesTable.Cols.DIFFICULTY, recipe.getDifficulty());
        values.put(DatabaseScheme.RecipesTable.Cols.TIME, recipe.getTime());
        values.put(DatabaseScheme.RecipesTable.Cols.CATEGORY, recipe.getCategory());
        values.put(DatabaseScheme.RecipesTable.Cols.SUPPLIER, recipe.getSupplier());
        values.put(DatabaseScheme.RecipesTable.Cols.PREPARATION, recipe.getPreparationStringToDatabase());
        values.put(DatabaseScheme.RecipesTable.Cols.IMAGE, recipe.getImage());
        values.put(DatabaseScheme.RecipesTable.Cols.INGREDIENTS, recipe.getIngredientsStringToDatabase());
        values.put(DatabaseScheme.RecipesTable.Cols.FAVORITE, recipe.isFavorite() ? 1 : 0);
        values.put(DatabaseScheme.RecipesTable.Cols.HASH, recipe.getHash());

        return values;
    }

    private static ContentValues getContentValuesIngredients(Ingredient ingredient) {

        // ContentValues work as a HashMap:  KEY (column name)  +  VALUE (value of that column)

        ContentValues values = new ContentValues();
        values.put(DatabaseScheme.IngredientsTable.Cols.ID, ingredient.getId());
        values.put(DatabaseScheme.IngredientsTable.Cols.NAME, ingredient.getName());

        return values;
    }

    private static ContentValues getContentValuesRelations(Relations relations) {

        // ContentValues work as a HashMap:  KEY (column name)  +  VALUE (value of that column)

        ContentValues values = new ContentValues();
        values.put(DatabaseScheme.RelationsTable.Cols.ID_RECIPE, relations.getId_recipe());
        values.put(DatabaseScheme.RelationsTable.Cols.ID_INGREDIENT, relations.getId_ingredient());

        return values;
    }

    public static ArrayList<Recipe> selectRecipesByIngredients(ArrayList<Ingredient> ingrs, SQLiteDatabase mDatabase) {

        // select all the recipes that use the selected ingredients and sort them

        ArrayList<Recipe> list = new ArrayList<>();
        String ingredientsQueryStr = getIngredientArrayListToString(ingrs);


        /*

        A  =  ingredients in common between the recipe and the searched_list
        B  =  ingredients of the recipe that were not selected
        C  =  total number of ingredients of the recipe  (A + B)

           A  DESC, B  ASC, time ASC, difficulty ASC, ID ASC

        */

        Cursor c = mDatabase.rawQuery(
                "SELECT *, " +
                           "( " +
                                "SELECT COUNT(*) " +
                                "FROM " + DatabaseScheme.RelationsTable.NAME + " AS rel " +
                                "WHERE rel.id_recipe = r.id  AND  rel.id_ingredient in " +  ingredientsQueryStr + " ) as A, " +
                           "( " +
                                "SELECT COUNT(*) " +
                                "FROM " + DatabaseScheme.RelationsTable.NAME + " AS rel " +
                                "WHERE rel.id_recipe = r.id  AND  rel.id_ingredient NOT in " +  ingredientsQueryStr + " ) as B " +
                    "FROM " + DatabaseScheme.RecipesTable.NAME + " as r " +
                    "WHERE A > 0 " +
                    "ORDER BY A DESC, " +
                          "B ASC, " +
                          "r." +DatabaseScheme.RecipesTable.Cols.TIME + " ASC, " +
                          "r." + DatabaseScheme.RecipesTable.Cols.DIFFICULTY + " ASC, " +
                          "r." + DatabaseScheme.RecipesTable.Cols.ID + " ASC"
        , new String[]{});

        /*
            In this query, we didn´t use the question mark '?' because what would replace it, would
            [ (1, 2, 3), for example] be converted to a string, which means the query would be like:

                SELECT *
                FROM Relations
                WHERE id_ingredient IN  "(1, 2, 3)"
                ORDER BY id_recipe ASC

           INSTEAD OF:
                SELECT *
                FROM Relations
                WHERE id_ingredient IN  (1, 2, 3)
                ORDER BY id_recipe ASC

           (the difference is in the quotation marks of the WHERE clause)


        */

        RecipesCursorWrapper cursor = new RecipesCursorWrapper(c);

        // this method returns 'false' if the cursor is empty
        if (!cursor.moveToFirst()) {
            cursor.close();
            c.close();
            return list;
        }

        // while theres more elements in the cursor
        while (!cursor.isAfterLast()) {
            Recipe recipe = cursor.getRecipe();

            /*
                check is the current recipe already exists in the list, because 1 recipe might have more than 1 on the searched
                ingredients and because of that it would be added multiple times
            */
            if (!list.contains(recipe)) {
                list.add(recipe);
            }
            cursor.moveToNext();
        }

        cursor.close();
        c.close();
        return list;
    }

    public static Recipe selectRecipeByID(long ID, SQLiteDatabase mDatabase) {
        Recipe recipe;

        Cursor c = mDatabase.query(DatabaseScheme.RecipesTable.NAME,
                null,
                String.format(Locale.getDefault(), DatabaseScheme.RecipesTable.Cols.ID + " = %d", ID),
                null,
                null,
                null,
                null);

        RecipesCursorWrapper cursor = new RecipesCursorWrapper(c);

        // this method returns 'false' if the cursor is empty
        if (!cursor.moveToFirst()) {
            cursor.close();
            c.close();
            return null;
        }

        recipe = cursor.getRecipe();

        cursor.close();
        c.close();
        return recipe;
    }

    private static String getIngredientArrayListToString(ArrayList<Ingredient> list) {
        StringBuilder res = new StringBuilder("(");

        // convert a list of reciped ID's to a string

        for (Ingredient i : list) {
            if (res.length() > 1) {
                res.append(", ");
            }
            res.append(i.getId());
        }
        res.append(")");

        return res.toString();
    }

    /*public static void turnOnForeignKeys(SQLiteDatabase mDatabase) {
        mDatabase.execSQL("PRAGMA foreign_keys = ON;");
    }*/
}
