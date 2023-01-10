package com.michi.manajemenbengkel.gold.warehouse;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
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
import com.michi.manajemenbengkel.gold.koneksi.KoneksiAPI;
import com.michi.manajemenbengkel.gold.R;

import org.json.JSONObject;

public class FormWarehouse extends AppCompatActivity {
    private EditText nama,harga,stok;
    private Button simpan, kembali, batal;
    private String strNama,strId, strHarga, strStok, tipe, stid,stnama,stharga,ststok;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_warehouse);
        AndroidNetworking.initialize(getApplicationContext());
        nama = findViewById(R.id.etNama);
        harga = findViewById(R.id.etHarga);
        stok = findViewById(R.id.etStok);
        simpan = findViewById(R.id.btn_simpan_warehouse);
        batal = findViewById(R.id.btn_batal_warehouse);
        kembali = findViewById(R.id.btn_kembali_warehouse);

        simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validasidata();
            }
        });
        kembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        batal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void validasidata() {
        strNama = nama.getText().toString();
        strHarga = harga.getText().toString();
        strStok = stok.getText().toString();
        if (strNama.isEmpty() || strHarga.isEmpty() || strStok.isEmpty()){
            Toast.makeText(getApplicationContext(), "Data harus diisi secara lengkap!", Toast.LENGTH_SHORT).show();
        }else{
            if (tipe.equals("1")){
                AlertDialog.Builder dialog = new AlertDialog.Builder(this)
                        .setTitle("Rubah Data")
                        .setMessage("Data akan dirubah")
                        .setNegativeButton("Tidak", null)
                        .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                allItem(strId,strNama,strHarga,strStok,tipe);
                            }
                        });
                dialog.create();
                dialog.show();
            } else if (tipe.equals("2")){
                AlertDialog.Builder dialog = new AlertDialog.Builder(this)
                        .setTitle("Hapus Data")
                        .setMessage("Data akan dihapus")
                        .setNegativeButton("Tidak", null)
                        .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                allItem(strId,strNama,strHarga,strStok,tipe);
                            }
                        });
                dialog.create();
                dialog.show();
            }else if (tipe.equals("3")){
                strId = "";
                allItem(strId,strNama,strHarga,strStok,tipe);
            }
        }
    }

    private void allItem(String strId, String strNama, String strHarga, String strStok, String tipe){
        AndroidNetworking.post(KoneksiAPI.AllItem)
            .addBodyParameter("id", strId)
            .addBodyParameter("nama", strNama)
            .addBodyParameter("harga", strHarga)
            .addBodyParameter("stok", strStok)
            .addBodyParameter("tipe", tipe)
            .setPriority(Priority.MEDIUM)
            .build()
            .getAsJSONObject(new JSONObjectRequestListener() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        Toast.makeText(getApplicationContext(), "Berhasil!", Toast.LENGTH_LONG).show();
                        onBackPressed();
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }

                @Override
                public void onError(ANError anError) {
                    Toast.makeText(getApplicationContext(), "Gagal!", Toast.LENGTH_SHORT).show();
                }
            });
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent masuk=getIntent();
        stid = masuk.getStringExtra("id");
        stnama = masuk.getStringExtra("nama");
        stharga = masuk.getStringExtra("harga");
        ststok = masuk.getStringExtra("stok");
        tipe = masuk.getStringExtra("tipe");
        strId = stid;
        nama.setText(stnama);
        harga.setText(stharga);
        stok.setText(ststok);
        if (tipe.equals("1")){
            simpan.setText("Rubah");
        } else if (tipe.equals("2")){
            Log.i(TAG, "tipee : " + tipe);
            simpan.setText("Hapus");
        }else {
            simpan.setText("Simpan");
        }
    }
}