package com.tubespbp.petshop.ui.shoppingCart;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.button.MaterialButton;
import com.tubespbp.petshop.API.ApiClient;
import com.tubespbp.petshop.API.ApiInterface;
import com.tubespbp.petshop.API.CartAPI;
import com.tubespbp.petshop.API.User.UserResponse;
import com.tubespbp.petshop.Constant;
import com.tubespbp.petshop.MainActivity;
import com.tubespbp.petshop.R;
import com.tubespbp.petshop.databinding.FragmentCheckoutBinding;
import com.tubespbp.petshop.ui.profile.model.User;
import com.tubespbp.petshop.ui.shoppingCart.adapter.RecyclerViewAdapterCheckout;
import com.tubespbp.petshop.ui.shoppingCart.model.Cart;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CheckoutFragment extends Fragment {
    private static final String KEY_ARRAY = "arraylist";
    private RecyclerView recyclerView;
    private RecyclerViewAdapterCheckout adapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private FragmentCheckoutBinding checkoutBinding;
    private ShoppingCartFragment shoppingCartFragment = new ShoppingCartFragment();
    private TextView totalprice, customername;
    private MaterialButton cancelBtn, confirmBtn;
    private double total = 0;
    private View view;

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

    public CheckoutFragment() {
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        checkoutBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_checkout, container, false);
        view = checkoutBinding.getRoot();

        //Get ArrayList from Bundle
        ArrayList<Cart> cartList = (ArrayList<Cart>) getArguments().getSerializable(KEY_ARRAY);

        //Declare total price textview
        totalprice = view.findViewById(R.id.tv_totalprice);
        for(int i=0; i<cartList.size(); i++) {
            total += cartList.get(i).getTotalB();
        }
        totalprice.setText(Double.toString(total));
        customername = view.findViewById(R.id.tv_customername);

        Intent i = getActivity().getIntent();
        sName = i.getStringExtra("name");

        //Get sharepreferences for ID user
        shared = getActivity().getSharedPreferences("getId", Context.MODE_PRIVATE);
        idUser = shared.getInt("idUser", -1);
        token = shared.getString("token", null);
        getUsers();

        customername.setText(sName);

        recyclerView = checkoutBinding.rvCheckout;

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

        cancelBtn = view.findViewById(R.id.cancelBtn);
        confirmBtn = view.findViewById(R.id.confirmBtn);

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(
                        R.id.action_checkoutFragment_to_navigation_dashboard);
            }
        });

        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Check-out Successful", Toast.LENGTH_SHORT).show();
                for(int i=0; i<cartList.size(); i++) {
                    if(cartList.get(i) != null && cartList.get(i).getIdUser().equals(String.valueOf(idUser))) {
                        Cart cart = cartList.get(i);
                        if(cart.getStatusB().equals("Not Paid"))
                            checkout(cart.getId(),
                                    cart.getJumlahB(),
                                    cart.getImgUrlC(),
                                    cart.getNamaB(),
                                    String.valueOf(cart.getHargaB()));
                    }
                }
                Navigation.findNavController(view).navigate(
                        R.id.action_checkoutFragment_to_navigation_dashboard);
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

    private void checkout(final int id, final int value, final String gambar, final String namaBarang, final String hargaBarang){
        RequestQueue queue = Volley.newRequestQueue(view.getContext());

        StringRequest stringRequest = new StringRequest(Request.Method.PUT, CartAPI.URL_UPDATE + id,
                new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject obj = new JSONObject(response);
                    if(obj.getString("status").equals("Success")) {
                        Toast.makeText(view.getContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                    Toast.makeText(view.getContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(view.getContext(),"Unable to checkout: " + error.toString(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("nama_barang", namaBarang);
                params.put("jmlbeli_barang", String.valueOf(value));
                params.put("harga_barang", hargaBarang);
                params.put("img_barang", gambar);
                params.put("user_barang", String.valueOf(idUser));
                params.put("status_barang", "Paid");

                System.out.println("params set!");
                System.out.println(namaBarang);
                System.out.println(value);
                System.out.println(hargaBarang);
                System.out.println(gambar);
                System.out.println(idUser);

                return params;
            }
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String,String> headers = new HashMap<>();

                headers.put("Content-Type","application/x-www-form-urlencoded");
                headers.put("Authorization","Bearer " + token);
                return headers;
            }
        };
        queue.add(stringRequest);
    }

}