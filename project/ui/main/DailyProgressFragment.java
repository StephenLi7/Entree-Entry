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
import android.view.ViewManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import edu.upenn.cis350.project.R;
import edu.upenn.cis350.project.data.Food;
import edu.upenn.cis350.project.datamanagement.MapUserDatabase;
import edu.upenn.cis350.project.datamanagement.NodeUserDatabase;
import edu.upenn.cis350.project.datamanagement.UserDatabase;
import edu.upenn.cis350.project.ProgressActivity;


public class DailyProgressFragment extends Fragment {
    private UserDatabase d;
    private String username;
    private int calorieGoal;
    private int calorieCurrent;
    private List<Food> entries;
    private LocalDate date;
    private TextView calorieCurrentText;
    private TextView calorieGoalText;
    private ProgressBar progressBar;
    private ViewGroup entriesLayout;
    private Button dateSelection;
    private DatePickerDialog picker;
    private Spinner nutritionTypeSpinner;
    private String nutritionType;

    public DailyProgressFragment(String username) {
        this.username = username;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        nutritionType = "calories";
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_daily_progress, container, false);
//        d = MapUserDatabase.getInstance();
        d = new NodeUserDatabase();
        calorieCurrentText = (TextView) view.findViewById(R.id.calorieCurrentText);
        calorieGoalText = (TextView) view.findViewById(R.id.calorieGoalText);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        entriesLayout = (LinearLayout) view.findViewById(R.id.entriesLayout);

        nutritionTypeSpinner = (Spinner) view.findViewById(R.id.nutritionTypeSpinner);
        nutritionTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View view, int position, long id) {
                String nutType = (String) arg0.getItemAtPosition(position);
                nutritionType = nutType;
                updateTodaysEntries(username, view, date, nutType);
                updateProgress(username, date, nutType);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(view.getContext(),
                R.array.nutrition_types, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        nutritionTypeSpinner.setAdapter(adapter);

        dateSelection = (Button) view.findViewById(R.id.dateSelection);
        date = LocalDate.now();
        dateSelection.setText(date.getMonth().toString() + " " + String.valueOf(date.getDayOfMonth())
                + ", " + String.valueOf(date.getYear()));
        dateSelection.setOnClickListener(new View.OnClickListener() {
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
                                                                 date = LocalDate.of(year, month, day);
                                                                 dateSelection.setText(date.getMonth().toString() + " " + String.valueOf(date.getDayOfMonth())
                                                                         + ", " + String.valueOf(date.getYear()));
                                                                 updateProgress(username, date, nutritionType);
                                                                 updateTodaysEntries(username, view, date,
                                                                         nutritionType);
                                                             }
                                                         }, year, month, day);
                                                 picker.show();
                                             }
                                         });



        updateProgress(username, date, nutritionType);
        updateTodaysEntries(username, view, date, "calories");
        return view;
    }

    private void updateProgress(String username, LocalDate date, String type) {
        calorieGoal = d.getCalorieGoal(username);
        calorieCurrent = (int) Math.round(getCurrentAmountConsumed(type));
        progressBar.setMax(calorieGoal);
        progressBar.setProgress(calorieCurrent, true);
        calorieGoalText.setText(String.valueOf(calorieGoal));
        calorieCurrentText.setText(String.valueOf(calorieCurrent));

        // Changes from green to red if user is over their daily goal
        if (calorieCurrent > calorieGoal) {

        }
    }

    private void updateTodaysEntries(String username, View v, LocalDate date, String type) {
        entries = (ArrayList<Food>) d.getFoodEntries(username, date.getYear(), date.getMonthValue(), date.getDayOfMonth());
        entriesLayout.removeAllViews();
        if (entries != null) {
            int numRows = entries.size();
            if (numRows != 0) {
                for (Food f : entries) {
                    if (!f.getFoodName().equals("Company")) {
                            LinearLayout entryRow = new LinearLayout(this.getActivity());
                            entryRow.setOrientation(LinearLayout.HORIZONTAL);

                            TextView entryNameText = new TextView(this.getActivity());
                            entryNameText.setText(f.getFoodName());
                            entryNameText.setTextSize(20);

                            TextView entryCalorieText = new TextView(this.getActivity());
                            entryCalorieText.setText("                                " + String.valueOf(getCorrectType(type, f)));
                            entryCalorieText.setTextSize(20);

                            entryRow.addView(entryNameText);
                            entryRow.addView(entryCalorieText);
                            entriesLayout.addView(entryRow);
                    }
                }
            }
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

    private double getCurrentAmountConsumed(String type) {
        if (type.equals("calories")) {
            return (double) d.getCalorieCount(username, date.getYear(), date.getMonthValue(), date.getDayOfMonth());
        } else {
            double amount = 0.0;
            entries = (ArrayList<Food>) d.getFoodEntries(username, date.getYear(), date.getMonthValue(), date.getDayOfMonth());
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