package com.gopher.MedsOne;

import java.util.ArrayList;
import java.util.List;

public class DailyMed {
    private String date;
    private String time;
    private List<String> medsList;
    private List<Boolean> medsTakenList;

    public DailyMed(String date, String time, List<String> medsList, List<Boolean> medsTakenList) {
        this.date = date;
        this.time = time;
        this.medsList = medsList;
        this.medsTakenList = medsTakenList;
    }
    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public List<String> getMedsList() {
        return medsList;
    }
    public List<Boolean> getMedsTakenList() {
        return medsTakenList;
    }

    public void setDate(String date) { this.date = date; }
    public void setTime(String time) { this.time = time; }
    public void setMedsList(List<String> medsList) { this.medsList = medsList; }
    public void setMedsTakenList(List<Boolean> medsTakenList) { this.medsTakenList = medsTakenList; }

    public static List<DailyMed> loadDailyMedsList() {
        List<DailyMed> dailyMedList = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            String date = "May " + (i+1);
            String time = "Morning";
            List<String> medsList = new ArrayList<>();
            List<Boolean> medsTakenList = new ArrayList<>();
            for (int j = 0; j<3; j++) {
                medsList.add("Medicine " + (i+1));
                medsTakenList.add(false);
            }

            DailyMed dailyMed = new DailyMed(date, time, medsList, medsTakenList);
            dailyMedList.add(dailyMed);
        }
        return dailyMedList;
    }
}
