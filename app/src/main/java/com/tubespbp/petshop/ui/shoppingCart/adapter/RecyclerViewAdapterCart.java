package com.tubespbp.petshop.ui.shoppingCart.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.text.InputFilter;
import android.text.InputType;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.button.MaterialButton;
import com.tubespbp.petshop.API.CartAPI;
import com.tubespbp.petshop.R;
import com.tubespbp.petshop.admin.API.CatalogAPI;
import com.tubespbp.petshop.databinding.ItemCartBinding;
import com.tubespbp.petshop.ui.shoppingCart.ShoppingCartFragment;
import com.tubespbp.petshop.ui.shoppingCart.model.Cart;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.android.volley.Request.Method.DELETE;
import static com.android.volley.Request.Method.PUT;

public class RecyclerViewAdapterCart extends RecyclerView.Adapter<RecyclerViewAdapterCart.CartViewHolder> {

    private Cart cart;
    private Context context;
    private List<Cart> cartList;
    SharedPreferences shared;
    int idUser;
    String token;
    private RecyclerViewAdapterCart.deleteItemListener mListener;

    public RecyclerViewAdapterCart(Context context, List<Cart> cartList,
                                   RecyclerViewAdapterCart.deleteItemListener mListener) {
        this.context = context;
        this.cartList = cartList;
        notifyDataSetChanged();
        this.mListener = mListener;
    }


    public interface deleteItemListener {
        void deleteItem(Boolean delete);
    }

    @NonNull
    @Override
    public RecyclerViewAdapterCart.CartViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());

        //Get idUser from sharedpreferences
        shared = context.getSharedPreferences("getId", Context.MODE_PRIVATE);
        idUser = shared.getInt("idUser", -1);
        token = shared.getString("token", null);

        ItemCartBinding binding = ItemCartBinding.inflate(layoutInflater, viewGroup, false);
        return new RecyclerViewAdapterCart.CartViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull final CartViewHolder holder, final int position) {
        final Cart cart = cartList.get(position);
//        holder.itemCartBinding.setCart(cart);

        holder.txtNama.setText(cart.getNamaB());
        holder.txtJml.setText(String.valueOf(cart.getJumlahB()));
        holder.txtHarga.setText(String.valueOf(cart.getHargaB()));

        cart.setTotalB(cart.getHargaB() * cart.getJumlahB());
        holder.txtTotal.setText(String.valueOf(cart.getTotalB()));


        Glide.with(context)
                .load(cart.getImgUrlC())
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(holder.ivImage);

        holder.removeItemBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //deletes item from database (still crashing)
                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                deleteData(cart.getId());

                //item removal animation
                notifyItemRemoved(position);

                //deletes item from temporary cart list
                //cartList.remove(cart);
                ShoppingCartFragment shoppingCartFragment = new ShoppingCartFragment();
                activity.getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.nav_host_fragment, shoppingCartFragment)
                        .commit();
            }
        });

        holder.editItemBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                final String namaBarang = holder.txtNama.getText().toString();
                final String hargaBarang = holder.txtHarga.getText().toString();

                //Creates alert dialog every time the add button is clicked asking for user's input
                android.app.AlertDialog.Builder alert = new android
                        .app.AlertDialog.Builder(view.getContext())
                        .setTitle("Edit Quantity");

                final EditText input = new EditText(view.getContext());
                input.setInputType(InputType.TYPE_CLASS_NUMBER);
                input.setRawInputType(Configuration.KEYBOARD_12KEY);
                input.setGravity(Gravity.CENTER);
                //Prevent character input length more than 9 characters (also to prevent crash)
                input.setFilters(new InputFilter[]{new InputFilter.LengthFilter(9)});
                alert.setView(input);

                alert.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        if (input.getText().toString().equals(""))
                            return; //prevent crash if input is empty

                        Integer value = Integer.parseInt(String.valueOf(input.getText()));
                        //adds item to cart if input is greater than zero
                        if (value > 0) {
                            final String imageUrl = cart.getImgUrlC();

                            editCart(cart.getId(), value, imageUrl, namaBarang, hargaBarang, cart.getStatusB(), position);
                            if (value == 1) {
                                notifyItemChanged(position);
                                Toast.makeText(holder.itemView.getContext(), value.toString() + "x " + cart.getNamaB() + " has been edited", Toast.LENGTH_SHORT).show();
                            } else {
                                notifyItemChanged(position);
                                Toast.makeText(holder.itemView.getContext(), value.toString() + "x " + cart.getNamaB() + "(s) have been edited", Toast.LENGTH_SHORT).show();
                                ShoppingCartFragment shoppingCartFragment = new ShoppingCartFragment();
                                activity.getSupportFragmentManager()
                                        .beginTransaction()
                                        .replace(R.id.nav_host_fragment, shoppingCartFragment)
                                        .commit();
                            }
                        }
                    }
                });

                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // Canceled.
                    }
                });

                alert.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return cartList.size();
    }

    public class CartViewHolder extends RecyclerView.ViewHolder {
        private TextView txtNama, txtHarga, txtJml, txtTotal;
        private ImageView ivImage;
        private MaterialButton removeItemBtn, editItemBtn;

        public CartViewHolder(ItemCartBinding itemView) {
            super(itemView.getRoot());
            txtNama = itemView.getRoot().findViewById(R.id.tv_namaProduk);
            txtHarga = itemView.getRoot().findViewById(R.id.tv_hargaProduk);
            txtJml = itemView.getRoot().findViewById(R.id.tv_quantity);
            txtTotal = itemView.getRoot().findViewById(R.id.tv_total);
            ivImage = itemView.getRoot().findViewById(R.id.profile_image);
            removeItemBtn = itemView.getRoot().findViewById(R.id.removeItem);
            editItemBtn = itemView.getRoot().findViewById(R.id.editItem);
        }
    }

    public Cart getItem(int position) {
        return cartList.get(position);
    }


    public void deleteData(int id) {
        RequestQueue queue = Volley.newRequestQueue(context);

        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(context);
        progressDialog.setTitle("Menghapus Barang Cart");
        progressDialog.setMessage("loading....");
        progressDialog.setProgressStyle(android.app.ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        //Memulai membuat permintaan request menghapus data ke jaringan
        StringRequest stringRequest = new StringRequest(DELETE, CartAPI.URL_DELETE + id, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Disini bagian jika response jaringan berhasil tidak terdapat ganguan/error
                progressDialog.dismiss();

                try {
                    //Mengubah response string menjadi object
                    JSONObject obj = new JSONObject(response);

                    //obj.getString("message") digunakan untuk mengambil pesan message dari response
                    Toast.makeText(context, obj.getString("message"), Toast.LENGTH_SHORT).show();
                    notifyDataSetChanged();
                    mListener.deleteItem(true);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Disini bagian jika response jaringan terdapat ganguan/error
                progressDialog.dismiss();
                Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
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

    private void editCart(final int id, final int value, final String gambar, final String namaBarang, final String hargaBarang, final String status, int position) {
        RequestQueue queue = Volley.newRequestQueue(context);

        StringRequest stringRequest = new StringRequest(Request.Method.PUT, CartAPI.URL_UPDATE + id,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            if (obj.getString("status").equals("Success")) {
                                Toast.makeText(context, obj.getString("message"), Toast.LENGTH_SHORT).show();

                            }
                            Toast.makeText(context, obj.getString("message"), Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Unable to checkout: " + error.toString(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("nama_barang", namaBarang);
                params.put("jmlbeli_barang", String.valueOf(value));
                params.put("harga_barang", hargaBarang);
                params.put("img_barang", gambar);
                params.put("user_barang", String.valueOf(idUser));
                params.put("status_barang", status);

                System.out.println("params set!");
                System.out.println(namaBarang);
                System.out.println(value);
                System.out.println(hargaBarang);
                System.out.println(gambar);
//                System.out.println(idUser);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();

                headers.put("Content-Type", "application/x-www-form-urlencoded");
                headers.put("Authorization", "Bearer " + token);
                return headers;
            }
        };
        queue.add(stringRequest);
    }

}
