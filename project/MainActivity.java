package edu.upenn.cis350.project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private String currentUser;
    private static final int LOGIN_ID = 11;
    private static final int PROFILE_ID = 12;
    private static final int FRIENDS_ID = 13;
    private static final int PROGRESS_ID = 14;
    private static final int ENTRY_ID = 15;
    private static final int COMPANIES_ID = 16;
    private static final int DIETPLANNER_ID = 17;
    private Button loginB;
    private TextView welcome;
    private Button profileB;
    private Button progressB;
    private Button manualEntryB;
    private Button friendB;
    private Button companiesB;
    private Button dietPlannerB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loginB = (Button) findViewById(R.id.loginButton);
        welcome = (TextView) findViewById(R.id.welcomeMessage);
        profileB = (Button) findViewById(R.id.profileButton);
        progressB = (Button) findViewById(R.id.progressButton);
        manualEntryB = (Button) findViewById(R.id.manualFoodEntryButton);
        friendB = (Button) findViewById(R.id.friendsButton);
        companiesB = (Button) findViewById(R.id.companiesButton);
        dietPlannerB = (Button) findViewById(R.id.dietPlannerButton);
        refreshForUser();
    }

    public void refreshForUser() {
        //handle login/logoff toggle
        if (currentUser == null) {
            loginB.setText("Log in");
            welcome.setText("Welcome guest! Please log in.");
            profileB.setVisibility(View.GONE);
            progressB.setVisibility(View.GONE);
            manualEntryB.setVisibility(View.GONE);
            friendB.setVisibility(View.GONE);
            companiesB.setVisibility(View.GONE);
            dietPlannerB.setVisibility(View.GONE);
        } else {
            loginB.setText("Log off");
            welcome.setText("Welcome " + currentUser +"!");
            profileB.setVisibility(View.VISIBLE);
            progressB.setVisibility(View.VISIBLE);
            manualEntryB.setVisibility(View.VISIBLE);
            friendB.setVisibility(View.VISIBLE);
            companiesB.setVisibility(View.VISIBLE);
            dietPlannerB.setVisibility(View.VISIBLE);
        }
    }
    public void onClickLaunchLogin(View v) {
        if (currentUser != null) {
            currentUser = null;
            refreshForUser();
            Toast.makeText(this, "Logged off successfully",
                    Toast.LENGTH_LONG).show();
        } else {
            Intent i = new Intent(this, LoginActivity.class);
            startActivityForResult(i, LOGIN_ID);
        }
    }

    public void onClickLaunchProfile(View v) {
        Intent i = new Intent(this, ProfileActivity.class);
        i.putExtra("username", currentUser);
        startActivityForResult(i, PROFILE_ID);
    }

    public void onClickLaunchFriends(View v) {
        Intent i = new Intent(this, FriendsActivity.class);
        i.putExtra("username", currentUser);
        startActivityForResult(i, FRIENDS_ID);
    }

    public void onClickLaunchProgress(View v) {
        Intent i = new Intent(this, ProgressActivity.class);
        i.putExtra("username", currentUser);
        startActivityForResult(i, PROGRESS_ID);
    }

    public void onClickLaunchFoodEntry(View v) {
        Intent i = new Intent(this, ManualEntryActivity.class);
        i.putExtra("username", currentUser);
        startActivityForResult(i, ENTRY_ID);
    }

    public void onClickLaunchSustainableCompanies(View v) {
        Intent i = new Intent(this, SustainableCompaniesActivity.class);
        i.putExtra("username", currentUser);
        startActivityForResult(i, COMPANIES_ID);
    }

    public void onClickLaunchDietPlanner(View v) {
        Intent i = new Intent(this, DietPlannerActivity.class);
        i.putExtra("username", currentUser);
        startActivityForResult(i, DIETPLANNER_ID);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case LOGIN_ID:
                if (resultCode == RESULT_CANCELED) {
                    Toast.makeText(this, "Log in cancelled",
                            Toast.LENGTH_LONG).show();
                } else {
                    currentUser = data.getStringExtra("username");
                    Toast.makeText(this, "Logged in successfully",
                            Toast.LENGTH_LONG).show();
                    refreshForUser();
                }
                break;
            case PROFILE_ID:
                currentUser = data.getStringExtra("username");
                refreshForUser();
                break;

            default:
                refreshForUser();
                break;
        }
    }
}