package com.vladv.esguard.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.vladv.esguard.R;
import com.vladv.esguard.entities.KPI;
import com.vladv.esguard.utilities.Constants;
import com.vladv.esguard.utilities.DataValidation;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class StatisticsActivity extends AppCompatActivity {
    LineChart chart;
    DatabaseReference databaseReference;
    TextView allButton;
    TextView homeButton;
    TextView e_Button;
    TextView s_Button;
    TextView g_Button;
    SeekBar seekBar;
    int currentChart;

    float initial_E_value;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        chart = findViewById(R.id.lineChart);
        e_Button = findViewById(R.id.E_button_statistics);
        g_Button = findViewById(R.id.G_button_statistics);
        s_Button = findViewById(R.id.S_button_statistics);
        allButton = findViewById(R.id.O_button_statistics);
        homeButton = findViewById(R.id.home_button_statistics);
        seekBar = findViewById(R.id.seekBar);
        currentChart = 0;

        initial_E_value = 0;


        String sector;
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                sector= "";
            } else {
                sector= extras.getString("SECTOR");
            }
        } else {
            sector= (String) savedInstanceState.getSerializable("SECTOR ");
        }


        FirebaseDatabase db = FirebaseDatabase.getInstance("https://esguard-11076-default-rtdb.europe-west1.firebasedatabase.app");
        databaseReference = db.getReference(KPI.class.getSimpleName());

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {

            List<KPI> kpiList = new ArrayList<>();
            @Override
            public void onDataChange(@NonNull @org.jetbrains.annotations.NotNull DataSnapshot snapshot) {
                snapshot.getChildren().forEach(s -> {
                    KPI kpi = s.getValue(KPI.class);
                    kpiList.add(kpi);
                });

                //DATA VALIDATION_ENVIRONMENT

                List<KPI> E_Kpi = kpiList.stream()
                        .filter(t -> t.getCategory().equals(Constants.ENVIRONMENT))
                        .filter(t -> t.getSector().equals(sector))
                        .collect(Collectors.toList());

                List<KPI> e_copyList = new ArrayList<>(E_Kpi);

                e_copyList.sort(Comparator.comparingInt(KPI::getScore));

                KPI e_25 = e_copyList.get(25);
                KPI e_75 = e_copyList.get(75);
                int e_dif = e_75.getScore() - e_25.getScore();

                double e_sup = e_75.getScore() + e_dif * 1.5;
                double e_inf = e_25.getScore() - e_dif * 1.5;

                List<KPI> e_finalList = new ArrayList<>();

                for(int i = 0; i < E_Kpi.size(); i++){

                    if(E_Kpi.get(i).getScore() < e_sup && E_Kpi.get(i).getScore() > e_inf){
                        e_finalList.add(E_Kpi.get(i));
                    }
                }


                //DATA VALIDATION_GOVERNANCE

                List<KPI> G_Kpi = kpiList.stream()
                        .filter(t -> t.getCategory().equals(Constants.GOVERNANCE))
                        .filter(t -> t.getSector().equals(sector))
                        .collect(Collectors.toList());

                List<KPI> g_copyList = new ArrayList<>(G_Kpi);

                g_copyList.sort(Comparator.comparingInt(KPI::getScore));

                KPI g_25 = g_copyList.get(25);
                KPI g_75 = g_copyList.get(75);
                int g_dif = g_75.getScore() - g_25.getScore();

                double g_sup = g_75.getScore() + g_dif * 1.5;
                double g_inf = g_25.getScore() - g_dif * 1.5;

                List<KPI> g_finalList = new ArrayList<>();

                for(int i = 0; i < G_Kpi.size(); i++){

                    if(G_Kpi.get(i).getScore() < g_sup && G_Kpi.get(i).getScore() > g_inf){
                        g_finalList.add(G_Kpi.get(i));
                    }
                }

                //DATA SOCIAL

                List<KPI> S_Kpi = kpiList.stream()
                        .filter(t -> t.getCategory().equals(Constants.SOCIAL))
                        .filter(t -> t.getSector().equals(sector))
                        .collect(Collectors.toList());

                List<KPI> s_copyList = new ArrayList<>(S_Kpi);

                s_copyList.sort(Comparator.comparingInt(KPI::getScore));

                KPI s_25 = s_copyList.get(25);
                KPI s_75 = s_copyList.get(75);
                int s_dif = s_75.getScore() - s_25.getScore();

                double s_sup = s_75.getScore() + s_dif * 1.5;
                double s_inf = s_25.getScore() - s_dif * 1.5;

                List<KPI> s_finalList = new ArrayList<>();

                for(int i = 0; i < S_Kpi.size(); i++){

                    if(S_Kpi.get(i).getScore() < s_sup && S_Kpi.get(i).getScore() > s_inf){
                        s_finalList.add(S_Kpi.get(i));
                    }
                }

                //DATA LISTS

                List<Entry> e_Axes = new ArrayList<>();
                List<Entry> g_Axes = new ArrayList<>();
                List<Entry> s_Axes = new ArrayList<>();
                List<Entry> o_Axes = new ArrayList<>();

                List<Entry> future = new ArrayList<>();


                for(int i = 0; i < e_finalList.size(); i++){
                    e_Axes.add(new Entry(i, e_finalList.get(i).getScore()));
                }

                for(int i = 0; i < g_finalList.size(); i++){
                   g_Axes.add(new Entry(i, g_finalList.get(i).getScore()));
                }

                for(int i = 0; i < s_finalList.size(); i++){
                    s_Axes.add(new Entry(i, s_finalList.get(i).getScore()));
                }
                int o_size = Math.min( Math.min(e_finalList.size(), g_finalList.size())
                        , s_finalList.size());

                for(int i = 0; i < o_size; i++){
                    int val = s_finalList.get(i).getScore() +
                            e_finalList.get(i).getScore() +
                            g_finalList.get(i).getScore();

                    val /= 3;

                    o_Axes.add(new Entry(i, val));
                }
                int val1 = s_finalList.get(o_size-1).getScore() +
                        e_finalList.get(o_size-1).getScore() +
                        g_finalList.get(o_size-1).getScore();
                val1/=3;

                int val2 = s_finalList.get(o_size-2).getScore() +
                        e_finalList.get(o_size-2).getScore() +
                        g_finalList.get(o_size-2).getScore();
                val2/=3;


                //FUTURE

                future.add(new Entry(o_size-1,val1));

                int futureVal = val1 - val2;

                future.add(new Entry(o_size -1 , val1));
                future.add(new Entry(o_size , val1 + futureVal));
                future.add(new Entry(o_size + 1, val1 + 2 * futureVal));
                future.add(new Entry(o_size + 2, val1 + 3 * futureVal));

                LineDataSet E_dataSet = new LineDataSet(e_Axes,Constants.ENVIRONMENT);
                LineDataSet G_dataSet = new LineDataSet(g_Axes, Constants.GOVERNANCE);
                LineDataSet S_dataSet = new LineDataSet(s_Axes, Constants.SOCIAL);
                LineDataSet O_dataSet = new LineDataSet(o_Axes, Constants.OVERALL);

                LineDataSet future_dataSet = new LineDataSet(future,"Possible Future");

                E_dataSet.setColor(Color.YELLOW);
                G_dataSet.setColor(Color.GREEN);
                S_dataSet.setColor(R.color.pink);
                O_dataSet.setColor(Color.WHITE);
                future_dataSet.setColor(Color.WHITE);

                E_dataSet.setValueTextColor(Color.WHITE);
                G_dataSet.setValueTextColor(Color.WHITE);
                S_dataSet.setValueTextColor(Color.WHITE);
                O_dataSet.setValueTextColor(Color.WHITE);
                future_dataSet.setValueTextColor(Color.WHITE);

                future_dataSet.enableDashedLine(3f,9f,2f);

                chart.setData(new LineData(O_dataSet,future_dataSet));
                chart.invalidate();

            }

            @Override
            public void onCancelled(@NonNull @org.jetbrains.annotations.NotNull DatabaseError error) {

            }
        });

        e_Button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                currentChart = 1;
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {

                    List<KPI> kpiList = new ArrayList<>();
                    @Override
                    public void onDataChange(@NonNull @org.jetbrains.annotations.NotNull DataSnapshot snapshot) {
                        snapshot.getChildren().forEach(s -> {
                            KPI kpi = s.getValue(KPI.class);
                            kpiList.add(kpi);
                        });

                        //DATA VALIDATION_ENVIRONMENT

                        List<KPI> E_Kpi = kpiList.stream()
                                .filter(t -> t.getCategory().equals(Constants.ENVIRONMENT))
                                .filter(t -> t.getSector().equals(sector))
                                .collect(Collectors.toList());

                        List<KPI> e_copyList = new ArrayList<>(E_Kpi);

                        e_copyList.sort(Comparator.comparingInt(KPI::getScore));

                        KPI e_25 = e_copyList.get(25);
                        KPI e_75 = e_copyList.get(75);
                        int e_dif = e_75.getScore() - e_25.getScore();

                        double e_sup = e_75.getScore() + e_dif * 1.5;
                        double e_inf = e_25.getScore() - e_dif * 1.5;

                        List<KPI> e_finalList = new ArrayList<>();



                        for(int i = 0; i < E_Kpi.size(); i++){

                            if(E_Kpi.get(i).getScore() < e_sup && E_Kpi.get(i).getScore() > e_inf){
                                e_finalList.add(E_Kpi.get(i));
                            }
                        }

                        //DATA LISTS

                        List<Entry> e_Axes = new ArrayList<>();

                        List<Entry> future = new ArrayList<>();

                        for(int i = 0; i < e_finalList.size(); i++){
                            e_Axes.add(new Entry(i, e_finalList.get(i).getScore()));
                        }

                        int val1 =  e_finalList.get(e_finalList.size()-1).getScore();

                        int val2 = e_finalList.get(e_finalList.size()-2).getScore();


                        //FUTURE

                        int futureVal = val1 - val2;

                        future.add(new Entry(e_finalList.size() -1 , val1));
                        future.add(new Entry(e_finalList.size() , val1 + futureVal));
                        future.add(new Entry(e_finalList.size()+ 1, val1 + 2 * futureVal));
                        future.add(new Entry(e_finalList.size() + 2, val1 + 3 * futureVal));

                        LineDataSet E_dataSet = new LineDataSet(e_Axes,Constants.ENVIRONMENT);
                        LineDataSet future_dataSet = new LineDataSet(future,"Possible Future");
                        E_dataSet.setColor(Color.YELLOW);
                        future_dataSet.setValueTextColor(Color.WHITE);
                        future_dataSet.enableDashedLine(6f,9f,2f);

                        chart.setData(new LineData(E_dataSet,future_dataSet));
                        chart.invalidate();

                    }
                    @Override
                    public void onCancelled(@NonNull @org.jetbrains.annotations.NotNull DatabaseError error) {

                    }
                });
            }
        });

        g_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentChart = 2;
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {

                    List<KPI> kpiList = new ArrayList<>();
                    @Override
                    public void onDataChange(@NonNull @org.jetbrains.annotations.NotNull DataSnapshot snapshot) {
                        snapshot.getChildren().forEach(s -> {
                            KPI kpi = s.getValue(KPI.class);
                            kpiList.add(kpi);
                        });

                        //DATA VALIDATION_ENVIRONMENT

                        List<KPI> Kpi = kpiList.stream()
                                .filter(t -> t.getCategory().equals(Constants.GOVERNANCE))
                                .filter(t -> t.getSector().equals(sector))
                                .collect(Collectors.toList());

                        List<KPI> copyList = new ArrayList<>(Kpi);

                        copyList.sort(Comparator.comparingInt(KPI::getScore));

                        KPI e_25 = copyList.get(25);
                        KPI e_75 = copyList.get(75);
                        int dif = e_75.getScore() - e_25.getScore();

                        double sup = e_75.getScore() + dif * 1.5;
                        double inf = e_25.getScore() - dif * 1.5;

                        List<KPI> finalList = new ArrayList<>();

                        for(int i = 0; i < Kpi.size(); i++){

                            if(Kpi.get(i).getScore() < sup && Kpi.get(i).getScore() > inf){
                                finalList.add(Kpi.get(i));
                            }
                        }

                        //DATA LISTS

                        List<Entry> Axes = new ArrayList<>();
                        List<Entry> future = new ArrayList<>();

                        for(int i = 0; i < finalList.size(); i++){
                            Axes.add(new Entry(i, finalList.get(i).getScore()));
                        }

                        int val1 =  finalList.get(finalList.size()-1).getScore();

                        int val2 = finalList.get(finalList.size()-2).getScore();


                        //FUTURE

                        int futureVal = val1 - val2;

                        future.add(new Entry(finalList.size() -1 , val1));
                        future.add(new Entry(finalList.size() , val1 + futureVal));
                        future.add(new Entry(finalList.size()+ 1, val1 + 2 * futureVal));
                        future.add(new Entry(finalList.size() + 2, val1 + 3 * futureVal));


                        LineDataSet dataSet = new LineDataSet(Axes,Constants.GOVERNANCE);
                        LineDataSet future_dataSet = new LineDataSet(future,"Possible Future");

                        dataSet.setColor(Color.GREEN);
                        future_dataSet.setValueTextColor(Color.WHITE);
                        future_dataSet.enableDashedLine(6f,9f,2f);

                        chart.setData(new LineData(dataSet,future_dataSet));
                        chart.invalidate();

                    }
                    @Override
                    public void onCancelled(@NonNull @org.jetbrains.annotations.NotNull DatabaseError error) {

                    }


                });
            }
        });

        s_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentChart = 3;
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {

                    List<KPI> kpiList = new ArrayList<>();
                    @Override
                    public void onDataChange(@NonNull @org.jetbrains.annotations.NotNull DataSnapshot snapshot) {
                        snapshot.getChildren().forEach(s -> {
                            KPI kpi = s.getValue(KPI.class);
                            kpiList.add(kpi);
                        });

                        //DATA VALIDATION_ENVIRONMENT

                        List<KPI> Kpi = kpiList.stream()
                                .filter(t -> t.getCategory().equals(Constants.SOCIAL))
                                .filter(t -> t.getSector().equals(sector))
                                .collect(Collectors.toList());

                        List<KPI> copyList = new ArrayList<>(Kpi);

                        copyList.sort(Comparator.comparingInt(KPI::getScore));

                        KPI e_25 = copyList.get(25);
                        KPI e_75 = copyList.get(75);
                        int dif = e_75.getScore() - e_25.getScore();

                        double sup = e_75.getScore() + dif * 1.5;
                        double inf = e_25.getScore() - dif * 1.5;

                        List<KPI> finalList = new ArrayList<>();

                        for(int i = 0; i < Kpi.size(); i++){

                            if(Kpi.get(i).getScore() < sup && Kpi.get(i).getScore() > inf){
                                finalList.add(Kpi.get(i));
                            }
                        }

                        //DATA LISTS

                        List<Entry> Axes = new ArrayList<>();
                        List<Entry> future = new ArrayList<>();

                        for(int i = 0; i < finalList.size(); i++){
                            Axes.add(new Entry(i, finalList.get(i).getScore()));
                        }

                        int val1 =  finalList.get(finalList.size()-1).getScore();

                        int val2 = finalList.get(finalList.size()-2).getScore();


                        //FUTURE

                        int futureVal = val1 - val2;

                        future.add(new Entry(finalList.size() -1 , val1));
                        future.add(new Entry(finalList.size() , val1 + futureVal));
                        future.add(new Entry(finalList.size()+ 1, val1 + 2 * futureVal));
                        future.add(new Entry(finalList.size() + 2, val1 + 3 * futureVal));

                        LineDataSet dataSet = new LineDataSet(Axes,Constants.SOCIAL);
                        LineDataSet future_dataSet = new LineDataSet(future,"Possible Future");

                        dataSet.setColor(Color.CYAN);
                        future_dataSet.setColor(Color.WHITE);

                        future_dataSet.enableDashedLine(6f,9f,2f);

                        chart.setData(new LineData(dataSet,future_dataSet));
                        chart.invalidate();

                    }
                    @Override
                    public void onCancelled(@NonNull @org.jetbrains.annotations.NotNull DatabaseError error) {

                    }


                });
            }
        });

        allButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentChart = 0;
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {

                    List<KPI> kpiList = new ArrayList<>();
                    @Override
                    public void onDataChange(@NonNull @org.jetbrains.annotations.NotNull DataSnapshot snapshot) {
                        snapshot.getChildren().forEach(s -> {
                            KPI kpi = s.getValue(KPI.class);
                            kpiList.add(kpi);
                        });

                        //DATA VALIDATION_ENVIRONMENT

                        List<KPI> E_Kpi = kpiList.stream()
                                .filter(t -> t.getCategory().equals(Constants.ENVIRONMENT))
                                .filter(t -> t.getSector().equals(sector))
                                .collect(Collectors.toList());

                        List<KPI> e_copyList = new ArrayList<>(E_Kpi);

                        e_copyList.sort(Comparator.comparingInt(KPI::getScore));

                        KPI e_25 = e_copyList.get(25);
                        KPI e_75 = e_copyList.get(75);
                        int e_dif = e_75.getScore() - e_25.getScore();

                        double e_sup = e_75.getScore() + e_dif * 1.5;
                        double e_inf = e_25.getScore() - e_dif * 1.5;

                        List<KPI> e_finalList = new ArrayList<>();

                        for(int i = 0; i < E_Kpi.size(); i++){

                            if(E_Kpi.get(i).getScore() < e_sup && E_Kpi.get(i).getScore() > e_inf){
                                e_finalList.add(E_Kpi.get(i));
                            }
                        }


                        //DATA VALIDATION_GOVERNANCE

                        List<KPI> G_Kpi = kpiList.stream()
                                .filter(t -> t.getCategory().equals(Constants.GOVERNANCE))
                                .filter(t -> t.getSector().equals(sector))
                                .collect(Collectors.toList());

                        List<KPI> g_copyList = new ArrayList<>(G_Kpi);

                        g_copyList.sort(Comparator.comparingInt(KPI::getScore));

                        KPI g_25 = g_copyList.get(25);
                        KPI g_75 = g_copyList.get(75);
                        int g_dif = g_75.getScore() - g_25.getScore();

                        double g_sup = g_75.getScore() + g_dif * 1.5;
                        double g_inf = g_25.getScore() - g_dif * 1.5;

                        List<KPI> g_finalList = new ArrayList<>();

                        for(int i = 0; i < G_Kpi.size(); i++){

                            if(G_Kpi.get(i).getScore() < g_sup && G_Kpi.get(i).getScore() > g_inf){
                                g_finalList.add(G_Kpi.get(i));
                            }
                        }

                        //DATA SOCIAL

                        List<KPI> S_Kpi = kpiList.stream()
                                .filter(t -> t.getCategory().equals(Constants.SOCIAL))
                                .filter(t -> t.getSector().equals(sector))
                                .collect(Collectors.toList());

                        List<KPI> s_copyList = new ArrayList<>(S_Kpi);

                        s_copyList.sort(Comparator.comparingInt(KPI::getScore));

                        KPI s_25 = s_copyList.get(25);
                        KPI s_75 = s_copyList.get(75);
                        int s_dif = s_75.getScore() - s_25.getScore();

                        double s_sup = s_75.getScore() + s_dif * 1.5;
                        double s_inf = s_25.getScore() - s_dif * 1.5;

                        List<KPI> s_finalList = new ArrayList<>();

                        for(int i = 0; i < S_Kpi.size(); i++){

                            if(S_Kpi.get(i).getScore() < s_sup && S_Kpi.get(i).getScore() > s_inf){
                                s_finalList.add(S_Kpi.get(i));
                            }
                        }

                        //DATA LISTS

                        List<Entry> e_Axes = new ArrayList<>();
                        List<Entry> g_Axes = new ArrayList<>();
                        List<Entry> s_Axes = new ArrayList<>();
                        List<Entry> o_Axes = new ArrayList<>();


                        for(int i = 0; i < e_finalList.size(); i++){
                            e_Axes.add(new Entry(i, e_finalList.get(i).getScore()));
                        }

                        for(int i = 0; i < g_finalList.size(); i++){
                            g_Axes.add(new Entry(i, g_finalList.get(i).getScore()));
                        }

                        for(int i = 0; i < s_finalList.size(); i++){
                            s_Axes.add(new Entry(i, s_finalList.get(i).getScore()));
                        }
                        int o_size = Math.min( Math.min(e_finalList.size(), g_finalList.size())
                                , s_finalList.size());

                        for(int i = 0; i < o_size; i++){
                            int val = s_finalList.get(i).getScore() +
                                    e_finalList.get(i).getScore() +
                                    g_finalList.get(i).getScore();

                            val /= 3;

                            o_Axes.add(new Entry(i, val));
                        }

                        List<Entry> future = new ArrayList<>();

                        int val1 = s_finalList.get(o_size-1).getScore() +
                                e_finalList.get(o_size-1).getScore() +
                                g_finalList.get(o_size-1).getScore();
                        val1/=3;

                        int val2 = s_finalList.get(o_size-2).getScore() +
                                e_finalList.get(o_size-2).getScore() +
                                g_finalList.get(o_size-2).getScore();
                        val2/=3;


                        //FUTURE

                        future.add(new Entry(o_size-1,val1));

                        int futureVal = val1 - val2;

                        future.add(new Entry(o_size -1 , val1));
                        future.add(new Entry(o_size , val1 + futureVal));
                        future.add(new Entry(o_size + 1, val1 + 2 * futureVal));
                        future.add(new Entry(o_size + 2, val1 + 3 * futureVal));

                        LineDataSet E_dataSet = new LineDataSet(e_Axes,Constants.ENVIRONMENT);
                        LineDataSet G_dataSet = new LineDataSet(g_Axes, Constants.GOVERNANCE);
                        LineDataSet S_dataSet = new LineDataSet(s_Axes, Constants.SOCIAL);
                        LineDataSet O_dataSet = new LineDataSet(o_Axes, Constants.OVERALL);

                        LineDataSet future_dataSet = new LineDataSet(future,"Possible Future");

                        E_dataSet.setColor(Color.YELLOW);
                        G_dataSet.setColor(Color.GREEN);
                        S_dataSet.setColor(R.color.pink);
                        O_dataSet.setColor(Color.WHITE);

                        future_dataSet.setColor(Color.WHITE);

                        E_dataSet.setValueTextColor(Color.WHITE);
                        G_dataSet.setValueTextColor(Color.WHITE);
                        S_dataSet.setValueTextColor(Color.WHITE);
                        O_dataSet.setValueTextColor(Color.WHITE);

                        future_dataSet.setValueTextColor(Color.WHITE);

                        future_dataSet.enableDashedLine(6f,9f,2f);

                        chart.setData(new LineData(O_dataSet, future_dataSet));
                        chart.invalidate();

                    }

                    @Override
                    public void onCancelled(@NonNull @org.jetbrains.annotations.NotNull DatabaseError error) {

                    }
                });
            }
        });

        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StatisticsActivity.this, StartActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int i = seekBar.getProgress();
                List<ILineDataSet> dataSet = chart.getLineData().getDataSets();

                ILineDataSet future = dataSet.get(1);
                ILineDataSet current = dataSet.get(0);

                List<Entry> futureEntry = new ArrayList<>();
                List<Entry> currentEntry = new ArrayList<>();

                if(initial_E_value == 0){
                    initial_E_value = future.getEntryForIndex(0).getY();
                }

                int futureEntriesCount = future.getEntryCount();
                int currentEntriesCount = current.getEntryCount();

                for(int j = 0; j < currentEntriesCount; j++){
                    Entry e = current.getEntryForIndex(j);
                    currentEntry.add(e);
                }

                for(int j = 0; j < futureEntriesCount; j++){
                    Entry e = future.getEntryForIndex(j);
                    futureEntry.add(e);
                }

                switch (currentChart){
                    case 0:
                        System.out.println("oll");
                        break;
                    case 1:
                        int count = -1;
                        for(Entry e: futureEntry){
                            count++;
                            float oldValue = e.getY();
                            float newValue;
                            if(oldValue < 0){
                                newValue = oldValue - (initial_E_value * i ) / 100;
                            }else{
                                newValue = oldValue + (initial_E_value * i) / 100;
                            }
                            if(count != 0){
                                e.setY(newValue);
                            }

                        }
                        LineDataSet E_dataSet = new LineDataSet(currentEntry,Constants.ENVIRONMENT);
                        LineDataSet future_dataSet = new LineDataSet(futureEntry,"Possible Future");
                        E_dataSet.setColor(Color.YELLOW);
                        future_dataSet.setValueTextColor(Color.WHITE);
                        future_dataSet.enableDashedLine(6f,9f,2f);
                        chart.setData(new LineData(E_dataSet,future_dataSet));
                        chart.invalidate();

                        break;
                }



            }
        });

    }
}