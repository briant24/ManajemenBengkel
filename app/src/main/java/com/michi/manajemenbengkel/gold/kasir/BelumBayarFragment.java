package com.michi.manajemenbengkel.gold.kasir;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.michi.manajemenbengkel.gold.R;
import com.michi.manajemenbengkel.gold.koneksi.KoneksiAPI;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BelumBayarFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener{

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private SearchView searchView;
    private View rootView;
    private AdapterTransaksi Adapter;
    private ListView listView;
    private ArrayList<HashMap<String, String>> list_transaksi;
    private Activity activity;
    private String mParam1;
    private String mParam2;

    public BelumBayarFragment() {
    }

    public static BelumBayarFragment newInstance(String param1, String param2) {
        BelumBayarFragment fragment = new BelumBayarFragment();
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
         rootView = inflater.inflate(R.layout.fragment_belum_bayar, container, false);
         listView = rootView.findViewById(R.id.listbelumbayar);
         activity = getActivity();
         listView.setOnItemClickListener(this);
         listView.setOverScrollMode(View.OVER_SCROLL_NEVER);
         searchView = rootView.findViewById(R.id.searchbelumbayar);
         searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                GetData(s);
                return false;
            }
         });
         searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                GetData("");
                return false;
            }
         });
         return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        GetData("");
    }

    private void GetData(String s) {
        list_transaksi = new ArrayList<HashMap<String,String>>();
        String status_pembayaran = "Belum Bayar";
        if (s.isEmpty()) {
            AndroidNetworking.post(KoneksiAPI.ShowTransaksi)
                    .addBodyParameter("status",status_pembayaran)
                    .setPriority(Priority.MEDIUM)
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                JSONArray jsonArray = response.getJSONArray("list_trans");
                                for (int a = 0; a < jsonArray.length(); a++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(a);
                                    HashMap<String, String> map = new HashMap<String, String>();
                                    map.put("id_transaksi", jsonObject.getString("id_transaksi"));
                                    map.put("nama_customer", jsonObject.getString("nama_customer"));
                                    map.put("motor", jsonObject.getString("motor"));
                                    map.put("nomor_polisi", jsonObject.getString("nomor_polisi"));
                                    map.put("status_pembayaran", jsonObject.getString("status_pembayaran"));
                                    map.put("jumlah", jsonObject.getString("jumlah"));
                                    map.put("tanggal", jsonObject.getString("tanggal"));
                                    map.put("namatek", jsonObject.getString("nama_user"));
                                    list_transaksi.add(map);
                                }
                                Adapter = new AdapterTransaksi(activity, list_transaksi, R.layout.layout_list_transaksi, new String[]
                                        {"nama_customer", "motor", "nomor_polisi", "status_pembayaran", "jumlah", "tanggal", "namatek"}, new int[]{R.id.tvNama5, R.id.tvMotor5, R.id.tvNopol5,R.id.tvBayar5, R.id.tvJumlah5,R.id.tvTanggal5,R.id.tvNamaTeknisi5});
                                Parcelable state = listView.onSaveInstanceState();
                                listView.setAdapter(Adapter);
                                listView.onRestoreInstanceState(state);
                                Adapter.notifyDataSetChanged();
                            }catch (Exception e){
                            }
                        }

                        @Override
                        public void onError(ANError anError) {

                        }
                    });
        }else {
            AndroidNetworking.post(KoneksiAPI.ShowTransaksibyUser)
                    .addBodyParameter("status",status_pembayaran)
                    .addBodyParameter("nama", s)
                    .setPriority(Priority.MEDIUM)
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                JSONArray jsonArray = response.getJSONArray("list_trans");
                                for (int a = 0; a < jsonArray.length(); a++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(a);
                                    HashMap<String, String> map = new HashMap<String, String>();
                                    map.put("id_transaksi", jsonObject.getString("id_transaksi"));
                                    map.put("nama_customer", jsonObject.getString("nama_customer"));
                                    map.put("motor", jsonObject.getString("motor"));
                                    map.put("nomor_polisi", jsonObject.getString("nomor_polisi"));
                                    map.put("status_pembayaran", jsonObject.getString("status_pembayaran"));
                                    map.put("jumlah", jsonObject.getString("jumlah"));
                                    map.put("tanggal", jsonObject.getString("tanggal"));
                                    map.put("namatek", jsonObject.getString("nama_user"));
                                    list_transaksi.add(map);
                                }
                                Adapter = new AdapterTransaksi(activity, list_transaksi, R.layout.layout_list_transaksi, new String[]
                                        {"nama_customer", "motor", "nomor_polisi", "status_pembayaran", "jumlah", "tanggal", "namatek"}, new int[]{R.id.tvNama5, R.id.tvMotor5, R.id.tvNopol5,R.id.tvBayar5, R.id.tvJumlah5,R.id.tvTanggal5,R.id.tvNamaTeknisi5});
                                Parcelable state = listView.onSaveInstanceState();
                                listView.setAdapter(Adapter);
                                listView.onRestoreInstanceState(state);
                                Adapter.notifyDataSetChanged();
                            }catch (Exception e){
                            }
                        }

                        @Override
                        public void onError(ANError anError) {

                        }
                    });
        }
    }

    public class AdapterTransaksi extends SimpleAdapter {
        private Context mContext;
        public LayoutInflater inflater = null;

        public AdapterTransaksi(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[]to){
            super(context,data,resource,from,to);
            mContext = context;
            inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent){
            View view = convertView;
            if (convertView == null)
                view = inflater.inflate(R.layout.layout_list_transaksi, null);
            HashMap<String, Object> data = (HashMap<String, Object>) getItem(position);
            final TextView txtnama = view.findViewById(R.id.tvNama5);
            final TextView txtnopol = view.findViewById(R.id.tvNopol5);
            final TextView txtmotor = view.findViewById(R.id.tvMotor5);
            final TextView txtbayar = view.findViewById(R.id.tvBayar5);
            final TextView txtjumlah = view.findViewById(R.id.tvJumlah5);
            final TextView txttanggal = view.findViewById(R.id.tvTanggal5);
            final TextView txtnamatek = view.findViewById(R.id.tvNamaTeknisi5);
            final Button btnBayar = view.findViewById(R.id.btnBayar);
            final String strid = (String) data.get("id_transaksi");
            final String strnama = (String) data.get("nama_customer");
            final String strmotor = (String) data.get("motor");
            final String strnopol = (String) data.get("nomor_polisi");
            final String strbayar = (String) data.get("status_pembayaran");
            final String strjumlah = (String) data.get("jumlah");
            final String strtanggal = (String) data.get("tanggal");
            final String strnamatek = (String) data.get("namatek");
            txttanggal.setText(strtanggal);
            txtnamatek.setText(strnamatek);
            txtnama.setText(strnama);
            txtnopol.setText(strnopol);
            txtmotor.setText(strmotor);
            txtbayar.setText(strbayar);
            txtjumlah.setText(strjumlah);
            btnBayar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AndroidNetworking.post(KoneksiAPI.Updatestatus)
                            .addBodyParameter("id", strid)
                            .addBodyParameter("status", "Lunas")
                            .setPriority(Priority.MEDIUM)
                            .build()
                            .getAsJSONObject(new JSONObjectRequestListener() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    Toast.makeText(getActivity(), "Sukses", Toast.LENGTH_SHORT).show();
                                    GetData("");
                                }

                                @Override
                                public void onError(ANError anError) {
                                    Toast.makeText(getActivity(), "Gagal", Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            });
            return view;
        }
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        HashMap<String, Object> data = (HashMap<String, Object>) Adapter.getItem(i);
        Log.d(TAG, "onItemClick() returned: " + data);
        final String strid = (String) data.get("id_transaksi");
        Intent detail = new Intent(getActivity(),ActivityDetailTransaksi.class);
        detail.putExtra("idtrans",strid);
        startActivity(detail);
    }
}