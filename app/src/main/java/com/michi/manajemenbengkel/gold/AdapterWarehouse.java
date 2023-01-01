package com.michi.manajemenbengkel.gold;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdapterWarehouse extends SimpleAdapter{
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

            }
        });
        return view;
    }
}
