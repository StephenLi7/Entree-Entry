package edu.upenn.cis350.project;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class SpinnerActivity extends AppCompatActivity
        implements AdapterView.OnItemSelectedListener{

    private String food;
    private Context lonzo;

    @Override
    public void onItemSelected(AdapterView<?> arg0, View view, int position, long id) {
        this.food = (String) arg0.getItemAtPosition(position);
        EditText karateDuck = ((Activity) lonzo).findViewById(R.id.custom_food_type);
        EditText lonz0 = ((Activity) lonzo).findViewById(R.id.custom_food_calories);
        if (food.equals("Other")) {
            karateDuck.setVisibility(View.VISIBLE);
            lonz0.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    public String getFood() {
        return this.food;
    }

    public void setContext(Context context) {
        this.lonzo = context;
    }
}

