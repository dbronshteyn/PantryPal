import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RecipeListTest {
    private RecipeList recipeList;

    @BeforeEach
    public void setUp() {
        recipeList = new RecipeList();
    }

    @Test
    public void testAddRecipe_SortedCorrectly() {
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
    public void testRemoveRecipe() {
        Recipe recipe1 = new Recipe("Recipe1", "Instructions1", new Date());
        recipeList.addRecipe(recipe1);
        assertEquals(1, recipeList.getRecipes().size(), "Recipe list should have 1 recipe before removal.");

        recipeList.removeRecipe(recipe1);
        assertTrue(recipeList.getRecipes().isEmpty(), "Recipe list should be empty after removal.");
    }

    @Test
    public void testGetRecipes_ImmutableList() {
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
}
