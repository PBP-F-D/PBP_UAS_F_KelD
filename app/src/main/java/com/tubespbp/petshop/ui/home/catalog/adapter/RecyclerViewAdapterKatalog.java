package com.tubespbp.petshop.ui.home.catalog.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
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
import com.tubespbp.petshop.API.CatalogAPI;
import com.tubespbp.petshop.LoginActivity;
import com.tubespbp.petshop.R;
import com.tubespbp.petshop.databinding.KatalogBarangBinding;
import com.tubespbp.petshop.ui.home.catalog.model.Barang;
import com.tubespbp.petshop.ui.shoppingCart.model.Cart;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.ContentValues.TAG;
import static com.android.volley.Request.Method.POST;
import static com.mapbox.mapboxsdk.Mapbox.getApplicationContext;

public class RecyclerViewAdapterKatalog extends RecyclerView.Adapter<RecyclerViewAdapterKatalog.KatalogViewHolder> {
    Context context;
    private List<Barang> result;
    SharedPreferences shared;
    int idUser;
    String token;
    private Bitmap bitmap;
    private RecyclerViewAdapterKatalog.deleteItemListener mListener;

    public RecyclerViewAdapterKatalog(List<Barang> result) {
        this.result = result;
    }

    public RecyclerViewAdapterKatalog(Context context, List<Barang> result,
                          RecyclerViewAdapterKatalog.deleteItemListener mListener) {
        this.context            = context;
        this.result             = result;
        this.mListener          = mListener;
    }

    public interface deleteItemListener {
        void deleteItem( Boolean delete);
    }

    @NonNull
    @Override
    public KatalogViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        context = viewGroup.getContext();

        //Get idUser from sharedpreferences
        shared = context.getSharedPreferences("getId", Context.MODE_PRIVATE);
        idUser = shared.getInt("idUser", -1);
        token = shared.getString("token", null);

        KatalogBarangBinding binding = KatalogBarangBinding.inflate(layoutInflater, viewGroup, false);
        return new KatalogViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull final KatalogViewHolder holder, int position) {
        final Barang brg = result.get(position);

        NumberFormat formatter = new DecimalFormat("#,###");
        holder.txtNama.setText(brg.getNama());
        holder.txtHarga.setText(String.valueOf(brg.getHarga()));
//        holder.txtStok.setText(String.valueOf(brg.getStok()));

        Glide.with(context)
                .load(brg.getImgURL())
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(holder.ivFoto);

        holder.btnTambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String namaBarang = holder.txtNama.getText().toString();
                String hargaBarang = holder.txtHarga.getText().toString();

                //Creates alert dialog every time the add button is clicked asking for user's input
                android.app.AlertDialog.Builder alert = new android
                        .app.AlertDialog.Builder(v.getContext())
                        .setTitle("Insert Quantity");

                final EditText input = new EditText(v.getContext());
                input.setInputType(InputType.TYPE_CLASS_NUMBER);
                input.setRawInputType(Configuration.KEYBOARD_12KEY);
                input.setGravity(Gravity.CENTER);
                //Prevent character input length more than 9 characters (also to prevent crash)
                input.setFilters(new InputFilter[] { new InputFilter.LengthFilter(9) });
                alert.setView(input);

                alert.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        if(input.getText().toString().equals(""))
                            return; //prevent crash if input is empty

                        Integer value = Integer.parseInt(String.valueOf(input.getText()));
                        //adds item to cart if input is greater than zero
                        if(value > 0) {
                            String gambar = "";
                            if (bitmap != null){
                                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                                bitmap.compress(Bitmap.CompressFormat.PNG,100,byteArrayOutputStream);
                                byte[] bytes = byteArrayOutputStream.toByteArray();
                                gambar = Base64.encodeToString(bytes, Base64.DEFAULT);
                            }

                            addBarangToCart(holder, value, gambar, namaBarang, hargaBarang);
//                            if(value == 1)
////                                Toast.makeText(holder.itemView.getContext(),value.toString() + "x " + brg.getNama()+" has been added to cart", Toast.LENGTH_SHORT).show();
////                            else
////                                Toast.makeText(holder.itemView.getContext(),value.toString() + "x " + brg.getNama()+"(s) have been added to cart", Toast.LENGTH_SHORT).show();
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
        return result.size();
    }

    public class KatalogViewHolder extends RecyclerView.ViewHolder{
        private TextView txtNama, txtStok, txtHarga, ivEdit, ivHapus;
        private ImageView ivFoto;
        private MaterialButton btnTambah;

        public KatalogViewHolder(KatalogBarangBinding itemView){
            super(itemView.getRoot());
            txtNama    = itemView.getRoot().findViewById(R.id.namaBarang);
            txtStok    = itemView.getRoot().findViewById(R.id.stok);
            txtHarga   = itemView.getRoot().findViewById(R.id.hargaBarang);
            ivFoto     = itemView.getRoot().findViewById(R.id.ivFotoBarang);
            btnTambah  = itemView.getRoot().findViewById(R.id.btn_tambah);
        }
    }

    private void addBarangToCart(@NonNull final KatalogViewHolder holder, final int value,
                                 final String gambar, final String namaBarang, final String hargaBarang){

        int jml =  value;
        RequestQueue queue = Volley.newRequestQueue(context);

        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Loading....");
        progressDialog.setTitle("Adding Item to cart");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(POST, CartAPI.URL_ADD, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Disini bagian jika response jaringan berhasil tidak terdapat ganguan/error
                progressDialog.dismiss();
                try {
                    //Mengubah response string menjadi object
                    JSONObject obj = new JSONObject(response);
                    //obj.getString("message") digunakan untuk mengambil pesan status dari response
                    if (obj.getString("status").equals("Success")) {
                        JSONObject object = new JSONObject(response);

                        //obj.getString("message") digunakan untuk mengambil pesan message dari response
                        Toast.makeText(context, object.getString("message"), Toast.LENGTH_SHORT).show();
                        notifyDataSetChanged();
                    } else {
                        //obj.getString("message") digunakan untuk mengambil pesan message dari response
                        Toast.makeText(context, obj.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                try {
                    byte[] htmlBodyBytes = error.networkResponse.data;
                    Toast.makeText(context, new String(htmlBodyBytes), Toast.LENGTH_SHORT).show();
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
                progressDialog.dismiss();
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                /*
                    Disini adalah proses memasukan/mengirimkan parameter key dengan data value,
                    dan nama key nya harus sesuai dengan parameter key yang diminta oleh jaringan
                    API.
                */
                Map<String, String>  params = new HashMap<String, String>();
                params.put("nama_barang", namaBarang);
                params.put("jmlbeli_barang", String.valueOf(jml));
                params.put("harga_barang", hargaBarang);
                params.put("img_barang", gambar);
                params.put("user_barang", String.valueOf(idUser));
                params.put("status_barang", "Not Paid");

                return params;
            }
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();

                headers.put("Content-Type", "application/form-data");
//                headers.put("Content-Type","application/x-www-form-urlencoded");
                headers.put("Authorization", "Bearer " + token);
                headers.put("abc", "value");
                return headers;
            }
        };

        //Disini proses penambahan request yang sudah kita buat ke reuest queue yang sudah dideklarasi
        queue.add(stringRequest);
    }
}
