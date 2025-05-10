package com.example.w_safe;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;


public class ViewContactsActivity extends AppCompatActivity {
    private ListView listViewContacts;
    private DatabaseHelper databaseHelper;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> contactList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_contacts);

        listViewContacts = findViewById(R.id.listViewContacts);
        databaseHelper = new DatabaseHelper(this);
        contactList = new ArrayList<>();

        // Load contacts from the database
        loadContacts();
    }

    private void loadContacts() {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor cursor = db.query(
                DatabaseHelper.EMERGENCY_CONTACTS_TABLE,
                new String[]{DatabaseHelper.EC_NAME, DatabaseHelper.EC_PHONE},
                DatabaseHelper.EC_USER_EMAIL + " = ?",
                new String[]{getLoggedInUserEmail()},
                null, null, null
        );

        if (cursor != null && cursor.moveToFirst()) {
            do {
                String name = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.EC_NAME));
                String phone = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.EC_PHONE));
                contactList.add(name + " - " + phone);
            } while (cursor.moveToNext());
            cursor.close();
        }

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, contactList);
        listViewContacts.setAdapter(adapter);
    }

    private String getLoggedInUserEmail() {
        return getSharedPreferences("UserPrefs", MODE_PRIVATE).getString("USER_EMAIL", null);
    }
}
