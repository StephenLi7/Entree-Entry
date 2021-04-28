package edu.upenn.cis350.project.datamanagement;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

import edu.upenn.cis350.project.data.Food;

public class NodeUserDatabase implements UserDatabase {

    private String getField(String field, String username) {
        try {
            URL url = new URL("http://10.0.2.2:3000/api?username=" + username);
            AccessWebTask task = new AccessWebTask(field);
            task.execute(url);
            String data = task.get();
            return data;
        } catch (Exception e) {
            Log.v("oops in searchUser NUD", e.toString());
            return "";
        }
    }

    private String changeField(String field, String username, String newValue) {
        try {
            URL url = new URL("http://10.0.2.2:3000/apichange?username2=" + username +
                    "&" + field + "2=" + newValue);
            AccessWebTask task = new AccessWebTask("change");
            task.execute(url);
            String data = task.get();
            return data;
        } catch (Exception e) {
            Log.v("oops in searchUser NUD", e.toString());
            return "";
        }
    }

    @Override
    public boolean searchUser(String username) {
        if (username == null) {
            throw new IllegalArgumentException();
        }
        return getField("username", username).length() != 0;
    }

    @Override
    public boolean login(String username, String password) {
        if (username == null || password == null) {
            throw new IllegalArgumentException();
        }
        String dbusername = getField("username", username);
        if (dbusername.length() == 0) {
            return false;
        }
        String dbpass = getField("password", username);
        if (password.equals(dbpass)) {
            return true;
        }
        return false;
    }

    @Override
    public void addUser(String username, String password) {
        if (username == null || password == null) {
            throw new IllegalArgumentException();
        }
        String dbusername = getField("username", username);
        if (dbusername.length() != 0) {
            throw new IllegalArgumentException();
        }
        try {
            URL url = new URL("http://10.0.2.2:3000/apicreate?username=" +
                    username + "&password=" + password);
            AccessWebTask task = new AccessWebTask("createNewUser");
            task.execute(url);
            String data = task.get();
        } catch (Exception e) {
            Log.v("error:", e.toString());
        }
    }

    @Override
    public int getPasswordLength(String username) {
        if (username == null) {
            throw new IllegalArgumentException();
        }
        if (!searchUser(username)) {
            throw new IllegalArgumentException();
        }
        String dbpass = getField("password", username);
        return dbpass.length();
    }

    @Override
    public String getHometown(String username) {
        if (username == null) {
            throw new IllegalArgumentException();
        }
        if (!searchUser(username)) {
            throw new IllegalArgumentException();
        }
        return getField("hometown", username);
    }

    @Override
    public String getBio(String username) {
        if (username == null) {
            throw new IllegalArgumentException();
        }
        if (!searchUser(username)) {
            throw new IllegalArgumentException();
        }
        return getField("bio", username);
    }

    @Override
    public int getCalorieGoal(String username) {
        if (username == null) {
            throw new IllegalArgumentException();
        }
        if (!searchUser(username)) {
            return 0;
//            throw new IllegalArgumentException();
        }
        String s = getField("caloriegoal", username);
        if (s.isEmpty()) {
            return 0;
        }
        return Integer.parseInt(s);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public Map<?, ?> getAllFoodEntries(String user) {
        Map<LocalDate, List<Food>> entries = new TreeMap<>();
        try {
            URL url = new URL("http://10.0.2.2:3000/getentries?username=" + user);
            AccessWebTask task = new AccessWebTask("getentries");
            task.execute(url);
            String results = task.get();
//            Log.v("fuckgetallfoodentries", results);
            String[] oneEntry = results.split("\n");
            for (String s : oneEntry) {
                String[] info = s.split("\\?");
                Log.v("fuckgetallfoodentries", info[0] + " " + info[1] + " " + info[2]);
                LocalDate d = LocalDate.parse(info[0]);
                Food f = new Food(info[1], Integer.parseInt(info[2]));
                if (!entries.containsKey(d)) {
                    entries.put(d, new ArrayList<Food>());
                }
                entries.get(d).add(f);
            }
        } catch (Exception e) {
            Log.v("oops", e.toString());
        }
        return entries;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public List<?> getTodaysFoodEntries(String user) {
        try {
            LocalDate d = LocalDate.now();
            return (List<?>) getAllFoodEntries(user).get(d);
        } catch (Exception e) {
            return new ArrayList<Food>();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public List<?> getFoodEntries(String user, int year, int month, int day) {
        try {
            LocalDate d = LocalDate.of(year, month, day);
            return (List<?>) getAllFoodEntries(user).get(d);
        } catch (Exception e) {
            return new ArrayList<Food>();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public int getTodaysCalorieCount(String user) {
        try {
            Map<LocalDate, List<Food>> m = (Map<LocalDate, List<Food>>) getAllFoodEntries(user);
            LocalDate d = LocalDate.now();
            int sum = 0;
            for (Food f : m.get(d)) {
                if (!f.getFoodName().equals("Company")) {
                    sum += f.getCalories();
                }
            }
            return sum;

        } catch (Exception e) {
            Log.v("oopstodayscaloriecount", e.toString());
        }
        return 0;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public int getCalorieCount(String user, int year, int month, int day) {
        try {
            Map<LocalDate, List<Food>> m = (Map<LocalDate, List<Food>>) getAllFoodEntries(user);
            LocalDate d = LocalDate.of(year, month, day);
            int sum = 0;
            for (Food f : m.get(d)) {
                if (!f.getFoodName().equals("Company")) {
                    sum += f.getCalories();
                }
            }
            return sum;

        } catch (Exception e) {
            Log.v("oopstodayscaloriecount", e.toString());
        }
        return 0;    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public int[] getWeekCalorieCount(String user, int year, int month, int day) {
        try {
            Map<LocalDate, List<Food>> m = (Map<LocalDate, List<Food>>) getAllFoodEntries(user);
            LocalDate d = LocalDate.of(year, month, day);
            int sum[] = new int[7];
            sum[0] = getCalorieCount(user, year, month, day);
            for (int i = 1; i < 7; i++) {
                d = d.minusDays(i);
                sum[i] = getCalorieCount(user, d.getYear(), d.getMonthValue(), d.getDayOfMonth());
                Log.v("fuck",""+sum[i]);
            }
            return sum;

        } catch (Exception e) {
            Log.v("oopstodayscaloriecount", e.toString());
        }
        return new int[7];
    }

    @Override
    public boolean changeUsername(String username, String password, String newUsername) {
        if (username == null || password == null || newUsername == null) {
            throw new IllegalArgumentException();
        }
        if (username.equals(newUsername)) {
            return false;
        }
        if (!searchUser(username)) {
            throw new IllegalArgumentException();
        }
        if (!login(username, password)) {
            throw new IllegalArgumentException();
        }
        String confirm = changeField("newusername", username, newUsername);
        return confirm.isEmpty();
    }

    @Override
    public void changePassword(String username, String password, String newPassword) {
        if (username == null || password == null || newPassword == null) {
            throw new IllegalArgumentException();
        }
        if (!searchUser(username)) {
            throw new IllegalArgumentException();
        }
        if (!login(username, password)) {
            throw new IllegalArgumentException();
        }
        changeField("password", username, newPassword);
    }

    @Override
    public void changeHometown(String username, String newHometown) {
        if (username == null || newHometown == null) {
            throw new IllegalArgumentException();
        }
        if (!searchUser(username)) {
            throw new IllegalArgumentException();
        }
        changeField("hometown", username, newHometown);
    }

    @Override
    public void changeBio(String username, String newBio) {
        if (username == null || newBio == null) {
            throw new IllegalArgumentException();
        }
        if (!searchUser(username)) {
            throw new IllegalArgumentException();
        }
        changeField("bio", username, newBio);
    }

    @Override
    public void changeCalorieGoal(String username, int newCG) {
        if (username == null) {
            throw new IllegalArgumentException();
        }
        if (!searchUser(username)) {
            throw new IllegalArgumentException();
        }
        try {
            URL url = new URL("http://10.0.2.2:3000/apichange?username2=" + username +
                    "&caloriegoal2=" + newCG);
            AccessWebTask task = new AccessWebTask("change");
            task.execute(url);
            task.get();
        } catch (Exception e) {
            Log.v("oops", e.toString());
        }
    }

    @Override
    public List<String> getFriends(String username) {
        try {
            URL url = new URL("http://10.0.2.2:3000/getfriends?username="+username);
            AccessWebTask task = new AccessWebTask("getfriends");
            task.execute(url);
            String friends = task.get();
            List<String> friendsArray = Arrays.asList(friends.split("\n"));
            Log.v("fuckgetfriends", friends + " " + friendsArray.size());

            return friendsArray;

        } catch (Exception e) {
            Log.v("oops", e.toString());
        }
        return null;
    }

    @Override
    public boolean addFriend(String user, String friend) {
        if (!searchUser(user) || !searchUser(friend)) {
            throw new IllegalArgumentException();
        }
        try {
            URL url = new URL("http://10.0.2.2:3000/addfriend?username="+user+
                    "&friendName="+friend);
            AccessWebTask task = new AccessWebTask("addfriend");
            task.execute(url);
            String confirm = task.get();
            Log.v("fuckaddfriend", confirm);
            return !confirm.isEmpty();

        } catch (Exception e) {
            Log.v("oops", e.toString());
        }
        return false;
    }

    @Override
    public boolean removeFriend(String user, String friend) {
        return false;
    }

    @Override
    public Map<String, Boolean> getFriendRequests(String username) {
        if (!searchUser(username)) {
            throw new IllegalArgumentException();
        }
        Map<String, Boolean> requests = new HashMap<>();
        try {
            URL url = new URL("http://10.0.2.2:3000/getrequests?username="+username);
            AccessWebTask task = new AccessWebTask("getrequests");
            task.execute(url);
            String results = task.get();
            String r[] = results.split("\n");
            for (String s : r) {
                if (s.substring(0, 1).equals("t")) {
                    requests.put(s.substring(1,s.length()), true);
                } else {
                    requests.put(s.substring(1, s.length()), false);
                }
            }
            Log.v("fuckgetrequests", results);
        } catch (Exception e) {
            Log.v("oops", e.toString());
        }
        return requests;
    }

    @Override
    public boolean addFriendRequest(String user, String friend) {
        if (!searchUser(user) || !searchUser(friend)) {
            throw new IllegalArgumentException();
        }
        if (user.equals(friend)) {
            throw new IllegalArgumentException();
        }
        if (getFriends(user).contains(friend) || getFriends(friend).contains(user)) {
            return false;
        }
        try {
            URL url = new URL("http://10.0.2.2:3000/addrequest?username="+user +
                    "&requestName="+ friend+ "&rstatus=true");
            AccessWebTask task = new AccessWebTask("addrequest");
            task.execute(url);
            String results = task.get();
            URL url2 = new URL("http://10.0.2.2:3000/addrequest?username="+friend +
                    "&requestName="+ user+ "&rstatus=false");
            AccessWebTask task2 = new AccessWebTask("addrequest");
            task2.execute(url2);
            String results2 = task2.get();
            return !results.isEmpty() && !results2.isEmpty();
        } catch (Exception e) {
            Log.v("oops", e.toString());
        }
        return false;
    }

    @Override
    public boolean removeFriendRequest(String user, String friend) {
        if (!searchUser(user) || !searchUser(friend)) {
            throw new IllegalArgumentException();
        }
        try {
            URL url = new URL("http://10.0.2.2:3000/removerequest?username="+user +
                    "&requestName="+ friend);
            AccessWebTask task = new AccessWebTask("addrequest");
            task.execute(url);
            String results = task.get();
            URL url2 = new URL("http://10.0.2.2:3000/removerequest?username="+friend +
                    "&requestName="+ user);
            AccessWebTask task2 = new AccessWebTask("addrequest");
            task.execute(url2);
            String results2 = task2.get();
            return !results.isEmpty() && !results2.isEmpty();
        } catch (Exception e) {
            Log.v("oops", e.toString());
        }
        return false;
    }

    @Override
    public boolean confirmFriendRequest(String user, String friend) {
        if (!searchUser(user) || !searchUser(friend)) {
            throw new IllegalArgumentException();
        }
        if (getFriends(user).contains(friend)) {
            return false;
        }
        try {
            removeFriendRequest(user, friend);
            URL url = new URL("http://10.0.2.2:3000/addrequest?username="+user +
                    "&requestName="+ friend + "&rstatus=true");
            AccessWebTask task = new AccessWebTask("addrequest");
            task.execute(url);
            String results = task.get();

            if (getFriendRequests(user).containsKey(friend) && getFriendRequests(friend).containsKey(user)) {
                addFriend(user, friend);
                addFriend(friend, user);
                removeFriendRequest(user, friend);
                removeFriendRequest(friend, user);
                return true;
            }
        } catch (Exception e) {
            Log.v("oops", e.toString());
        }
        return false;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void addFoodEntryForToday(String user, String foodType, int calories, double fat,
                                     double protein, double fiber, int sodium, double carbs) {
        try {
            LocalDate d = LocalDate.now();
            String date = d.toString();

            URL url = new URL("http://10.0.2.2:3000/addentry?username="+user +
                    "&date="+ date + "&foodName=" + foodType + "&foodCalories=" + calories);
            AccessWebTask task = new AccessWebTask("addfood");
            task.execute(url);
            String results = task.get();
            return;
        } catch (Exception e) {
            Log.v("oops", e.toString());
        }
    }

    @Override
    public int getFoodCalories(String foodName) {
        return Integer.parseInt(getFoodField("foodcalories", foodName));
    }

    @Override
    public String getFoodCompany(String foodName) {
        return getFoodField("foodcompany", foodName);
    }

    @Override
    public double getFoodFat(String foodName) {
        double foodFat = Double.parseDouble(getFoodField("foodfat", foodName));
        return foodFat;
    }

    @Override
    public int getFoodSugar(String foodName) {
        return Integer.parseInt(getFoodField("foodsugar", foodName));
    }

    @Override
    public double getFoodProtein(String foodName) {
        double foodProtein = Double.parseDouble(getFoodField("foodprotein", foodName));
        return foodProtein;
    }

    @Override
    public int getFoodSalt(String foodName) {
        return Integer.parseInt(getFoodField("foodsalt", foodName));
    }

    @Override
    public double getFoodFiber(String foodName) {
        double foodFiber = Double.parseDouble(getFoodField("foodfiber", foodName));
        return foodFiber;
    }

    @Override
    public double getFoodCarbs(String foodName) {
        double foodCarbs = Double.parseDouble(getFoodField("foodcarbs", foodName));
        return foodCarbs;
    }

    private String getFoodField(String field, String foodName) {
        try {
            URL url = new URL("http://10.0.2.2:3000/apifoods?foodName=" + foodName);
            AccessWebTask task = new AccessWebTask(field);
            task.execute(url);
            String data = task.get();
            return data;
        } catch (Exception e) {
            Log.v("oops in searchUser NUD", e.toString());
            return "";
        }
    }

    @Override
    public List<String> getAllFoods() {
        List<String> foods = new ArrayList<>();
        try {
            URL url = new URL("http://10.0.2.2:3000/apigetallfoods?");
            AccessWebTask task = new AccessWebTask("getallfoods");
            task.execute(url);
            String data = task.get();
            Log.v("fuckfoods", data);
            foods = Arrays.asList(data.split("\n"));
        } catch (Exception e) {
            Log.v("oops in searchUser NUD", e.toString());
        }
        return foods;
    }
}