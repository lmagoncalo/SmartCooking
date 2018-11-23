package smartcooking.developer.com.smartcooking.db.Relation;

import android.database.Cursor;
import android.database.CursorWrapper;

import smartcooking.developer.com.smartcooking.db.DatabaseScheme;

public class RelationCursorWrapper extends CursorWrapper {

    public RelationCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Relation getRelation() {

        Relation relation = new Relation();

        long ID_recipe = getLong(getColumnIndex(DatabaseScheme.RelationTable.Cols.ID_RECIPE));
        long ID_ingredient = getLong(getColumnIndex(DatabaseScheme.RelationTable.Cols.ID_INGREDIENT));

        relation.setID_recipe(ID_recipe);
        relation.setID_ingredient(ID_ingredient);

        return relation;
    }

}
