package com.example.listeners;

import android.graphics.Color;
import android.view.View;
import android.widget.EditText;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OnFocusChangeAddShift implements OnFocusChangeAddShiftInterface {

    private final EditText et;

    public OnFocusChangeAddShift(EditText et) {
        this.et = et;
    }

    //******************************************//
    //  This method check if the format in
    //  hour EditText is in the right format
    //  if the format is not correctly
    //  fix the string or give the user a sign
    //*****************************************//
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
                    if (matcher.matches() || matcher2.matches()) {
                        if (matcher2.matches()) {
                            String hour = et.getText().subSequence(0,2).toString();
                            String minutes = et.getText().subSequence(2,4).toString();
                            et.setText(hour+":"+minutes);
                        }
                        et.setBackgroundColor(Color.TRANSPARENT);
                    }
                    else {
                        et.setBackgroundColor(Color.RED);
                        et.setText("");
                    }
                }
            }
        };
    }
}
