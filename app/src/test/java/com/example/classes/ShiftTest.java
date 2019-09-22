package com.example.classes;

import com.example.database.Shift;

import org.junit.Test;

public class ShiftTest {

    @Test
    public void testIfHoursOk() {
        Shift shift = new Shift();
        shift.start = "04:55";
        shift.end = "06:52";
        assertEquals(-1, shift.compereHours());
    }
}