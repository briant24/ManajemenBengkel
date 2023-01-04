package com.michi.manajemenbengkel.gold;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.CallSuper;
import androidx.annotation.MainThread;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ChildCustItemFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChildCustItemFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener{
    private TextView stNama,stMotor,stNopol;
    private Button btnSimpan;
    private Activity actvity;
    private ListView listBarang;
    private AdapterWarehouse Adapter;
    private SharedPreferences sharedPreferences;
    private ArrayList<HashMap<String, String>> list_barang;
    private View rootView;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ChildCustItemFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ChildCustItemFragment.
     */
    // TODO: Rename and change types and number of parameters
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
        // Inflate the layout for this fragment
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
    public void onStart() {
        super.onStart();
        getData();
    }

    private void getData() {
        list_barang = new ArrayList<HashMap<String ,String >>();
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
                        Log.i(TAG, "onError: " + anError);
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
                    AlertDialog.Builder inputjum = new AlertDialog.Builder(getActivity());
                    inputjum.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            AndroidNetworking.post(KoneksiAPI.tempTrans)
                                    .addBodyParameter("id","")
                                    .addBodyParameter("iduser",idtek)
                                    .addBodyParameter("idbarang",strid)
                                    .addBodyParameter("jumlah", "1")
                                    .setPriority(Priority.MEDIUM)
                                    .build()
                                    .getAsJSONObject(new JSONObjectRequestListener() {
                                        @Override
                                        public void onResponse(JSONObject response) {
                                            Toast.makeText(getActivity(), "Barang Berhasil Ditambahkan", Toast.LENGTH_SHORT).show();
                                        }

                                        @Override
                                        public void onError(ANError anError) {
                                            Log.i(TAG, "onError: " + anError);
                                        }
                                    });
                        }
                    });
                    inputjum.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    });
                    inputjum.show();
                }
            });
            return view;
        }
    }

    private void initview() {
        btnSimpan = rootView.findViewById(R.id.button_kembali);
    }
}