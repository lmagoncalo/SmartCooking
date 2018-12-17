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

    public static void recipeControlledInsert(Recipe new_recipe, SQLiteDatabase mDatabase) {
        Recipe old_recipe = recipeExists(new_recipe, mDatabase);
        if (old_recipe != null) {
            // se já existe uma receita com o mesmo ID
            if (!old_recipe.getHash().equals(new_recipe.getHash())) {
                updateRecipe(new_recipe, mDatabase);
            }
        } else {
            // se a receita ainda não existe na base de dados
            insertRecipe(new_recipe, mDatabase);

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
            cursor.close();
            c.close();
            return null;
        }

        Recipe r = cursor.getRecipe();

        cursor.close();
        c.close();

        return r;
    }

    private static void insertRecipe(Recipe recipe, SQLiteDatabase mDatabase) {
        ContentValues values = getContentValuesRecipes(recipe);
        mDatabase.insert(DatabaseScheme.RecipesTable.NAME, null, values);

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

    public static void insertIngredient(Ingredient ingredient, SQLiteDatabase mDatabase) {
        ContentValues values = getContentValuesIngredients(ingredient);
        mDatabase.insert(DatabaseScheme.IngredientsTable.NAME, null, values);

        // o "insert" retorna '-1' se houver erro
    }

    /*public static boolean updateIngredient(Ingredient ingredient, SQLiteDatabase mDatabase) {
        // aqui, não meto o ID no argumento "selectionArgs" porque assim ia estar a convertê-lo para String e o ID é um 'serial'

        ContentValues values = getContentValuesIngredients(ingredient);
        return mDatabase.update(DatabaseScheme.IngredientsTable.NAME, values, String.format(Locale.getDefault(), DatabaseScheme.IngredientsTable.Cols.ID + " = %d", ingredient.getId()), null) != 0;

        // o "update" retorna o número de linhas afectadas, ou seja, se retornar '0', há algum problema
    }*/

    public static void insertRelation(Relations relations, SQLiteDatabase mDatabase) {
        ContentValues values = getContentValuesRelations(relations);
        mDatabase.insert(DatabaseScheme.RelationsTable.NAME, null, values);

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
            cursor.close();
            c.close();
            return list;
        }

        while (!cursor.isAfterLast()) {
            Recipe recipe = cursor.getRecipe();
            list.add(recipe);
            cursor.moveToNext();
        }

        cursor.close();
        c.close();

        return list;
    }

    private static final char[] hex = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

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
            cursor.close();
            c.close();
            return list;
        }

        while (!cursor.isAfterLast()) {
            Recipe recipe = cursor.getRecipe(); //10 8739591
            list.add(recipe);
            cursor.moveToNext();


            //System.out.println("update public.\"Recipes\" set hash = '" + byteArray2Hex(recipe.toString().getBytes()) + "' where id= " + recipe.getId() +";");

        }

        cursor.close();
        c.close();

        return list;
    }

    private static String byteArray2Hex(byte[] bytes) {
        StringBuilder sb = new StringBuilder(bytes.length * 2);
        for (final byte b : bytes) {
            sb.append(hex[(b & 0xF0) >> 4]);
            sb.append(hex[b & 0x0F]);
        }
        return sb.toString();
    }

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

        Cursor c = mDatabase.query(DatabaseScheme.IngredientsTable.NAME,
                null,
                null,
                null,
                null,
                null,
                DatabaseScheme.IngredientsTable.Cols.ID + " ASC");

        IngredientsCursorWrapper cursor = new IngredientsCursorWrapper(c);

        if (!cursor.moveToFirst()) {
            cursor.close();
            c.close();
            return list;
        }

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
            cursor.close();
            c.close();
            return list;
        }

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

        // atenção que em SQLite, os valores boolean são 1 (true) e 0 (false). Não existe uma class boolean com valores "true" e "false"

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

        // atenção que em SQLite, os valores boolean são 1 (true) e 0 (false). Não existe uma class boolean com valores "true" e "false"

        ContentValues values = new ContentValues();
        values.put(DatabaseScheme.IngredientsTable.Cols.ID, ingredient.getId());
        values.put(DatabaseScheme.IngredientsTable.Cols.NAME, ingredient.getName());

        return values;
    }

    private static ContentValues getContentValuesRelations(Relations relations) {

        // atenção que em SQLite, os valores boolean são 1 (true) e 0 (false). Não existe uma class boolean com valores "true" e "false"

        ContentValues values = new ContentValues();
        values.put(DatabaseScheme.RelationsTable.Cols.ID_RECIPE, relations.getId_recipe());
        values.put(DatabaseScheme.RelationsTable.Cols.ID_INGREDIENT, relations.getId_ingredient());

        return values;
    }

    public static ArrayList<Recipe> selectRecipesByIngredients(ArrayList<Ingredient> ingrs, SQLiteDatabase mDatabase) {

        /*TODO: falta fazer a verificação se o utilizador introduziu ingredientes repetidos
               OPÇÕES:
                    1 -->  na função "getIngredientArrayListToString" que é chamada aqui em baixo, ter em atenção para não repetir ingredientes
                    2 -->  fazer uma verificação antes de chamar esta função, para não passar ingredientes repetidos
                    3 -->  fazer uma verificação antes de chamar esta função, para aparecer uma mensagem de erro, caso haja ingredientes repetidos
        */

        ArrayList<Recipe> list = new ArrayList<>();
        String ingredientsQueryStr = getIngredientArrayListToString(ingrs);
        // atenção que em SQLite, os valores boolean são 1 (true) e 0 (false). Não existe uma class boolean com valores "true" e "false"

        // este hashMap vai fazer a contagem do número de ingredientes em comum entre as receitas e a lista de ingredientes pesquisados
        /*Cursor c = mDatabase.query(DatabaseScheme.RelationsTable.NAME,
                null,
                DatabaseScheme.RelationsTable.Cols.ID_INGREDIENT + " IN " + ingredientsQueryStr,
                null,
                null,
                null,
                DatabaseScheme.RelationsTable.Cols.ID_RECIPE + " ASC");*/

        /*

        A  =  ingrs em comum entre receita e pesquisa
        B  =  ingrs da receita que não estão na pesquisa
        C  =  nº total de ingrs da receita   (A + B)

           A  DESC, B  ASC, tempo_preparecao ASC, dificuldade ASC, ID ASC

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
            Nesta query não podemos fazer com aquilo com os pontos de interrugação, porque depois o que ia substituir esse ponto de interrugação
            [ (1, 2, 3), por exemplo] ia ser tratado como uma string e não como argumento, ou seja a query ia ficar assim:

                SELECT *
                FROM Relations
                WHERE id_ingredient IN  "(1, 2, 3)"
                ORDER BY id_recipe ASC

           EM VEZ DE:
                SELECT *
                FROM Relations
                WHERE id_ingredient IN  (1, 2, 3)
                ORDER BY id_recipe ASC

           (a diferença está nas aspas do WHERE)


        */

        RecipesCursorWrapper cursor = new RecipesCursorWrapper(c);

        if (!cursor.moveToFirst()) {
            cursor.close();
            c.close();
            return list;
        }

        while (!cursor.isAfterLast()) {


            Recipe recipe = cursor.getRecipe();

            // é feita esta verificação porque 1 receita pode ter mais do que 1 dos ingredientes pesquisados e assim ia ser adicionada mais do que 1 vez
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

        for (Ingredient i : list) {
            if (res.length() > 1) {  //aqui é >1 e não >0  por causa do parentisis inicial que é posto em cima. Caso contrário a string ficava com uma vírgula logo a seguir ao parentisis
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
