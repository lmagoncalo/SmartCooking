package smartcooking.developer.com.smartcooking.db.Recipe;

import android.database.Cursor;
import android.database.CursorWrapper;

import java.util.ArrayList;
import java.util.List;

import smartcooking.developer.com.smartcooking.db.DatabaseScheme;

public class RecipesCursorWrapper extends CursorWrapper {

    public RecipesCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Recipe getRecipe() {
        Recipe recipe = new Recipe();

        long ID = getLong(getColumnIndex(DatabaseScheme.RecipesTable.Cols.ID));
        String name = getString(getColumnIndex(DatabaseScheme.RecipesTable.Cols.NAME));
        int dificulty = getInt(getColumnIndex(DatabaseScheme.RecipesTable.Cols.DIFFICULTY));
        int time = getInt(getColumnIndex(DatabaseScheme.RecipesTable.Cols.TIME));
        String category = getString(getColumnIndex(DatabaseScheme.RecipesTable.Cols.CATEGORY));
        String supplier = getString(getColumnIndex(DatabaseScheme.RecipesTable.Cols.SUPPLIER));
        String preparation_str = getString(getColumnIndex(DatabaseScheme.RecipesTable.Cols.PREPARATION));
        String image = getString(getColumnIndex(DatabaseScheme.RecipesTable.Cols.IMAGE));
        String ingredients_str = getString(getColumnIndex(DatabaseScheme.RecipesTable.Cols.INGREDIENTS));
        boolean favorite = getInt(getColumnIndex(DatabaseScheme.RecipesTable.Cols.FAVORITE)) == 1;
        String hash = getString(getColumnIndex(DatabaseScheme.RecipesTable.Cols.HASH));

        String[] preparation_aux = preparation_str.split("\\|");
        List<String> preparation = new ArrayList<String>();
        ;
        for (String p : preparation_aux) {
            preparation.add(p);
        }

        String[] ingredients_aux = ingredients_str.split("\\|");
        List<String> ingredients = new ArrayList<String>();
        ;
        for (String i : ingredients_aux) {
            ingredients.add(i);
        }

        recipe.setId(ID);
        recipe.setName(name);
        recipe.setDifficulty(dificulty);
        recipe.setTime(time);
        recipe.setCategory(category);
        recipe.setSupplier(supplier);
        recipe.setPreparation(preparation);
        recipe.setImage(image);
        recipe.setIngredients(ingredients);
        recipe.setFavorite(favorite);
        recipe.setHash(hash);

        return recipe;
    }

}
