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
        assertEquals("1969-12-31T16:00:00-08:00", json.getString("dateCreated"));
    }

    @Test
    void testGetMealType() {
        assertEquals("breakfast", Recipe.getMealType("breakfast"));
        assertEquals("breakfast", Recipe.getMealType("I want breakfast!"));
        assertEquals("lunch", Recipe.getMealType("I want lunch!"));
        assertEquals("dinner", Recipe.getMealType("I want dinner!"));
        assertEquals("breakfast", Recipe.getMealType("I want !!!fhasfBREAKFAST___"));
        assertEquals(null, Recipe.getMealType("I'm not sure what I want"));
        assertEquals(null, Recipe.getMealType("BREAK_FAST"));
    }
}
