package edu.upenn.cis350.project.data;

import android.util.Log;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class User {
    private String username;
    private String password;
    private String hometown = "";
    private String bio ="";
    private int calorieGoal = 2000;
    private List<String> friends;
    private Map<String, Boolean> friendRequests;
    private Map<LocalDate, List<Food>> foodEntries;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        friends = new ArrayList<>();
        friendRequests = new TreeMap<>();
        foodEntries = new TreeMap<>();
    }

    public void changeUsername(String newName) {
        this.username = newName;
    }

    public void changePassword(String newPass) {
        this.password = newPass;
    }

    public void changeHometown(String newHometown) {
        this.hometown = newHometown;
    }

    public void changeBio(String newBio) {
        this.bio = newBio;
    }

    public void changeCalorieGoal(int newCG) { this.calorieGoal = newCG; }

    public boolean verifyPass(String password) {
        return this.password.equals(password);
    }

    public String getUsername() {
        return this.username;
    }

    public String getPassword() {
        return this.password;
    }

    public String getHometown() {
        return this.hometown;
    }

    public String getBio() {
        return this.bio;
    }

    public int getCalorieGoal() { return this.calorieGoal; }

    public List<String> getFriendList() {
        return friends;
    }

    public boolean addFriend(String username) {
        if (friends.contains(username)) {
            return false;
        }
        friends.add(username);
        return true;
    }

    public boolean removeFriend(String username) {
        if (friends.contains(username)) {
            friends.remove(username);
            return true;
        }
        return false;
    }

    public Map<String, Boolean> getFriendRequests() {
        return friendRequests;
    }

    //use when someone else is sending a request to this user
    public boolean addFriendInvite(String username) {
        if (friendRequests.containsKey(username)) {
            return false;
        }
        friendRequests.put(username, false);

        return true;
    }

    //use when this user is sending request to someone else
    public boolean addFriendRequest(String username) {
        if (friendRequests.containsKey(username)) {
            return false;
        }
        friendRequests.put(username, true);
        return true;
    }

    public boolean confirmFriendRequest(String username) {
        if (friendRequests.containsKey(username)) {
            friendRequests.put(username, true);
            return true;
        }
        return false;
    }

    public boolean removeFriendRequest(String username) {
        if (friendRequests.containsKey(username)) {
            friendRequests.remove(username);
            return true;
        }
        return false;
    }

    public Map<LocalDate, List<Food>> getFoodEntries() {
        return this.foodEntries;
    }

    public List<Food> getTodaysFoodEntries() {
        LocalDate today = LocalDate.now();
        return getFoodEntries(today);
    }

    public List<Food> getFoodEntriesForDate(int year, int month, int day) {
        LocalDate date = LocalDate.of(year, month, day);
        return getFoodEntries(date);
    }

    private List<Food> getFoodEntries(LocalDate date) {
        List<Food> food = new ArrayList<>();
        if (foodEntries.containsKey(date)) {
            food = foodEntries.get(date);
        }
        return food;
    }

    public int getCaloriesForDate(int year, int month, int day) {
        List<Food> dateFood = getFoodEntriesForDate(year, month, day);
        return getCalories(dateFood);
    }

    public int getCaloriesForToday() {
        List<Food> todaysFood = getTodaysFoodEntries();
        return getCalories(todaysFood);
    }

    public int[] getCaloriesPastWeek(int year, int month, int day) {
        int[] weekCalories = new int[7];
        LocalDate date = LocalDate.of(year, month, day);
        weekCalories[0] = getCalories(getFoodEntries(date));
        for (int d = 1; d < 7; d++) {
            weekCalories[d] = getCalories(getFoodEntries(date.minusDays(d)));
        }
        return weekCalories;
    }

    private int getCalories(List<Food> entries) {
        int calorieCount = 0;
        List<Food> empty = new ArrayList<>();
        if (!entries.equals(empty)) {
            for (Food f : entries) {
                if (!f.getFoodName().equals("Company")) {
                    calorieCount += f.getCalories();
                }
            }
        }
        return calorieCount;
    }

    public void addFoodEntryforToday(String name, int calories) {
        LocalDate today = LocalDate.now();
        addFoodEntry(today, name, calories);
    }

    private void addFoodEntry(LocalDate date, String name, int calories) {
        Food entry = new Food(name, calories);
        List<Food> food = new ArrayList<>();
        if (foodEntries.containsKey(date)) {
            food = foodEntries.get(date);
            food.add(entry);
            foodEntries.put(date, food);
        }
        food.add(entry);
        foodEntries.put(date, food);
    }
}
