package com.vladv.dbpopulate.entities;

import java.util.Objects;

public class KPI {

    private String sector;
    private String description;
    private String category; //E, S or G
    private int score; //from 1 to 100

    public KPI() {

    }

    public KPI(String sector, String description, String category, int score) {
        this.sector = sector;
        this.description = description;
        this.category = category;
        this.score = score;
    }

    public String getSector() {
        return sector;
    }

    public void setSector(String sector) {
        this.sector = sector;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        KPI kpi = (KPI) o;
        return score == kpi.score &&
                sector.equals(kpi.sector) &&
                description.equals(kpi.description) &&
                category.equals(kpi.category);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sector, description, category, score);
    }

    @Override
    public String toString() {
        return "KPI{" +
                "sector='" + sector + '\'' +
                ", description='" + description + '\'' +
                ", category='" + category + '\'' +
                ", score=" + score +
                '}';
    }
}
