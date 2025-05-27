package app;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/recipe_manager";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }

    public static List<Recipe> getAllRecipeObjects() {
        List<Recipe> recipes = new ArrayList<>();
        String sql = "SELECT * FROM recipes";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Recipe recipe = new Recipe(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("ingredients"),
                        rs.getInt("cooking_time"),
                        rs.getString("type"),
                        rs.getString("allergens"),
                        rs.getString("steps")
                );
                recipes.add(recipe);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return recipes;
    }

    public static void addRecipe(String name, String ingredients, int cookingTime, String type, String allergens, String steps) {
        String sql = "INSERT INTO recipes (name, ingredients, cooking_time, type, allergens, steps) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, name);
            pstmt.setString(2, ingredients);
            pstmt.setInt(3, cookingTime);
            pstmt.setString(4, type);
            pstmt.setString(5, allergens);
            pstmt.setString(6, steps);

            int rowsInserted = pstmt.executeUpdate();

            if (rowsInserted > 0) {
                System.out.println("Recipe added successfully to the database!");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static String getAllRecipes() {
        StringBuilder sb = new StringBuilder();
        String sql = "SELECT * FROM recipes";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                sb.append("ID: ").append(rs.getInt("id")).append("\n");
                sb.append("Name: ").append(rs.getString("name")).append("\n");
                sb.append("Ingredients: ").append(rs.getString("ingredients")).append("\n");
                sb.append("Cooking Time: ").append(rs.getInt("cooking_time")).append(" minutes\n");
                sb.append("Type: ").append(rs.getString("type")).append("\n");
                sb.append("Allergens: ").append(rs.getString("allergens")).append("\n");
                sb.append("Steps: ").append(rs.getString("steps")).append("\n");
                sb.append("------------------------------\n");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return sb.toString();
    }

    public static void deleteRecipeById(int id) {
        String sql = "DELETE FROM recipes WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            int rowsDeleted = pstmt.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Recipe deleted successfully!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void updateRecipe(int id, String name, String ingredients, int cookingTime, String type, String allergens, String steps) {
        String sql = "UPDATE recipes SET name = ?, ingredients = ?, cooking_time = ?, type = ?, allergens = ?, steps = ? WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, name);
            pstmt.setString(2, ingredients);
            pstmt.setInt(3, cookingTime);
            pstmt.setString(4, type);
            pstmt.setString(5, allergens);
            pstmt.setString(6, steps);
            pstmt.setInt(7, id);

            int rowsUpdated = pstmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Recipe updated successfully!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
