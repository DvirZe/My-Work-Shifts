package com.example.classes;

import com.example.database.Shift;

import org.junit.Test;

import static junit.framework.TestCase.assertEquals;

public class ShiftTest {

    @Test
    public void testIfHoursOk() {
        Shift shift = new Shift();
        shift.start = "04:55";
        shift.end = "06:52";
        ShiftHours shiftHours = new ShiftHours(shift);
        assertEquals(-1, shiftHours.compereHours());
    }
}