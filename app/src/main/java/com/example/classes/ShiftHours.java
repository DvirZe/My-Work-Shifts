package com.example.classes;


import com.example.database.Shift;

import java.time.LocalTime;

public class ShiftHours implements ShiftHoursInterface {

    private String startHour;
    private String endHour;

    public ShiftHours(Shift shift) {
        if (shift != null) {
            startHour = shift.start;
            endHour = shift.end;
        }
    }

    public ShiftHours() {}

    @Override
    public String getStart() {
        return startHour;
    }

    @Override
    public void setStart(String start) {
        startHour = start;
    }

    @Override
    public String getEnd() {
        return endHour;
    }

    @Override
    public void setEnd(String end) {
        endHour = end;
    }

    @Override
    public int compereHours() {
        LocalTime st = LocalTime.parse(startHour);
        LocalTime en = LocalTime.parse(endHour);

        return st.compareTo(en);
    }
}
