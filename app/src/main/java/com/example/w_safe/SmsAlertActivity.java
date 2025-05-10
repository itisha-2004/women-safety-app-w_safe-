package com.example.w_safe;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class SmsAlertActivity extends AppCompatActivity {

    private static final int SMS_PERMISSION_CODE = 101;
    private EditText emergencyMessage;
    private Button sendAlertButton;

    private static final String PREF_NAME = "UserPref";
    private static final String EMERGENCY_CONTACT = "EmergencyContact";
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms_alert);

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);

        // Initialize UI components
        emergencyMessage = findViewById(R.id.emergencyMessage);
        sendAlertButton = findViewById(R.id.sendAlertButton);

        // Check if SMS permission is granted; otherwise, request it
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, SMS_PERMISSION_CODE);
        }

        // Set up click listener for the send alert button
        sendAlertButton.setOnClickListener(v -> {
            String message = emergencyMessage.getText().toString().trim();
            String emergencyContact = sharedPreferences.getString(EMERGENCY_CONTACT, "");

            if (TextUtils.isEmpty(emergencyContact)) {
                Toast.makeText(SmsAlertActivity.this, "Please add an emergency contact first!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(SmsAlertActivity.this, EmergencyContactActivity.class));
            } else if (TextUtils.isEmpty(message)) {
                Toast.makeText(SmsAlertActivity.this, "Please enter an emergency message!", Toast.LENGTH_SHORT).show();
            } else {
                sendSmsAlert(emergencyContact, message);
            }
        });
    }

    private void sendSmsAlert(String contactNumber, String message) {
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(contactNumber, null, message, null, null);
            Toast.makeText(SmsAlertActivity.this, "Emergency alert sent successfully!", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(SmsAlertActivity.this, "Failed to send SMS. Please try again!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == SMS_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "SMS permission granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "SMS permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
