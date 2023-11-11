package backend;

import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RecipeTest {
    private Recipe recipe;
    private Date dateCreated;

    @BeforeEach
    public void setUp() {
        dateCreated = new Date(0);
        recipe = new Recipe("Chocolate Cake", "Mix ingredients and bake for 30 minutes.", dateCreated);
    }

    @Test
    void testSetInstructions() {
        String newInstructions = "Mix ingredients and bake for 45 minutes.";
        Date oldDate = recipe.getDateCreated();
        recipe.setInstructions(newInstructions);
        assertEquals(newInstructions, recipe.getInstructions());
        assertTrue(recipe.getDateCreated().after(oldDate));
    }

    @Test
    void testToString() {
        assertEquals("Chocolate Cake", recipe.toString());
    }

    @Test
    void testFromJSON() {
        JSONObject json = new JSONObject("{\"title\":\"abc\",\"instructions\":\"ab\",\"dateCreated\":\"1970-01-01T00:00:00-00:00\"}");
        Recipe recipe = new Recipe(json);
        assertEquals("abc", recipe.getTitle());
        assertEquals("ab", recipe.getInstructions());
        assertEquals(new Date(0), recipe.getDateCreated());
    }

    @Test
    void testToJSON() {
        JSONObject json = recipe.toJSON();
        assertEquals("Chocolate Cake", json.getString("title"));
        assertEquals("Mix ingredients and bake for 30 minutes.", json.getString("instructions"));

        // this one seems to depend on the system
        assertTrue(json.getString("dateCreated").startsWith("1969") || json.getString("dateCreated").startsWith("1970"));
    }

    // based on Story 4 BDD Scenario 1
    // also tests Feature 6 in the MS1 delivery document
    @Test
    void testEditRecipeScenarioOne() {
        long currentTime = System.currentTimeMillis();
        Recipe recipe = new Recipe("Muffins", "Add 1 cup of sugar and flour.", new Date(currentTime - 1000));
        recipe.setInstructions("Add 1/2 cup of sugar and flour.");
        assertEquals("Add 1/2 cup of sugar and flour.", recipe.getInstructions());
        assertTrue(recipe.getDateCreated().after(new Date(currentTime - 1000)));
    }

    // sort of a trivial test, but tests Feature 3 in the MS1 delivery document
    @Test
    void testRetrieveRecipeDetails() {
        assertEquals("Chocolate Cake", recipe.getTitle());
        assertEquals("Mix ingredients and bake for 30 minutes.", recipe.getInstructions());
        assertEquals(dateCreated, recipe.getDateCreated());
    }
}
