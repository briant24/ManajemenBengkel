package com.michi.manajemenbengkel.gold.teknisi;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import com.michi.manajemenbengkel.gold.KoneksiAPI;
import com.michi.manajemenbengkel.gold.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ConfirmTransFragment extends Fragment {
    private SharedPreferences sharedPreferences;
    private Button kembali,confirm;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private SimpleDateFormat dateFormat;
    private String date;
    private String mParam1;
    private String mParam2;
    private EditText etjasa,etbiayabarang;
    private View rootView;

    public ConfirmTransFragment() {
    }
    public static ConfirmTransFragment newInstance(String param1, String param2) {
        ConfirmTransFragment fragment = new ConfirmTransFragment();
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

    private void getData() {
        String iduser = sharedPreferences.getString("id_user",null);
        AndroidNetworking.post(KoneksiAPI.ShowPaidTemTek)
                .addBodyParameter("iduser",iduser)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("jumlah_smt");
                            for (int a = 0; a < jsonArray.length(); a++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(a);
                                String jumlah = jsonObject.getString("jumlah");
                                etbiayabarang.setText(jumlah);
                            }
                        } catch (Exception e) {
                        }
                    }
                    @Override
                    public void onError(ANError anError) {
                    }
                });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        sharedPreferences = this.getActivity().getSharedPreferences("user-session", Context.MODE_PRIVATE);
        rootView = inflater.inflate(R.layout.fragment_confirm_trans, container, false);
        Bundle bundle = this.getArguments();
        String nama_cust = bundle.getString("nama");
        String nopol_cust = bundle.getString("nopol");
        String status_cust = bundle.getString("status");
        etjasa = rootView.findViewById(R.id.etBiayaJasa);
        etbiayabarang = rootView.findViewById(R.id.etBiayaBarang);
        initView(nama_cust,nopol_cust,status_cust);
        return rootView;
    }

    private void initView(String nama,String nopol,String status) {
        getData();
        kembali = rootView.findViewById(R.id.btnKembali);
        confirm = rootView.findViewById(R.id.btnKonfirm);
        kembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String iduser = sharedPreferences.getString("id_user",null);
                int biaya = Integer.parseInt(etjasa.getText().toString());
                int barang = Integer.parseInt(etbiayabarang.getText().toString());
                int totalll = biaya+barang;
                String total = String.valueOf(totalll);
                AlertDialog.Builder inputjum = new AlertDialog.Builder(getActivity());
                inputjum.setTitle("Konfirmasi Transaksi?");
                inputjum.setMessage("Total Biaya : "+total);
                inputjum.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String idtrans = sharedPreferences.getString("id_trans",null);
                        AndroidNetworking.post(KoneksiAPI.UpdateTrans)
                                .addBodyParameter("id",idtrans)
                                .addBodyParameter("jumlah",total)
                                .setPriority(Priority.MEDIUM)
                                .build()
                                .getAsJSONObject(new JSONObjectRequestListener() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        makedetail();
                                        hapustemp();
                                        Toast.makeText(getActivity(), "Transaksi Berhasil", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(getActivity(), MainActivity.class);
                                        startActivity(intent);
                                    }

                                    private void makedetail() {
                                        Calendar calendar = Calendar.getInstance();
                                        dateFormat = new SimpleDateFormat("yyyy-MM-DD");
                                        date = dateFormat.format(calendar.getTime());
                                        String idtranss = sharedPreferences.getString("id_trans",null);
                                        String hargaaa = etjasa.getText().toString();
                                        AndroidNetworking.post(KoneksiAPI.AddDetailTrans)
                                                .addBodyParameter("id","")
                                                .addBodyParameter("transaksi",idtranss)
                                                .addBodyParameter("barang","1")
                                                .addBodyParameter("jumlah", "1")
                                                .addBodyParameter("harga", hargaaa)
                                                .addBodyParameter("tanggal", date)
                                                .setPriority(Priority.MEDIUM)
                                                .build()
                                                .getAsJSONObject(new JSONObjectRequestListener() {
                                                    @Override
                                                    public void onResponse(JSONObject response) {
                                                    }

                                                    @Override
                                                    public void onError(ANError anError) {
                                                    }
                                                });
                                    }

                                    private void hapustemp() {
                                        AndroidNetworking.post(KoneksiAPI.delAlltempTrans)
                                                .addBodyParameter("id",iduser)
                                                .setPriority(Priority.MEDIUM)
                                                .build()
                                                .getAsJSONObject(new JSONObjectRequestListener() {
                                                    @Override
                                                    public void onResponse(JSONObject response) {
                                                    }

                                                    @Override
                                                    public void onError(ANError anError) {
                                                        Toast.makeText(getActivity(), "Gagal Hapus", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                    }

                                    @Override
                                    public void onError(ANError anError) {
                                        Toast.makeText(getActivity(), "Transaksi Gagal", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                });
                inputjum.setNegativeButton("Batal", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                inputjum.show();
            }
        });
    }
}