package smartcooking.developer.com.smartcooking.db.Ingredient;

import java.io.Serializable;

public class Ingredient implements Serializable{
    private long id;
    private String name;

    public Ingredient() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
