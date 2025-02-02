package com.example.smrthomegestureapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity {
    // Constants
    public static final String SELECTED_GESTURE = "selectedGesture";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Setup the dropdown menu with the gesture options
        Spinner spinner = findViewById(R.id.gestures_dropdown);

        // Create an ArrayAdapter using the string array and a default spinner layout.
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.gestures_array,
                android.R.layout.simple_spinner_item
        );
        // Specify the layout to use when the list of choices appears.
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner.
        spinner.setAdapter(adapter);

        // Set the spinner callback function
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            Boolean firstTime = true;

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(firstTime) {
                    firstTime = false; // Not the best solution, need to find a better way to go about this
                }
                else {
                    Intent intent = new Intent(MainActivity.this, GestureDemoViewsActivity.class);
                    intent.putExtra(SELECTED_GESTURE, adapterView.getItemAtPosition(i).toString());
                    startActivity(intent);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


    }
}