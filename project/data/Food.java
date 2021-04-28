package edu.upenn.cis350.project.data;

public class Food {
    private String name;
    private int calories;

    public Food(String name, int calories) {
        this.name = name;
        this.calories = calories;
    }

    public void changeFoodName(String newName) {
        this.name = newName;
    }

    public void changeCalories(int newCal) {
        this.calories = newCal;
    }

    public String getFoodName() {
        return this.name;
    }

    public int getCalories() {
        return this.calories;
    }
}
