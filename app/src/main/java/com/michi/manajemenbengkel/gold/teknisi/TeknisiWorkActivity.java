package com.michi.manajemenbengkel.gold.teknisi;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.michi.manajemenbengkel.gold.R;
import com.michi.manajemenbengkel.gold.teknisi.CustFragment;

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