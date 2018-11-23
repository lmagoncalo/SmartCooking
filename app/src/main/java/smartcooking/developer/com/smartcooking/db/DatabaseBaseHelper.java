package smartcooking.developer.com.smartcooking.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseBaseHelper extends SQLiteOpenHelper {

    public DatabaseBaseHelper(Context context) {
        super(context, DatabaseScheme.DATABASE_NAME, null, DatabaseScheme.VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // atenção que em SQLite, os valores boolean são 1 (true) e 0 (false). Não existe uma class boolean com valores "true" e "false"

        db.execSQL("create table " + DatabaseScheme.RecipesTable.NAME + "( " +
                DatabaseScheme.RecipesTable.Cols.ID + " integer primary key autoincrement, " +
                DatabaseScheme.RecipesTable.Cols.NAME + " varchar(100), " +
                DatabaseScheme.RecipesTable.Cols.DIFFICULTY + " integer, " +
                DatabaseScheme.RecipesTable.Cols.TIME + " integer, " +
                DatabaseScheme.RecipesTable.Cols.CATEGORY + " varchar(20), " +
                DatabaseScheme.RecipesTable.Cols.SUPPLIER + " varchar(50), " +
                DatabaseScheme.RecipesTable.Cols.PREPARATION + " varchar(1500), " +
                DatabaseScheme.RecipesTable.Cols.IMAGE + " varchar(200), " +
                DatabaseScheme.RecipesTable.Cols.INGREDIENTS + " varchar(500), " +
                DatabaseScheme.RecipesTable.Cols.FAVORITE + " boolean NOT NULL default 0, " +
                DatabaseScheme.RecipesTable.Cols.HASH + " text)");

        db.execSQL("create table " + DatabaseScheme.IngredientsTable.NAME + "( " +
                DatabaseScheme.IngredientsTable.Cols.ID + " integer primary key autoincrement, " +
                DatabaseScheme.IngredientsTable.Cols.NAME + " varchar(50))");

        db.execSQL("create table " + DatabaseScheme.RelationTable.NAME + "( " +
                DatabaseScheme.RelationTable.Cols.ID_RECIPE + " integer, " +
                DatabaseScheme.RelationTable.Cols.ID_INGREDIENT + " integer, " +
                "FOREIGN KEY (" + DatabaseScheme.RelationTable.Cols.ID_RECIPE + ") REFERENCES " + DatabaseScheme.RecipesTable.NAME + "(" + DatabaseScheme.RecipesTable.Cols.ID + "), " +
                "FOREIGN KEY (" + DatabaseScheme.RelationTable.Cols.ID_INGREDIENT + ") REFERENCES " + DatabaseScheme.IngredientsTable.NAME + "(" + DatabaseScheme.IngredientsTable.Cols.ID + "))");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
