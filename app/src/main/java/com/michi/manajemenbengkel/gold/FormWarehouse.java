package com.michi.manajemenbengkel.gold;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONObject;

import java.util.Calendar;

public class FormWarehouse extends AppCompatActivity {
    private EditText jenis,nama,harga,stok;
    private Button simpan, kembali, batal;
    private String strJenis,strNama,strId, strHarga, strStok, tipe, stid,stnama,stjenis,stharga,ststok;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_warehouse);
        AndroidNetworking.initialize(getApplicationContext());
        jenis = findViewById(R.id.etJenis);
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
    }

    private void validasidata() {
        strJenis = jenis.getText().toString();
        strNama = nama.getText().toString();
        strHarga = harga.getText().toString();
        strStok = stok.getText().toString();
        if (strJenis.isEmpty() || strNama.isEmpty() || strHarga.isEmpty() || strStok.isEmpty()){
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
                                rubahdata(strId,strNama,strJenis,strHarga,strStok);
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
                                hapusdata(strId);
                            }
                        });
                dialog.create();
                dialog.show();
            }else {
                String id = strNama.substring(0,2);
                Calendar calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int year = calendar.get(Calendar.YEAR);
                String years = Integer.toString(year);
                strId = id+years.substring(2)+Integer.toString(day);
                simpandata(strId, strJenis,strNama,strHarga,strStok);
            }
        }
    }

    private void hapusdata(String strId){
        AndroidNetworking.post(KoneksiAPI.hapusbarang)
                .addBodyParameter("id", strId)
                .setPriority(Priority.MEDIUM)
                .build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Log.i("Delete Item Warehouse", "onResponse: "+response);
                    Toast.makeText(getApplicationContext(), "Data berhasil dihapus", Toast.LENGTH_SHORT).show();
                    bersih();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(ANError anError) {
                Toast.makeText(getApplicationContext(), "Data gagal dihapus", Toast.LENGTH_SHORT).show();
                Log.i(TAG, "onError: "+ anError.getErrorDetail());
            }
        });
    }
    private void rubahdata(String strId, String strJenis, String strNama, String strHarga, String strStok){
        AndroidNetworking.post(KoneksiAPI.updateBarang)
                .addBodyParameter("id", strId)
                .addBodyParameter("nama", strNama)
                .addBodyParameter("jenis", strJenis)
                .addBodyParameter("harga", strHarga)
                .addBodyParameter("stok", strStok)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.i("Update Item Warehouse", "onResponse: "+response);
                            Toast.makeText(getApplicationContext(), "Data berhasil diupdate", Toast.LENGTH_SHORT).show();
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(getApplicationContext(), "Data gagal diupdate", Toast.LENGTH_SHORT).show();
                        Log.i(TAG, "onError: "+ anError.getErrorDetail());
                    }
                });
    }
    private void simpandata(String strId, String strJenis, String strNama, String strHarga, String strStok) {
        AndroidNetworking.post(KoneksiAPI.tambahBarang)
                .addBodyParameter("id", strId)
                .addBodyParameter("nama", strNama)
                .addBodyParameter("jenis", strJenis)
                .addBodyParameter("harga", strHarga)
                .addBodyParameter("stok", strStok)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.i("Entri Warehouse", "onResponse: "+response);
                            Toast.makeText(getApplicationContext(), "Data berhasil ditambahkan", Toast.LENGTH_SHORT).show();
                            bersih();
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(getApplicationContext(), "Data gagal ditambahkan", Toast.LENGTH_SHORT).show();
                        Log.i(TAG, "onError: "+ anError.getErrorDetail());
                    }
                });
    }

    private void bersih() {
        jenis.setText("");
        nama.setText("");
        harga.setText("");
        stok.setText("");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent masuk=getIntent();
        stid = masuk.getStringExtra("id");
        stnama = masuk.getStringExtra("nama");
        stjenis = masuk.getStringExtra("jenis");
        stharga = masuk.getStringExtra("harga");
        ststok = masuk.getStringExtra("stok");
        tipe = masuk.getStringExtra("tipe");
        strId = stid;
        jenis.setText(stjenis);
        nama.setText(stnama);
        harga.setText(stharga);
        stok.setText(ststok);
        if (tipe.equals("1")){
            simpan.setText("Rubah");
        } else if (tipe.equals("2")){
            simpan.setText("Hapus");
        }else {
            simpan.setText("Simpan");
        }
    }
}