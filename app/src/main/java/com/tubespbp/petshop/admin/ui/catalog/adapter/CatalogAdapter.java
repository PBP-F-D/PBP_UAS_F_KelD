package com.tubespbp.petshop.admin.ui.catalog.adapter;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.tubespbp.petshop.R;
import com.tubespbp.petshop.admin.API.CatalogAPI;
import com.tubespbp.petshop.admin.ui.catalog.model.Barang;


import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

import static com.android.volley.Request.Method.DELETE;

public class CatalogAdapter extends RecyclerView.Adapter<CatalogAdapter.KatalogViewHolder> {
    Context context;
    private List<Barang> result;
    SharedPreferences shared;
    int idUser;
    String token;
    private View view;
    private deleteItemListener mListener;

    public CatalogAdapter(List<Barang> result) {
        this.result = result;
    }

    public CatalogAdapter(Context context, List<Barang> result,
                          deleteItemListener mListener) {
        this.context            = context;
        this.result             = result;
        this.mListener          = mListener;
    }

    public interface deleteItemListener {
        void deleteItem( Boolean delete);
    }

    @NonNull
    @Override
    public KatalogViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        view = layoutInflater.inflate(R.layout.item_catalog, parent, false);

//        Get idUser from sharedpreferences
        shared = context.getSharedPreferences("getId", Context.MODE_PRIVATE);
        idUser = shared.getInt("idUser", -1);
        token = shared.getString("token", null);
        return new KatalogViewHolder(view);

//        KatalogBarangBinding binding = KatalogBarangBinding.inflate(layoutInflater, viewGroup, false);
    }

    @Override
    public void onBindViewHolder(@NonNull final KatalogViewHolder holder, int position) {
        final Barang brg = result.get(position);
        NumberFormat formatter = new DecimalFormat("#,###");
        holder.txtNama.setText(brg.getNama());
        holder.txtHarga.setText(String.valueOf(brg.getHarga()));
        holder.txtStok.setText(String.valueOf(brg.getStok()));
        holder.txtKategori.setText(brg.getKategori());

        Glide.with(context)
                .load(brg.getImgURL())
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(holder.ivFoto);

        holder.ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Delete this product?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteProduct(brg.getIdBarang());
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        notifyDataSetChanged();
                        dialog.cancel();
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });

        holder.ivEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(
                        R.id.action_navigation_catalog_to_editCatalogFragment);
            }
        });
    }

    @Override
    public int getItemCount() {
        return result.size();
    }

    public class KatalogViewHolder extends RecyclerView.ViewHolder{
        private TextView txtNama, txtStok, txtHarga, txtKategori;
        private ImageView ivFoto, ivEdit, ivDelete;

        public KatalogViewHolder(@NonNull View itemView){
            super(itemView);
            txtNama    = itemView.findViewById(R.id.namaBarang);
            txtStok    = itemView.findViewById(R.id.stok);
            txtHarga   = itemView.findViewById(R.id.hargaBarang);
            txtKategori = itemView.findViewById(R.id.category);
            ivFoto     = itemView.findViewById(R.id.fotoBarang);
            ivEdit     = itemView.findViewById(R.id.btnEditCatalog);
            ivDelete     = itemView.findViewById(R.id.btnDeleteCatalog);
        }
//        private TextInputEditText tiNama, tiStok, tiHarga, tiUrl, tiKategori;
//        private TextInputLayout tilNama, tilStok, tilHarga, tilUrl, tilKategori;
//        private MaterialButton btnCancel, btnSubmit;
    }

    //Fungsi menghapus data mahasiswa
    public void deleteProduct(int id){
        //Pendeklarasian queue
        RequestQueue queue = Volley.newRequestQueue(context);

        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("loading....");
        progressDialog.setTitle("Delete a product");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        //Memulai membuat permintaan request menghapus data ke jaringan
        StringRequest stringRequest = new StringRequest(DELETE, CatalogAPI.URL_UPDATE + id, new Response.Listener<String>() {
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
        });

        //Disini proses penambahan request yang sudah kita buat ke reuest queue yang sudah dideklarasi
        queue.add(stringRequest);
    }
}
