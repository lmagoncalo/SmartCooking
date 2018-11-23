package smartcooking.developer.com.smartcooking.db.Ingredient;

public class Ingredient {
    private long ID;
    private String name;

    public Ingredient() {
    }

    public Ingredient(long id, String name) {
        this.ID = id;
        this.name = name;
    }

    public long getID() {
        return ID;
    }

    public void setID(long ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
