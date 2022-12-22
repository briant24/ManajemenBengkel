package com.michi.manajemenbengkel.gold;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

public class LoginActivity extends AppCompatActivity {

    private EditText etuser,etpass;
    private String stuser,stpass;
    private Button btnlog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        etuser = findViewById(R.id.etUserLog);
        etpass = findViewById(R.id.etPassLog);
        btnlog = findViewById(R.id.btnLog);

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
        Log.i(TAG, "cekuser: "+ stuser + stpass);
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
                                    String username = json.getString("username");
                                    String password = json.getString("password");
                                    String level = json.getString("level");
                                    if (level.equals("Teknisi")) {
                                        Intent teknisi = new Intent(getApplicationContext(), MainActivity.class);
                                        teknisi.putExtra("nama", nama);
                                        startActivity(teknisi);
                                    } else if (level.equals("Admin Warehouse")) {
                                        Intent admin = new Intent(getApplicationContext(), ListItem.class);
                                        startActivity(admin);
                                    } else if (level.equals("Kasir")) {
                                        Intent kasir = new Intent(getApplicationContext(), RegisterActivity.class);
                                        startActivity(kasir);
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
                        Log.i("cek error", "onError: " + anError.getErrorDetail());
                        Toast.makeText(getApplicationContext(), "Jaringan Gagal", Toast.LENGTH_SHORT).show();
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