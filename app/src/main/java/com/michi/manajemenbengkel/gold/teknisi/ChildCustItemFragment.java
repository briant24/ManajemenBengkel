package com.michi.manajemenbengkel.gold.teknisi;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.michi.manajemenbengkel.gold.koneksi.KoneksiAPI;
import com.michi.manajemenbengkel.gold.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChildCustItemFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener{
    private Activity actvity;
    private ListView listBarang;
    private AdapterWarehouse Adapter;
    private SharedPreferences sharedPreferences;
    private ArrayList<HashMap<String, String>> list_barang;
    private View rootView;
    private Handler handler = new Handler();
    private Runnable runnable;
    private int delay = 1000;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    public ChildCustItemFragment() {
    }
    public static ChildCustItemFragment newInstance(String param1, String param2) {
        ChildCustItemFragment fragment = new ChildCustItemFragment();
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
        sharedPreferences = this.getActivity().getSharedPreferences("user-session",Context.MODE_PRIVATE);
        actvity = getActivity();
        rootView = inflater.inflate(R.layout.fragment_child_cust_item, container, false);
        listBarang = rootView.findViewById(R.id.listViewTeknisi);
        listBarang.setOnItemClickListener(this);
        listBarang.setOverScrollMode(View.OVER_SCROLL_NEVER);
        initview();
        return rootView;
    }

    @Override
    public void onResume() {
        handler.postDelayed( runnable = new Runnable() {
            public void run() {
                getData();
                handler.postDelayed(runnable, delay);
            }
        }, delay);
        super.onResume();
    }

    @Override
    public void onPause() {
        handler.removeCallbacks(runnable);
        super.onPause();
    }

    @Override
    public void onStart() {
        super.onStart();
        getData();
    }

    private void getData() {
        list_barang = new ArrayList<HashMap<String ,String >>();
        AndroidNetworking.get(KoneksiAPI.ShowListItem)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("list_item");
                            for (int a = 0; a < jsonArray.length(); a++){
                                JSONObject jsonObject = jsonArray.getJSONObject(a);
                                HashMap<String, String>map = new HashMap<String, String>();
                                map.put("id_barang", jsonObject.getString("id_barang"));
                                map.put("nama_barang",jsonObject.getString("nama_barang"));
                                map.put("harga_barang",jsonObject.getString("harga_barang"));
                                map.put("stok",jsonObject.getString("stok"));
                                list_barang.add(map);
                            }
                            Adapter = new AdapterWarehouse(actvity, list_barang, R.layout.layout_list_barang, new String[]
                                    {"nama_barang", "harga_barang", "stok"}, new int[]{R.id.tvNama1, R.id.tvHarga1, R.id.tvStok1});
                            Parcelable state = listBarang.onSaveInstanceState();
                            listBarang.setAdapter(Adapter);
                            listBarang.onRestoreInstanceState(state);
                            Adapter.notifyDataSetChanged();
                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                    }
                });
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }

    public class AdapterWarehouse extends SimpleAdapter {
        private Context mContext;
        public LayoutInflater inflater = null;

        public AdapterWarehouse(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[]to){
            super(context,data,resource,from,to);
            mContext = context;
            inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent){
            View view = convertView;
            if (convertView == null)
                view = inflater.inflate(R.layout.layout_list_barang, null);
            HashMap<String, Object> data = (HashMap<String, Object>) getItem(position);
            String idtek = sharedPreferences.getString("id_user",null);
            String idtranss = sharedPreferences.getString("id_trans",null);
            final TextView txtnama = view.findViewById(R.id.tvNama1);
            final TextView txtharga = view.findViewById(R.id.tvHarga1);
            final TextView txtstok = view.findViewById(R.id.tvStok1);
            final Button btnAdd = view.findViewById(R.id.btnAddItem);
            final String strid = (String) data.get("id_barang");
            final String strnama = (String) data.get("nama_barang");
            final String strharga = (String) data.get("harga_barang");
            final String strstok = (String) data.get("stok");
            txtnama.setText(strnama);
            txtharga.setText(strharga);
            txtstok.setText(strstok);
            btnAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String hargaaa = txtharga.getText().toString();
                    AndroidNetworking.post(KoneksiAPI.AddDetailTrans)
                            .addBodyParameter("id","")
                            .addBodyParameter("transaksi",idtranss)
                            .addBodyParameter("barang",strid)
                            .addBodyParameter("jumlah", "1")
                            .addBodyParameter("harga", hargaaa)
                            .setPriority(Priority.MEDIUM)
                            .build()
                            .getAsJSONObject(new JSONObjectRequestListener() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    Toast.makeText(getActivity(), "Barang Berhasil Ditambahkan", Toast.LENGTH_SHORT).show();
                                    changeWarehouse();
                                }

                                private void changeWarehouse() {
                                    AndroidNetworking.post(KoneksiAPI.ShowStokItem)
                                            .addBodyParameter("id", strid)
                                            .setPriority(Priority.MEDIUM)
                                            .build()
                                            .getAsJSONObject(new JSONObjectRequestListener() {
                                                @Override
                                                public void onResponse(JSONObject response) {
                                                    try {
                                                        JSONArray jsonArray = response.getJSONArray("show_stok");
                                                        for (int a = 0; a < jsonArray.length(); a++) {
                                                            JSONObject jsonObject = jsonArray.getJSONObject(a);
                                                            String stok = jsonObject.getString("stok");
                                                            int stokskrg = Integer.parseInt(stok);
                                                            int stokbaru = stokskrg-1;
                                                            setnewStok(stokbaru);
                                                        }
                                                    } catch (Exception e) {
                                                    }
                                                }

                                                private void setnewStok(int stokbaru) {
                                                    String newstok = String.valueOf(stokbaru);
                                                    AndroidNetworking.post(KoneksiAPI.updatestok)
                                                            .addBodyParameter("id", strid)
                                                            .addBodyParameter("stok", newstok)
                                                            .setPriority(Priority.MEDIUM)
                                                            .build()
                                                            .getAsJSONObject(new JSONObjectRequestListener() {
                                                                @Override
                                                                public void onResponse(JSONObject response) {
                                                                    getData();
                                                                }

                                                                @Override
                                                                public void onError(ANError anError) {
                                                                }
                                                            });
                                                }

                                                @Override
                                                public void onError(ANError anError) {
                                                }
                                            });
                                }
                                @Override
                                public void onError(ANError anError) {
                                }
                            });
                }
            });
            return view;
        }
    }

    private void initview() {
    }
}