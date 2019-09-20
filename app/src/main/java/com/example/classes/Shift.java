package com.example.classes;

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
}
