package edu.upenn.cis350.project;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class CompaniesSpinnerActivity extends AppCompatActivity
        implements AdapterView.OnItemSelectedListener{

    private String company;
    private Context lonzo;

    @Override
    public void onItemSelected(AdapterView<?> arg0, View view, int position, long id) {
        this.company = (String) arg0.getItemAtPosition(position);
        TextView karateDuck = ((Activity) lonzo).findViewById(R.id.company_sustainability_grade);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    public String getCompany() {
        return this.company;
    }

    public void setContext(Context context) {
        this.lonzo = context;
    }
}

