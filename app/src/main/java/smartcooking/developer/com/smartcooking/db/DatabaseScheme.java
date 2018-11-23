package smartcooking.developer.com.smartcooking.db;

public class DatabaseScheme {

    public static final int VERSION = 1;
    public static final String DATABASE_NAME = "smartcooking.db";

    public static final class RecipesTable {
        public static final String NAME = "Recipes";

        public static final class Cols {
            public static final String ID = "id";
            public static final String NAME = "name";
            public static final String DIFFICULTY = "difficulty";
            public static final String TIME = "time";
            public static final String CATEGORY = "category";
            public static final String SUPPLIER = "supplier";
            public static final String PREPARATION = "preparation";
            public static final String IMAGE = "image";
            public static final String INGREDIENTS = "ingredients";
            public static final String FAVORITE = "favorite";
            public static final String HASH = "hash";
        }
    }

    public static final class IngredientsTable {
        public static final String NAME = "Ingredients";

        public static final class Cols {
            public static final String ID = "id";
            public static final String NAME = "name";
            //public static final String HASH = "hash";
        }
    }

    public static final class RelationTable {
        public static final String NAME = "Relation";

        public static final class Cols {
            public static final String ID_RECIPE = "id_recipe";
            public static final String ID_INGREDIENT = "id_ingredient";
            //public static final String HASH = "hash";
        }
    }
}
