package smartcooking.developer.com.smartcooking.db.Relation;

import java.io.Serializable;

public class Relations implements Serializable {

    private long id_recipe;
    private long id_ingredient;

    public Relations() {
    }

    public long getId_recipe() {
        return id_recipe;
    }

    public void setId_recipe(long id_recipe) {
        this.id_recipe = id_recipe;
    }

    public long getId_ingredient() {
        return id_ingredient;
    }

    public void setId_ingredient(long id_ingredient) {
        this.id_ingredient = id_ingredient;
    }
}
