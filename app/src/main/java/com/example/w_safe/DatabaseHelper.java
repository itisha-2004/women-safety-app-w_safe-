package com.example.w_safe;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "safety_app.db";
    private static final int DATABASE_VERSION = 1;

    // Users Table
    public static final String USERS_TABLE = "users";
    public static final String USER_ID = "id";
    public static final String USER_EMAIL = "email";
    public static final String USER_PASSWORD = "password";

    // Emergency Contacts Table
    public static final String EMERGENCY_CONTACTS_TABLE = "emergency_contacts";
    public static final String EC_ID = "id";
    public static final String EC_NAME = "name";
    public static final String EC_PHONE = "phone";
    public static final String EC_USER_EMAIL = "user_email";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create Users Table
        String createUsersTable = "CREATE TABLE " + USERS_TABLE + " (" +
                USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                USER_EMAIL + " TEXT NOT NULL UNIQUE, " +
                USER_PASSWORD + " TEXT NOT NULL);";

        // Create Emergency Contacts Table
        String createEmergencyContactsTable = "CREATE TABLE " + EMERGENCY_CONTACTS_TABLE + " (" +
                EC_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                EC_NAME + " TEXT NOT NULL, " +
                EC_PHONE + " TEXT NOT NULL, " +
                EC_USER_EMAIL + " TEXT NOT NULL, " +
                "FOREIGN KEY(" + EC_USER_EMAIL + ") REFERENCES " + USERS_TABLE + "(" + USER_EMAIL + "));";

        db.execSQL(createUsersTable);
        db.execSQL(createEmergencyContactsTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + USERS_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + EMERGENCY_CONTACTS_TABLE);
        onCreate(db);
    }

    // Insert a new user
    public boolean insertUser(String email, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(USER_EMAIL, email);
        contentValues.put(USER_PASSWORD, password);
        long result = db.insert(USERS_TABLE, null, contentValues);
        return result != -1; // returns true if insertion was successful
    }

    // Insert a new emergency contact
    public boolean insertEmergencyContact(String name, String phone, String userEmail) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(EC_NAME, name);
        contentValues.put(EC_PHONE, phone);
        contentValues.put(EC_USER_EMAIL, userEmail);
        long result = db.insert(EMERGENCY_CONTACTS_TABLE, null, contentValues);
        return result != -1; // returns true if insertion was successful
    }

    // Get emergency contacts for a specific user
    public Cursor getEmergencyContacts(String userEmail) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(EMERGENCY_CONTACTS_TABLE,
                null,
                EC_USER_EMAIL + " = ?",
                new String[]{userEmail},
                null,
                null,
                null);
    }

    // Update user password
    public boolean updatePassword(String email, String newPassword) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(USER_PASSWORD, newPassword);
        // Update password where email matches
        int result = db.update(USERS_TABLE, contentValues, USER_EMAIL + "=?", new String[]{email});
        return result > 0; // returns true if the update was successful
    }

    // Validate user credentials
    public boolean validateUser(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(USERS_TABLE,
                null,
                USER_EMAIL + " = ? AND " + USER_PASSWORD + " = ?",
                new String[]{email, password},
                null,
                null,
                null);
        return cursor.getCount() > 0; // returns true if user exists
    }
}
