package com.vladv.dbpopulate.dao;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.vladv.dbpopulate.entities.KPI;

public class KPIDao {
    private final DatabaseReference databaseReference;

    public KPIDao() {
        FirebaseDatabase db =FirebaseDatabase.getInstance("https://esguard-11076-default-rtdb.europe-west1.firebasedatabase.app");
        databaseReference =db.getReference(KPI.class.getSimpleName());
    }

    public Task<Void> addKPI(KPI kpi){
        return databaseReference.push().setValue(kpi);
    }
}
