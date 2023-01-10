package com.michi.manajemenbengkel.gold.manager;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.michi.manajemenbengkel.gold.R;
import com.michi.manajemenbengkel.gold.kasir.ListTransaksiActivity;
import com.michi.manajemenbengkel.gold.koneksi.RegisterActivity;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.List;

public class ManagerActivity extends AppCompatActivity {
    private TextView nama, tanggal, iduser;
    private String currentdate;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager);
        nama = findViewById(R.id.intNamaMan);
        tanggal = findViewById(R.id.txt_tglll);
        iduser = findViewById(R.id.idUserr);
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
    }
    public void listuser(View view) {
        Intent list = new Intent(getApplicationContext(), ListUserActivity.class);
        startActivity(list);
    }

    public void tambahuser(View view) {
        Intent reg = new Intent(getApplicationContext(), RegisterActivity.class);
        startActivity(reg);
    }

    public void history(View view) {
        Intent his = new Intent(getApplicationContext(), ListTransaksiActivity.class);
        startActivity(his);
    }

    public void keluar(View view) {
        finish();
        System.exit(0);
    }
}