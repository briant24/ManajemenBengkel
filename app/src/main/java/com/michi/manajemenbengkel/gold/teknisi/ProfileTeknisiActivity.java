package com.michi.manajemenbengkel.gold.teknisi;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.android.material.textfield.TextInputEditText;
import com.michi.manajemenbengkel.gold.R;
import com.michi.manajemenbengkel.gold.koneksi.KoneksiAPI;

import org.json.JSONArray;
import org.json.JSONObject;

public class ProfileTeknisiActivity extends AppCompatActivity {
    private Button btnkembali,btnubah;
    private TextInputEditText txnama,txuser,txpass,txjabatan;
    private String stnama,stuser,stpass,stlevel;
    private SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_teknisi);
        sharedPreferences = this.getSharedPreferences("user-session", Context.MODE_PRIVATE);
        txnama = findViewById(R.id.txtNamaprof);
        txuser = findViewById(R.id.txtUsernameprof);
        txpass = findViewById(R.id.txtPasswordprof);
        txjabatan = findViewById(R.id.txtJabatanprof);
        txjabatan.isEnabled();
        btnkembali = findViewById(R.id.btnKembaliProfileTek);
        btnubah = findViewById(R.id.btnUbahProfileTek);
        btnubah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String idtek = sharedPreferences.getString("id_user",null);
                stnama = txnama.getText().toString();
                stuser = txuser.getText().toString();
                stpass = txpass.getText().toString();
                stlevel = txjabatan.getText().toString();
                if (stnama.isEmpty() || stuser.isEmpty() || stpass.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Data Belum Lengkap", Toast.LENGTH_SHORT).show();
                }else {
                    updateData(idtek,stnama,stuser,stpass,stlevel);
                }
            }
        });
        btnkembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                finish();
            }
        });
    }

    private void updateData(String idtek, String stnama, String stuser, String stpass, String stlevel) {
        AndroidNetworking.post(KoneksiAPI.AllUser)
                .addBodyParameter("id",idtek)
                .addBodyParameter("level",stlevel)
                .addBodyParameter("nama",stnama)
                .addBodyParameter("username",stuser)
                .addBodyParameter("password",stpass)
                .addBodyParameter("tipe","1")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(getApplicationContext(), "Berhasil Diupdate", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(getApplicationContext(), "Gagal Diupdate", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    protected void onStart() {
        super.onStart();
        GetData();
    }

    private void GetData() {
        String idtek = sharedPreferences.getString("id_user",null);
        AndroidNetworking.post(KoneksiAPI.showProfile)
                .addBodyParameter("id",idtek)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("data_user");
                            for (int a = 0; a < jsonArray.length(); a++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(a);
                                String nama = jsonObject.getString("nama_user");
                                String user = jsonObject.getString("username");
                                String pass = jsonObject.getString("password");
                                String lev = jsonObject.getString("level");
                                txnama.setText(nama);
                                txuser.setText(user);
                                txpass.setText(pass);
                                txjabatan.setText(lev);
                            }
                        }catch (Exception e){
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                    }
                });
    }
}