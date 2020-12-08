package com.tubespbp.petshop.ui.home.catalog;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.tubespbp.petshop.API.CatalogAPI;
import com.tubespbp.petshop.Constant;
import com.tubespbp.petshop.MainActivity;
import com.tubespbp.petshop.R;
import com.tubespbp.petshop.databinding.FragmentCatalogBinding;
import com.tubespbp.petshop.ui.home.HomeFragment;
import com.tubespbp.petshop.ui.home.catalog.adapter.RecyclerViewAdapterKatalog;
import com.tubespbp.petshop.ui.home.catalog.model.Barang;
import com.tubespbp.petshop.ui.home.catalog.model.DaftarBarang;
import com.tubespbp.petshop.ui.shoppingCart.model.Cart;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.android.volley.Request.Method.GET;

public class CatalogFragment extends Fragment {

    private List<Barang> ListBarang;
    private ArrayList<Barang> newList;
    private RecyclerView recyclerView;
    private RecyclerViewAdapterKatalog adapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private FragmentCatalogBinding catalogBinding, binding;
    private View view;
    private RequestQueue queue;

    public String name;

    Constant constant;
    SharedPreferences.Editor editor;
    SharedPreferences app_preferences, shared;
    int appTheme;
    int themeColor;
    int appColor;
    String token;

    public CatalogFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        catalogBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_catalog, container, false);

        MainActivity main = (MainActivity)getActivity();

        app_preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        appColor = app_preferences.getInt("color", 0);
        appTheme = app_preferences.getInt("theme", 0);
        themeColor = appColor;
        constant.color = appColor;

        if (themeColor == 0){
            main.setTheme(Constant.theme);
        }else if (appTheme == 0){
            main.setTheme(Constant.theme);
        }else{
            main.setTheme(appTheme);
        }

//        view = catalogBinding.getRoot();
        view = inflater.inflate(R.layout.fragment_catalog, container, false);

        //Mengambil Bundle dari HomeFragment
        if (getArguments() != null)
            name = getArguments().getString(HomeFragment.name);
        else
            name = "";

        shared = getActivity().getSharedPreferences("getId", Context.MODE_PRIVATE);
        token = shared.getString("token", null);

        //List barang baru yang dikosongkan
        newList = new ArrayList<Barang>();
        ListBarang = new ArrayList<Barang>();

        //Recycler View
        recyclerView = catalogBinding.rvKatalog;

        //Menampilkan list barang baru
        adapter = new RecyclerViewAdapterKatalog(ListBarang);
        recyclerView.setAdapter(adapter);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);

        //Daftar Semua List Barang
        getBarang();

        //Membandingkan nama/kategori supaya dimasukkan ke list baru
        switch(name) {
            case "Dogs":
                for (int i=0; i<ListBarang.size(); i++)
                    if(ListBarang.get(i).getKategori().equals("Dog"))
                        newList.add(ListBarang.get(i));
                break;
            case "Cats":
                for (int i=0; i<ListBarang.size(); i++)
                    if(ListBarang.get(i).getKategori().equals("Cat"))
                        newList.add(ListBarang.get(i));
                break;
            case "Other":
                for (int i=0; i<ListBarang.size(); i++)
                    if(ListBarang.get(i).getKategori().equals("Other"))
                        newList.add(ListBarang.get(i));
                break;
            default:
                break;
        }

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public void getBarang() {
        RequestQueue queue = Volley.newRequestQueue(view.getContext());

        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(view.getContext());
        progressDialog.setMessage("loading....");
        progressDialog.setTitle("Menampilkan data catalog");
        progressDialog.setProgressStyle(android.app.ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        final JsonObjectRequest stringRequest = new JsonObjectRequest(GET, CatalogAPI.URL_SELECT
                , null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //Disini bagian jika response jaringan berhasil tidak terdapat ganguan/error
                progressDialog.dismiss();
                try {
                    //Mengambil data response json object yang berupa data mahasiswa
                    JSONArray jsonArray = response.getJSONArray("data");
                    if(!ListBarang.isEmpty())
                        ListBarang.clear();

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = (JSONObject) jsonArray.get(i);

                        int idBarang    = jsonObject.optInt("id");
                        String nama     = jsonObject.optString("nama_barang");
                        double harga    = jsonObject.optDouble("harga_barang");
                        int stok        = jsonObject.optInt("stok_barang");
                        String kategori = jsonObject.optString("kategori_barang");
                        String gambar   = jsonObject.optString("img_barang");

                        Barang barang = new Barang(idBarang, nama,harga,stok,kategori,gambar);
                        ListBarang.add(barang);
                    }
                    adapter.notifyDataSetChanged();
                }catch (JSONException e){
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
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String,String> headers = new HashMap<>();

                headers.put("Content-Type","application/x-www-form-urlencoded");
                headers.put("Authorization","Bearer " + token);
                return headers;
            }
        };
        // Disini proses penambahan request yang sudah kita buat ke request queue yang sudah dideklarasi
        queue.add(stringRequest);
    }

}