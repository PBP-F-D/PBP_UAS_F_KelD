package com.tubespbp.petshop.admin.ui.catalog;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.tubespbp.petshop.R;
import com.tubespbp.petshop.admin.API.CatalogAPI;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.android.volley.Request.Method.POST;

public class AddCatalogFragment extends Fragment {
    private SharedPreferences shared;
    private int idUser;
    private View view;
    private MaterialButton btnCancel, btnAdd;
    private String selectedCategory, token;
    private TextInputEditText tiNama, tiStok, tiHarga, tiUrl;
    private MaterialAutoCompleteTextView tiKategori;
    private TextInputLayout tilNama, tilStok, tilHarga, tilUrl, tilKategori;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_add_catalog, container, false);
        //Get sharepreferences for ID user
        shared = getActivity().getSharedPreferences("getId", Context.MODE_PRIVATE);
        token = shared.getString("token", null);
        idUser = shared.getInt("idUser", -1);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //findViewById
        tiNama = view.findViewById(R.id.ti_namaBarang);
        tiStok = view.findViewById(R.id.ti_stok);
        tiHarga = view.findViewById(R.id.ti_harga);
        tiKategori = view.findViewById(R.id.ti_kategori);
        tiUrl = view.findViewById(R.id.ti_url);

        tilNama = view.findViewById(R.id.til_namaBarang);
        tilStok = view.findViewById(R.id.til_stok);
        tilHarga = view.findViewById(R.id.til_harga);
        tilKategori = view.findViewById(R.id.til_kategori);
        tilUrl = view.findViewById(R.id.til_url);

        btnCancel = view.findViewById(R.id.btn_addCancel);
        btnAdd = view.findViewById(R.id.btn_addSubmit);

        //Isi dropdown Categories
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.categories, android.R.layout.simple_spinner_item);
        tiKategori.setText(selectedCategory);
        tiKategori.setAdapter(adapter);
        tiKategori.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                selectedCategory = tiKategori.getEditableText().toString();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(
                        R.id.action_addCatalogFragment_to_navigation_catalog);
            }
        });
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String namaBrg = tiNama.getText().toString();
                final String stokBrg = tiStok.getText().toString();
                final String hargaBrg = tiHarga.getText().toString();
                final String kategoriBrg = tiKategori.getText().toString();
                final String urlBrg = tiUrl.getText().toString();

                //Validation Check
                if(namaBrg.isEmpty()) {tilNama.setError("Enter product name"); }
                else {tilNama.setError("");}

                if(stokBrg.isEmpty()) {tilStok.setError("Enter stock"); }
                else {tilStok.setError("");}

                if(hargaBrg.isEmpty()) {tilHarga.setError("Enter price"); }
                else {tilHarga.setError("");}

                if(kategoriBrg.isEmpty()) {tilKategori.setError("Choose a category"); }
                else {tilKategori.setError("");}

                if(urlBrg.isEmpty()) {tilUrl.setError("Enter picture url"); }
                else {tilUrl.setError("");}

                if(!namaBrg.isEmpty() && !stokBrg.isEmpty() && !hargaBrg.isEmpty() &&
                        !kategoriBrg.isEmpty() && !urlBrg.isEmpty()){
                    addProduct(namaBrg, Integer.parseInt(stokBrg), Double.parseDouble(hargaBrg), kategoriBrg, urlBrg);
                }
            }
        });
    }

    public void addProduct(final String nama, final int stok, final double harga, final String kategori, final String url) {
        //Pendeklarasian queue
        RequestQueue queue = Volley.newRequestQueue(getContext());

        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("loading....");
        progressDialog.setTitle("Menambahkan data mahasiswa");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        //Memulai membuat permintaan request menghapus data ke jaringan
        StringRequest stringRequest = new StringRequest(POST, CatalogAPI.URL_ADD, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Disini bagian jika response jaringan berhasil tidak terdapat ganguan/error
                progressDialog.dismiss();
                try {
                    //Mengubah response string menjadi object
                    JSONObject obj = new JSONObject(response);
                    //obj.getString("message") digunakan untuk mengambil pesan status dari response
                    if (obj.getString("message").equalsIgnoreCase("Add Barang Success")) {
                        Navigation.findNavController(view).navigate(
                                R.id.action_addCatalogFragment_to_navigation_catalog);
                    }

                    //obj.getString("message") digunakan untuk mengambil pesan message dari response
                    Toast.makeText(getContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Disini bagian jika response jaringan terdapat ganguan/error
                progressDialog.dismiss();
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();

                headers.put("Content-Type", "application/x-www-form-urlencoded");
                headers.put("Authorization", "Bearer " + token);
                return headers;
            }

            @Override
            protected Map<String, String> getParams() {
                /*
                    Disini adalah proses memasukan/mengirimkan parameter key dengan data value,
                    dan nama key nya harus sesuai dengan parameter key yang diminta oleh jaringan
                    API.
                */
                Map<String, String> params = new HashMap<String, String>();
                params.put("nama_barang", nama);
                params.put("stok_barang", String.valueOf(stok));
                params.put("harga_barang", String.valueOf(harga));
                params.put("kategori_barang", kategori);
                params.put("img_barang", url);

                return params;
            }
        };

        //Disini proses penambahan request yang sudah kita buat ke reuest queue yang sudah dideklarasi
        queue.add(stringRequest);
    }
}
