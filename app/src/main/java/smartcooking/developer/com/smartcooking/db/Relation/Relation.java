package smartcooking.developer.com.smartcooking.db.Relation;

import java.io.Serializable;

public class Relation  implements Serializable {

    private long ID_recipe;
    private long ID_ingredient;

    public Relation() {
    }

    public long getID_recipe() {
        return ID_recipe;
    }

    public void setID_recipe(long ID_recipe) {
        this.ID_recipe = ID_recipe;
    }

    public long getID_ingredient() {
        return ID_ingredient;
    }

    public void setID_ingredient(long ID_ingredient) {
        this.ID_ingredient = ID_ingredient;
    }
}
