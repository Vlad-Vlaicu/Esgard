package com.vladv.esguard.utilities;

import com.vladv.esguard.entities.KPI;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class DataValidation {

    public static List<KPI> dataValidation(List<KPI> kpiList){

        List<KPI> copyList = new ArrayList<>(kpiList);

        copyList.sort(Comparator.comparingInt(KPI::getScore));

        KPI e_25 = kpiList.get(25);
        KPI e_75 = kpiList.get(75);
        int dif = e_75.getScore() - e_25.getScore();

        double sup = e_75.getScore() + dif * 1.5;
        double inf = e_25.getScore() - dif * 1.5;

        List<KPI> indexList = new ArrayList<>();

        for(int i = 0; i < kpiList.size(); i++){

            if(kpiList.get(i).getScore() < sup && kpiList.get(i).getScore() > inf){
                indexList.add(kpiList.get(i));
            }
        }

        return indexList;

    }
}
