package edu.upenn.cis350.project.datamanagement;
import android.os.AsyncTask;
import android.util.Log;


import org.json.JSONArray;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class AccessWebTask extends AsyncTask<URL, String, String> {
    /*
    This method is called in background when this object's "execute"
    method is invoked.
    The arguments passed to "execute" are passed to this method.
    */
    private String field;
    public AccessWebTask(String field) {
        this.field = field;
    }

    protected String doInBackground(URL... urls) {
        try {
            URL url = urls[0];
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();
            Scanner in = new Scanner(url.openStream());
            String msg = in.nextLine();
            if (field.equals("getfriends")) {
                JSONArray jos = new JSONArray(msg);
                String friends = "";
                for (int i = 0; i < jos.length(); i++) {
                    friends += jos.getJSONObject(i).getString("friendName") + "\n";
                }
                return friends;
            } else if (field.equals("getrequests")) {
                JSONArray jos = new JSONArray(msg);
                String requests = "";
                for (int i = 0; i < jos.length(); i++) {
                    if (jos.getJSONObject(i).getString("rstatus").equals("true")) {
                        requests += "t";
                    } else {
                        requests += "f";
                    }
                    requests += jos.getJSONObject(i).getString("requestName") + "\n";
                }
                return requests;
            } else if (field.equals("getentries")) {
                JSONArray jos = new JSONArray(msg);
                String requests = "";
                for (int i = 0; i < jos.length(); i++) {
                    requests += jos.getJSONObject(i).getString("date") + "?" +
                            jos.getJSONObject(i).getString("foodName") + "?" +
                            jos.getJSONObject(i).getString("foodCalories") + "\n";
                }
                return requests;
            } else if (field.equals("getallfoods")) {
                JSONArray jos = new JSONArray(msg);
                String requests = "";
                for (int i = 0; i < jos.length(); i++) {
                    requests += jos.getJSONObject(i).getString("foodName") + "\n";
                }
                return requests;
            }
            JSONObject jo = new JSONObject(msg);
            if (jo.length() == 0) {
                return "";
            }
            if (field.equals("username")) {
                String username = jo.getString("username");
                return username;
            } else if (field.equals("password")) {
                String password = jo.getString("password");
                return password;
            } else if (field.equals("createNewUser")) {
                Log.v("added new person", jo.getString("username"));
                return "";
            } else if ( field.equals("hometown")) {
                String hometown = jo.getString("hometown");
                return hometown;
            } else if (field.equals("bio")) {
                String bio = jo.getString("bio");
                return bio;
            } else if (field.equals("caloriegoal")) {
                String caloriegoal = jo.getString("caloriegoal");
                return caloriegoal;
            } else if (field.equals("change")) {
                String username = jo.getString("username");
                if (username.isEmpty()) {
                    throw new IllegalArgumentException();
                }
            } else if (field.equals("addfriend")) {
                String username = jo.getString("username");
                return username;
            } else if (field.equals("addrequest")) {
                String username = jo.getString("username");
                return username;
            } else if (field.equals("addfood")) {
                String username = jo.getString("username");
                return username;
            } else if (field.equals("foodcalories")) {
                return jo.getString("calories");
            } else if (field.equals("foodcompany")) {
                return jo.getString("company");
            } else if (field.equals("foodfat")) {
                return jo.getString("fat");
            } else if (field.equals("foodsugar")) {
                return jo.getString("sugar");
            } else if (field.equals("foodprotein")) {
                return jo.getString("protein");
            } else if (field.equals("foodsalt")) {
                return jo.getString("salt");
            } else if (field.equals("foodfiber")) {
                return jo.getString("fiber");
            } else if (field.equals("foodcarbs")) {
                return jo.getString("carbs");
            }
            return "";
        }
        catch (Exception e) {
            Log.v("accesswebtask", e.toString());
            return "";
        }
    }

    /*
    This method is called in foreground after doInBackground finishes.
    It can access and update Views in user interface.
    */
    protected void onPostExecute(String msg) {
        // not implemented but you can use this if you’d like
    }
}