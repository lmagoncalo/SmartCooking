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
import smartcooking.developer.com.smartcooking.db.Relation.Relation;
import smartcooking.developer.com.smartcooking.db.Relation.RelationCursorWrapper;

public class OperationsDb {
    /*** CLASSE QUE VAI TER TODAS AS OPERAÇÕES DA BASE DE DADOS, PARA ISTO NÃO SE MISTURAR COM O RESTO DO CÓDIGO DA APP ***/

    public static int recipeControlledInsert(Recipe new_recipe, SQLiteDatabase mDatabase) {
        Recipe old_recipe = recipeExists(new_recipe, mDatabase);
        if (old_recipe != null) {
            // se já existe uma receita com o mesmo ID
            if (old_recipe.getHash().equals(new_recipe.getHash())) {
                return 0;   // a receita foi ignorada porque já existe uma igual na base de dados
            } else {
                // se a receita já existe, mas precisa de update porque o hash é diferente
                if (updateRecipe(new_recipe, mDatabase)) {
                    return 2;   // se correu tudo bem com o update
                }

                return -2;      // se houve um erro com o update
            }
        } else {
            // se a receita ainda não existe na base de dados
            if (insertRecipe(new_recipe, mDatabase)) {
                return 1;       // se correu tudo bem com o insert
            }

            return -1;          // se houve um erro com o insert
        }
    }


    private static Recipe recipeExists(Recipe recipe, SQLiteDatabase mDatabase) {
        // aqui, não meto o ID no argumento "selectionArgs" porque assim ia estar a convertê-lo para String e o ID é um 'serial'

        Cursor c = mDatabase.query(DatabaseScheme.RecipesTable.NAME,
                null,
                String.format(Locale.getDefault(), DatabaseScheme.RecipesTable.Cols.ID + " = %d", recipe.getId()),
                null,
                null,
                null,
                null);

        RecipesCursorWrapper cursor = new RecipesCursorWrapper(c);

        // esta função retorna 'false' se o cursor estiver vazio
        if (!cursor.moveToFirst()) {
            return null;
        }

        return cursor.getRecipe();
    }

    private static boolean insertRecipe(Recipe recipe, SQLiteDatabase mDatabase) {
        ContentValues values = getContentValuesRecipes(recipe);
        return mDatabase.insert(DatabaseScheme.RecipesTable.NAME, null, values) != -1;

        // o "insert" retorna '-1' se houver erro
    }

    private static boolean updateRecipe(Recipe recipe, SQLiteDatabase mDatabase) {
        // aqui, não meto o ID no argumento "selectionArgs" porque assim ia estar a convertê-lo para String e o ID é um 'serial'

        ContentValues values = getContentValuesRecipes(recipe);
        return mDatabase.update(DatabaseScheme.RecipesTable.NAME, values, String.format(Locale.getDefault(), DatabaseScheme.RecipesTable.Cols.ID + " = %d", recipe.getId()), null) != 0;

        // o "update" retorna o número de linhas afectadas, ou seja, se retornar '0', há algum problema
    }

    public static boolean changeRecipeFavorite(Recipe recipe, SQLiteDatabase mDatabase) {
        recipe.setFavorite(!recipe.isFavorite());
        return updateRecipe(recipe, mDatabase);
    }

    public static boolean insertIngredient(Ingredient ingredient, SQLiteDatabase mDatabase) {
        ContentValues values = getContentValuesIngredients(ingredient);
        return mDatabase.insert(DatabaseScheme.IngredientsTable.NAME, null, values) != -1;

        // o "insert" retorna '-1' se houver erro
    }

    public static boolean updateIngredient(Ingredient ingredient, SQLiteDatabase mDatabase) {
        // aqui, não meto o ID no argumento "selectionArgs" porque assim ia estar a convertê-lo para String e o ID é um 'serial'

        ContentValues values = getContentValuesIngredients(ingredient);
        return mDatabase.update(DatabaseScheme.IngredientsTable.NAME, values, String.format(Locale.getDefault(), DatabaseScheme.IngredientsTable.Cols.ID + " = %d", ingredient.getID()), null) != 0;

        // o "update" retorna o número de linhas afectadas, ou seja, se retornar '0', há algum problema
    }

    public static boolean insertRelation(Relation relation, SQLiteDatabase mDatabase) {
        ContentValues values = getContentValuesRelations(relation);
        return mDatabase.insert(DatabaseScheme.RelationTable.NAME, null, values) != -1;

        // o "insert" retorna '-1' se houver erro
    }

    public static ArrayList<Recipe> selectRecipeByCategory(String category, SQLiteDatabase mDatabase) {
        ArrayList<Recipe> list = new ArrayList<>();

        Cursor c = mDatabase.query(DatabaseScheme.RecipesTable.NAME,
                null,
                "LOWER(" + DatabaseScheme.RecipesTable.Cols.CATEGORY + ") = ?",
                new String[]{category.toLowerCase()},
                null,
                null,
                DatabaseScheme.RecipesTable.Cols.ID + " ASC");

        RecipesCursorWrapper cursor = new RecipesCursorWrapper(c);

        if (!cursor.moveToFirst()) {
            return list;
        }

        while (!cursor.isAfterLast()) {
            Recipe recipe = cursor.getRecipe();
            list.add(recipe);
            cursor.moveToNext();
        }

        return list;
    }

    public static ArrayList<Recipe> selectAllRecipes(SQLiteDatabase mDatabase) {
        ArrayList<Recipe> list = new ArrayList<>();

        Cursor c = mDatabase.query(DatabaseScheme.RecipesTable.NAME,
                null,
                null,
                null,
                null,
                null,
                DatabaseScheme.RecipesTable.Cols.ID + " ASC");

        RecipesCursorWrapper cursor = new RecipesCursorWrapper(c);

        if (!cursor.moveToFirst()) {
            return list;
        }

        while (!cursor.isAfterLast()) {
            Recipe recipe = cursor.getRecipe();
            list.add(recipe);
            cursor.moveToNext();
        }

        return list;
    }

    public static ArrayList<Relation> selectAllRelations(SQLiteDatabase mDatabase) {
        ArrayList<Relation> list = new ArrayList<>();

        Cursor c = mDatabase.query(DatabaseScheme.RelationTable.NAME,
                null,
                null,
                null,
                null,
                null,
                null);

        RelationCursorWrapper cursor = new RelationCursorWrapper(c);

        if (!cursor.moveToFirst()) {
            return list;
        }

        while (!cursor.isAfterLast()) {
            Relation relation = cursor.getRelation();
            list.add(relation);
            cursor.moveToNext();
        }

        return list;
    }

    public static ArrayList<Ingredient> selectAllIngredients(SQLiteDatabase mDatabase) {
        ArrayList<Ingredient> list = new ArrayList<>();

        Cursor c = mDatabase.query(DatabaseScheme.IngredientsTable.NAME,
                null,
                null,
                null,
                null,
                null,
                DatabaseScheme.IngredientsTable.Cols.ID + " ASC");

        IngredientsCursorWrapper cursor = new IngredientsCursorWrapper(c);

        if (!cursor.moveToFirst()) {
            return list;
        }

        while (!cursor.isAfterLast()) {
            Ingredient ingredient = cursor.getIngredient();
            list.add(ingredient);
            cursor.moveToNext();
        }

        return list;
    }

    public static ArrayList<String> selectAllIngredientsStrings(SQLiteDatabase mDatabase) {
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
            return list;
        }

        while (!cursor.isAfterLast()) {
            Ingredient ingredient = cursor.getIngredient();
            list.add(ingredient.getName());
            cursor.moveToNext();
        }

        return list;
    }

    public static ArrayList<Recipe> selectFavoriteRecipes(SQLiteDatabase mDatabase) {
        ArrayList<Recipe> list = new ArrayList<Recipe>();

        // atenção que em SQLite, os valores boolean são 1 (true) e 0 (false). Não existe uma class boolean com valores "true" e "false"

        Cursor c = mDatabase.query(DatabaseScheme.RecipesTable.NAME,
                null,
                DatabaseScheme.RecipesTable.Cols.FAVORITE + " = 1",
                null,
                null,
                null,
                DatabaseScheme.RecipesTable.Cols.ID + " ASC");

        RecipesCursorWrapper cursor = new RecipesCursorWrapper(c);

        if (!cursor.moveToFirst()) {
            return list;
        }

        while (!cursor.isAfterLast()) {
            Recipe recipe = cursor.getRecipe();
            list.add(recipe);
            cursor.moveToNext();
        }

        return list;
    }


    private static ContentValues getContentValuesRecipes(Recipe recipe) {

        // atenção que em SQLite, os valores boolean são 1 (true) e 0 (false). Não existe uma class boolean com valores "true" e "false"

        ContentValues values = new ContentValues();
        //values.put(DatabaseScheme.RecipesTable.Cols.ID, recipe.getId());
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

        // atenção que em SQLite, os valores boolean são 1 (true) e 0 (false). Não existe uma class boolean com valores "true" e "false"

        ContentValues values = new ContentValues();
        values.put(DatabaseScheme.IngredientsTable.Cols.ID, ingredient.getID());
        values.put(DatabaseScheme.IngredientsTable.Cols.NAME, ingredient.getName());

        return values;
    }

    private static ContentValues getContentValuesRelations(Relation relation) {

        // atenção que em SQLite, os valores boolean são 1 (true) e 0 (false). Não existe uma class boolean com valores "true" e "false"

        ContentValues values = new ContentValues();
        values.put(DatabaseScheme.RelationTable.Cols.ID_RECIPE, relation.getID_recipe());
        values.put(DatabaseScheme.RelationTable.Cols.ID_INGREDIENT, relation.getID_ingredient());

        return values;
    }

    public static ArrayList<Recipe> selectRecipesByIngredients(ArrayList<Ingredient> ingrs, SQLiteDatabase mDatabase) {
        //TODO: testar se aqui não dá borrada por estar a converter o boolean para string e a comparar com o boolean da base de dados

        ArrayList<Recipe> list = new ArrayList<Recipe>();
        String ingredientsQueryStr = getIngredientArrayListToString(ingrs);
        // atenção que em SQLite, os valores boolean são 1 (true) e 0 (false). Não existe uma class boolean com valores "true" e "false"

        Cursor c = mDatabase.query(DatabaseScheme.RelationTable.NAME,
                null,
                DatabaseScheme.RelationTable.Cols.ID_INGREDIENT + " IN ?",
                new String[]{ingredientsQueryStr},
                null,
                null,
                DatabaseScheme.RelationTable.Cols.ID_RECIPE + " ASC");

        RelationCursorWrapper cursor = new RelationCursorWrapper(c);

        if (!cursor.moveToFirst()) {
            return list;
        }

        while (!cursor.isAfterLast()) {
            Relation relation = cursor.getRelation();

            Recipe recipe = selectRecipeByID(relation.getID_recipe(), mDatabase);

            if (recipe == null) {
                return list;
            }

            list.add(recipe);
            cursor.moveToNext();
        }

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

        if (!cursor.moveToFirst()) {
            return null;
        }

        recipe = cursor.getRecipe();

        return recipe;
    }

    private static String getIngredientArrayListToString(ArrayList<Ingredient> list) {
        StringBuilder res = new StringBuilder("(");

        for (Ingredient i : list) {
            if (res.length() > 0) {
                res.append(", ");
            }
            res.append(i.getID());
        }
        res.append(")");

        return res.toString();
    }

    public static void turnOnForeignKeys(SQLiteDatabase mDatabase) {
        mDatabase.execSQL("PRAGMA foreign_keys = ON;");
    }
}