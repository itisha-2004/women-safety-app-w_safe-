package com.example.w_safe;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;


public class EmergencyContactActivity extends AppCompatActivity {
    private EditText editTextContactName, editTextContactPhone;
    private Button buttonSaveContact, buttonViewContacts;
    private Spinner genderSpinner;
    private ImageView avatarImageView;
    private DatabaseHelper databaseHelper;
    private static final String PREFS_NAME = "UserPrefs";
    private String loggedInUserEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency_contact);

        // Initialize views
        editTextContactName = findViewById(R.id.editTextContact);
        editTextContactPhone = findViewById(R.id.editTextContactPhone);
        buttonSaveContact = findViewById(R.id.buttonSaveContact);
        buttonViewContacts = findViewById(R.id.buttonViewContacts);
        genderSpinner = findViewById(R.id.genderSpinner);
        avatarImageView = findViewById(R.id.avatarImageView);
        databaseHelper = new DatabaseHelper(this);

        // Retrieve logged-in user email from SharedPreferences
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        loggedInUserEmail = prefs.getString("USER_EMAIL", null);

        // Setup gender spinner with avatars
        setupGenderSpinner();

        // Save contact button click listener
        buttonSaveContact.setOnClickListener(v -> {
            String contactName = editTextContactName.getText().toString().trim();
            String contactNumber = editTextContactPhone.getText().toString().trim();
            String selectedGender = genderSpinner.getSelectedItem().toString();

            if (validateInputs(contactName, contactNumber)) {
                // Save the emergency contact information
                saveEmergencyContact(contactName, contactNumber, selectedGender);
                Toast.makeText(EmergencyContactActivity.this, "Emergency Contact Saved", Toast.LENGTH_SHORT).show();
                resetInputs();

                // Open the ViewContactsActivity to display saved contacts
                Intent intent = new Intent(EmergencyContactActivity.this, ViewContactsActivity.class);
                startActivity(intent);
            }
        });

        // View contacts button click listener
        buttonViewContacts.setOnClickListener(v -> {
            Intent intent = new Intent(EmergencyContactActivity.this, ViewContactsActivity.class);
            startActivity(intent);
        });
    }

    private void saveEmergencyContact(String name, String number, String gender) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.EC_NAME, name);
        values.put(DatabaseHelper.EC_PHONE, number);
        values.put(DatabaseHelper.EC_USER_EMAIL, loggedInUserEmail);
        db.insert(DatabaseHelper.EMERGENCY_CONTACTS_TABLE, null, values);
        db.close();
    }

    private void setupGenderSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.gender_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genderSpinner.setAdapter(adapter);

        genderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String selectedGender = parentView.getItemAtPosition(position).toString();
                if (selectedGender.equals("Male")) {
                    avatarImageView.setImageResource(R.drawable.male);
                } else if (selectedGender.equals("Female")) {
                    avatarImageView.setImageResource(R.drawable.female);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });
    }

    private boolean validateInputs(String name, String number) {
        if (TextUtils.isEmpty(name) || !name.matches("[a-zA-Z ]+")) {
            editTextContactName.setError("Enter a valid name (letters only)");
            return false;
        }
        if (!validatePhoneNumber(number)) {
            editTextContactPhone.setError("Enter a valid 10-digit contact number");
            return false;
        }
        return true;
    }

    private boolean validatePhoneNumber(String number) {
        return !TextUtils.isEmpty(number) && number.matches("^\\d{10}$");
    }

    private void resetInputs() {
        editTextContactName.setText("");
        editTextContactPhone.setText("");
        genderSpinner.setSelection(0);
        avatarImageView.setImageResource(0);
    }
}
