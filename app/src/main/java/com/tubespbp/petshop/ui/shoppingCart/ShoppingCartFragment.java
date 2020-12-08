package com.tubespbp.petshop.ui.shoppingCart;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.button.MaterialButton;
import com.tubespbp.petshop.API.CartAPI;
import com.tubespbp.petshop.Constant;
import com.tubespbp.petshop.MainActivity;
import com.tubespbp.petshop.R;
import com.tubespbp.petshop.databinding.FragmentShoppingCartBinding;
import com.tubespbp.petshop.ui.shoppingCart.adapter.RecyclerViewAdapterCart;
import com.tubespbp.petshop.ui.shoppingCart.database.DatabaseClient;
import com.tubespbp.petshop.ui.shoppingCart.model.Cart;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.android.volley.Request.Method.GET;

public class ShoppingCartFragment extends Fragment {

    private ArrayList<Cart> ListCart, newList;
    private RecyclerView recyclerView;
    private RecyclerViewAdapterCart adapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private FragmentShoppingCartBinding shoppingCartBinding;
    private MaterialButton checkOutBtn;
    private View view;

    private ShoppingCartViewModel shoppingCartViewModel;
    private SharedPreferences shared;
    int idUser;

    String token;

    Constant constant;
    SharedPreferences.Editor editor;
    SharedPreferences app_preferences;
    int appTheme;
    int themeColor;
    int appColor;

    public ShoppingCartFragment() {
        // Required empty public constructor
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        shoppingCartBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_shopping_cart, container, false);
        view = shoppingCartBinding.getRoot();

        MainActivity main = (MainActivity)getActivity();

        //Get theme
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

        //Get idUser from sharedpreferences
        shared = getActivity().getSharedPreferences("getId", Context.MODE_PRIVATE);
        idUser = shared.getInt("idUser", -1);
        token = shared.getString("token", null);

        //Recycler View
        recyclerView = shoppingCartBinding.rvFollow;

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);

        getCart();

        //Check out Button Pressed
        checkOutBtn = view.findViewById(R.id.btn_checkout);
        checkOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Get cart list when clicked
                ArrayList<Cart> items = new ArrayList<Cart>();
                for(int i=0; i<adapter.getItemCount(); i++){
                    items.add(adapter.getItem(i));
                }

                //Create a new ArrayList bundle
                Bundle bundle= new Bundle();
                bundle.putSerializable("arraylist", items);

                //Navigate to CheckoutFragment
                if(adapter.getItemCount()>0)
                    Navigation.findNavController(view).navigate(
                            R.id.action_navigation_cart_to_checkoutFragment, bundle);
                else
                    Toast.makeText(getContext(), "Cart is Empty",
                            Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

//    public void getCart(){
//        class GetCart extends AsyncTask<Void, Void, List<Cart>> {
//
//            @Override
//            protected void onPostExecute(List<Cart> carts) {
//                super.onPostExecute(carts);
//                adapter = new RecyclerViewAdapterCart(getContext(), carts);
//                recyclerView.setAdapter(adapter);
//                if (carts.isEmpty()){
//                    Toast.makeText(getContext(), "Empty List", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            protected List<Cart> doInBackground(Void... voids) {
//                List<Cart> userCart = DatabaseClient
//                        .getInstance(getContext())
//                        .getDatabase()
//                        .userDAO()
//                        .getUserCart(idUser);
//                return userCart;
//            }
//        }
//
//        GetCart get = new GetCart();
//        get.execute();
//    }

    public void getCart() {
        //Tambahkan tampil buku disini
        RequestQueue queue = Volley.newRequestQueue(view.getContext());

        //Meminta tanggapan string dari URL yang telah disediakan menggunakan method GET
        //untuk request ini tidak memerlukan parameter
        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(view.getContext());
        progressDialog.setMessage("loading....");
        progressDialog.setTitle("Menampilkan data cart");
        progressDialog.setProgressStyle(android.app.ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        final JsonObjectRequest stringRequest = new JsonObjectRequest(GET, CartAPI.URL_SELECT
                , null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //Disini bagian jika response jaringan berhasil tidak terdapat ganguan/error
                progressDialog.dismiss();
                try {
                    //Mengambil data response json object yang berupa data dataBuku
                    JSONArray jsonArray = response.getJSONArray("data");

                    if(!ListCart.isEmpty())
                        ListCart.clear();

                    for (int i = 0; i < jsonArray.length(); i++) {
                        //Mengubah data jsonArray tertentu menjadi json Object
                        JSONObject jsonObject = (JSONObject) jsonArray.get(i);

                        int userbarang      = jsonObject.optInt("user_barang");
                        String namaBarang   = jsonObject.optString("nama_barang");
                        Double hargaBarang  = jsonObject.optDouble("harga_barang");
                        Integer jumlah      = jsonObject.optInt("jmlbeli_barang");
                        String statusBarang = jsonObject.optString("status_barang");
                        String gambar       = jsonObject.optString("img_barang");

                        if(idUser == userbarang) {
                            //Membuat objek cart
                            Cart cart =
                                    new Cart(userbarang, namaBarang, hargaBarang, jumlah, statusBarang, gambar);

                            //Menambahkan objek user tadi ke list cart
                            ListCart.add(cart);
                        }
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