package edu.upenn.cis350.project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.Spinner;
import android.view.View;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


public class DietPlannerActivity extends AppCompatActivity {
    private TextView foodList;
    private TextView calories;
    private TextView protein;
    private TextView carbs;
    public int calorieCount = 0;
    public double proteinCount = 0.0;
    public double carbCount = 0.0;
    String message = "";
    int switchCase = 0;
    int randomMin = 1;
    public String text = "";
    HashMap<String, List<Double>> foodOptions = new HashMap<>();
    private String username;
    List<String> foods = new ArrayList<String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dietplanner);
        username = getIntent().getStringExtra("username");
        foodList = (TextView) findViewById(R.id.foodList);
        calories = (TextView) findViewById(R.id.mealCalories);
        protein = (TextView) findViewById(R.id.mealProtein);
        carbs = (TextView) findViewById(R.id.mealCarbs);

        foodOptions.put("Chicken", new ArrayList<Double>(Arrays.asList(165.0, 31.0, 0.0)));
        foodOptions.put("Beef", new ArrayList<Double>(Arrays.asList(217.0, 26.1, 0.0)));
        foodOptions.put("Pork", new ArrayList<Double>(Arrays.asList(297.0, 25.7, 0.0)));
        foodOptions.put("Broccoli", new ArrayList<Double>(Arrays.asList(34.0, 2.5, 6.0)));
        foodOptions.put("Kale", new ArrayList<Double>(Arrays.asList(50.0, 3.3, 10.0)));
        foodOptions.put("Avocado", new ArrayList<Double>(Arrays.asList(160.0, 2.0, 8.5)));
        foodOptions.put("Tomato", new ArrayList<Double>(Arrays.asList(18.0, 0.9, 3.9)));
        foodOptions.put("Lettuce", new ArrayList<Double>(Arrays.asList(14.0, 0.9, 3.2)));
        foodOptions.put("Potato", new ArrayList<Double>(Arrays.asList(93.0, 2.5, 21.2)));
        foodOptions.put("Brussels Sprouts", new ArrayList<Double>(Arrays.asList(36.0, 2.5, 7.1)));
        foodOptions.put("Apple", new ArrayList<Double>(Arrays.asList(52.0, 0.3, 13.8)));
        foodOptions.put("Orange", new ArrayList<Double>(Arrays.asList(47.0, 0.9, 11.7)));
        foodOptions.put("Banana", new ArrayList<Double>(Arrays.asList(89.0, 1.1, 22.8)));
        foodOptions.put("Pear", new ArrayList<Double>(Arrays.asList(58.0, 0.4, 15.5)));
        foodOptions.put("Blueberries", new ArrayList<Double>(Arrays.asList(57.0, 0.7, 14.5)));
        foodOptions.put("Strawberries", new ArrayList<Double>(Arrays.asList(33.0, 0.7, 8.0)));
    }

    public void onMealCreateClick(View v) {
        calorieCount = 0;
        proteinCount = 0.0;
        carbCount = 0.0;
        message = "";
        foods.clear();
        DecimalFormat df = new DecimalFormat("#.#");
        EditText mealCalories = findViewById(R.id.calories_edit_text);
        String desiredCalories = mealCalories.getText().toString();
        int caloriesConstraint = Integer.parseInt(desiredCalories);

        int mealSelection = (int)(Math.random() * 2) + randomMin;
        Log.v("mealSelection", "the mealSelection is " + mealSelection);

        if (caloriesConstraint < 200){
            switchCase = 1;
        } else if (caloriesConstraint >= 200 && caloriesConstraint < 300) {
            switchCase = 2;
        } else if (caloriesConstraint >= 300 && caloriesConstraint < 400) {
            switchCase = 3;
        } else if (caloriesConstraint >= 400 && caloriesConstraint < 500) {
            switchCase = 4;
        } else if (caloriesConstraint >= 500 && caloriesConstraint < 600) {
            switchCase = 5;
        } else if (caloriesConstraint >= 600 && caloriesConstraint < 700) {
            switchCase = 6;
        } else if (caloriesConstraint >= 700 && caloriesConstraint < 800) {
            switchCase = 7;
        } else {
            switchCase = 8;
        }

        switch (switchCase) {
            case 1:
                message = "Seems like what you're inputting is more of a snack than a meal!" + '\n'
                + "Try increasing the desired calorie intake for a meal to get a plan :)";
                foodList.setText(message);
                break;
            case 2:
                if (mealSelection == 1) {
                    foods.add("Avocado");
                    calorieCount += foodOptions.get("Avocado").get(0);
                    proteinCount += foodOptions.get("Avocado").get(1);
                    carbCount += foodOptions.get("Avocado").get(2);
                    foods.add("Tomato");
                    calorieCount += foodOptions.get("Tomato").get(0);
                    proteinCount += foodOptions.get("Tomato").get(1);
                    carbCount += foodOptions.get("Tomato").get(2);
                    foods.add("Blueberries");
                    calorieCount += foodOptions.get("Blueberries").get(0);
                    proteinCount += foodOptions.get("Blueberries").get(1);
                    carbCount += foodOptions.get("Blueberries").get(2);
                } else {
                    foods.add("Potato");
                    calorieCount += foodOptions.get("Potato").get(0);
                    proteinCount += foodOptions.get("Potato").get(1);
                    carbCount += foodOptions.get("Potato").get(2);
                    foods.add("Brussels Sprouts");
                    calorieCount += foodOptions.get("Brussels Sprouts").get(0);
                    proteinCount += foodOptions.get("Brussels Sprouts").get(1);
                    carbCount += foodOptions.get("Brussels Sprouts").get(2);
                    foods.add("Banana");
                    calorieCount += foodOptions.get("Banana").get(0);
                    proteinCount += foodOptions.get("Banana").get(1);
                    carbCount += foodOptions.get("Banana").get(2);
                }
                for (String s : foods) {
                    message += s + '\n';
                }
                foodList.setText(message);
                calories.setText("Calorie total: " + df.format(calorieCount));
                protein.setText("Protein total: " + df.format(proteinCount));
                carbs.setText("Carbs total: " + df.format(carbCount));

                break;

            case 3:
                if (mealSelection == 1) {
                    foods.add("Avocado");
                    calorieCount += foodOptions.get("Avocado").get(0);
                    proteinCount += foodOptions.get("Avocado").get(1);
                    carbCount += foodOptions.get("Avocado").get(2);
                    foods.add("Potato");
                    calorieCount += foodOptions.get("Potato").get(0);
                    proteinCount += foodOptions.get("Potato").get(1);
                    carbCount += foodOptions.get("Potato").get(2);
                    foods.add("Tomato");
                    calorieCount += foodOptions.get("Tomato").get(0);
                    proteinCount += foodOptions.get("Tomato").get(1);
                    carbCount += foodOptions.get("Tomato").get(2);
                    foods.add("Blueberries");
                    calorieCount += foodOptions.get("Blueberries").get(0);
                    proteinCount += foodOptions.get("Blueberries").get(1);
                    carbCount += foodOptions.get("Blueberries").get(2);
                } else {
                    foods.add("Avocado");
                    calorieCount += foodOptions.get("Avocado").get(0);
                    proteinCount += foodOptions.get("Avocado").get(1);
                    carbCount += foodOptions.get("Avocado").get(2);
                    foods.add("Kale");
                    calorieCount += foodOptions.get("Kale").get(0);
                    proteinCount += foodOptions.get("Kale").get(1);
                    carbCount += foodOptions.get("Kale").get(2);
                    foods.add("Brussels Sprouts");
                    calorieCount += foodOptions.get("Brussels Sprouts").get(0);
                    proteinCount += foodOptions.get("Brussels Sprouts").get(1);
                    carbCount += foodOptions.get("Brussels Sprouts").get(2);
                    foods.add("Banana");
                    calorieCount += foodOptions.get("Banana").get(0);
                    proteinCount += foodOptions.get("Banana").get(1);
                    carbCount += foodOptions.get("Banana").get(2);
                }
                for (String s : foods) {
                    message += s + '\n';
                }
                foodList.setText(message);
                calories.setText("Calorie total: " + df.format(calorieCount));
                protein.setText("Protein total: " + df.format(proteinCount));
                carbs.setText("Carbs total: " + df.format(carbCount));

                break;

            case 4:
                if (mealSelection == 1) {
                    foods.add("Chicken");
                    calorieCount += foodOptions.get("Chicken").get(0);
                    proteinCount += foodOptions.get("Chicken").get(1);
                    carbCount += foodOptions.get("Chicken").get(2);
                    foods.add("Avocado");
                    calorieCount += foodOptions.get("Avocado").get(0);
                    proteinCount += foodOptions.get("Avocado").get(1);
                    carbCount += foodOptions.get("Avocado").get(2);
                    foods.add("Kale");
                    calorieCount += foodOptions.get("Kale").get(0);
                    proteinCount += foodOptions.get("Kale").get(1);
                    carbCount += foodOptions.get("Kale").get(2);
                    foods.add("Blueberries");
                    calorieCount += foodOptions.get("Blueberries").get(0);
                    proteinCount += foodOptions.get("Blueberries").get(1);
                    carbCount += foodOptions.get("Blueberries").get(2);
                } else {
                    foods.add("Beef");
                    calorieCount += foodOptions.get("Beef").get(0);
                    proteinCount += foodOptions.get("Beef").get(1);
                    carbCount += foodOptions.get("Beef").get(2);
                    foods.add("Potato");
                    calorieCount += foodOptions.get("Potato").get(0);
                    proteinCount += foodOptions.get("Potato").get(1);
                    carbCount += foodOptions.get("Potato").get(2);
                    foods.add("Brussels Sprouts");
                    calorieCount += foodOptions.get("Brussels Sprouts").get(0);
                    proteinCount += foodOptions.get("Brussels Sprouts").get(1);
                    carbCount += foodOptions.get("Brussels Sprouts").get(2);
                    foods.add("Banana");
                    calorieCount += foodOptions.get("Banana").get(0);
                    proteinCount += foodOptions.get("Banana").get(1);
                    carbCount += foodOptions.get("Banana").get(2);
                }
                for (String s : foods) {
                    message += s + '\n';
                }
                foodList.setText(message);
                calories.setText("Calorie total: " + df.format(calorieCount));
                protein.setText("Protein total: " + df.format(proteinCount));
                carbs.setText("Carbs total: " + df.format(carbCount));

                break;

            case 5:
                if (mealSelection == 1) {
                    foods.add("Pork");
                    calorieCount += foodOptions.get("Pork").get(0);
                    proteinCount += foodOptions.get("Pork").get(1);
                    carbCount += foodOptions.get("Pork").get(2);
                    foods.add("Potato");
                    calorieCount += foodOptions.get("Potato").get(0);
                    proteinCount += foodOptions.get("Potato").get(1);
                    carbCount += foodOptions.get("Potato").get(2);
                    foods.add("Kale");
                    calorieCount += foodOptions.get("Kale").get(0);
                    proteinCount += foodOptions.get("Kale").get(1);
                    carbCount += foodOptions.get("Kale").get(2);
                    foods.add("Strawberries");
                    calorieCount += foodOptions.get("Strawberries").get(0);
                    proteinCount += foodOptions.get("Strawberries").get(1);
                    carbCount += foodOptions.get("Strawberries").get(2);
                    foods.add("Pear");
                    calorieCount += foodOptions.get("Pear").get(0);
                    proteinCount += foodOptions.get("Pear").get(1);
                    carbCount += foodOptions.get("Pear").get(2);
                } else {
                    foods.add("Chicken");
                    calorieCount += foodOptions.get("Chicken").get(0);
                    proteinCount += foodOptions.get("Chicken").get(1);
                    carbCount += foodOptions.get("Chicken").get(2);
                    foods.add("Chicken");
                    calorieCount += foodOptions.get("Chicken").get(0);
                    proteinCount += foodOptions.get("Chicken").get(1);
                    carbCount += foodOptions.get("Chicken").get(2);
                    foods.add("Avocado");
                    calorieCount += foodOptions.get("Avocado").get(0);
                    proteinCount += foodOptions.get("Avocado").get(1);
                    carbCount += foodOptions.get("Avocado").get(2);
                    foods.add("Kale");
                    calorieCount += foodOptions.get("Kale").get(0);
                    proteinCount += foodOptions.get("Kale").get(1);
                    carbCount += foodOptions.get("Kale").get(2);
                    foods.add("Tomato");
                    calorieCount += foodOptions.get("Tomato").get(0);
                    proteinCount += foodOptions.get("Tomato").get(1);
                    carbCount += foodOptions.get("Tomato").get(2);
                }

                for (String s : foods) {
                    message += s + '\n';
                }
                foodList.setText(message);
                calories.setText("Calorie total: " + df.format(calorieCount));
                protein.setText("Protein total: " + df.format(proteinCount));
                carbs.setText("Carbs total: " + df.format(carbCount));

                break;

            case 6:
                if (mealSelection == 1) {
                    foods.add("Pork");
                    calorieCount += foodOptions.get("Pork").get(0);
                    proteinCount += foodOptions.get("Pork").get(1);
                    carbCount += foodOptions.get("Pork").get(2);
                    foods.add("Avocado");
                    calorieCount += foodOptions.get("Avocado").get(0);
                    proteinCount += foodOptions.get("Avocado").get(1);
                    carbCount += foodOptions.get("Avocado").get(2);
                    foods.add("Kale");
                    calorieCount += foodOptions.get("Kale").get(0);
                    proteinCount += foodOptions.get("Kale").get(1);
                    carbCount += foodOptions.get("Kale").get(2);
                    foods.add("Tomato");
                    calorieCount += foodOptions.get("Tomato").get(0);
                    proteinCount += foodOptions.get("Tomato").get(1);
                    carbCount += foodOptions.get("Tomato").get(2);
                    foods.add("Banana");
                    calorieCount += foodOptions.get("Banana").get(0);
                    proteinCount += foodOptions.get("Banana").get(1);
                    carbCount += foodOptions.get("Banana").get(2);
                } else {
                    foods.add("Beef");
                    calorieCount += foodOptions.get("Beef").get(0);
                    proteinCount += foodOptions.get("Beef").get(1);
                    carbCount += foodOptions.get("Beef").get(2);
                    foods.add("Avocado");
                    calorieCount += foodOptions.get("Avocado").get(0);
                    proteinCount += foodOptions.get("Avocado").get(1);
                    carbCount += foodOptions.get("Avocado").get(2);
                    foods.add("Potato");
                    calorieCount += foodOptions.get("Potato").get(0);
                    proteinCount += foodOptions.get("Potato").get(1);
                    carbCount += foodOptions.get("Potato").get(2);
                    foods.add("Broccoli");
                    calorieCount += foodOptions.get("Broccoli").get(0);
                    proteinCount += foodOptions.get("Broccoli").get(1);
                    carbCount += foodOptions.get("Broccoli").get(2);
                    foods.add("Strawberries");
                    calorieCount += foodOptions.get("Strawberries").get(0);
                    proteinCount += foodOptions.get("Strawberries").get(1);
                    carbCount += foodOptions.get("Strawberries").get(2);
                    foods.add("Orange");
                    calorieCount += foodOptions.get("Orange").get(0);
                    proteinCount += foodOptions.get("Orange").get(1);
                    carbCount += foodOptions.get("Orange").get(2);
                }

                for (String s : foods) {
                    message += s + '\n';
                }
                foodList.setText(message);
                calories.setText("Calorie total: " + df.format(calorieCount));
                protein.setText("Protein total: " + df.format(proteinCount));
                carbs.setText("Carbs total: " + df.format(carbCount));

                break;

            case 7:
                if (mealSelection == 1) {
                    foods.add("Beef");
                    calorieCount += foodOptions.get("Beef").get(0);
                    proteinCount += foodOptions.get("Beef").get(1);
                    carbCount += foodOptions.get("Beef").get(2);
                    foods.add("Beef");
                    calorieCount += foodOptions.get("Beef").get(0);
                    proteinCount += foodOptions.get("Beef").get(1);
                    carbCount += foodOptions.get("Beef").get(2);
                    foods.add("Potato");
                    calorieCount += foodOptions.get("Potato").get(0);
                    proteinCount += foodOptions.get("Potato").get(1);
                    carbCount += foodOptions.get("Potato").get(2);
                    foods.add("Potato");
                    calorieCount += foodOptions.get("Potato").get(0);
                    proteinCount += foodOptions.get("Potato").get(1);
                    carbCount += foodOptions.get("Potato").get(2);
                    foods.add("Kale");
                    calorieCount += foodOptions.get("Kale").get(0);
                    proteinCount += foodOptions.get("Kale").get(1);
                    carbCount += foodOptions.get("Kale").get(2);
                    foods.add("Banana");
                    calorieCount += foodOptions.get("Banana").get(0);
                    proteinCount += foodOptions.get("Banana").get(1);
                    carbCount += foodOptions.get("Banana").get(2);
                } else {
                    foods.add("Chicken");
                    calorieCount += foodOptions.get("Chicken").get(0);
                    proteinCount += foodOptions.get("Chicken").get(1);
                    carbCount += foodOptions.get("Chicken").get(2);
                    foods.add("Pork");
                    calorieCount += foodOptions.get("Pork").get(0);
                    proteinCount += foodOptions.get("Pork").get(1);
                    carbCount += foodOptions.get("Pork").get(2);
                    foods.add("Avocado");
                    calorieCount += foodOptions.get("Avocado").get(0);
                    proteinCount += foodOptions.get("Avocado").get(1);
                    carbCount += foodOptions.get("Avocado").get(2);
                    foods.add("Broccoli");
                    calorieCount += foodOptions.get("Broccoli").get(0);
                    proteinCount += foodOptions.get("Broccoli").get(1);
                    carbCount += foodOptions.get("Broccoli").get(2);
                    foods.add("Brussels Sprouts");
                    calorieCount += foodOptions.get("Brussels Sprouts").get(0);
                    proteinCount += foodOptions.get("Brussels Sprouts").get(1);
                    carbCount += foodOptions.get("Brussels Sprouts").get(2);
                    foods.add("Apple");
                    calorieCount += foodOptions.get("Apple").get(0);
                    proteinCount += foodOptions.get("Apple").get(1);
                    carbCount += foodOptions.get("Apple").get(2);
                    foods.add("Orange");
                    calorieCount += foodOptions.get("Orange").get(0);
                    proteinCount += foodOptions.get("Orange").get(1);
                    carbCount += foodOptions.get("Orange").get(2);
                }

                for (String s : foods) {
                    message += s + '\n';
                }
                foodList.setText(message);
                calories.setText("Calorie total: " + df.format(calorieCount));
                protein.setText("Protein total: " + df.format(proteinCount));
                carbs.setText("Carbs total: " + df.format(carbCount));

                break;

            case 8:
                message = "Daily Calorie recommendations are 2,000. You should try to eat 3 " +
                        "meals a day so a single meal isn't advised to be this high in calories.";
                foodList.setText(message);
                break;
        }

    }


    @Override
    public void onBackPressed() {
        Intent i = new Intent();
        i.putExtra("username", username);
        setResult(RESULT_OK, i);
        finish();
    }
}
