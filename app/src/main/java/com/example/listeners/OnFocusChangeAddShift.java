package com.example.listeners;

import android.graphics.Color;
import android.view.View;
import android.widget.EditText;

import com.example.classes.Shift;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OnFocusChangeAddShift implements OnFocusChangeAddShiftInterface {

    private EditText et;
    private LocalTime time;

    public OnFocusChangeAddShift(EditText et, LocalTime time) {
        this.et = et;
        this.time = time;
    }

    @Override
    public View.OnFocusChangeListener listener() {
        return new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    Pattern pattern = Pattern.compile("([0-2][0-3]|[0-1][0-9]):[0-5][0-9]");
                    Pattern pattern2 = Pattern.compile("([0-2][0-3]|[0-1][0-9])[0-5][0-9]");
                    Matcher matcher = pattern.matcher(et.getText().toString());
                    Matcher matcher2 = pattern2.matcher(et.getText().toString());
                    //&& (Integer.getInteger(et.getText().subSequence(0,2).toString()) <= 23) && (Integer.getInteger(et.getText().subSequence(3,5).toString()) <= 59)
                    if (matcher.matches() || matcher2.matches()) {
                        if (matcher2.matches()) {
                            String hour = et.getText().subSequence(0,2).toString();
                            String minutes = et.getText().subSequence(2,4).toString();
                            et.setText(hour+":"+minutes);
                        }
                        time = LocalTime.parse(et.getText().toString());
                        et.setBackgroundColor(Color.WHITE);
                    }
                    else
                        //time = LocalTime.of(0,0);
                        et.setBackgroundColor(Color.RED);
                        //et.setText("");
                }
            }
        };
    }
}
