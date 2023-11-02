package cse.project.team;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class RecipeTrackerTest {
    @Test
    void appHasAGreeting() {
        RecipeTracker classUnderTest = new RecipeTracker();
        assertNotNull(classUnderTest.getGreeting(), "app should have a greeting");
    }
}
