package com.michi.manajemenbengkel.gold;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class TeknisiWorkActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teknisi_work);
        CustFragment custFragment = new CustFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, custFragment).commit();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }
}