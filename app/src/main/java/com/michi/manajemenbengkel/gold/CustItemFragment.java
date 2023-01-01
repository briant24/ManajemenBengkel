package com.michi.manajemenbengkel.gold;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.CallSuper;
import androidx.annotation.MainThread;
import androidx.fragment.app.Fragment;

import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CustItemFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CustItemFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener{
    TextView stNama,stMotor,stNopol;
    private Button btnSimpan;
    private Activity actvity;
    private ListView listBarang;
    private AdapterWarehouse Adapter;
    private ArrayList<HashMap<String, String>> list_barang;
    private View rootView;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CustItemFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CustItemFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CustItemFragment newInstance(String param1, String param2) {
        CustItemFragment fragment = new CustItemFragment();
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
        // Inflate the layout for this fragment
        Bundle bundle = this.getArguments();
        String nama_cust = bundle.getString("nama");
        String nopol_cust = bundle.getString("nopol");
        String motor_cust = bundle.getString("motor");
        String status_cust = bundle.getString("status");
        actvity = getActivity();
        rootView = inflater.inflate(R.layout.fragment_cust_item, container, false);
        stNama = rootView.findViewById(R.id.stNamaCust);
        stMotor = rootView.findViewById(R.id.stMotorCust);
        stNopol = rootView.findViewById(R.id.stNopolCust);
        stNama.setText(nama_cust);
        stMotor.setText(motor_cust);
        stNopol.setText(nopol_cust);
        initview();
        return rootView;
    }

    @MainThread
    @CallSuper
    public void onStart(){
        super.onStart();
        getData();
    }

    private void getData() {
        list_barang = new ArrayList<HashMap<String ,String >>();
        AndroidNetworking.get(KoneksiAPI.ShowItemTek)
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

    private void initview() {
        listBarang = rootView.findViewById(R.id.listViewTeknisi);
        listBarang.setOnItemClickListener(this);
        listBarang.setOverScrollMode(View.OVER_SCROLL_NEVER);
        btnSimpan = rootView.findViewById(R.id.button);
    }
}