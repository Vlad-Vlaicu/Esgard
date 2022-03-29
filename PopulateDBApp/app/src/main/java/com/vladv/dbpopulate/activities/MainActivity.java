package com.vladv.dbpopulate.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.vladv.dbpopulate.R;
import com.vladv.dbpopulate.dao.KPIDao;
import com.vladv.dbpopulate.entities.KPI;

public class MainActivity extends AppCompatActivity {
    Spinner categorySpinner;
    Spinner sectorSpinner;
    TextView addButton;
    EditText score;
    Spinner descriptionSpinner;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        KPIDao kpiDao = new KPIDao();
        score = findViewById(R.id.ratingEditText);
        descriptionSpinner = findViewById(R.id.descriptionSpinner);


        FirebaseDatabase db = FirebaseDatabase.getInstance("https://esguard-11076-default-rtdb.europe-west1.firebasedatabase.app");
        databaseReference = db.getReference(KPI.class.getSimpleName());

        //CATEGORY
        categorySpinner = findViewById(R.id.categorySpinner);
        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<String>(MainActivity.this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.categories));
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(categoryAdapter);

        sectorSpinner = findViewById(R.id.sectorSpinner);


        // SECTOR
        ArrayAdapter<String> sectorAdapter = new ArrayAdapter<String>(MainActivity.this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.sectors));
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        sectorSpinner.setAdapter(sectorAdapter);

        //DESCRIPTION
        ArrayAdapter<String> descriptionAdapter = new ArrayAdapter<String>(MainActivity.this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.description));
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        descriptionSpinner.setAdapter(descriptionAdapter);

        //ADD BUTTON
        addButton = findViewById(R.id.addButton);

        /*
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {

            int counter;
             @Override
             public void onDataChange(@NonNull @org.jetbrains.annotations.NotNull DataSnapshot snapshot) {
                snapshot.getChildren().forEach(s -> counter++);
                Toast.makeText(MainActivity.this, "Nr of elements: " + counter,
                        Toast.LENGTH_LONG).show();
                 System.out.println(counter);
             }

             @Override
             public void onCancelled(@NonNull @org.jetbrains.annotations.NotNull DatabaseError error) {

             }
         });*/

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String category = categorySpinner.getSelectedItem().toString();
                String sector = sectorSpinner.getSelectedItem().toString();
                int rating = Integer.parseInt(score.getText().toString());
                String descriptionText = descriptionSpinner.getSelectedItem().toString();

                KPI kpi = new KPI();
                kpi.setCategory(category);
                kpi.setDescription(descriptionText);
                kpi.setScore(rating);
                kpi.setSector(sector);

                kpiDao.addKPI(kpi);

                score.setText("");
                Toast.makeText(MainActivity.this, "KPI: " + kpi.toString() +
                        "has been added", Toast.LENGTH_SHORT).show();



            }
        });




    }
}