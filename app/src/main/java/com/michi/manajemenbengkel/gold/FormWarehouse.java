package com.michi.manajemenbengkel.gold;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.androidnetworking.AndroidNetworking;

public class FormWarehouse extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_warehouse);
        AndroidNetworking.initialize(getApplicationContext());
    }
}