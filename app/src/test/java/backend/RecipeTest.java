package backend;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class RecipeTest {
    private Recipe recipe;
    private Date dateCreated;

    @BeforeEach
    public void setUp() {
        // Setup a fixed date so that it can be used for comparison in the tests
        dateCreated = new Date(0); // (January 1, 1970, 00:00:00 GMT)
        recipe = new Recipe("Chocolate Cake", "Mix ingredients and bake for 30 minutes.", dateCreated);
    }

    @Test
    public void testGetTitle() {
        assertEquals("Chocolate Cake", recipe.getTitle());
    }

    @Test
    public void testGetInstructions() {
        assertEquals("Mix ingredients and bake for 30 minutes.", recipe.getInstructions());
    }

    @Test
    public void testGetDateCreated() {
        assertEquals(dateCreated, recipe.getDateCreated());
    }

    @Test
    public void testSetTitle() {
        String newTitle = "Vanilla Cake";
        recipe.setTitle(newTitle);
        assertEquals(newTitle, recipe.getTitle());
    }

    @Test
    public void testSetInstructions() {
        String newInstructions = "Mix ingredients and bake for 45 minutes.";
        recipe.setInstructions(newInstructions);
        assertEquals(newInstructions, recipe.getInstructions());
    }

    @Test
    public void testSetDateCreated() {
        Date newDateCreated = new Date();
        recipe.setDateCreated(newDateCreated);
        assertEquals(newDateCreated, recipe.getDateCreated());
    }

    @Test
    public void testToString() {
        assertEquals("Chocolate Cake", recipe.toString());
    }
}
