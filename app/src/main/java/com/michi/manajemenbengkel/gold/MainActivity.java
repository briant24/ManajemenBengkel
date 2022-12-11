package com.michi.manajemenbengkel.gold;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    TextView nama, tanggal;
    String snama, currentdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        nama = findViewById(R.id.intNama);
        tanggal = findViewById(R.id.txt_tgl);

        Calendar calendar = Calendar.getInstance();
        currentdate = DateFormat.getDateInstance(android.icu.text.DateFormat.FULL).format(calendar.getTime());
        tanggal.setText(currentdate);
    }
}