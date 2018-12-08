package smartcooking.developer.com.smartcooking.db.Recipe;


import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.List;

public class Recipe implements Serializable, Comparable<Recipe> {
    /*static Comparator<Recipe> RecipeNameComparator = new Comparator<Recipe>() {

        public int compare(Recipe left, Recipe right) {

            String leftName = left.getName().toUpperCase();
            String rightName = right.getName().toUpperCase();

            //ascending order
            return leftName.compareTo(rightName);

            //descending order
            //return fruitName2.compareTo(fruitName1);

            //Weight compare
            //return left.getPesoPesquisa() - right.getPesoPesquisa();
        }

    };*/

    private long id;
    private String name;
    private int difficulty;
    private int time; //Em minutos
    private String category;
    private String supplier;
    private List<String> preparation;
    private List<String> ingredients;
    private String image;
    private String hash;
    private boolean favorite = false;

    public Recipe() {
    }
/*
    public Recipe(int id, String name, int time, int difficulty, String category) {
        this.id = id;
        this.name = name;
        this.time = time;
        this.difficulty = difficulty;
        this.category = category;
    }

    public Recipe(int id, String name, int time, int difficulty, String category, String url) {
        this.id = id;
        this.name = name;
        this.time = time;
        this.difficulty = difficulty;
        this.category = category;
        this.image = url;
    }

    public Recipe(int id, String name, int difficulty, String url) {
        this.id = id;
        this.name = name;
        this.difficulty = difficulty;
        this.image = url;
    }

    public Recipe(long id, String name, int time, int difficulty, String category, String supplier, List<String> preparation, List<String> ingredients, String image) {
        this.id = id;
        this.name = name;
        this.difficulty = difficulty;
        this.time = time;
        this.category = category;
        this.supplier = supplier;
        this.preparation = preparation;
        this.ingredients = ingredients;
        this.image = image;
    }

    public Recipe(int id, String name, int time, int difficulty, String category, List<String> preparation, String image) {
        this.id = id;
        this.name = name;
        this.difficulty = difficulty;
        this.time = time;
        this.category = category;
        this.preparation = preparation;
        this.image = image;
    }

    public Recipe(long id, String title, int time_prep, int difficulty, String category, String supplier, String prep, String list_ingr, String URLLink) {
        this.id = id;
        this.name = title;
        this.category = category;
        this.ingredients = Arrays.asList(list_ingr.split("\\|"));
        this.difficulty = difficulty;
        this.supplier = supplier;
        this.preparation = Arrays.asList(prep.split("\\|"));
        this.image = URLLink;
        this.hash = "abd";
    }*/

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

    public int getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    public void setPreparation(List<String> preparation) {
        this.preparation = preparation;
    }

    public void setIngredients(List<String> ingredients) {
        this.ingredients = ingredients;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    public String getPreparationString() {
        StringBuilder prep = new StringBuilder();

        for (String s : this.preparation) {
            prep.append("- ").append(s).append("\n");
        }

        return prep.toString();
    }

    public String getIngredientsString() {
        StringBuilder prep = new StringBuilder();

        for (String i : this.ingredients) {
            prep.append("- ").append(i).append("\n");
        }

        return prep.toString();
    }

    public String getPreparationStringToDatabase() {
        StringBuilder res = new StringBuilder();
        for (String p : preparation) {
            if (res.length() > 0) {
                res.append("|");
            }
            res.append(p);
        }
        return res.toString();
    }

    public String getIngredientsStringToDatabase() {
        StringBuilder res = new StringBuilder();
        for (String i : ingredients) {
            if (res.length() > 0) {
                res.append("|");
            }
            res.append(i);
        }
        return res.toString();
    }

    @NonNull
    @Override
    public String toString() {
        return "Recipe{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", difficulty=" + difficulty +
                ", time=" + time +
                ", category='" + category + '\'' +
                ", supplier='" + supplier + '\'' +
                ", preparation=" + getPreparationStringToDatabase() +
                ", ingredients=" + getIngredientsStringToDatabase() +
                ", image='" + image +
                '}';
    }

    @Override
    public int compareTo(@NonNull Recipe recipe) {
        return 0;
    }
}
