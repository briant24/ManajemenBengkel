package com.michi.manajemenbengkel.gold;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CustItemFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CustItemFragment extends Fragment {
    TextView stNama,stMotor,stNopol;
    private Button btnSimpan;
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
        rootView = inflater.inflate(R.layout.fragment_cust_item, container, false);
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
        initview();
        return rootView;
    }

    private void initview() {
        btnSimpan = rootView.findViewById(R.id.button);
    }
}