package edu.upenn.cis350.project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.view.View;
import android.widget.Toast;

import java.util.HashMap;

import edu.upenn.cis350.project.datamanagement.MapUserDatabase;
import edu.upenn.cis350.project.datamanagement.NodeUserDatabase;
import edu.upenn.cis350.project.datamanagement.UserDatabase;
import edu.upenn.cis350.project.data.Food;

public class ManualEntryActivity extends AppCompatActivity {
    SpinnerActivity spinnerActivity = new SpinnerActivity();
    public int calorieCount = 0;
    public String text = "";
    public EditText currentAmount;
    HashMap<String, Integer> nutritionValues = new HashMap<String, Integer>();
    public Spinner spinner;
    private UserDatabase d;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Populate the HashMap of food values
        nutritionValues.put("Broccoli", 10);
        nutritionValues.put("Chicken", 100);
        nutritionValues.put("Beef", 150);
        nutritionValues.put("Pork", 165);
        nutritionValues.put("Kale", 5);
        nutritionValues.put("Avocado", 25);
        nutritionValues.put("Tomato", 10);
        nutritionValues.put("Lettuce", 7);
        nutritionValues.put("Potato", 30);
        nutritionValues.put("Brussels Sprouts", 10);
        nutritionValues.put("Apple", 15);
        nutritionValues.put("Banana", 25);
        nutritionValues.put("Orange", 20);
        nutritionValues.put("Pear", 15);
        nutritionValues.put("Blueberries", 15);
        nutritionValues.put("Strawberries", 20);
        //System.out.println(nutritionValues.keySet());

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manualentry);
        d = new NodeUserDatabase();
//        d = MapUserDatabase.getInstance();
        username = getIntent().getStringExtra("username");
        calorieCount = d.getTodaysCalorieCount(username);

        //getting spinner instance and applying OnClickListener
        spinner = (Spinner) findViewById(R.id.food_type_spinner);


        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.food_types_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(spinnerActivity);

        spinnerActivity.setContext(ManualEntryActivity.this);
    }

    public void onAddButtonClick(View v) {
        //Gets the string value of the selected item
        text = spinner.getSelectedItem().toString();
        if (text.equals("Other")) {
            EditText foodName = findViewById(R.id.custom_food_type);
            EditText foodCalories = findViewById(R.id.custom_food_calories);
            String foodType = foodName.getText().toString();
            if (foodType.matches("") || foodType == null) {
                Toast.makeText(this, "Error: empty food input.",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            String stringOtherCalories = foodCalories.getText().toString();
            if (stringOtherCalories.matches("") || stringOtherCalories == null) {
                Toast.makeText(this, "Error: empty calorie value.",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            int otherCalories = Integer.parseInt(stringOtherCalories);


            nutritionValues.put(foodType, otherCalories);
            text = foodType;
        }
        currentAmount = findViewById(R.id.amount_edit_text);
        int quantityEaten = Integer.parseInt(currentAmount.getText().toString());
        int input_calories = d.getFoodCalories(text) * quantityEaten;
        double input_fat = d.getFoodFat(text) * quantityEaten;
        double input_protein = d.getFoodProtein(text) * quantityEaten;
        double input_fiber = d.getFoodFiber(text) * quantityEaten;
        int input_sodium = d.getFoodSalt(text) * quantityEaten;
        double input_carbs = d.getFoodCarbs(text) * quantityEaten;

        // add food to database for user
        d.addFoodEntryForToday(username, text, input_calories, input_fat, input_protein,
                input_fiber, input_sodium, input_carbs);
        // update calorieCount variable
        calorieCount = d.getTodaysCalorieCount(username);

        Toast.makeText(this, text + " has been added! That was " +  input_calories +
                " added to your daily calorie count!", Toast.LENGTH_SHORT).show();
    }

    public void onCheckCaloriesClick(View v) {
        Toast.makeText(this, "Your total calories for today are: " + calorieCount,
                Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onBackPressed() {
        Intent i = new Intent();
        i.putExtra("username", username);
        setResult(RESULT_OK, i);
        finish();
    }
}