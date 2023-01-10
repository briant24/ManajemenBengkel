package com.michi.manajemenbengkel.gold.teknisi;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.android.material.tabs.TabLayout;
import com.michi.manajemenbengkel.gold.koneksi.KoneksiAPI;
import com.michi.manajemenbengkel.gold.R;

import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CustItemFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CustItemFragment extends Fragment{
    private TextView stNama,stMotor,stNopol;
    private SharedPreferences sharedPreferences;
    private Button btnSimpan,btnKembali;
    private View rootView;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public CustItemFragment() {
    }
    public static CustItemFragment newInstance(String param1, String param2) {
        CustItemFragment fragment = new CustItemFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUpViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void setUpViewPager(ViewPager viewPager) {
        SectionPageAdapter adapter = new SectionPageAdapter(getChildFragmentManager());
        adapter.addFragment(new ChildCustItemFragment(), "Tambah Item");
        adapter.addFragment(new ChildShowCustItemFragment(), "Lihat Item");
        viewPager.setAdapter(adapter);
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
        rootView = inflater.inflate(R.layout.fragment_cust_item, container, false);
        viewPager = rootView.findViewById(R.id.viewPager);
        tabLayout = rootView.findViewById(R.id.tabLayout);
        sharedPreferences = this.getActivity().getSharedPreferences("user-session", Context.MODE_PRIVATE);
        Bundle bundle = this.getArguments();
        String nama_cust = bundle.getString("nama");
        String nopol_cust = bundle.getString("nopol");
        String motor_cust = bundle.getString("motor");
        String status_cust = bundle.getString("status");
        stNama = rootView.findViewById(R.id.stNamaCust);
        stMotor = rootView.findViewById(R.id.stMotorCust);
        stNopol = rootView.findViewById(R.id.stNopolCust);
        stNama.setText(nama_cust);
        stMotor.setText(motor_cust);
        stNopol.setText(nopol_cust);
        initview(status_cust);
        return rootView;
    }

    private void initview(String status_bayar) {
        String idtrans = sharedPreferences.getString("id_trans",null);
        btnKembali = rootView.findViewById(R.id.button_kembali);
        btnKembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AndroidNetworking.post(KoneksiAPI.DelTrans)
                        .addBodyParameter("id", idtrans)
                        .setPriority(Priority.MEDIUM)
                        .build()
                        .getAsJSONObject(new JSONObjectRequestListener() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Toast.makeText(getActivity(), "Transaksi Berhasil dihapus", Toast.LENGTH_SHORT).show();
                                CustFragment custItemFragment = new CustFragment();
                                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, custItemFragment).commit();
                            }
                            @Override
                            public void onError(ANError anError) {
                                Toast.makeText(getActivity(), "Hapus Item terlebih dahulu", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
        btnSimpan = rootView.findViewById(R.id.button_selanjutnya);
        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nama = stNama.getText().toString();
                String nopol = stNopol.getText().toString();
                String motor = stMotor.getText().toString();
                Bundle bundle = new Bundle();
                bundle.putString("nama", nama);
                bundle.putString("nopol", nopol);
                bundle.putString("motor", motor);
                bundle.putString("status", status_bayar);
                ConfirmTransFragment custItemFragment = new ConfirmTransFragment();
                custItemFragment.setArguments(bundle);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, custItemFragment).commit();
            }
        });
    }
}