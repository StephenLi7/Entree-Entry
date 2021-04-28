package edu.upenn.cis350.project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import edu.upenn.cis350.project.datamanagement.MapUserDatabase;
import edu.upenn.cis350.project.datamanagement.NodeUserDatabase;
import edu.upenn.cis350.project.datamanagement.UserDatabase;

public class LoginActivity extends AppCompatActivity {
    private UserDatabase d;
    private EditText currentUsername;
    private EditText currentPassword;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

//        d = MapUserDatabase.getInstance();
        d = new NodeUserDatabase();
        currentUsername = (EditText) findViewById(R.id.usernameText);
        currentPassword = (EditText) findViewById(R.id.passwordText);
    }

    public void onClickCreateAccount(View v) {
        String username = currentUsername.getText().toString();
        String password = currentPassword.getText().toString();
        if (username == null || password == null) {
            throw new IllegalArgumentException();
        } else if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Error: empty parameter. Try again.",
                    Toast.LENGTH_LONG).show();
        } else if (d.searchUser(username)) {
            Toast.makeText(this, "Error: user already exists. Try again.",
                    Toast.LENGTH_LONG).show();
            currentUsername.getText().clear();
            currentPassword.getText().clear();
        } else {
            d.addUser(username, password);
            login(username);
        }
    }

    public void onClickLogin(View v) {
        String username = currentUsername.getText().toString();
        String password = currentPassword.getText().toString();
        if (username == null || password == null) {
            throw new IllegalArgumentException();
        } else if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Error: empty parameter. Try again.",
                    Toast.LENGTH_LONG).show();
        } else if (d.searchUser(username)) {
            if (d.login(username, password)) {
                login(username);
            } else {
                Toast.makeText(this, "Error: incorrect password. Try again.",
                        Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this, "Error: user does not exist. Try again.",
                    Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent();
        setResult(RESULT_CANCELED, i);
        finish();
    }
    private void login(String username) {
        Intent i = new Intent();
        i.putExtra("username", username);
        setResult(RESULT_OK, i);
        finish();
    }

}
