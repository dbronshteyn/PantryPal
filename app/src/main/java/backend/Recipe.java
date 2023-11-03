package backend;

import java.util.Date;

public class Recipe {
    private String title;
    private String instructions;
    private Date dateCreated;

    public Recipe(String title, String instructions, Date dateCreated) {
        this.title = title;
        this.instructions = instructions;
        this.dateCreated = dateCreated;
    }

    public String getTitle() {
        return this.title;
    }

    public String getInstructions() {
        return this.instructions;
    }

    public Date getDateCreated() {
        return this.dateCreated;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    @Override
    public String toString() {
        return this.title;
    }
}