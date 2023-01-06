package com.michi.manajemenbengkel.gold.teknisi;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import com.michi.manajemenbengkel.gold.KoneksiAPI;
import com.michi.manajemenbengkel.gold.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChildShowCustItemFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener{
    private ListView listView;
    private Activity activity;
    private AdapterWarehouse Adapter;
    private SharedPreferences sharedPreferences;
    private ArrayList<HashMap<String, String>> list_item;
    private TextView tvjumlah;
    private View rootView;
    private Handler handler = new Handler();
    private Runnable runnable;
    private int delay = 1000;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    public ChildShowCustItemFragment() {
    }

    public static ChildShowCustItemFragment newInstance(String param1, String param2) {
        ChildShowCustItemFragment fragment = new ChildShowCustItemFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        sharedPreferences = this.getActivity().getSharedPreferences("user-session", Context.MODE_PRIVATE);
        activity = getActivity();
        rootView = inflater.inflate(R.layout.fragment_child_show_cust_item, container, false);
        listView = rootView.findViewById(R.id.listshowItem);
        listView.setOnItemClickListener(this);
        listView.setOverScrollMode(View.OVER_SCROLL_NEVER);
        initview();
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        getData();
    }

    private void getData() {
        String iduser = sharedPreferences.getString("id_user",null);
        list_item = new ArrayList<HashMap<String,String>>();
        AndroidNetworking.post(KoneksiAPI.ShowItemTek)
                .addBodyParameter("iduser", iduser)
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
                                map.put("id_temp", jsonObject.getString("id_temptrans"));
                                map.put("nama_barang",jsonObject.getString("nama_barang"));
                                map.put("jumlah",jsonObject.getString("jumlah"));
                                list_item.add(map);
                            }
                            Adapter = new AdapterWarehouse(activity,list_item,R.layout.layout_list_show, new String[]
                                    {"nama_barang","jumlah"}, new int[]{R.id.tvNama2, R.id.tvJumlah1});
                            Parcelable state = listView.onSaveInstanceState();
                            listView.setAdapter(Adapter);
                            listView.onRestoreInstanceState(state);
                            Adapter.notifyDataSetChanged();
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
                                                    tvjumlah.setText("Biaya : "+jumlah);
                                                }
                                            } catch (Exception e) {
                                            }
                                        }
                                        @Override
                                        public void onError(ANError anError) {
                                        }
                                    });
                        }catch (Exception e){
                        }
                    }
                    @Override
                    public void onError(ANError anError) {
                    }
                });
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
                view = inflater.inflate(R.layout.layout_list_show, null);
            HashMap<String, Object> data = (HashMap<String, Object>) getItem(position);
            final TextView txtnama = view.findViewById(R.id.tvNama2);
            final TextView txtstok = view.findViewById(R.id.tvJumlah1);
            final String strid = (String) data.get("id_temp");
            final String strnama = (String) data.get("nama_barang");
            final String strstok = (String) data.get("jumlah");
            txtnama.setText(strnama);
            txtstok.setText(strstok);
            return view;
        }
    }

    private void initview() {
        tvjumlah = rootView.findViewById(R.id.tvBiaya);
        listView = rootView.findViewById(R.id.listshowItem);
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }
}