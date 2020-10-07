package com.tubespbp.petshop.ui.home.catalog.adapter;

import android.content.Context;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.tubespbp.petshop.R;
import com.tubespbp.petshop.databinding.KatalogBarangBinding;
import com.tubespbp.petshop.ui.home.catalog.model.Barang;
import com.tubespbp.petshop.ui.shoppingCart.database.DatabaseClient;
import com.tubespbp.petshop.ui.shoppingCart.model.Cart;

import java.util.List;

public class RecyclerViewAdapterKatalog extends RecyclerView.Adapter<RecyclerViewAdapterKatalog.KatalogViewHolder> {
    private Context context;
    private List<Barang> result;

    public RecyclerViewAdapterKatalog(List<Barang> result) {
        this.result = result;

    }

    @NonNull
    @Override
    public KatalogViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());

        KatalogBarangBinding binding = KatalogBarangBinding.inflate(layoutInflater, viewGroup, false);
        return new KatalogViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull final KatalogViewHolder holder, int position) {
        Barang brg = result.get(position);
        holder.bind(brg);

        holder.katalogBarangBinding.btnTambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "Button Jalan!", Toast.LENGTH_SHORT).show();
                addBarangToCart(holder);
            }
        });
    }

    @Override
    public int getItemCount() {
        return result.size();
    }

    public class KatalogViewHolder extends RecyclerView.ViewHolder{
        private KatalogBarangBinding katalogBarangBinding;

        public KatalogViewHolder(KatalogBarangBinding itemView){
            super(itemView.getRoot());
            katalogBarangBinding = itemView;
        }
        public void bind(Barang barang) {
            katalogBarangBinding.setBrg(barang);
            katalogBarangBinding.executePendingBindings();
        }
    }

    private void addBarangToCart(@NonNull KatalogViewHolder holder){

        final String name = holder.katalogBarangBinding.namaBarang.getText().toString();
        final double harga =  Double.parseDouble(holder.katalogBarangBinding.hargaBarang.getText().toString());
        final int jml =  1;
        final double total = Double.parseDouble(holder.katalogBarangBinding.hargaBarang.getText().toString()) * jml;
//        final String gmbr = holder.katalogBarangBinding.ivFotoBarang.toString();

        class addBarangToCart extends AsyncTask<Void, Void, Void> {

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                Toast.makeText(context, "Produk ke Keranjang", Toast.LENGTH_SHORT).show();
            }

            @Override
            protected Void doInBackground(Void... voids) {
                Cart cart = new Cart();
                cart.setNamaB(name);
                cart.setHargaB(harga);
                cart.setJumlahB(jml);
                cart.setTotalB(total);
                cart.setImgUrlC("http://balipetshop.co.id/upload/item/thumb/w9c1SYZlotb0gKNRRcpR.png");
                cart.setPembeliB("rastra");


                DatabaseClient.getInstance(context).getDatabase()
                        .userDAO()
                        .insert(cart);
                return null;
            }
        }

        addBarangToCart add = new addBarangToCart();
        add.execute();
    }
}
