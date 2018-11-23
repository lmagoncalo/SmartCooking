package smartcooking.developer.com.smartcooking.db.Ingredient;

import android.database.Cursor;
import android.database.CursorWrapper;

import smartcooking.developer.com.smartcooking.db.DatabaseScheme;

public class IngredientsCursorWrapper extends CursorWrapper {

    public IngredientsCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Ingredient getIngredient() {

        Ingredient ingredient = new Ingredient();

        long ID = getLong(getColumnIndex(DatabaseScheme.IngredientsTable.Cols.ID));
        String name = getString(getColumnIndex(DatabaseScheme.IngredientsTable.Cols.NAME));

        ingredient.setID(ID);
        ingredient.setName(name);

        return ingredient;
    }

}
