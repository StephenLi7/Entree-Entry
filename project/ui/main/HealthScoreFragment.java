package edu.upenn.cis350.project.ui.main;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import edu.upenn.cis350.project.R;
import edu.upenn.cis350.project.data.Food;
import edu.upenn.cis350.project.data.HealthScore;
import edu.upenn.cis350.project.datamanagement.UserDatabase;
import edu.upenn.cis350.project.datamanagement.NodeUserDatabase;
import edu.upenn.cis350.project.datamanagement.MapUserDatabase;

public class HealthScoreFragment extends Fragment {

    private String username;
    private UserDatabase d;
    private HealthScore hs;
    private LocalDate date; // starts set to today
    private int calorieConsumed;
    private int fatConsumed;
    private int fiberConsumed;
    private int sugarConsumed;
    private int saltConsumed;
    private int proteinConsumed;
    private int carbsConsumed;
    private int calorieGoal;
    private int fatGoal;
    private int fiberGoal;
    private int sugarGoal;
    private int saltGoal;
    private int proteinGoal;
    private int carbsGoal;
    private int score;
    private int grade;

    private TextView scoreText;
    private DatePicker datePicker;
    private TextView gradeText;

    private CheckBox fatRadio;
    private CheckBox fiberRadio;
    private CheckBox sugarRadio;
    private CheckBox saltRadio;
    private CheckBox proteinRadio;
    private CheckBox carbsRadio;
    private EditText fatText;
    private EditText fiberText;
    private EditText sugarText;
    private EditText saltText;
    private EditText proteinText;
    private EditText carbsText;
    private Button updateBtn;

    public HealthScoreFragment(String username) {
        this.username = username;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        d = MapUserDatabase.getInstance();
        d = new NodeUserDatabase();
        date = LocalDate.now();
        updateAllAmountConsumed();
        calorieGoal = d.getCalorieGoal(username);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_health_score, container, false);
        fatRadio = (CheckBox) view.findViewById(R.id.radio_fat);
        fiberRadio = (CheckBox) view.findViewById(R.id.radio_fiber);
        sugarRadio = (CheckBox) view.findViewById(R.id.radio_sugar);
        saltRadio = (CheckBox) view.findViewById(R.id.radio_salt);
        proteinRadio = (CheckBox) view.findViewById(R.id.radio_protein);
        carbsRadio = (CheckBox) view.findViewById(R.id.radio_carbs);
        resetRadioButtons();
        fatText = (EditText) view.findViewById(R.id.edit_fat);
        fatText.setText("");
        fiberText = (EditText) view.findViewById(R.id.edit_fiber);
        fiberText.setText("");
        sugarText = (EditText) view.findViewById(R.id.edit_sugar);
        sugarText.setText("");
        saltText = (EditText) view.findViewById(R.id.edit_salt);
        saltText.setText("");
        proteinText = (EditText) view.findViewById(R.id.edit_protein);
        proteinText.setText("");
        carbsText = (EditText) view.findViewById(R.id.edit_carbs);
        carbsText.setText("");
        updateBtn = (Button) view.findViewById(R.id.updateBtn);
        updateBtn.setOnClickListener(new AdapterView.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickUpdateScore();
                score = hs.calculateHealthScore();
                updateScore(score);
            }
        });

        scoreText = (TextView) view.findViewById(R.id.scoreText);
        gradeText = (TextView) view.findViewById(R.id.gradeText);
        datePicker = (DatePicker) view.findViewById(R.id.datePicker_healthScore);
        datePicker.setOnDateChangedListener(new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker dp, int year, int month, int day) {
                day = datePicker.getDayOfMonth();
                month = datePicker.getMonth() + 1;
                year = datePicker.getYear();
                date = LocalDate.of(year, month, day);
                updateAllAmountConsumed();
                hs.setConsumed(calorieConsumed, fatConsumed, fiberConsumed, sugarConsumed, saltConsumed,
                        proteinConsumed, carbsConsumed);
                score = hs.calculateHealthScore();
                grade = calculateGrade();
                updateScore(score);
                updateGrade(grade);
            }
        });

        hs = new HealthScore(calorieGoal, calorieConsumed);
        score = hs.calculateHealthScore();
        grade = calculateGrade();
        updateScore(score);
        updateGrade(grade);
        return view;
    }

    private void onClickUpdateScore() {
        int num = 1;
        if (fatRadio.isChecked() && !fatText.getText().toString().isEmpty()) {
            fatGoal = Integer.parseInt(fatText.getText().toString());
            num++;
        } else {
            fatGoal = 0;
        }

        if (fiberRadio.isChecked() && !fiberText.getText().toString().isEmpty()) {
            fiberGoal = Integer.parseInt(fiberText.getText().toString());
            num++;
        } else {
            fiberGoal = 0;
        }

        if (sugarRadio.isChecked() && !sugarText.getText().toString().isEmpty()) {
            sugarGoal = Integer.parseInt(sugarText.getText().toString());
            num++;
        } else {
            sugarGoal = 0;
        }

        if (saltRadio.isChecked() && !saltText.getText().toString().isEmpty()) {
            saltGoal = Integer.parseInt(saltText.getText().toString());
            num++;
        } else {
            saltGoal = 0;
        }

        if (proteinRadio.isChecked() && !proteinText.getText().toString().isEmpty()) {
            proteinGoal= Integer.parseInt(proteinText.getText().toString());
            num++;
        } else {
            proteinGoal = 0;
        }

        if (carbsRadio.isChecked() && !carbsText.getText().toString().isEmpty()) {
            carbsGoal= Integer.parseInt(carbsText.getText().toString());
            num++;
        } else {
            fatGoal = 0;
        }

        hs.setNum(num);
        hs.setGoals(calorieGoal, fatGoal, fiberGoal, sugarGoal, saltGoal,
                proteinGoal, carbsGoal);
    }

    private void updateScore(int s) {
        if (s >= 7) {
            scoreText.setTextColor(Color.GREEN);
        } else if (s >= 4) {
            scoreText.setTextColor(Color.YELLOW);
        } else {
            scoreText.setTextColor(Color.RED);
        }
        scoreText.setText(String.valueOf(s));
    }

    private int calculateGrade() {
        Log.v("calc grade", "here");
        List<Food> entries = (ArrayList<Food>) d.getFoodEntries(username, date.getYear(),
                date.getMonthValue(), date.getDayOfMonth());
        int currentGrade = 0;
        int num = 0;
        if (entries != null) {
            for (Food f : entries) {
                if (f.getFoodName().equals("Company")) {
                    currentGrade += f.getCalories();
                    num++;
                }
            }

            if (num == 0) {
                return 0;
            }

            float g = (float) currentGrade / (float) num;

            return Math.round(g);
        }

         return 0;
    }

    private void updateGrade(int g) {
        String letter;
        if (g >= 65) {
            letter = "A";
            gradeText.setTextColor(Color.GREEN);
        } else if (g >= 50) {
            letter = "B";
            gradeText.setTextColor(Color.YELLOW);
        } else if (g >= 40) {
            letter = "C";
            gradeText.setTextColor(Color.YELLOW);
        } else if (g >= 20) {
            letter = "D";
            gradeText.setTextColor(Color.RED);
        } else {
            letter = "F";
            gradeText.setTextColor(Color.RED);
        }
        gradeText.setText(letter);
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

    private double getCurrentAmountConsumed(String type) {
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

    private void updateAllAmountConsumed() {
        calorieConsumed = (int) Math.round(getCurrentAmountConsumed("calories"));
        fatConsumed = (int) Math.round(getCurrentAmountConsumed("fat"));
        fiberConsumed = (int) Math.round(getCurrentAmountConsumed("fiber"));
        sugarConsumed = (int) Math.round(getCurrentAmountConsumed("sugar"));
        saltConsumed = (int) Math.round(getCurrentAmountConsumed("salt"));
        proteinConsumed = (int) Math.round(getCurrentAmountConsumed("protein"));
        carbsConsumed = (int) Math.round(getCurrentAmountConsumed("carbs"));
    }

    private void resetRadioButtons() {
        fatRadio.setChecked(false);
        sugarRadio.setChecked(false);
        proteinRadio.setChecked(false);
        fiberRadio.setChecked(false);
        saltRadio.setChecked(false);
        carbsRadio.setChecked(false);
    }
}
