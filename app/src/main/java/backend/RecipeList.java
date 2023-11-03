package backend;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.ArrayList;

public class RecipeList {

    private List<Recipe> recipes;

    public RecipeList() {
        this.recipes = new ArrayList<>();
    }

    public RecipeList(List<Recipe> recipes) {
        this.recipes = new ArrayList<>(recipes);
        sortRecipesByDate();
    }

    public void addRecipe(Recipe recipe) {
        this.recipes.add(recipe);
        sortRecipesByDate();
    }

    public void removeRecipe(Recipe recipe) {
        this.recipes.remove(recipe);
    }

    /*
     * Returns an unmodifiable list of recipes sorted by date created in descending
     * 
     * Daniel's reasoning. I want this list to not be modifiable by other classes
     * just in case. This is the reason that I am relying on the
     * Collections.unmodifiableList class so that I don't return a pointer directly
     * to out list. Let me know if you have any questions.
     */
    public List<Recipe> getRecipes() {
        return Collections.unmodifiableList(this.recipes);
    }

    private void sortRecipesByDate() {
        // Sort the list with a custom comparator that compares the dates
        Collections.sort(this.recipes, new Comparator<Recipe>() {
            @Override
            public int compare(Recipe r1, Recipe r2) {
                // Sort in descending order so the most recent dates come first
                return r2.getDateCreated().compareTo(r1.getDateCreated());
            }
        });
    }
}
