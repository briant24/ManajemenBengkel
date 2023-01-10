package com.michi.manajemenbengkel.gold.teknisi;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class CustFragment extends Fragment {
    private EditText namaCust,nopolCust,motorCust;
    private SharedPreferences sharedPreferences;
    private Button btnNext;
    private String nama,nopol,motor,id_tek,date;
    private SimpleDateFormat dateFormat;
    private View rootView;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    public CustFragment() {
    }
    public static CustFragment newInstance(String param1, String param2) {
        CustFragment fragment = new CustFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_cust, container, false);
        initview();
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    private void initview() {
        sharedPreferences = this.getActivity().getSharedPreferences("user-session", Context.MODE_PRIVATE);
        btnNext = rootView.findViewById(R.id.btnNext);
        namaCust = rootView.findViewById(R.id.etNamaCust);
        nopolCust = rootView.findViewById(R.id.etNopolCust);
        motorCust = rootView.findViewById(R.id.etMotorCust);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validasidata();
            }
        });
    }

    private void validasidata() {
        String data_status = "Belum Bayar";
        nama = namaCust.getText().toString();
        nopol = nopolCust.getText().toString();
        motor = motorCust.getText().toString();
        if (nama.isEmpty() || nopol.isEmpty() || motor.isEmpty()){
            Toast.makeText(getActivity(), "Data Belum Lengkap!", Toast.LENGTH_SHORT).show();
        }else {
            passdata(nama,nopol,motor,data_status);
        }
    }

    private void passdata(String nama, String nopol, String motor, String data_status) {
        String iduser = sharedPreferences.getString("id_user",null);
        Calendar calendar = Calendar.getInstance();
        dateFormat = new SimpleDateFormat("yyyy/MM/DD");
        date = dateFormat.format(calendar.getTime());
        String idd = date+nama+motor;
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("id_trans",idd);
        editor.apply();
        AndroidNetworking.post(KoneksiAPI.AddTrans)
                .addBodyParameter("id",idd.toLowerCase(Locale.ROOT))
                .addBodyParameter("user",iduser)
                .addBodyParameter("nama",nama)
                .addBodyParameter("nopol",nopol)
                .addBodyParameter("motor",motor)
                .addBodyParameter("status",data_status)
                .addBodyParameter("jumlah","0")
                .addBodyParameter("tanggal",date)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(getActivity(), "Transaksi Berhasil Dibuat", Toast.LENGTH_SHORT).show();
                        Bundle bundle = new Bundle();
                        bundle.putString("nama", nama);
                        bundle.putString("nopol", nopol);
                        bundle.putString("motor", motor);
                        bundle.putString("status", data_status);
                        CustItemFragment custItemFragment = new CustItemFragment();
                        custItemFragment.setArguments(bundle);
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, custItemFragment).commit();
                    }

                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(getActivity(), "Transaksi Gagal Dibuat", Toast.LENGTH_SHORT).show();
                    }
                });

    }
}