package com.example.tusher.cityppolish;

import com.example.tusher.cityppolish.helper.SQLiteHandler;
import com.example.tusher.cityppolish.helper.SessionManager;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class DashboardScreen extends AppCompatActivity {

    private TextView txtName;
    private TextView txtEmail;
    private ImageButton btnLogout;
    private ImageButton btnreport;
    private ImageButton btnnewsfeed;
    private ImageButton join_comunity;

    private SQLiteHandler db;
    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_screen);

        btnnewsfeed = (ImageButton) findViewById(R.id.btnnewsfeed);
        btnreport = (ImageButton)findViewById(R.id.btnreport);
        join_comunity = (ImageButton) findViewById(R.id.join_comunity);
        btnLogout = (ImageButton) findViewById(R.id.btnLogout);



        btnreport.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent i = new Intent(DashboardScreen.this,report.class);
                startActivity(i);
                finish();
            }
        });



        btnnewsfeed.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent i = new Intent(DashboardScreen.this,newsfeed.class);
                startActivity(i);
                finish();
            }
        });

        //(DashboardScreen.this,report.class)
        //setOnClickListener(new View.OnClickListener() {

        //    public void onClick(View view) {
        //        Intent i = new Intent(DashboardScreen.this,report.class);
        //        startActivity(i);
        //        finish();
        //    }
        //});



        join_comunity.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent i = new Intent(DashboardScreen.this,citycom.class);
                startActivity(i);
                finish();
            }
        });


        btnLogout.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent i = new Intent(DashboardScreen.this,MainActivity.class);
                startActivity(i);
                finish();
            }
        });


        /** txtName = (TextView) findViewById(R.id.name);
        txtEmail = (TextView) findViewById(R.id.email);


        //* SqLite database handler
        db = new SQLiteHandler(getApplicationContext());

        //* session manager
        session = new SessionManager(getApplicationContext());

        if (!session.isLoggedIn()) {
            logoutUser();
        }

        // *Fetching user details from sqlite
        HashMap<String, String> user = db.getUserDetails();

        String name = user.get("name");
        String email = user.get("email");

        // *Displaying the user details on the screen
        txtName.setText(name);
        txtEmail.setText(email);

        // *Logout button click event
        btnLogout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                logoutUser();
            }
        });
    }

    /**
     * Logging out the user. Will set isLoggedIn flag to false in shared
     * preferences Clears the user data from sqlite users table

    private void logoutUser() {
        session.setLogin(false);

        db.deleteUsers();

        // *Launching the login activity
        Intent intent = new Intent(DashboardScreen.this, MainActivity.class);
        startActivity(intent);
        finish();  **/
    }
}
