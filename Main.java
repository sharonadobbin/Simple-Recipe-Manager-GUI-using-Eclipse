package app;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Recipe Manager");
        frame.setSize(500, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(0, 2, 5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Recipe Name
        JLabel nameLabel = new JLabel("Recipe Name:");
        JTextField nameField = new JTextField();
        panel.add(nameLabel);
        panel.add(nameField);

        // Ingredients
        JLabel ingredientsLabel = new JLabel("Ingredients (comma-separated):");
        JTextField ingredientsField = new JTextField();
        panel.add(ingredientsLabel);
        panel.add(ingredientsField);

        // Cooking Time
        JLabel timeLabel = new JLabel("Cooking Time (minutes):");
        JTextField timeField = new JTextField();
        panel.add(timeLabel);
        panel.add(timeField);

        // Type (Veg/Non-Veg)
        JLabel typeLabel = new JLabel("Type:");
        JPanel typePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JRadioButton vegButton = new JRadioButton("Veg");
        JRadioButton nonVegButton = new JRadioButton("Non-Veg");
        ButtonGroup typeGroup = new ButtonGroup();
        typeGroup.add(vegButton);
        typeGroup.add(nonVegButton);
        typePanel.add(vegButton);
        typePanel.add(nonVegButton);
        panel.add(typeLabel);
        panel.add(typePanel);

        // Allergens
        JLabel allergensLabel = new JLabel("Allergens (optional):");
        JTextField allergensField = new JTextField();
        panel.add(allergensLabel);
        panel.add(allergensField);

        // Cooking Steps
        JLabel stepsLabel = new JLabel("Cooking Steps:");
        JTextArea stepsArea = new JTextArea(5, 20);
        JScrollPane stepsScroll = new JScrollPane(stepsArea);
        panel.add(stepsLabel);
        panel.add(stepsScroll);

        // Add Recipe Button (spanning two columns)
        JButton addButton = new JButton("Add Recipe");
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addButton);
        panel.add(new JLabel()); // empty cell to align button
        panel.add(buttonPanel);

        // View Recipes Button (spanning two columns)
        JButton viewButton = new JButton("View Recipes");
        JPanel viewButtonPanel = new JPanel();
        viewButtonPanel.add(viewButton);
        panel.add(new JLabel()); // empty cell to align button
        panel.add(viewButtonPanel);

        // Add Recipe Button Action
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String recipeName = nameField.getText();
                String ingredients = ingredientsField.getText();
                String time = timeField.getText();
                String type = vegButton.isSelected() ? "Veg" : nonVegButton.isSelected() ? "Non-Veg" : "Unspecified";
                String allergens = allergensField.getText();
                String steps = stepsArea.getText();

                if (recipeName.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "Please enter a recipe name.");
                } else {
                    try {
                        int cookingTime = Integer.parseInt(time);

                        // Call the database helper to save the recipe
                        DatabaseHelper.addRecipe(recipeName, ingredients, cookingTime, type, allergens, steps);

                        JOptionPane.showMessageDialog(frame, "Recipe added successfully to the database!");

                        // Clear fields
                        nameField.setText("");
                        ingredientsField.setText("");
                        timeField.setText("");
                        typeGroup.clearSelection();
                        allergensField.setText("");
                        stepsArea.setText("");

                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(frame, "Please enter a valid number for cooking time.");
                    }
                }
            }
        });

        // View Recipes Button Action
        viewButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                List<Recipe> recipeList = DatabaseHelper.getAllRecipeObjects();
                if (recipeList.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "No recipes found in the database.");
                } else {
                    JFrame viewFrame = new JFrame("All Recipes");
                    viewFrame.setSize(500, 600);
                    viewFrame.setLayout(new BorderLayout());

                    JPanel recipePanel = new JPanel();
                    recipePanel.setLayout(new BoxLayout(recipePanel, BoxLayout.Y_AXIS));

                    for (Recipe recipe : recipeList) {
                        JPanel singlePanel = new JPanel();
                        singlePanel.setLayout(new BoxLayout(singlePanel, BoxLayout.Y_AXIS));
                        singlePanel.setBorder(BorderFactory.createTitledBorder(recipe.getName()));

                        String details = "ID: " + recipe.getId() + "\n"
                                + "Ingredients: " + recipe.getIngredients() + "\n"
                                + "Cooking Time: " + recipe.getCookingTime() + " minutes\n"
                                + "Type: " + recipe.getType() + "\n"
                                + "Allergens: " + recipe.getAllergens() + "\n"
                                + "Steps: " + recipe.getSteps();

                        JTextArea textArea = new JTextArea(details);
                        textArea.setEditable(false);
                        singlePanel.add(new JScrollPane(textArea));

                        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
                        JButton editButton = new JButton("Edit");
                        JButton deleteButton = new JButton("Delete");

                        // Delete action
                        deleteButton.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                int confirm = JOptionPane.showConfirmDialog(viewFrame,
                                        "Are you sure you want to delete this recipe?",
                                        "Confirm Delete", JOptionPane.YES_NO_OPTION);
                                if (confirm == JOptionPane.YES_OPTION) {
                                    DatabaseHelper.deleteRecipeById(recipe.getId());
                                    JOptionPane.showMessageDialog(viewFrame, "Recipe deleted!");
                                    viewFrame.dispose();  // close and refresh
                                }
                            }
                        });

                        // Edit action (placeholder)
                        editButton.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                JDialog editDialog = new JDialog(viewFrame, "Edit Recipe", true);
                                editDialog.setSize(450, 500);
                                editDialog.setLayout(new BorderLayout());

                                JPanel inputPanel = new JPanel(new GridLayout(0, 1, 5, 5));
                                inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

                                JTextField nameField = new JTextField(recipe.getName());
                                JTextField ingredientsField = new JTextField(recipe.getIngredients());
                                JTextField timeField = new JTextField(String.valueOf(recipe.getCookingTime()));
                                JTextField typeField = new JTextField(recipe.getType());
                                JTextField allergensField = new JTextField(recipe.getAllergens());
                                JTextArea stepsArea = new JTextArea(recipe.getSteps(), 5, 20);
                                stepsArea.setLineWrap(true);
                                stepsArea.setWrapStyleWord(true);

                                inputPanel.add(new JLabel("Name:"));
                                inputPanel.add(nameField);
                                inputPanel.add(new JLabel("Ingredients:"));
                                inputPanel.add(ingredientsField);
                                inputPanel.add(new JLabel("Cooking Time (min):"));
                                inputPanel.add(timeField);
                                inputPanel.add(new JLabel("Type:"));
                                inputPanel.add(typeField);
                                inputPanel.add(new JLabel("Allergens:"));
                                inputPanel.add(allergensField);
                                inputPanel.add(new JLabel("Steps:"));
                                inputPanel.add(new JScrollPane(stepsArea));

                                // Wrap input panel inside a scroll pane so all content is scrollable
                                JScrollPane scrollPane = new JScrollPane(inputPanel);
                                editDialog.add(scrollPane, BorderLayout.CENTER);

                                JPanel buttonPanel = new JPanel();
                                JButton saveButton = new JButton("Save");
                                JButton cancelButton = new JButton("Cancel");
                                buttonPanel.add(saveButton);
                                buttonPanel.add(cancelButton);
                                editDialog.add(buttonPanel, BorderLayout.SOUTH);

                                saveButton.addActionListener(ev -> {
                                    try {
                                        int cookingTime = Integer.parseInt(timeField.getText());
                                        DatabaseHelper.updateRecipe(
                                                recipe.getId(),
                                                nameField.getText(),
                                                ingredientsField.getText(),
                                                cookingTime,
                                                typeField.getText(),
                                                allergensField.getText(),
                                                stepsArea.getText()
                                        );
                                        JOptionPane.showMessageDialog(editDialog, "Recipe updated!");
                                        editDialog.dispose();
                                        viewFrame.dispose(); // close or refresh the main view to reload updated data
                                    } catch (NumberFormatException ex) {
                                        JOptionPane.showMessageDialog(editDialog, "Please enter a valid number for cooking time.");
                                    }
                                });

                                cancelButton.addActionListener(ev -> editDialog.dispose());

                                editDialog.setLocationRelativeTo(viewFrame); // center on parent
                                editDialog.setVisible(true);
                            }
                        });


                        buttonPanel.add(editButton);
                        buttonPanel.add(deleteButton);

                        singlePanel.add(buttonPanel);
                        recipePanel.add(singlePanel);
                    }

                    JScrollPane scrollPane = new JScrollPane(recipePanel);
                    viewFrame.add(scrollPane, BorderLayout.CENTER);
                    viewFrame.setLocationRelativeTo(frame);
                    viewFrame.setVisible(true);
                }
            }
        });

        frame.add(panel);
        frame.setVisible(true);
    }
}
