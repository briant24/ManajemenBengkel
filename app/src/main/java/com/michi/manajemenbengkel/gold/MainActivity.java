package com.michi.manajemenbengkel.gold;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    private TextView nama, tanggal, iduser;
    private String currentdate;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        nama = findViewById(R.id.intNama);
        tanggal = findViewById(R.id.txt_tgl);
        iduser = findViewById(R.id.idUser);
        Calendar calendar = Calendar.getInstance();
        currentdate = DateFormat.getDateInstance(android.icu.text.DateFormat.FULL).format(calendar.getTime());
        tanggal.setText(currentdate);
        sharedPreferences = getSharedPreferences("user-session",MODE_PRIVATE);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent data = getIntent();
        String namaUser = data.getStringExtra("nama");
        nama.setText(namaUser);
        Log.i(TAG, "cekshared" + sharedPreferences.getString("id_user",null));
    }

    public void keluar(View view) {
        onBackPressed();
    }

    public void history(View view) {
    }

    public void bekerja(View view) {
        Intent kerja = new Intent(getApplicationContext(), TeknisiWorkActivity.class);
        startActivity(kerja);
    }

    public void profile(View view) {
    }
}