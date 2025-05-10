package com.example.w_safe;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity {

    private Button panicButton;
    private LinearLayout selfDefenseLayout;
    private LinearLayout womenLawsLayout;
    private LinearLayout smsAlertLayout;
    private LinearLayout contactLayout;
    private LinearLayout policeStationLayout;
    private LinearLayout locationLayout; // Added location layout
    private Spinner joinSpinner;
    private MediaPlayer mediaPlayer;

    private static final String PREF_NAME = "UserPref";
    private static final String IS_USER_LOGGED_IN = "IsUserLoggedIn";
    private static final String USER_EMAIL_KEY = "UserEmail";
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);

        initLayouts();
        setupJoinSpinner();
        panicButton = findViewById(R.id.panicButton);
        panicButton.setOnClickListener(v -> handlePanicButton());
        policeStationLayout = findViewById(R.id.policeStationLayout);
        // Set up police station click to open CallActivity
        policeStationLayout.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, CallActivity.class)));
    }

    private void initLayouts() {
        selfDefenseLayout = findViewById(R.id.selfDefenseLayout);
        selfDefenseLayout.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, Instructions.class)));

        womenLawsLayout = findViewById(R.id.womenLawsLayout);
        womenLawsLayout.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, WomenLawActivity.class)));

        smsAlertLayout = findViewById(R.id.smsAlertLayout);
        smsAlertLayout.setOnClickListener(v -> {
            if (isUserLoggedIn()) {
                startActivity(new Intent(MainActivity.this, SmsAlertActivity.class));
            } else {
                redirectToLogin("Please log in to access SMS Alerts");
            }
        });

        contactLayout = findViewById(R.id.contactLayout);
        contactLayout.setOnClickListener(v -> {
            if (isUserLoggedIn()) {
                Intent intent = new Intent(MainActivity.this, EmergencyContactActivity.class);
                intent.putExtra(USER_EMAIL_KEY, getLoggedInUserEmail());
                startActivity(intent);
            } else {
                redirectToLogin("Please log in to access Emergency Contacts");
            }
        });

        // Initialize the location layout and set click listener
        locationLayout = findViewById(R.id.locationlayout);
        locationLayout.setOnClickListener(v -> {
            if (isUserLoggedIn()) {
                startActivity(new Intent(MainActivity.this, SmsAlertActivity.class));
            } else {
                redirectToLogin("Please log in to access Location Alerts");
            }
        });
    }

    private void setupJoinSpinner() {
        joinSpinner = findViewById(R.id.joinSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.join_options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        joinSpinner.setAdapter(adapter);

        joinSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedOption = parent.getItemAtPosition(position).toString();
                switch (selectedOption) {
                    case "Login":
                        if (!isUserLoggedIn()) {
                            startActivity(new Intent(MainActivity.this, LoginActivity.class));
                        } else {
                            Toast.makeText(MainActivity.this, "Already logged in", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case "Logout":
                        if (isUserLoggedIn()) {
                            showLogoutConfirmationDialog();
                        } else {
                            Toast.makeText(MainActivity.this, "Not logged in", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case "Signup":
                        startActivity(new Intent(MainActivity.this, SignUpActivity.class));
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void handlePanicButton() {
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.alarm_sound_1);
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        }

        if (!mediaPlayer.isPlaying()) {
            mediaPlayer.start();
            Toast.makeText(MainActivity.this, "Panic Alarm Activated", Toast.LENGTH_SHORT).show();
        } else {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
            Toast.makeText(MainActivity.this, "Panic Alarm Deactivated", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isUserLoggedIn() {
        return sharedPreferences.getBoolean(IS_USER_LOGGED_IN, false);
    }

    private String getLoggedInUserEmail() {
        return sharedPreferences.getString(USER_EMAIL_KEY, "");
    }

    private void redirectToLogin(String message) {
        Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
        startActivity(new Intent(MainActivity.this, LoginActivity.class));
        finish(); // Optionally finish the current activity to prevent going back
    }

    private void showLogoutConfirmationDialog() {
        new AlertDialog.Builder(MainActivity.this)
                .setTitle("Logout")
                .setMessage("Are you sure you want to log out?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean(IS_USER_LOGGED_IN, false);
                    editor.remove(USER_EMAIL_KEY);
                    editor.apply();

                    Toast.makeText(MainActivity.this, "Logged out successfully", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    finish();
                })
                .setNegativeButton("No", null)
                .show();
    }
}
