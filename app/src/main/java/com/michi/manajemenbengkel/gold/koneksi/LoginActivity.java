package com.michi.manajemenbengkel.gold.koneksi;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.michi.manajemenbengkel.gold.R;
import com.michi.manajemenbengkel.gold.kasir.ListTransaksiActivity;
import com.michi.manajemenbengkel.gold.manager.ManagerActivity;
import com.michi.manajemenbengkel.gold.teknisi.MainActivity;
import com.michi.manajemenbengkel.gold.warehouse.ListItem;

import org.json.JSONArray;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    private EditText etuser,etpass;
    private String stuser,stpass;
    private Button btnlog;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        etuser = findViewById(R.id.etUserLog);
        etpass = findViewById(R.id.etPassLog);
        btnlog = findViewById(R.id.btnLog);
        sharedPreferences = getSharedPreferences("user-session",MODE_PRIVATE);
        btnlog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stuser = etuser.getText().toString();
                stpass = etpass.getText().toString();
                cekuser(stuser,stpass);
            }
        });
    }

    private void cekuser(String stuser, String stpass) {
        AndroidNetworking.post(KoneksiAPI.login)
                .addBodyParameter("username", stuser)
                .addBodyParameter("password", stpass)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("login");
                            Integer inthasil = jsonArray.length();
                            if (inthasil.equals(1)) {
                                for (int a = 0; a < jsonArray.length(); a++) {
                                    JSONObject json = jsonArray.getJSONObject(a);
                                    String nama = json.getString("nama_user");
                                    String iduser = json.getString("id_user");
                                    String level = json.getString("level");
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putString("id_user",iduser);
                                    editor.apply();
                                    if (level.equals("Teknisi")) {
                                        Intent teknisi = new Intent(getApplicationContext(), MainActivity.class);
                                        teknisi.putExtra("nama", nama);
                                        startActivity(teknisi);
                                    } else if (level.equals("Admin Warehouse")) {
                                        Intent admin = new Intent(getApplicationContext(), ListItem.class);
                                        startActivity(admin);
                                    } else if (level.equals("Kasir")) {
                                        Intent kasir = new Intent(getApplicationContext(), ListTransaksiActivity.class);
                                        startActivity(kasir);
                                    } else if (level.equals("Manager")) {
                                        Intent manager = new Intent(getApplicationContext(), ManagerActivity.class);
                                        manager.putExtra("nama", nama);
                                        startActivity(manager);
                                    }
                                }
                            } else {
                                Toast.makeText(getApplicationContext(), "Gagal", Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(getApplicationContext(), "Jaringan Gagal", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "onError() returned: " + anError);
                    }
                });
    }

    public void btnForgot(View view) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this)
                .setTitle("Info")
                .setMessage("Silahkan Menghubungi Admin!")
                .setNeutralButton("OK", null);
        dialog.create();
        dialog.show();
    }
}