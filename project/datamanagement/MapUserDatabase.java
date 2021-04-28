package edu.upenn.cis350.project.datamanagement;

import android.util.Log;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.upenn.cis350.project.data.User;
import edu.upenn.cis350.project.data.Food;

public class MapUserDatabase implements UserDatabase{
    private Map<String, User> d;

    private MapUserDatabase() {
        d = new HashMap<String, User>();
    }

    private static MapUserDatabase instance = new MapUserDatabase();
    public static MapUserDatabase getInstance() {
        return instance;
    }


    @Override
    public boolean searchUser(String username) {
        if (username == null) {
            throw new IllegalArgumentException();
        }
        return d.containsKey(username);
    }

    @Override
    public boolean login(String username, String password) {
        if (username == null || password == null) {
            throw new IllegalArgumentException();
        }
        if (d.containsKey(username)) {
            return d.get(username).verifyPass(password);
        }
        return false;
    }

    @Override
    public void addUser(String username, String password) {
        if (username == null || password == null) {
            throw new IllegalArgumentException();
        }
        if (d.containsKey(username)) {
            return;
        }
        d.put(username, new User(username, password));
    }

    @Override
    public int getPasswordLength(String username) {
        if (!d.containsKey(username)) {
            throw new IllegalArgumentException();
        }
        return d.get(username).getPassword().length();
    }

    @Override
    public String getHometown(String username) {
        if (!d.containsKey(username)) {
            throw new IllegalArgumentException();
        }
        return d.get(username).getHometown();
    }

    @Override
    public String getBio(String username) {
        if (!d.containsKey(username)) {
            throw new IllegalArgumentException();
        }
        return d.get(username).getBio();
    }

    @Override
    public int getCalorieGoal(String username) {
        if (!d.containsKey(username)) {
            throw new IllegalArgumentException();
        }
        return d.get(username).getCalorieGoal();
    }

    @Override
    public boolean changeUsername(String username, String password, String newUsername) {
        if (username == newUsername) {
            return false;
        }
        if (d.containsKey(newUsername)) {
            return false;
        }
        if (!d.get(username).verifyPass(password)) {
            throw new IllegalArgumentException();
        }
        User temp = d.get(username);
        temp.changeUsername(newUsername);
        d.put(newUsername, temp);
        d.remove(username);
        return true;
    }

    @Override
    public void changePassword(String username, String password, String newPassword) {
        if (!d.get(username).verifyPass(password)) {
            throw new IllegalArgumentException();
        }
        d.get(username).changePassword(newPassword);
    }

    @Override
    public void changeHometown(String username, String newHometown) {
        d.get(username).changeHometown(newHometown);
    }

    @Override
    public void changeBio(String username, String newBio) {
        d.get(username).changeBio(newBio);
    }

    @Override
    public void changeCalorieGoal(String username, int newCG) {
        d.get(username).changeCalorieGoal(newCG);
    }

    @Override
    public List<String> getFriends(String username) {
        if (!d.containsKey(username)) {
            throw new IllegalArgumentException();
        }
        return d.get(username).getFriendList();
    }

    @Override
    public boolean addFriend(String user, String friend) {
        if (!d.containsKey(user) || !d.containsKey(friend)) {
            throw new IllegalArgumentException();
        }
        if (d.get(user).getFriendRequests().get(friend) &&
                d.get(friend).getFriendRequests().get(user)) {
            d.get(user).removeFriendRequest(friend);
            d.get(friend).removeFriendRequest(user);
            d.get(user).addFriend(friend);
            d.get(friend).addFriend(user);
            return true;
        }
        throw new IllegalStateException();
    }

    @Override
    public boolean removeFriend(String user, String friend) {
        if (!d.containsKey(user) || !d.containsKey(friend)) {
            throw new IllegalArgumentException();
        }
        d.get(user).removeFriend(friend);
        d.get(friend).removeFriend(user);
        return true;
    }

    @Override
    public Map<String, Boolean> getFriendRequests(String username) {
        if (!d.containsKey(username)) {
            throw new IllegalArgumentException();
        }
        return d.get(username).getFriendRequests();
    }

    @Override
    public boolean addFriendRequest(String user, String friend) {
        if (!d.containsKey(user) || !d.containsKey(friend)) {
            throw new IllegalArgumentException();
        }
        if (user.equals(friend)) {
            throw new IllegalArgumentException();
        }
        if (d.get(user).getFriendList().contains(friend) ||
                d.get(user).getFriendRequests().containsKey(friend)) {
            return false;
        }
        d.get(user).addFriendRequest(friend);
        d.get(friend).addFriendInvite(user);
        return true;
    }

    @Override
    public boolean removeFriendRequest(String user, String friend) {
        if (!d.containsKey(user) || !d.containsKey(friend)) {
            throw new IllegalArgumentException();
        }
        d.get(user).removeFriendRequest(friend);
        d.get(friend).removeFriendRequest(user);
        return true;
    }

    @Override
    public boolean confirmFriendRequest(String user, String friend) {
        if (!d.containsKey(user) || !d.containsKey(friend)) {
            throw new IllegalArgumentException();
        }
        if (d.get(user).confirmFriendRequest(friend)) {
            addFriend(user, friend);
            return true;
        }
        return false;
    }

    @Override
    public Map<?,?> getAllFoodEntries(String user) {
        if (!d.containsKey(user)) {
            throw new IllegalArgumentException();
        }
        return d.get(user).getFoodEntries();
    }

    @Override
    public List<?> getTodaysFoodEntries(String user) {
        if (!d.containsKey(user)) {
            throw new IllegalArgumentException();
        }
        return d.get(user).getTodaysFoodEntries();
    }

    @Override
    public List<?> getFoodEntries(String user, int year, int month, int day) {
        if (!d.containsKey(user)) {
            throw new IllegalArgumentException();
        }
        return d.get(user).getFoodEntriesForDate(year, month, day);
    }

    @Override
    public int getTodaysCalorieCount(String user) {
        if (!d.containsKey(user)) {
            throw new IllegalArgumentException();
        }
        return d.get(user).getCaloriesForToday();
    }

    @Override
    public int getCalorieCount(String user, int year, int month, int day) {
        if (!d.containsKey(user)) {
            throw new IllegalArgumentException();
        }
        return d.get(user).getCaloriesForDate(year, month, day);
    }

    @Override
    public int[] getWeekCalorieCount(String user, int year, int month, int day) {
        if (!d.containsKey(user)) {
            throw new IllegalArgumentException();
        }
        return d.get(user).getCaloriesPastWeek(year, month, day);
    }

    @Override
    public void addFoodEntryForToday(String user, String foodType, int calories, double fat,
                                     double protein, double fiber, int sodium, double carbs) {
        if (!d.containsKey(user)) {
            throw new IllegalArgumentException();
        }
        Log.v("addFEfT", foodType);
        Log.v("cal/score", String.valueOf(calories));
        d.get(user).addFoodEntryforToday(foodType, calories);
    }

    @Override
    public int getFoodCalories(String foodName) {
        return 0;
    }

    @Override
    public String getFoodCompany(String foodName) {
        return null;
    }

    @Override
    public double getFoodFat(String foodName) {
        return 0.0;
    }

    @Override
    public int getFoodSugar(String foodName) {
        return 0;
    }

    @Override
    public double getFoodProtein(String foodName) {
        return 0.0;
    }

    @Override
    public int getFoodSalt(String foodName) {
        return 0;
    }

    @Override
    public double getFoodFiber(String foodName) {
        return 0.0;
    }

    @Override
    public double getFoodCarbs(String foodName) {
        return 0.0;
    }

    @Override
    public List<String> getAllFoods() {
        return null;
    }
}