package com.tubespbp.petshop.ui.shoppingCart;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.button.MaterialButton;
import com.tubespbp.petshop.API.ApiClient;
import com.tubespbp.petshop.API.ApiInterface;
import com.tubespbp.petshop.API.CartAPI;
import com.tubespbp.petshop.API.User.UserResponse;
import com.tubespbp.petshop.Constant;
import com.tubespbp.petshop.MainActivity;
import com.tubespbp.petshop.R;
import com.tubespbp.petshop.databinding.FragmentHistoryBinding;
import com.tubespbp.petshop.ui.profile.model.User;
import com.tubespbp.petshop.ui.shoppingCart.adapter.RecyclerViewAdapterCheckout;
import com.tubespbp.petshop.ui.shoppingCart.model.Cart;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.android.volley.Request.Method.GET;

public class HistoryFragment extends Fragment {
    private RecyclerView recyclerView;
    private RecyclerViewAdapterCheckout adapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private MaterialButton closeBtn;
    private FragmentHistoryBinding historyBinding;
    private TextView totalprice, customername;
    private View view;
    private double total = 0;
    ArrayList<Cart> cartList;

    SharedPreferences shared;
    private List<User> userList;
    int idUser;
    String sName, token;

    Constant constant;
    SharedPreferences.Editor editor;
    SharedPreferences app_preferences;
    int appTheme;
    int themeColor;
    int appColor;

    public HistoryFragment() {
        //Empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        historyBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_history, container, false);
        view = historyBinding.getRoot();

        cartList = new ArrayList<Cart>();

        //Get sharepreferences for ID user
        shared = getActivity().getSharedPreferences("getId", Context.MODE_PRIVATE);
        idUser = shared.getInt("idUser", -1);
        token = shared.getString("token", null);
        getUsers();
        getCart();


        //Declare total price textview
        totalprice = view.findViewById(R.id.tv_totalpriceh);
        customername = view.findViewById(R.id.tv_customernameh);

        Intent i = getActivity().getIntent();
        sName = i.getStringExtra("name");
        customername.setText(sName);

        recyclerView = historyBinding.rvCheckout;

        adapter = new RecyclerViewAdapterCheckout(getContext(), cartList);
        recyclerView.setAdapter(adapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext()) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };

        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);

        closeBtn = view.findViewById(R.id.closeBtn);

        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(
                        R.id.action_historyFragment_to_navigation_dashboard);
            }
        });

        return view;
    }

    private void getUsers(){
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<UserResponse> load = apiService.getUser("Bearer " + token);
        load.enqueue(new Callback<UserResponse>()
        {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                sName = response.body().getUsers().getName();
                customername.setText(sName);
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Kesalahan Jaringan", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void getCart() {
        //Tambahkan tampil buku disini
        RequestQueue queue = Volley.newRequestQueue(view.getContext());

        //Meminta tanggapan string dari URL yang telah disediakan menggunakan method GET
        //untuk request ini tidak memerlukan parameter
        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(view.getContext());
        progressDialog.setMessage("loading....");
        progressDialog.setTitle("Menampilkan data history");
        progressDialog.setProgressStyle(android.app.ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        final JsonObjectRequest stringRequest = new JsonObjectRequest(GET, CartAPI.URL_SELECT
                , null, new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //Disini bagian jika response jaringan berhasil tidak terdapat ganguan/error
                progressDialog.dismiss();
                try {
                    //Mengambil data response json object yang berupa data dataBuku
                    JSONArray jsonArray = response.getJSONArray("data");

                    if(!cartList.isEmpty())
                        cartList.clear();

                    for (int i = 0; i < jsonArray.length(); i++) {
                        //Mengubah data jsonArray tertentu menjadi json Object
                        JSONObject jsonObject = (JSONObject) jsonArray.get(i);

                        String idCart       = jsonObject.optString("id");
                        String userbarang   = jsonObject.optString("user_barang");
                        String namaBarang   = jsonObject.optString("nama_barang");
                        Double hargaBarang  = jsonObject.optDouble("harga_barang");
                        Integer jumlah      = jsonObject.optInt("jmlbeli_barang");
                        String statusBarang = jsonObject.optString("status_barang");
                        String gambar       = jsonObject.optString("img_barang");

                        if(idUser == Integer.parseInt(userbarang) && statusBarang.equals("Paid")) {
                            //Membuat objek cart
                            Cart cart =
                                    new Cart(Integer.parseInt(idCart), userbarang, namaBarang, hargaBarang, jumlah, statusBarang, gambar);
                            total += jumlah * hargaBarang;
                            totalprice.setText(String.valueOf(total));

                            //Menambahkan objek user tadi ke list cart
                            cartList.add(cart);
                        }
                    }
                    adapter.notifyDataSetChanged();
                }catch (JSONException e){
                    e.printStackTrace();
                }
                Toast.makeText(view.getContext(), response.optString("message"),
                        Toast.LENGTH_SHORT).show();
            }
        }, new com.android.volley.Response.ErrorListener() {
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
                HashMap<String,String> headers = new HashMap<>();

                headers.put("Content-Type","application/x-www-form-urlencoded");
                headers.put("Authorization","Bearer " + token);
                return headers;
            }
        };

        //Disini proses penambahan request yang sudah kita buat ke request queue yang sudah dideklarasi
        queue.add(stringRequest);

    }
}
