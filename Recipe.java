package app;

public class Recipe {
    private int id;
    private String name;
    private String ingredients;
    private int cookingTime;
    private String type;
    private String allergens;
    private String steps;

    public Recipe(int id, String name, String ingredients, int cookingTime, String type, String allergens, String steps) {
        this.id = id;
        this.name = name;
        this.ingredients = ingredients;
        this.cookingTime = cookingTime;
        this.type = type;
        this.allergens = allergens;
        this.steps = steps;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getIngredients() { return ingredients; }
    public int getCookingTime() { return cookingTime; }
    public String getType() { return type; }
    public String getAllergens() { return allergens; }
    public String getSteps() { return steps; }
}
