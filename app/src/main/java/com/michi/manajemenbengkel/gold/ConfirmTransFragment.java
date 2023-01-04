package com.michi.manajemenbengkel.gold;

import static android.content.ContentValues.TAG;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ConfirmTransFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ConfirmTransFragment extends Fragment {
    private SharedPreferences sharedPreferences;
    private Button kembali,confirm;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private TextInputEditText etjasa,etbiayabarang;
    private View rootView;
    public ConfirmTransFragment() {
        // Required empty public constructor
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
                            Log.i(TAG, "onResponse: "+e);
                        }
                    }
                    @Override
                    public void onError(ANError anError) {
                        Log.i(TAG, "onError: " + anError);
                    }
                });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        sharedPreferences = this.getActivity().getSharedPreferences("user-session", Context.MODE_PRIVATE);
        rootView = inflater.inflate(R.layout.fragment_confirm_trans, container, false);
        Bundle bundle = this.getArguments();
        String nama_cust = bundle.getString("nama");
        String nopol_cust = bundle.getString("nopol");
        String status_cust = bundle.getString("status");
        etjasa = rootView.findViewById(R.id.etbiayaJasa);
        etbiayabarang = rootView.findViewById(R.id.etbiayaBarang);
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
                String idd = nama.substring(0,3)+totalll;
                String total = String.valueOf(totalll);
                AlertDialog.Builder inputjum = new AlertDialog.Builder(getActivity());
                inputjum.setTitle("Konfirmasi Transaksi?");
                inputjum.setMessage("Total Biaya : "+total);
                inputjum.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        AndroidNetworking.post(KoneksiAPI.AddTrans)
                                .addBodyParameter("id",idd)
                                .addBodyParameter("user",iduser)
                                .addBodyParameter("nama",nama)
                                .addBodyParameter("nopol",nopol)
                                .addBodyParameter("status",status)
                                .setPriority(Priority.MEDIUM)
                                .build()
                                .getAsJSONObject(new JSONObjectRequestListener() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        hapustemp();
                                        Toast.makeText(getActivity(), "Transaksi Berhasil", Toast.LENGTH_SHORT).show();
                                    }
                                    private void hapustemp() {
                                        AndroidNetworking.post(KoneksiAPI.delAlltempTrans)
                                                .addBodyParameter("id",iduser)
                                                .setPriority(Priority.MEDIUM)
                                                .build()
                                                .getAsJSONObject(new JSONObjectRequestListener() {
                                                    @Override
                                                    public void onResponse(JSONObject response) {
                                                        Log.i(TAG, "onResponse: "+response);
                                                    }

                                                    @Override
                                                    public void onError(ANError anError) {
                                                        Log.i(TAG, "onError: "+anError);
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