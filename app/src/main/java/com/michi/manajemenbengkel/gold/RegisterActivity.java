package com.michi.manajemenbengkel.gold;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.CountDownTimer;
import android.util.Base64;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONObject;

public class RegisterActivity extends AppCompatActivity {
    private EditText etNamareg, etUserreg, etPassreg;
    private String etnama, etuser, etpass, spinner_data;
    private Toast pesan;
    private Spinner dropdown;
    private Button goregister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        dropdown = findViewById(R.id.spinner);

        String[] items = new String[]{"Teknisi", "Admin Warehouse", "Kasir"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        dropdown.setAdapter(adapter);
        etNamareg = findViewById(R.id.etNamaReg);
        etUserreg = findViewById(R.id.etUserReg);
        etPassreg = findViewById(R.id.etPasswordReg);
        goregister = findViewById(R.id.btnReg);
        String spinner_data = dropdown.getSelectedItem().toString();
        goregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cekvalidasidata();
            }
        });
    }

    private void cekvalidasidata() {
        etnama = etNamareg.getText().toString();
        etuser = etUserreg.getText().toString();
        etpass = etPassreg.getText().toString();
        spinner_data = dropdown.getSelectedItem().toString();
        if (etnama.isEmpty() || etuser.isEmpty() || etpass.isEmpty()) {
            AlertDialog.Builder dialog = new AlertDialog.Builder(this)
                    .setTitle("Error")
                    .setMessage("Data belum lengkap!")
                    .setNeutralButton("Kembali", null);
            dialog.create();
            dialog.show();
        }else{
            uploadRegister(etnama,etuser,etpass,spinner_data);
        }
    }

    private void uploadRegister(String etnama, String etuser, String etpass, String spinner_data) {
        AndroidNetworking.post(KoneksiAPI.AllUser)
                .addBodyParameter("id","")
                .addBodyParameter("level", spinner_data)
                .addBodyParameter("nama", etnama)
                .addBodyParameter("username", etuser)
                .addBodyParameter("password", etpass)
                .addBodyParameter("tipe", "3")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.i("Success User", "onResponse: "+response);
                            pesan = Toast.makeText(getApplicationContext(), "Berhasil!", Toast.LENGTH_LONG);
                            pesan.show();
                            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                            startActivity(intent);
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.i(TAG, "onError: "+ anError.getErrorDetail());
                        int toastDurationInMilliSeconds = 10000;
                        pesan = Toast.makeText(getApplicationContext(), "Gagal!", Toast.LENGTH_LONG);
                        CountDownTimer countDownTimer;
                        countDownTimer = new CountDownTimer(toastDurationInMilliSeconds, 1000) {
                            @Override
                            public void onTick(long l) {
                                pesan.show();
                            }

                            @Override
                            public void onFinish() {
                                pesan.cancel();
                            }
                        };
                        pesan.show();
                        countDownTimer.start();
                    }
                });
    }

    public void btnLogin(View view) {
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
    }
}