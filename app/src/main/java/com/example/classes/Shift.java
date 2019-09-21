package com.example.classes;

import java.time.LocalTime;

public class Shift  {
    public String start;
    public String end;

    public Shift(String start, String end) {
        this.start = start;
        this.end = end;
    }

    public Shift() {}

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public int compereHours() {

        LocalTime st = LocalTime.parse(start);
        LocalTime en = LocalTime.parse(end);

        return st.compareTo(en);
    }

}
