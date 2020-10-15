package com.example.OmiWatch;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

public class StartActivity extends AppCompatActivity {

    private int countSeconds;
    private boolean isRunning;
    private boolean wasRunning;

    TextView btStart, btReset;
    TextView timeTV;
    Handler myHandler;

    TextView myAppLifeCycleTV;
    String lifeCycleText = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();

        if (savedInstanceState != null) {
            savedInstanceState.getInt("seconds");
            savedInstanceState.getBoolean("running");
            savedInstanceState.getBoolean("wasRunning");
            savedInstanceState.getString("lifeCycle");
        }

        runTime();

        setClickListener();
    }

    private void setClickListener() {
        btStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isRunning){
                    isRunning = true;
                    btStart.setText("Pause");
                    btReset.setVisibility(View.INVISIBLE);
                    lifeCycleText = lifeCycleText + "onStart\n";
                    showToast("Start");
                }
                else {
                    isRunning = false;
                    btStart.setText("Start");
                    btReset.setVisibility(View.VISIBLE);
                    lifeCycleText = lifeCycleText + "onStop\n";
                    showToast("Pause");
                }
                myAppLifeCycleTV.setText(lifeCycleText);
            }
        });
    }

    private void showToast(String status) {
        Toast toast = Toast.makeText(StartActivity.this, status, Toast.LENGTH_SHORT);
        toast.show();
    }

    private void init() {
        btReset = findViewById(R.id.resetBT);
        timeTV = findViewById(R.id.timeView);
        btStart = findViewById(R.id.startBT);
        myHandler = new Handler();
        myAppLifeCycleTV = findViewById(R.id.textViewTV);
        lifeCycleText = lifeCycleText + "onCreate\n";
        myAppLifeCycleTV.setText(lifeCycleText);
        btReset.setVisibility(View.INVISIBLE);
    }

    public void onReset(View view) {
        isRunning = false;
        countSeconds = 0;
        showToast("Reset");
        showOnResetStatus();
    }

    private void showOnResetStatus() {
        btReset.setVisibility(View.INVISIBLE);
        lifeCycleText = lifeCycleText + "onReset\n";
        myAppLifeCycleTV.setText(lifeCycleText);
    }

    @Override
    protected void onPause() {
        super.onPause();
        showOnPauseStatus();
    }
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("seconds", countSeconds);
        outState.putBoolean("running", isRunning);
        outState.putBoolean("wasRunning", wasRunning);
        outState.putString("lifeCycle", lifeCycleText);
        lifeCycleText = lifeCycleText + "onSaveInstanceState\n";
        myAppLifeCycleTV.setText(lifeCycleText);
    }


    private void showOnPauseStatus() {
        wasRunning = isRunning;
        isRunning = false;
        lifeCycleText = lifeCycleText + "onPause\n";
        myAppLifeCycleTV.setText(lifeCycleText);
    }

    @Override
    protected void onResume() {
        super.onResume();
        showOnResumeStatus();
    }

    private void showOnResumeStatus() {
        if (wasRunning) {
            isRunning = true;
        }
        lifeCycleText = lifeCycleText + "onResume\n";
        myAppLifeCycleTV.setText(lifeCycleText);
    }

    private void runTime() {
        myHandler.post(new Runnable() {
            @Override
            public void run() {
                int hours = countSeconds / 3600;
                int minutes = (countSeconds % 3600) / 60;
                int secs = countSeconds % 60;

                String time = String.format(Locale.getDefault(), "%d:%02d:%02d", hours, minutes, secs);
                timeTV.setText(time);

                if (isRunning) {
                    countSeconds++;
                }
                myHandler.postDelayed(this, 1000);
            }
        });
        lifeCycleText = lifeCycleText + "runTime\n";
        myAppLifeCycleTV.setText(lifeCycleText);
    }



    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        countSeconds = savedInstanceState.getInt("seconds");
        isRunning = savedInstanceState.getBoolean("running");
        wasRunning = savedInstanceState.getBoolean("wasRunning");
        lifeCycleText = savedInstanceState.getString("lifeCycle");
        lifeCycleText = lifeCycleText + "onRestoreInstanceState\n";
        myAppLifeCycleTV.setText(lifeCycleText);
    }
}