package com.michi.manajemenbengkel.gold.teknisi;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        sharedPreferences = this.getActivity().getSharedPreferences("user-session", Context.MODE_PRIVATE);
        activity = getActivity();
        rootView = inflater.inflate(R.layout.fragment_child_show_cust_item, container, false);
        SwipeRefreshLayout swipeRefreshLayout;
        swipeRefreshLayout = rootView.findViewById(R.id.refreshshowchild);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
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

        String idtranss = sharedPreferences.getString("id_trans",null);
        list_item = new ArrayList<HashMap<String,String>>();
        AndroidNetworking.post(KoneksiAPI.ShowDetailTrans)
                .addBodyParameter("idtrans", idtranss)
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
                                map.put("id_trans", jsonObject.getString("id_detailtransaksi"));
                                map.put("id_barang", jsonObject.getString("id_barang"));
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
                                    .addBodyParameter("idtransaksi",idtranss)
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
            final Button hapus = view.findViewById(R.id.btnHapusdet);
            final String strid = (String) data.get("id_trans");
            final String strnama = (String) data.get("nama_barang");
            final String strstok = (String) data.get("jumlah");
            final String stridbarang = (String) data.get("id_barang");
            txtnama.setText(strnama);
            txtstok.setText(strstok);
            hapus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AndroidNetworking.post(KoneksiAPI.deldetailtrans)
                            .addBodyParameter("id",strid)
                            .setPriority(Priority.MEDIUM)
                            .build()
                            .getAsJSONObject(new JSONObjectRequestListener() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    Toast.makeText(getActivity(), "Berhasil dihapus", Toast.LENGTH_SHORT).show();
                                    getData();
                                    changeWarehouse(stridbarang);
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
        tvjumlah = rootView.findViewById(R.id.tvBiaya);
        listView = rootView.findViewById(R.id.listshowItem);
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }

    private void changeWarehouse(String strid) {
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
                                int stokbaru = stokskrg+1;
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
}