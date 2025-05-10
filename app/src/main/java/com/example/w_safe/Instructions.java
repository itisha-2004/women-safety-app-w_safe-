package com.example.w_safe;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;


public class Instructions extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instructions);

        Button staySafeButton = findViewById(R.id.staySafeButton);
        staySafeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the QuickDefenseTips activity when the button is clicked
                Intent intent = new Intent(Instructions.this, QuickDefenseTipsActivity.class);
                startActivity(intent);
            }
        });
    }
}
