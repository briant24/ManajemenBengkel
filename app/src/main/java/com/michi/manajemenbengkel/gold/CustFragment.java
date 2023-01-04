package com.michi.manajemenbengkel.gold;

import static android.content.ContentValues.TAG;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CustFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CustFragment extends Fragment {
    private EditText namaCust,nopolCust,motorCust;
    private Button btnNext;
    private String nama,nopol,motor,id_tek;
    private View rootView;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CustFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CustFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CustFragment newInstance(String param1, String param2) {
        CustFragment fragment = new CustFragment();
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
        rootView = inflater.inflate(R.layout.fragment_cust, container, false);
        initview();
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    private void initview() {
        btnNext = rootView.findViewById(R.id.btnNext);
        namaCust = rootView.findViewById(R.id.etNamaCust);
        nopolCust = rootView.findViewById(R.id.etNopolCust);
        motorCust = rootView.findViewById(R.id.etMotorCust);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validasidata();
            }
        });
    }

    private void validasidata() {
        String data_status = "Belum Bayar";
        nama = namaCust.getText().toString();
        nopol = nopolCust.getText().toString();
        motor = motorCust.getText().toString();
        if (nama.isEmpty() || nopol.isEmpty() || motor.isEmpty()){
            Toast.makeText(getActivity(), "Data Belum Lengkap!", Toast.LENGTH_SHORT).show();
        }else {
            passdata(nama,nopol,motor,data_status);
        }
    }

    private void passdata(String nama, String nopol, String motor, String data_status) {
        Bundle bundle = new Bundle();
        bundle.putString("nama", nama);
        bundle.putString("nopol", nopol);
        bundle.putString("motor", motor);
        bundle.putString("status", data_status);
        CustItemFragment custItemFragment = new CustItemFragment();
        custItemFragment.setArguments(bundle);
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, custItemFragment).commit();
    }
}