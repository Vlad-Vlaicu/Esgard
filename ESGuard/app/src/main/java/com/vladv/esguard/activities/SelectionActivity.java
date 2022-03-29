package com.vladv.esguard.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.vladv.esguard.R;
import com.vladv.esguard.activities.StartActivity;
import com.vladv.esguard.activities.StatisticsActivity;
import com.vladv.esguard.utilities.Constants;

public class SelectionActivity extends AppCompatActivity {
    TextView home;
    TextView commerce;
    TextView energy;
    TextView telecomm;
    TextView IT;
    TextView health;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selection);

        home = findViewById(R.id.homeButton_selectionActivity);
        IT = findViewById(R.id.itTextView_selectionActivity);
        commerce = findViewById(R.id.commerceTextView_selectionActivity);
        energy = findViewById(R.id.energyTextView_selectionActivity);
        telecomm = findViewById(R.id.telecommtextView_selectionActivity);
        health = findViewById(R.id.healthTextView_selectionActivity);

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SelectionActivity.this, StartActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        commerce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SelectionActivity.this, StatisticsActivity.class);
                intent.putExtra("SECTOR", Constants.COMMERCE);
                startActivity(intent);
            }
        });

        energy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SelectionActivity.this,StatisticsActivity.class);
                intent.putExtra("SECTOR", Constants.ENERGY);
                startActivity(intent);
            }
        });

        telecomm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SelectionActivity.this,StatisticsActivity.class);
                intent.putExtra("SECTOR", Constants.TELECOMM);
                startActivity(intent);
            }
        });

        IT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SelectionActivity.this,StatisticsActivity.class);
                intent.putExtra("SECTOR", Constants.TECH);
                startActivity(intent);
            }
        });

        health.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SelectionActivity.this,StatisticsActivity.class);
                intent.putExtra("SECTOR", Constants.HEALTH);
                startActivity(intent);
            }
        });



    }
}