package com.michi.manajemenbengkel.gold;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.speech.tts.TextToSpeech;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListItem extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener{
    Button btnkembali;
    Activity actvity;
    ListView listItem;
    ListAdapter Adapter;
    ArrayList<HashMap<String, String>> list_item;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_item);
        btnkembali = findViewById(R.id.button_kembaliwarehouse);
        actvity = this;
        FloatingActionButton fab = findViewById(R.id.fab);
        listItem = (ListView) findViewById(R.id.listViewWarehouse);
        listItem.setOnItemClickListener(this);
        listItem.setOverScrollMode(View.OVER_SCROLL_NEVER);
        btnkembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent tambah = new Intent(getApplicationContext(), FormWarehouse.class);
                tambah.putExtra("id", "");
                tambah.putExtra("nama", "");
                tambah.putExtra("jenis", "");
                tambah.putExtra("harga", "");
                tambah.putExtra("stok", "");
                tambah.putExtra("tipe", "3");
                startActivity(tambah);
            }
        });
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    protected void onStart() {
        super.onStart();
        GetData();
    }

    private void GetData() {
        list_item = new ArrayList<HashMap<String, String>>();
        AndroidNetworking.get(KoneksiAPI.ShowItem)
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
                                list_item.add(map);
                            }
                            Adapter = new ListItem.ListAdapter(actvity, list_item, R.layout.layout_list_item, new String[]
                                    {"id_barang", "nama_barang", "harga_barang", "stok"}, new int[]{R.id.tvId, R.id.tvNama, R.id.tvHarga, R.id.tvStok});
                            Parcelable state = listItem.onSaveInstanceState();
                            listItem.setAdapter(Adapter);
                            listItem.onRestoreInstanceState(state);
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
    public class ListAdapter extends SimpleAdapter{
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
                view = inflater.inflate(R.layout.layout_list_item, null);
            HashMap<String, Object> data = (HashMap<String, Object>) getItem(position);
            final TextView txtid = view.findViewById(R.id.tvId);
            final TextView txtnama = view.findViewById(R.id.tvNama);
            final TextView txtharga = view.findViewById(R.id.tvHarga);
            final TextView txtstok = view.findViewById(R.id.tvStok);
            final Button btnrubah = view.findViewById(R.id.btnitemRubah);
            final Button btnhapus = view.findViewById(R.id.btnitemHapus);
            final String strid = (String) data.get("id_barang");
            final String strnama = (String) data.get("nama_barang");
            final String strharga = (String) data.get("harga_barang");
            final String strstok = (String) data.get("stok");

            txtid.setText(strid);
            txtnama.setText(strnama);
            txtharga.setText(strharga);
            txtstok.setText(strstok);

            btnrubah.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent rubah = new Intent(getApplicationContext(), FormWarehouse.class);
                    rubah.putExtra("id", strid);
                    rubah.putExtra("nama", strnama);
                    rubah.putExtra("harga", strharga);
                    rubah.putExtra("stok", strstok);
                    rubah.putExtra("tipe", "1");
                    startActivity(rubah);
                }
            });

            btnhapus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent hapus = new Intent(getApplicationContext(), FormWarehouse.class);
                    hapus.putExtra("id", strid);
                    hapus.putExtra("nama", strnama);
                    hapus.putExtra("harga", strharga);
                    hapus.putExtra("stok", strstok);
                    hapus.putExtra("tipe", "2");
                    startActivity(hapus);
                }
            });

            return view;
        }
    }
}