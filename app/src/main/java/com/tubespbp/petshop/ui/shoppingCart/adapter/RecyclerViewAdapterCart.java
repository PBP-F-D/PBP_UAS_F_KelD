package com.tubespbp.petshop.ui.shoppingCart.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
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
import com.tubespbp.petshop.databinding.ItemCartBinding;
import com.tubespbp.petshop.ui.shoppingCart.ShoppingCartFragment;
import com.tubespbp.petshop.ui.shoppingCart.model.Cart;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.android.volley.Request.Method.DELETE;

public class RecyclerViewAdapterCart extends RecyclerView.Adapter< RecyclerViewAdapterCart.CartViewHolder>{

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
        void deleteItem( Boolean delete);
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
    }

    @Override
    public int getItemCount() {
        return cartList.size();
    }

    public class CartViewHolder extends RecyclerView.ViewHolder {
        private TextView txtNama, txtHarga, txtJml, txtTotal;
        private ImageView ivImage;
        private MaterialButton removeItemBtn;

        public CartViewHolder(ItemCartBinding itemView){
            super(itemView.getRoot());
            txtNama = itemView.getRoot().findViewById(R.id.tv_namaProduk);
            txtHarga = itemView.getRoot().findViewById(R.id.tv_hargaProduk);
            txtJml = itemView.getRoot().findViewById(R.id.tv_quantity);
            txtTotal = itemView.getRoot().findViewById(R.id.tv_total);
            ivImage = itemView.getRoot().findViewById(R.id.profile_image);
            removeItemBtn = itemView.getRoot().findViewById(R.id.removeItem);
        }
    }

    public Cart getItem(int position) {
        return cartList.get(position);
    }


    public void deleteData(int id){
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
                HashMap<String,String> headers = new HashMap<>();

                headers.put("Content-Type","application/x-www-form-urlencoded");
                headers.put("Authorization","Bearer " + token);
                return headers;
            }
        };
        //Disini proses penambahan request yang sudah kita buat ke reuest queue yang sudah dideklarasi
        queue.add(stringRequest);

    }

}
