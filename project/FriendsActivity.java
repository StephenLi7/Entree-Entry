package edu.upenn.cis350.project;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.upenn.cis350.project.datamanagement.MapUserDatabase;
import edu.upenn.cis350.project.datamanagement.NodeUserDatabase;
import edu.upenn.cis350.project.datamanagement.UserDatabase;

public class FriendsActivity extends AppCompatActivity {
    private TextView friendList;
    private Button addFriendButton;
    private LinearLayout addFriendFields;
    private EditText addFriendName;
    private LinearLayout friendsAndRequestsList;
    private UserDatabase d;
    private String username;
    private Map<String, Integer> friends;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);
        friendList = (TextView) findViewById(R.id.friendList);
        addFriendButton = (Button) findViewById(R.id.addFriendButton);
        addFriendFields = (LinearLayout) findViewById(R.id.addFriendFields);
        addFriendName = (EditText) findViewById(R.id.newFriendName);
        friendsAndRequestsList = (LinearLayout) findViewById(R.id.listOfRequestsAndFriends);
//        d = MapUserDatabase.getInstance();
        d = new NodeUserDatabase();
        username = getIntent().getStringExtra("username");
        friends = new HashMap<>();

        //testing example:
        for (int i = 10; i < 15; i++) {
            try {
                d.addUser("bob" + i, "bob");
                d.changeCalorieGoal("bob" + i, (int) (Math.random() * 1000));
                d.addFriendRequest("bob" + i, username);
                if (i < 14) {
//                d.confirmFriendRequest(username, "bob"+i);
                }
            } catch (Exception e) {

            }
        }
        updateFriendRequestList();
        updateFriendList();
    }

    private void updateFriendList() {
        addFriendFields.setVisibility(View.GONE);
        addFriendName.setHint("New friend username");
        addFriendName.setText("");
        try {
            for ( String s : d.getFriends(username)) {
                friends.put(s, d.getCalorieGoal(s));
            }
            friends.put(username + "(you!)", d.getCalorieGoal(username));
            //sorting friends
            List<Map.Entry<String, Integer>> sl = new ArrayList<>(friends.entrySet());
            Collections.sort(sl, new Comparator<Map.Entry<String, Integer>>() {
                public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                    return -(o1.getValue()).compareTo(o2.getValue());
                }
            });
            String msg = "";
            for (Map.Entry<String, Integer> e : sl) {
                msg +=e.getKey() + ", " + e.getValue() + '\n';
            }
            friendList.setText(msg);

        } catch (IllegalArgumentException e) {
            Toast.makeText(this, "Error: " + username + " does not exist???",
                    Toast.LENGTH_LONG).show();
        }
    }

    private void updateFriendRequestList() {
        Map<String, Boolean> requests = d.getFriendRequests(username);
        LinearLayout.LayoutParams linearP = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        LinearLayout.LayoutParams buttonP = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        friendsAndRequestsList.removeAllViews();
        for (Map.Entry e : requests.entrySet()) {
            TextView t = new TextView(this);
            t.setTextSize(20);
            //case where this user sent the invite
            if ((boolean) e.getValue()) {
                t.setText(((String) e.getKey()) + ": pending");
                friendsAndRequestsList.addView(t, linearP);
            //case where this user recieved the invite
            } else {
                LinearLayout l = new LinearLayout(this);
                l.setOrientation(LinearLayout.HORIZONTAL);
                t.setText(((String) e.getKey()));
                l.addView(t, buttonP);

                Button b = new Button(this);
                b.setId(((String) e.getKey()).hashCode());
                b.setText("confirm invite");
                b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        for (Map.Entry e : d.getFriendRequests(username).entrySet()) {
                            if (((String) e.getKey()).hashCode() == v.getId()) {
                                try {
                                    d.confirmFriendRequest(username, (String) e.getKey());
                                    updateFriendList();
                                    updateFriendRequestList();
                                } catch (IllegalArgumentException ex) {
                                    Toast.makeText(v.getContext(), "rly bad error1",
                                            Toast.LENGTH_LONG).show();                                }
                                break;
                            }
                        }
                    }
                });
                l.addView(b, buttonP);

                Button b2 = new Button(this);
                b2.setId(((String) e.getKey()).hashCode()+1);
                b2.setText("delete request");
                b2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        for (Map.Entry e : d.getFriendRequests(username).entrySet()) {
                            if (((String) e.getKey()).hashCode() + 1 == v.getId()) {
                                try {
                                    d.removeFriendRequest(username, (String) e.getKey());
                                    updateFriendList();
                                    updateFriendRequestList();
                                } catch (IllegalArgumentException ex) {
                                    Toast.makeText(v.getContext(), "rly bad error2",
                                            Toast.LENGTH_LONG).show();                                }
                                break;
                            }
                        }
                    }
                });
                l.addView(b2, buttonP);

                friendsAndRequestsList.addView(l, linearP);
            }
        }
    }

    public void onClickAddFriend(View view) {
        addFriendFields.setVisibility(View.VISIBLE);
    }

    public void onClickConfirmSendInvite(View view) {
        String newFriendName = addFriendName.getText().toString();
        try {
            d.addFriendRequest(username, newFriendName);
        } catch (IllegalArgumentException e) {
            Toast.makeText(this, "Error: cannot add " + newFriendName,
                    Toast.LENGTH_LONG).show();
        }
        updateFriendList();
        updateFriendRequestList();
    }

    public void onClickCancelAddFriend(View view) {
        updateFriendList();
        updateFriendRequestList();
    }
}