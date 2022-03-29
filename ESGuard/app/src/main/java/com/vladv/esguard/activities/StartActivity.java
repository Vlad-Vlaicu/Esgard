
package com.vladv.esguard.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.vladv.esguard.R;

public class StartActivity extends AppCompatActivity {
    TextView selectionButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        selectionButton = findViewById(R.id.selectionButton);
        selectionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StartActivity.this,SelectionActivity.class);
                startActivity(intent);
            }
        });
    }
}