package com.michi.manajemenbengkel.gold.kasir;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.michi.manajemenbengkel.gold.R;
import com.michi.manajemenbengkel.gold.teknisi.ChildCustItemFragment;
import com.michi.manajemenbengkel.gold.teknisi.ChildShowCustItemFragment;
import com.michi.manajemenbengkel.gold.teknisi.SectionPageAdapter;

public class ListTransaksiActivity extends AppCompatActivity {
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private Button logout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_transaksi);
        logout = findViewById(R.id.btnlogoutkasir);
        viewPager = findViewById(R.id.viewlistTrans);
        tabLayout = findViewById(R.id.tabLayout2);
        setUpViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void setUpViewPager(ViewPager viewPager) {
        SectionAdapter adapter = new SectionAdapter(getSupportFragmentManager());
        adapter.addFragment(new BelumBayarFragment(), "Belum Bayar");
        adapter.addFragment(new SudahBayarFragment(), "Sudah Bayar");
        viewPager.setAdapter(adapter);
    }
}