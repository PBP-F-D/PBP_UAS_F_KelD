package com.tubespbp.petshop.UnitTest;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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

import static android.widget.Toast.LENGTH_SHORT;
import static com.android.volley.Request.Method.POST;

public class AddCatalogFragment extends Fragment implements CreateView{
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
    }

    @Override
    public String getNamaBarang() {
        return tiNama.getText().toString();
    }

    @Override
    public String getStok() {
        return tiStok.getText().toString();
    }

    @Override
    public String getHarga() {
        return tiHarga.getText().toString();
    }

    @Override
    public String getKategori() {
        return tiKategori.getText().toString();
    }

    @Override
    public String getUrl() {
        return tiUrl.getText().toString();
    }

    @Override
    public void showNamaBarangError(String message) {
        tilNama.setError(message);
    }

    @Override
    public void showStokError(String message) {
        tilStok.setError(message);
    }

    @Override
    public void showHargaError(String message) {
        tilHarga.setError(message);
    }

    @Override
    public void showKategoriError(String message) {
        tilKategori.setError(message);
    }

    @Override
    public void showUrlError(String message) {
        tilUrl.setError(message);
    }

    @Override
    public void startCatalogFragment() {
        new ActivityUtil(getContext()).startCatalogFragment();
    }

    @Override
    public void showCreateError(String message) {
        Toast.makeText(getContext(), message, LENGTH_SHORT).show();
    }

    @Override
    public void showErrorResponse(String message) {
        Toast.makeText(getContext(), message, LENGTH_SHORT).show();
    }
}
