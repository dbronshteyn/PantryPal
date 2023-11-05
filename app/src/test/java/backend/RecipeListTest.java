package backend;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RecipeListTest {
    private RecipeList recipeList;

    @BeforeEach
    public void setUp() {
        recipeList = new RecipeList();
    }

    @Test
    void testAddRecipe_SortedCorrectly() {
        // Assume Recipe class has a constructor that takes title, instructions, and
        // dateCreated
        Recipe recipe1 = new Recipe("Recipe1", "Instructions1", new Date(System.currentTimeMillis() - 10000)); // Older
        Recipe recipe2 = new Recipe("Recipe2", "Instructions2", new Date()); // Newer

        recipeList.addRecipe(recipe1);
        recipeList.addRecipe(recipe2);

        List<Recipe> recipes = recipeList.getRecipes();

        assertEquals(2, recipes.size(), "There should be two recipes in the list.");
        assertEquals("Recipe2", recipes.get(0).getTitle(), "Recipe2 should be first as it is newer.");
        assertEquals("Recipe1", recipes.get(1).getTitle(), "Recipe1 should be second as it is older.");
    }

    @Test
    void testRemoveRecipe() {
        Recipe recipe1 = new Recipe("Recipe1", "Instructions1", new Date());
        recipeList.addRecipe(recipe1);
        assertEquals(1, recipeList.getRecipes().size(), "Recipe list should have 1 recipe before removal.");

        recipeList.removeRecipe(recipe1);
        assertTrue(recipeList.getRecipes().isEmpty(), "Recipe list should be empty after removal.");
    }

    @Test
    void testGetRecipes_ImmutableList() {
        Recipe recipe1 = new Recipe("Recipe1", "Instructions1", new Date());
        recipeList.addRecipe(recipe1);

        List<Recipe> recipes = recipeList.getRecipes();
        try {
            recipes.add(new Recipe("Recipe2", "Instructions2", new Date()));
        } catch (UnsupportedOperationException e) {
            // This is expected as we're attempting to modify an unmodifiable list.
        }

        assertEquals(1, recipeList.getRecipes().size(), "Recipe list should not be modified through the getter.");
    }

    /*
     * This test is not necessary as it is testing the Collections.sort method
     * 
     * However, I just wanted to experiment with the Comparator class and how it
     * works on a larger list. I also wanted to make sure that the sortRecipesByDate
     * method was called in the constructor.
     */

    @Test
    void testGetRecipes_SortedCorrectly() {
        // Assume Recipe class has a constructor that takes title, instructions, and
        // dateCreated
        Recipe recipe1 = new Recipe("Recipe1", "Instructions1", new Date(System.currentTimeMillis() - 10000)); // Older
        Recipe recipe2 = new Recipe("Recipe2", "Instructions2", new Date()); // Newer
        Recipe recipe3 = new Recipe("Recipe3", "Instructions3", new Date(System.currentTimeMillis() - 20000)); // Older
        Recipe recipe4 = new Recipe("Recipe4", "Instructions4", new Date(System.currentTimeMillis() - 30000)); // Older
        Recipe recipe5 = new Recipe("Recipe5", "Instructions5", new Date(System.currentTimeMillis() - 40000)); // Older

        recipeList.addRecipe(recipe1);
        recipeList.addRecipe(recipe2);
        recipeList.addRecipe(recipe3);
        recipeList.addRecipe(recipe4);
        recipeList.addRecipe(recipe5);

        List<Recipe> recipes = recipeList.getRecipes();

        assertEquals(5, recipes.size(), "There should be five recipes in the list.");
        assertEquals("Recipe2", recipes.get(0).getTitle(), "Recipe2 should be first as it is newer.");
        assertEquals("Recipe1", recipes.get(1).getTitle(), "Recipe1 should be second as it is older.");
        assertEquals("Recipe3", recipes.get(2).getTitle(), "Recipe3 should be third as it is older.");
        assertEquals("Recipe4", recipes.get(3).getTitle(), "Recipe4 should be fourth as it is older.");
        assertEquals("Recipe5", recipes.get(4).getTitle(), "Recipe5 should be fifth as it is older.");
    }
}
