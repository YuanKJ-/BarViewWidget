package com.texchi.car.widget;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import com.texchi.car.widget.NumberInputView.NumberInput;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private String CURSOR_STR = "";

    private NumberInputView numberInput;
    private TextView inputView;
    private String pwdStr = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.number_input_view);

        CURSOR_STR = getString(R.string.input_cursor);
        inputView = (TextView) findViewById(R.id.input);
        numberInput = (NumberInputView) findViewById(R.id.recycler_view);
        numberInput.setCommandListener(new NumberInputView.CommandListener() {
            @Override
            public void onCommand(String command) {
                if(command.equals(NumberInput.BACK)){
                    if(pwdStr.length() > 0) {
                        pwdStr = pwdStr.substring(0, pwdStr.length() - 1);
                    }
                }else if(command.equals(NumberInput.OK)) {

                }else {
                    if(pwdStr.length() < 4) {
                        pwdStr += command;
                    }
                }
                String text = pwdStr;
                if(pwdStr.length() <= 0) {
                    text = pwdStr + CURSOR_STR;
                }
                inputView.setText(text);
            }
        });
    }


}
