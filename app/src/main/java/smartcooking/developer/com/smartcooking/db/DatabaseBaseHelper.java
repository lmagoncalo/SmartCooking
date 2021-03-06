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

        // here, we create the tables

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

        db.execSQL("create table " + DatabaseScheme.RelationsTable.NAME + "( " +
                DatabaseScheme.RelationsTable.Cols.ID_RECIPE + " integer, " +
                DatabaseScheme.RelationsTable.Cols.ID_INGREDIENT + " integer, " +
                "FOREIGN KEY (" + DatabaseScheme.RelationsTable.Cols.ID_RECIPE + ") REFERENCES " + DatabaseScheme.RecipesTable.NAME + "(" + DatabaseScheme.RecipesTable.Cols.ID + "), " +
                "FOREIGN KEY (" + DatabaseScheme.RelationsTable.Cols.ID_INGREDIENT + ") REFERENCES " + DatabaseScheme.IngredientsTable.NAME + "(" + DatabaseScheme.IngredientsTable.Cols.ID + "))");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
