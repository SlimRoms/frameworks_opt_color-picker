package com.enrico.sample;


import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ContextThemeWrapper;
import android.view.View;
import android.widget.TextView;

import com.enrico.colorpicker.ColorPickerDialog;
import com.enrico.colorpicker.ColorPickerDialogFragment;

public class MainActivity extends AppCompatActivity implements
        ColorPickerDialogFragment.ColorPickedListener {

    int color, color2;

    //All the views :D
    View oneView, twoView, thirdView;

    TextView textView1, textView2;

    //ContextThemeWrapper
    ContextThemeWrapper themeWrapper;

    SharedPreferences mPrefs;

    @SuppressWarnings("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //apply activity's theme if dark theme is enabled
        themeWrapper = new ContextThemeWrapper(getBaseContext(), this.getTheme());

        mPrefs = getPreferences(0);

        color = mPrefs.getInt("color", Color.RED);
        color2 = mPrefs.getInt("color2", Color.BLUE);

        Preferences.applyTheme(themeWrapper, getBaseContext());

        setContentView(R.layout.activity_main);

        oneView = findViewById(R.id.one);

        oneView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ColorPickerDialogFragment.showColorPicker(
                        MainActivity.this, MainActivity.this, color, "one");

            }
        });

        twoView = findViewById(R.id.two);

        twoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ColorPickerDialogFragment.showColorPicker(
                        MainActivity.this, MainActivity.this, color2, "two");
            }
        });

        thirdView = findViewById(R.id.third);

        thirdView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent prefActivity = new Intent(MainActivity.this, PreferenceActivity.class);
                startActivity(prefActivity);
            }
        });

        textView1 = (TextView) findViewById(R.id.text1);
        textView2 = (TextView) findViewById(R.id.text2);

        setViewColor(color, oneView, textView1);
        setViewColor(color2, twoView, textView2);

    }

    private void setViewColor(int color, View view, TextView textView) {
        view.setBackgroundColor(color);
        textView.setText(Integer.toHexString(color).toUpperCase());
        textView.setTextColor(ColorPickerDialog.getComplementaryColor(color));
    }

    @Override
    public void onColorPicked(String key, int color) {
        if (key.equals("one")) {
            setViewColor(color, oneView, textView1);
        } else if (key.equals("two")) {
            setViewColor(color, twoView, textView2);
        }
    }
}

