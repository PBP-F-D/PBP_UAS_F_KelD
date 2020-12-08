package com.tubespbp.petshop.admin.ui.user;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.tubespbp.petshop.R;
import com.tubespbp.petshop.admin.API.CatalogAPI;
import com.tubespbp.petshop.admin.API.UserAPI;
import com.tubespbp.petshop.admin.ui.catalog.adapter.CatalogAdapter;
import com.tubespbp.petshop.admin.ui.catalog.model.Barang;
import com.tubespbp.petshop.admin.ui.user.adapter.UserAdapter;
import com.tubespbp.petshop.admin.ui.user.model.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.android.volley.Request.Method.GET;


public class UserFragment extends Fragment {

    private RecyclerView recyclerView;
    private UserAdapter adapter;
    private List<User> listUser;
    private View view;
    private String token;
    SharedPreferences shared;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_user, container, false);
        shared = getActivity().getSharedPreferences("getId", Context.MODE_PRIVATE);
        token = shared.getString("token", null);

        setAdapter();
        getUsers();

        return view;
    }
    public void setAdapter(){
        listUser = new ArrayList<User>();
        recyclerView = view.findViewById(R.id.rv_users);
        adapter = new UserAdapter(view.getContext(), listUser);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }

    public void getUsers() {
        //Pendeklarasian queue
        RequestQueue queue = Volley.newRequestQueue(view.getContext());

        //Meminta tanggapan string dari URL yang telah disediakan menggunakan method GET
        //untuk request ini tidak memerlukan parameter
        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(view.getContext());
        progressDialog.setMessage("loading....");
        progressDialog.setTitle("Loading catalog");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        final JsonObjectRequest stringRequest = new JsonObjectRequest(GET, UserAPI.URL_GET
                , null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //Disini bagian jika response jaringan berhasil tidak terdapat ganguan/error
                progressDialog.dismiss();
                try {
                    //Mengambil data response json object yang berupa data mahasiswa
                    JSONArray jsonArray = response.getJSONArray("data");

                    if (!listUser.isEmpty())
                        listUser.clear();

                    for (int i = 0; i < jsonArray.length(); i++) {
                        //Mengubah data jsonArray tertentu menjadi json Object
                        JSONObject jsonObject = (JSONObject) jsonArray.get(i);

                        Integer id = jsonObject.optInt("id");
                        String nama = jsonObject.optString("name");
                        String email = jsonObject.optString("email");
                        String city = jsonObject.optString("city");
                        String country = jsonObject.optString("country");
                        String phone = jsonObject.optString("phone");
                        String gambar = jsonObject.optString("img_user");

                        //Membuat objek user
                        User user = new User(id, email,  nama, phone, city, country, gambar);

                        //Menambahkan objek user tadi ke list user
                        listUser.add(user);
                    }
                    adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Toast.makeText(view.getContext(), response.optString("message"),
                        Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Disini bagian jika response jaringan terdapat ganguan/error
                progressDialog.dismiss();
                Toast.makeText(view.getContext(), error.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();

                headers.put("Content-Type", "application/x-www-form-urlencoded");
                headers.put("Authorization", "Bearer " + token);
                return headers;
            }
        };
        //Disini proses penambahan request yang sudah kita buat ke reuest queue yang sudah dideklarasi
        queue.add(stringRequest);
    }

}