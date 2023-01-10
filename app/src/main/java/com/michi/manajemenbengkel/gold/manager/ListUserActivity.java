package com.michi.manajemenbengkel.gold.manager;

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
import com.michi.manajemenbengkel.gold.kasir.ActivityDetailTransaksi;
import com.michi.manajemenbengkel.gold.koneksi.KoneksiAPI;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListUserActivity extends AppCompatActivity {
    private ListView listView;
    private Button button;
    private Activity activity;
    private AdapterUser Adapter;
    private ArrayList<HashMap<String, String>> list_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_user);
        listView = findViewById(R.id.listUser);
        activity = this;
        button = findViewById(R.id.btnkembaliAllUser);
        button.setOnClickListener(new View.OnClickListener() {
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
        GetData();
    }

    private void GetData() {
        list_user = new ArrayList<HashMap<String,String>>();
        AndroidNetworking.get(KoneksiAPI.ShowUser)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("list_user");
                            for (int a = 0; a < jsonArray.length(); a++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(a);
                                HashMap<String, String> map = new HashMap<String, String>();
                                map.put("nama_user", jsonObject.getString("nama_user"));
                                map.put("level", jsonObject.getString("level"));
                                list_user.add(map);
                            }
                            Adapter = new AdapterUser(activity, list_user, R.layout.layout_show_user, new String[]
                                    {"nama_user", "level"}, new int[]{R.id.tvNama69,R.id.tvLevel69});
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

    public class AdapterUser extends SimpleAdapter {
        private Context mContext;
        public LayoutInflater inflater = null;

        public AdapterUser(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to) {
            super(context, data, resource, from, to);
            mContext = context;
            inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;
            if (convertView == null)
                view = inflater.inflate(R.layout.layout_show_user, null);
            HashMap<String, Object> data = (HashMap<String, Object>) getItem(position);
            final TextView txtnama = view.findViewById(R.id.tvNama69);
            final TextView txtlevel = view.findViewById(R.id.tvLevel69);
            final String strnama = (String) data.get("nama_user");
            final String strlevel = (String) data.get("level");
            txtnama.setText(strnama);
            txtlevel.setText(strlevel);
            return view;
        }
    }
}