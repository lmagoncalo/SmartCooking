package smartcooking.developer.com.smartcooking.db.Relation;

import android.database.Cursor;
import android.database.CursorWrapper;

import smartcooking.developer.com.smartcooking.db.DatabaseScheme;

public class RelationsCursorWrapper extends CursorWrapper {

    public RelationsCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Relations getRelations() {

        Relations relations = new Relations();

        long ID_recipe = getLong(getColumnIndex(DatabaseScheme.RelationsTable.Cols.ID_RECIPE));
        long ID_ingredient = getLong(getColumnIndex(DatabaseScheme.RelationsTable.Cols.ID_INGREDIENT));

        relations.setId_recipe(ID_recipe);
        relations.setId_ingredient(ID_ingredient);

        return relations;
    }

}
