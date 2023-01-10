package com.michi.manajemenbengkel.gold.kasir;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.michi.manajemenbengkel.gold.R;
import com.michi.manajemenbengkel.gold.koneksi.KoneksiAPI;
import com.michi.manajemenbengkel.gold.teknisi.ChildShowCustItemFragment;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActivityDetailTransaksi extends AppCompatActivity {
    private ListView listView;
    private Button btnkembali;
    private AdapterTransaksi Adapter;
    private Activity activity;
    private ArrayList<HashMap<String, String>> list_transaksi;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_transaksi);
        listView = findViewById(R.id.listdetailtrans);
        btnkembali = findViewById(R.id.btnkembalidet);
        activity = this;
        btnkembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                finish();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent data = getIntent();
        String idtrans = data.getStringExtra("idtrans");
        Log.d(TAG, "onStart() returned: " + idtrans);
        GetData(idtrans);
    }

    private void GetData(String idtrans) {
        list_transaksi = new ArrayList<HashMap<String,String>>();
        AndroidNetworking.post(KoneksiAPI.ShowDetailTrans)
                .addBodyParameter("idtrans", idtrans)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("list_item");
                            for (int a = 0; a < jsonArray.length(); a++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(a);
                                HashMap<String, String> map = new HashMap<String, String>();
                                map.put("id_trans", jsonObject.getString("id_transaksi"));
                                map.put("nama_barang", jsonObject.getString("nama_barang"));
                                map.put("jumlah", jsonObject.getString("jumlah"));
                                map.put("harga", jsonObject.getString("harga"));
                                list_transaksi.add(map);
                            }
                            Adapter = new AdapterTransaksi(activity, list_transaksi, R.layout.layout_detail_transaksi, new String[]
                                    {"id_trans","nama_barang", "jumlah", "harga"}, new int[]{R.id.tvIdTransaksi7,R.id.tvBarang7, R.id.tvJumlah7,R.id.tvHarga7});
                            Parcelable state = listView.onSaveInstanceState();
                            listView.setAdapter(Adapter);
                            listView.onRestoreInstanceState(state);
                            Adapter.notifyDataSetChanged();
                        } catch (Exception e) {
                        }
                    }
                    @Override
                    public void onError(ANError anError) {

                    }
                });
    }

    public class AdapterTransaksi extends SimpleAdapter {
        private Context mContext;
        public LayoutInflater inflater = null;

        public AdapterTransaksi(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to) {
            super(context, data, resource, from, to);
            mContext = context;
            inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;
            if (convertView == null)
                view = inflater.inflate(R.layout.layout_detail_transaksi, null);
            HashMap<String, Object> data = (HashMap<String, Object>) getItem(position);
            final TextView txtidtrans = view.findViewById(R.id.tvIdTransaksi7);
            final TextView txtnamabarang = view.findViewById(R.id.tvBarang7);
            final TextView txtbayar = view.findViewById(R.id.tvHarga7);
            final TextView txtjumlah = view.findViewById(R.id.tvJumlah7);
            final String strnama = (String) data.get("nama_barang");
            final String stridtrans = (String) data.get("id_trans");
            final String strjumlah = (String) data.get("jumlah");
            final String strharga = (String) data.get("harga");
            txtidtrans.setText(stridtrans);
            txtnamabarang.setText(strnama);
            txtjumlah.setText(strjumlah);
            txtbayar.setText(strharga);
            return view;
        }
    }
}

