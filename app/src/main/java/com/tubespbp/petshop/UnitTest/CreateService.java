package com.tubespbp.petshop.UnitTest;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.Toast;

import androidx.navigation.Navigation;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.tubespbp.petshop.API.ApiClient;
import com.tubespbp.petshop.API.ApiInterface;
import com.tubespbp.petshop.API.User.UserDAO;
import com.tubespbp.petshop.API.User.UserResponse;
import com.tubespbp.petshop.R;
import com.tubespbp.petshop.admin.API.CatalogAPI;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.android.volley.Request.Method.POST;

public class CreateService {
    private SharedPreferences shared;
    String token;
    Context context;

    public void addProduct(final CreateView view, final String nama, final int stok, final double harga, final String kategori, final String url, final CreateCallback callback) {
        //Pendeklarasian queue
        //Get sharepreferences for ID user
        shared = context.getSharedPreferences("getId", Context.MODE_PRIVATE);
        token = shared.getString("token", null);
        RequestQueue queue = Volley.newRequestQueue(context);

        //Memulai membuat permintaan request menghapus data ke jaringan
        StringRequest stringRequest = new StringRequest(POST, CatalogAPI.URL_ADD, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Disini bagian jika response jaringan berhasil tidak terdapat ganguan/error
                try {
                    //Mengubah response string menjadi object
                    JSONObject obj = new JSONObject(response);

                    if (obj.getString("message").equalsIgnoreCase("Add Barang Success")) {
                        callback.onSuccess(true, obj.getString("message"));
                    } else {
                        callback.onError();
                        view.showCreateError(obj.getString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onError();
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

    public Boolean getValid(final CreateView view,  String nama,  int stok,  double harga,  String kategori,  String url) {
        final Boolean[] bool = new Boolean[1];
        addProduct(view, nama, stok, harga, kategori, url, new CreateCallback() {
            @Override
            public void onSuccess(boolean value, String message) {
                bool[0] = true;
            }

            @Override
            public void onError() {
                bool[0] = false;
            }
        });
        return bool[0];
    }
}
