package edu.upenn.cis350.project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import edu.upenn.cis350.project.datamanagement.MapUserDatabase;
import edu.upenn.cis350.project.datamanagement.NodeUserDatabase;
import edu.upenn.cis350.project.datamanagement.UserDatabase;

public class ProfileActivity extends AppCompatActivity {

    private UserDatabase d;
    private String username;
    private int passLength;
    private int dailyCalorieGoal;
    private String hometown;
    private String bio;
    private TextView usernameText;
    private TextView passwordText;
    private TextView dailyCalorieGoalText;
    private TextView hometownText;
    private TextView bioText;
    private EditText newInfo;
    private EditText confirmPass;
    private LinearLayout changerL;
    private String fieldToChange;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
//        d = MapUserDatabase.getInstance();
        d = new NodeUserDatabase();
        usernameText = (TextView) findViewById(R.id.usernameProfileText);
        passwordText = (TextView) findViewById(R.id.passwordProfileText);
        hometownText = (TextView) findViewById(R.id.hometownText);
        dailyCalorieGoalText = (TextView) findViewById(R.id.dailyCalorieGoalText);
        bioText = (TextView) findViewById(R.id.bioDisplayText);
        newInfo = (EditText) findViewById(R.id.newText);
        confirmPass = (EditText) findViewById(R.id.passwordConfirm);
        changerL = (LinearLayout) findViewById(R.id.changerFields);
        username = getIntent().getStringExtra("username");
        refresh(username);
    }

    private void refresh(String username) {
        usernameText.setText("username: " + username);

        passLength = d.getPasswordLength(username);
        String filteredPass = "";
        for (int i = 0; i < passLength; i++) {
            filteredPass += "*";
        }
        passwordText.setText("password: " + filteredPass);

        dailyCalorieGoal = d.getCalorieGoal(username);
        dailyCalorieGoalText.setText("daily calorie goal: " + String.valueOf(dailyCalorieGoal));

        hometown = d.getHometown(username);
        hometownText.setText("hometown: " + hometown);

        bio = d.getBio(username);
        bioText.setText(bio);
        changerL.setVisibility(View.GONE);

        newInfo.setText("");
        confirmPass.setVisibility(View.GONE);
        confirmPass.setText("");
    }

    public void onClickUpdateProfile(View view) {
        changerL.setVisibility(View.VISIBLE);
        newInfo.setTransformationMethod(null);

        switch(view.getId()) {
            case R.id.usernameButton:
                fieldToChange = "username";
                confirmPass.setVisibility(View.VISIBLE);
                break;
            case R.id.passwordButton:
                fieldToChange = "password";
                confirmPass.setVisibility(View.VISIBLE);
                newInfo.setTransformationMethod(PasswordTransformationMethod.getInstance());
                break;
            case R.id.hometownButton:
                fieldToChange = "hometown";
                break;
            case R.id.bioButton:
                fieldToChange = "bio";
                break;
            case R.id.dailyCalorieGoalButton:
                fieldToChange = "calorieGoal";
                break;
        }
        newInfo.setHint("New " + fieldToChange);
    }

    public void onClickCancelChange(View view) {
        changerL.setVisibility(View.GONE);
        newInfo.setText("");
        Toast.makeText(this, "Change cancelled",
                Toast.LENGTH_LONG).show();
    }

    public void onClickConfirmChange(View view) {
        String temp = newInfo.getText().toString();
        String tempPass = confirmPass.getText().toString();
        if (fieldToChange.equals("username")) {
            boolean changed = false;
            try {
                changed = d.changeUsername(username, tempPass, temp);
                if (changed) {
                    username = temp;
                    Toast.makeText(this, "Username changed",
                            Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(this, "Error: username already exists",
                            Toast.LENGTH_LONG).show();
                }
            } catch (IllegalArgumentException e) {
                Toast.makeText(this, "Error: old password incorrect",
                        Toast.LENGTH_LONG).show();
                changed = false;
            }
        } else if (fieldToChange.equals("password")) {
            try {
                d.changePassword(username, tempPass, temp);
                Toast.makeText(this, "Password changed",
                        Toast.LENGTH_LONG).show();
            } catch (IllegalArgumentException e) {
                Toast.makeText(this, "Error: old password incorrect",
                        Toast.LENGTH_LONG).show();
            }
        } else if (fieldToChange.equals("calorieGoal")) {
            d.changeCalorieGoal(username, Integer.parseInt(temp));
            Toast.makeText(this, "Daily calorie goal changed",
                    Toast.LENGTH_LONG).show();
        } else if (fieldToChange.equals("hometown")) {
            d.changeHometown(username, temp);
            Toast.makeText(this, "Hometown changed",
                    Toast.LENGTH_LONG).show();
        } else {
            d.changeBio(username, temp);
            Toast.makeText(this, "Bio changed",
                    Toast.LENGTH_LONG).show();
        }
        refresh(username);
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent();
        i.putExtra("username", username);
        setResult(RESULT_OK, i);
        finish();
    }
}
