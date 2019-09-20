package com.example.classes;

import java.time.LocalTime;

public class Shift  {
    public LocalTime start;
    public LocalTime end;

    public Shift(LocalTime start, LocalTime end) {
        this.start = start;
        this.end = end;
    }

    public Shift() {}

}
