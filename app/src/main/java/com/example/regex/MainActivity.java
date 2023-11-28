package com.example.regex;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.sql.Array;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    private EditText editText;
    private Spinner spinner;
    private Button validateBtn;
    private TextView resultText;

    private String[] regexArray = {
            // ąćęłńóśźżĄĆĘŁŃÓŚŹŻ - ciag polskich znakow
            // @#$%^&+=! - ciag znakow specjanych
            "^\\d{2}-\\d{3}$",
            "^[A-Za-z]+(\\s[A-Za-z]+)?\\s\\d+[A-Za-z]*$",
            "^(\\+\\d{11}|\\d{9}|\\d{7})$",
            "^[A-Za-z][A-Za-z0-9]{2,}@[A-Za-z0-9]{3,}+\\.[A-Za-z]{2,}$",
            "^\\d{11}$",
            "^[a-zA-Z](?=.*\\d)(?=.*[a-zA-Z])\\w+$",
            "^[A-Za-z]+\\s[A-Za-z]+(-[A-Za-z]+)?$",
            "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!]).{7,}$",
            "^(?!.*(?:wtf|ftw)).*$" // wtf; ftw
            };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText = findViewById(R.id.editText);
        spinner = findViewById(R.id.spinner);
        validateBtn = findViewById(R.id.validateBtn);
        resultText = findViewById(R.id.resultText);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.spinnerList,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        validateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard((Button)v);
                int checked = (int) spinner.getSelectedItemId();
                String checkedValue = (String) spinner.getSelectedItem();
                String text = String.valueOf(editText.getText());
                String result = "CORRECT";

                if(!Pattern.matches(regexArray[checked], text)){
                    result = "WRONG";
                }
                else if(checkedValue.equals("pesel")){
                    PeselValidator pesel = new PeselValidator(text);
                    if(!pesel.isValid()){
                        result = "WRONG";
                    }
                    else{
                        result += " ";
                        result += pesel.getSex().substring(0,1);
                    }
                }
                else if(checkedValue.equals("zip-code")){
                    try {
                        ZipCodeChecker.checkCity(text);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }

                resultText.setText(result);
            }
        });

    }

    public void hideKeyboard(View view) {
        try {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        } catch(Exception ignored) {
        }
    }
}