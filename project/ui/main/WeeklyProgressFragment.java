package edu.upenn.cis350.project.ui.main;

import android.app.DatePickerDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Button;

import org.w3c.dom.Text;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import edu.upenn.cis350.project.R;
import edu.upenn.cis350.project.data.Food;
import edu.upenn.cis350.project.data.User;
import edu.upenn.cis350.project.datamanagement.MapUserDatabase;
import edu.upenn.cis350.project.datamanagement.NodeUserDatabase;
import edu.upenn.cis350.project.datamanagement.UserDatabase;

public class WeeklyProgressFragment extends Fragment {

    private UserDatabase d;
    private String username;
    private LocalDate day1; // today (day 7 is 7 days back)
    private LocalDate day2;
    private LocalDate day3;
    private LocalDate day4;
    private LocalDate day5;
    private LocalDate day6;
    private LocalDate day7;
    private int calDay1;
    private int calDay2;
    private int calDay3;
    private int calDay4;
    private int calDay5;
    private int calDay6;
    private int calDay7;
    private int dailyCalGoal;
    private int calWeek;
    private int weeklyCalGoal;
    private String nutritionType;

    private TextView day1Text;
    private TextView day2Text;
    private TextView day3Text;
    private TextView day4Text;
    private TextView day5Text;
    private TextView day6Text;
    private TextView day7Text;
    private TextView calorieCurrentWeekText;
    private TextView calorieGoalWeekText;
    private Button weekSelection;
    private DatePickerDialog picker;
    private Spinner nutritionTypeSpinner;

    private ProgressBar day1PB;
    private ProgressBar day2PB;
    private ProgressBar day3PB;
    private ProgressBar day4PB;
    private ProgressBar day5PB;
    private ProgressBar day6PB;
    private ProgressBar day7PB;
    private ProgressBar weekPB;

    public WeeklyProgressFragment(String username) {
        this.username = username;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        d = MapUserDatabase.getInstance();
        d = new NodeUserDatabase();

        day1 = LocalDate.now();
        calculateDates(day1);
        nutritionType = "calories";
        calculateCalorieGoals(username);
        calculateCaloriesConsumed(nutritionType);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_weekly_progress, container, false);

        day1Text = (TextView) view.findViewById(R.id.day1);
        day2Text = (TextView) view.findViewById(R.id.day2);
        day3Text = (TextView) view.findViewById(R.id.day3);
        day4Text = (TextView) view.findViewById(R.id.day4);
        day5Text = (TextView) view.findViewById(R.id.day5);
        day6Text = (TextView) view.findViewById(R.id.day6);
        day7Text = (TextView) view.findViewById(R.id.day7);
        calorieCurrentWeekText = (TextView) view.findViewById(R.id.calorieCurrentWeekText);
        calorieGoalWeekText = (TextView) view.findViewById(R.id.calorieGoalWeekText);
        weekPB = (ProgressBar) view.findViewById(R.id.progressBarWeek);
        day1PB = (ProgressBar) view.findViewById(R.id.progressBar1);
        day2PB = (ProgressBar) view.findViewById(R.id.progressBar2);
        day3PB = (ProgressBar) view.findViewById(R.id.progressBar3);
        day4PB = (ProgressBar) view.findViewById(R.id.progressBar4);
        day5PB = (ProgressBar) view.findViewById(R.id.progressBar5);
        day6PB = (ProgressBar) view.findViewById(R.id.progressBar6);
        day7PB = (ProgressBar) view.findViewById(R.id.progressBar7);



        updateAllProgressViews();

        nutritionTypeSpinner = (Spinner) view.findViewById(R.id.nutritionTypeSpinnerWeek);
        nutritionTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View view, int position, long id) {
                String nutType = (String) arg0.getItemAtPosition(position);
                nutritionType = nutType;
                calculateCaloriesConsumed(nutritionType);
                updateAllProgressViews();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(view.getContext(),
                R.array.nutrition_types, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        nutritionTypeSpinner.setAdapter(adapter);

        // calendar date picker
        weekSelection = (Button) view.findViewById(R.id.weekSelection);
        weekSelection.setText(day7.getMonth().toString() + " "
                + String.valueOf(day7.getDayOfMonth()));
        weekSelection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                picker = new DatePickerDialog(v.getContext(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int day) {
                                month = month + 1;
                                day1 = LocalDate.of(year, month, day);
                                weekSelection.setText(day1.getMonth().toString() + " "
                                        + String.valueOf(day1.getDayOfMonth()));
                                calculateCaloriesConsumed(nutritionType);
                                calculateDates(day1);
                                updateAllProgressViews();
                            }
                        }, year, month, day);
                picker.show();
            }
        });

        return view;
    }

    private void calculateCalorieGoals(String username) {
        dailyCalGoal = d.getCalorieGoal(username);
        weeklyCalGoal = dailyCalGoal * 7;
    }

    private void calculateCaloriesConsumed(String type) {
        if (type.equals("calories")) {
            int[] calories = d.getWeekCalorieCount(username, day1.getYear(), day1.getMonthValue(), day1.getDayOfMonth());
            calDay1 = calories[0];
            calDay2 = calories[1];
            calDay3 = calories[2];
            calDay4 = calories[3];
            calDay5 = calories[4];
            calDay6 = calories[5];
            calDay7 = calories[6];
            calWeek = calDay1 + calDay2 + calDay3 + calDay4 + calDay5 + calDay6 + calDay7;
        } else {
            calDay1 = (int) Math.round(getCurrentAmountConsumed(type, day1));
            calDay2 = (int) Math.round(getCurrentAmountConsumed(type, day2));
            calDay3 = (int) Math.round(getCurrentAmountConsumed(type, day3));
            calDay4 = (int) Math.round(getCurrentAmountConsumed(type, day4));
            calDay5 = (int) Math.round(getCurrentAmountConsumed(type, day5));
            calDay6 = (int) Math.round(getCurrentAmountConsumed(type, day6));
            calDay7 = (int) Math.round(getCurrentAmountConsumed(type, day7));
            calWeek = calDay1 + calDay2 + calDay3 + calDay4 + calDay5 + calDay6 + calDay7;
        }

    }

    private void calculateDates(LocalDate date) {
        day2 = date.minusDays(1);
        day3 = date.minusDays(2);
        day4 = date.minusDays(3);
        day5 = date.minusDays(4);
        day6 = date.minusDays(5);
        day7 = date.minusDays(6);
    }

    private void updateAllProgressViews() {
        day1Text.setText("Day 1: " + day1.toString());
        day2Text.setText("Day 2: " + day2.toString());
        day3Text.setText("Day 3: " + day3.toString());
        day4Text.setText("Day 4: " + day4.toString());
        day5Text.setText("Day 5: " + day5.toString());
        day6Text.setText("Day 6: " + day6.toString());
        day7Text.setText("Day 7: " + day7.toString());
        calorieCurrentWeekText.setText(String.valueOf(calWeek));
        calorieGoalWeekText.setText(String.valueOf(weeklyCalGoal));

        // progress bars
        updateProgress(calWeek, weeklyCalGoal, weekPB);
        updateProgress(calDay1, dailyCalGoal, day1PB);
        updateProgress(calDay2, dailyCalGoal, day2PB);
        updateProgress(calDay3, dailyCalGoal, day3PB);
        updateProgress(calDay4, dailyCalGoal, day4PB);
        updateProgress(calDay5, dailyCalGoal, day5PB);
        updateProgress(calDay6, dailyCalGoal, day6PB);
        updateProgress(calDay7, dailyCalGoal, day7PB);
    }

    private void updateProgress(int calories, int goal, ProgressBar progressBar) {
        progressBar.setMax(goal);
        progressBar.setProgress(calories, true);

        // Changes from green to red if user is over their daily goal
        if (calories > goal) {

        }
    }

    private double getCorrectType(String type, Food f) {
        switch (type) {
            case "calories":
                return (double) f.getCalories();
            case "fat":
                return d.getFoodFat(f.getFoodName());
            case "fiber":
                return d.getFoodFiber(f.getFoodName());
            case "salt":
                return (double) d.getFoodSalt(f.getFoodName());
            case "protein":
                return d.getFoodProtein(f.getFoodName());
            case "sugar":
                return (double) d.getFoodSugar(f.getFoodName());
            case "carbs":
                return d.getFoodCarbs(f.getFoodName());
        }
        return 0.0;
    }

    private double getCurrentAmountConsumed(String type, LocalDate date) {
        if (type.equals("calories")) {
            return (double) d.getCalorieCount(username, date.getYear(), date.getMonthValue(), date.getDayOfMonth());
        } else {
            double amount = 0.0;
            List<Food> entries = (ArrayList<Food>) d.getFoodEntries(username, date.getYear(), date.getMonthValue(), date.getDayOfMonth());
            if (entries != null) {
                for (Food f : entries) {
                    if (!f.getFoodName().equals("Company")) {
                        amount += getCorrectType(type, f);
                    }
                }
            }
            return amount;
        }
    }
}
