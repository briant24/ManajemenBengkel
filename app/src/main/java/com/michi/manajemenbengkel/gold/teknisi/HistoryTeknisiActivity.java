package com.michi.manajemenbengkel.gold.teknisi;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.michi.manajemenbengkel.gold.R;
import com.michi.manajemenbengkel.gold.kasir.ActivityDetailTransaksi;
import com.michi.manajemenbengkel.gold.koneksi.KoneksiAPI;
import com.michi.manajemenbengkel.gold.warehouse.FormWarehouse;
import com.michi.manajemenbengkel.gold.warehouse.ListItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HistoryTeknisiActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener{
    private SearchView searchView;
    private ListView listView;
    private Activity activity;
    private ListAdapter Adapter;
    private SharedPreferences sharedPreferences;
    private ArrayList<HashMap<String, String>> list_history;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_teknisi);
        sharedPreferences = this.getSharedPreferences("user-session", Context.MODE_PRIVATE);
        activity = this;
        searchView = findViewById(R.id.searchhistoryteknisi);
        listView = findViewById(R.id.list_history_tek);
        listView.setOnItemClickListener(this);
        listView.setOverScrollMode(View.OVER_SCROLL_NEVER);
        list_history = new ArrayList<HashMap<String, String>>();
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
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        HashMap<String, Object> data = (HashMap<String, Object>) Adapter.getItem(i);
        final String strid = (String) data.get("id_transaksi");
        Intent detail = new Intent(getApplicationContext(), ActivityDetailTransaksi.class);
        detail.putExtra("idtrans",strid);
        startActivity(detail);
    }

    @Override
    protected void onStart() {
        super.onStart();
        GetData("");
    }

    private void GetData(String data) {
        String idtek = sharedPreferences.getString("id_user",null);
        list_history = new ArrayList<HashMap<String, String>>();
        Log.d(TAG, "GetData() returned: " + data);
        if (data.isEmpty()){
            AndroidNetworking.post(KoneksiAPI.historyTek)
                    .addBodyParameter("id",idtek)
                    .setPriority(Priority.MEDIUM)
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                JSONArray jsonArray = response.getJSONArray("list_trans");
                                for (int a = 0; a < jsonArray.length(); a++){
                                    JSONObject jsonObject = jsonArray.getJSONObject(a);
                                    HashMap<String, String>map = new HashMap<String, String>();
                                    map.put("id_transaksi", jsonObject.getString("id_transaksi"));
                                    map.put("id_user",jsonObject.getString("id_user"));
                                    map.put("nama_customer",jsonObject.getString("nama_customer"));
                                    map.put("motor",jsonObject.getString("motor"));
                                    map.put("nomor_polisi",jsonObject.getString("nomor_polisi"));
                                    map.put("status_pembayaran",jsonObject.getString("status_pembayaran"));
                                    map.put("jumlah",jsonObject.getString("jumlah"));
                                    list_history.add(map);
                                }
                                Adapter = new HistoryTeknisiActivity.ListAdapter(activity, list_history, R.layout.layout_history_tek, new String[]
                                        {"nama_customer", "motor", "nomor_polisi", "status_pembayaran", "jumlah"}, new int[]{R.id.tvNama3, R.id.tvMotor3, R.id.tvNopol3, R.id.tvBayar3, R.id.tvJumlah3});
                                Parcelable state = listView.onSaveInstanceState();
                                listView.setAdapter(Adapter);
                                listView.onRestoreInstanceState(state);
                                Adapter.notifyDataSetChanged();
                            }catch (JSONException e){
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onError(ANError anError) {
                        }
                    });
        }else {
            AndroidNetworking.post(KoneksiAPI.SearchhistoryTek)
                    .addBodyParameter("id",idtek)
                    .addBodyParameter("nama",data)
                    .setPriority(Priority.MEDIUM)
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                JSONArray jsonArray = response.getJSONArray("list_trans");
                                for (int a = 0; a < jsonArray.length(); a++){
                                    JSONObject jsonObject = jsonArray.getJSONObject(a);
                                    HashMap<String, String>map = new HashMap<String, String>();
                                    map.put("id_transaksi", jsonObject.getString("id_transaksi"));
                                    map.put("id_user",jsonObject.getString("id_user"));
                                    map.put("nama_customer",jsonObject.getString("nama_customer"));
                                    map.put("motor",jsonObject.getString("motor"));
                                    map.put("nomor_polisi",jsonObject.getString("nomor_polisi"));
                                    map.put("status_pembayaran",jsonObject.getString("status_pembayaran"));
                                    map.put("jumlah",jsonObject.getString("jumlah"));
                                    list_history.add(map);
                                }
                                Adapter = new HistoryTeknisiActivity.ListAdapter(activity, list_history, R.layout.layout_history_tek, new String[]
                                        {"nama_customer", "motor", "nomor_polisi", "status_pembayaran", "jumlah"}, new int[]{R.id.tvNama3, R.id.tvMotor3, R.id.tvNopol3, R.id.tvBayar3, R.id.tvJumlah3});
                                Parcelable state = listView.onSaveInstanceState();
                                listView.setAdapter(Adapter);
                                listView.onRestoreInstanceState(state);
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
    }
    public class ListAdapter extends SimpleAdapter {
        private Context mContext;
        public LayoutInflater inflater = null;

        public ListAdapter(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[]to){
            super(context,data,resource,from,to);
            mContext = context;
            inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent){
            View view = convertView;
            if (convertView == null)
                view = inflater.inflate(R.layout.layout_history_tek, null);
            HashMap<String, Object> data = (HashMap<String, Object>) getItem(position);
            final TextView txtnama = view.findViewById(R.id.tvNama3);
            final TextView txtmotor = view.findViewById(R.id.tvMotor3);
            final TextView txtnopol = view.findViewById(R.id.tvNopol3);
            final TextView txtbayar = view.findViewById(R.id.tvBayar3);
            final TextView txtjumlah = view.findViewById(R.id.tvJumlah3);
            final String strnama = (String) data.get("nama_customer");
            final String strmotor = (String) data.get("motor");
            final String strnopol = (String) data.get("nomor_polisi");
            final String strbayar = (String) data.get("status_pembayaran");
            final String strjumlah = (String) data.get("jumlah");

            txtmotor.setText(strmotor);
            txtnama.setText(strnama);
            txtnopol.setText(strnopol);
            txtbayar.setText(strbayar);
            txtjumlah.setText(strjumlah);
            return view;
        }
    }
}