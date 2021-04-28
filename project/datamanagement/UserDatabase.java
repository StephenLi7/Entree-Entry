package edu.upenn.cis350.project.datamanagement;

import java.util.List;
import java.util.Map;

public interface UserDatabase {
    public boolean searchUser(String username);
    public boolean login(String username, String password);
    public void addUser(String username, String password);
    public int getPasswordLength(String username);
    public String getHometown(String username);
    public String getBio(String username);
    public int getCalorieGoal(String username);
    public Map<?,?> getAllFoodEntries(String user);
    public List<?> getTodaysFoodEntries(String user);
    public List<?> getFoodEntries(String user, int year, int month, int day);
    public int getTodaysCalorieCount(String user);
    public int getCalorieCount(String user, int year, int month, int day);
    public int[] getWeekCalorieCount(String user, int year, int month, int day);

    public boolean changeUsername(String username, String password, String newUsername);
    public void changePassword(String username, String password, String newPassword);
    public void changeHometown(String username, String newHometown);
    public void changeBio(String username, String newBio);
    public void changeCalorieGoal(String username, int newCG);
    public List<String> getFriends(String username);
    public boolean addFriend(String user, String friend);
    public boolean removeFriend(String user, String friend);
    public Map<String, Boolean> getFriendRequests(String username);
    public boolean addFriendRequest(String user, String friend);
    public boolean removeFriendRequest(String user, String friend);
    public boolean confirmFriendRequest(String user, String friend);
    public void addFoodEntryForToday(String user, String foodType, int calories,
                                     double fat, double protein, double fiber,
                                     int sodium, double carbs);

    public int getFoodCalories(String foodName);
    public String getFoodCompany(String foodName);
    public double getFoodFat(String foodName);
    public int getFoodSugar(String foodName);
    public double getFoodProtein(String foodName);
    public int getFoodSalt(String foodName);
    public double getFoodFiber(String foodName);
    public double getFoodCarbs(String foodName);
    public List<String> getAllFoods();

}