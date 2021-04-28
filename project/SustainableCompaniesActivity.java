package edu.upenn.cis350.project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

import edu.upenn.cis350.project.datamanagement.MapUserDatabase;
import edu.upenn.cis350.project.datamanagement.NodeUserDatabase;
import edu.upenn.cis350.project.datamanagement.UserDatabase;

public class SustainableCompaniesActivity extends AppCompatActivity {
    CompaniesSpinnerActivity companiesSpinnerActivity = new CompaniesSpinnerActivity();
    public String text = "";
    HashMap<String, Integer> companyScores = new HashMap<String, Integer>();
    public Spinner spinner;
    private UserDatabase d;
    private String username;
    private TextView score;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Populate the HashMap of food values
        companyScores.put("Unilever", 74);
        companyScores.put("Nestlé", 69);
        companyScores.put("Coca Cola", 57);
        companyScores.put("Kellogg's", 53);
        companyScores.put("MARS", 49);
        companyScores.put("Pepsico", 49);
        companyScores.put("Mondelez International", 41);
        companyScores.put("General Mills", 40);
        companyScores.put("Associated British Foods plc", 36);
        companyScores.put("Danone", 36);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_companies);
//        d = MapUserDatabase.getInstance();
        d = new NodeUserDatabase();
        username = getIntent().getStringExtra("username");
        score = (TextView) findViewById(R.id.company_sustainability_grade);

        //getting spinner instance and applying OnClickListener
        spinner = (Spinner) findViewById(R.id.companies_list_spinner);


        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.companies_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(companiesSpinnerActivity);

        companiesSpinnerActivity.setContext(SustainableCompaniesActivity.this);
    }

    public void onCheckSustainabilityScoreClick(View v) {
        //Gets the string value of the selected item
        text = spinner.getSelectedItem().toString();
        score = (TextView) findViewById(R.id.company_sustainability_grade);
        int sustainabilityScore = companyScores.get(text);
        if (sustainabilityScore <= 50) {
            score.setTextColor(Color.parseColor("#F90A0A"));
        } else if (sustainabilityScore > 50 && sustainabilityScore <= 65) {
            score.setTextColor(Color.parseColor("#F9BB0A"));
        } else {
            score.setTextColor(Color.parseColor("#04FF00"));
        }
        score.setText(sustainabilityScore + "");
        score.setVisibility(View.VISIBLE);
    }

    public void onAddCompanyClick(View v) {
        text = spinner.getSelectedItem().toString();
        score = (TextView) findViewById(R.id.company_sustainability_grade);
        int sustainabilityScore = companyScores.get(text);
        d.addFoodEntryForToday(username, "Company", sustainabilityScore, 0, 0,
                0, 0, 0);

        Toast.makeText(this, text + " has been added! It has a sustainability score of "
                +  sustainabilityScore + "!", Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onBackPressed() {
        Intent i = new Intent();
        i.putExtra("username", username);
        setResult(RESULT_OK, i);
        finish();
    }
}