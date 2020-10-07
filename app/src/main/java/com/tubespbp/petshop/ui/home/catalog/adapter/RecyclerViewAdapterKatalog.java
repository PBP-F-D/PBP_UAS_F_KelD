package com.tubespbp.petshop.ui.home.catalog.adapter;

import android.content.Context;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tubespbp.petshop.databinding.KatalogBarangBinding;
import com.tubespbp.petshop.ui.home.catalog.model.Barang;
import com.tubespbp.petshop.ui.shoppingCart.database.DatabaseClient;
import com.tubespbp.petshop.ui.shoppingCart.model.Cart;

import java.util.List;

public class RecyclerViewAdapterKatalog extends RecyclerView.Adapter<RecyclerViewAdapterKatalog.KatalogViewHolder> {
    Context context;
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
        final Barang brg = result.get(position);
        holder.bind(brg);

        holder.katalogBarangBinding.btnTambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(),brg.getNama()+" has been added to cart", Toast.LENGTH_SHORT).show();

                //TODO: Update quantity if item already exists in cart instead of adding a new one
//                boolean exists = false;
//                for (int i=0; i<cartList.size(); i++) {
//                    if(cartList.get(i).getNamaB().equals(brg.getNama())) {
//                        exists = true;
//                    }
//                }
//                if (exists == false) {
                    addBarangToCart(holder);
//                }
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

    private void addBarangToCart(@NonNull final KatalogViewHolder holder){

        class addBarangToCart extends AsyncTask<Void, Void, Void> {
            String name = holder.katalogBarangBinding.namaBarang.getText().toString();
            double harga =  Double.parseDouble(holder.katalogBarangBinding.hargaBarang.getText().toString());
            Cart cart;
            int jml =  1;
            final String gmbr = holder.katalogBarangBinding.getBrg().getImgURL();

            double total = Double.parseDouble(holder.katalogBarangBinding.hargaBarang.getText().toString()) * jml;

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
            }

            @Override
            protected Void doInBackground(Void... voids) {
                cart = new Cart();
                cart.setNamaB(name);
                cart.setHargaB(harga);
                cart.setJumlahB(jml);
                cart.setTotalB(total);
                cart.setImgUrlC(gmbr);
                cart.setPembeliB("rastra");

                DatabaseClient.getInstance(holder.itemView.getContext())
                        .getDatabase()
                        .userDAO()
                        .insert(cart);
                return null;
            }
        }

        addBarangToCart add = new addBarangToCart();
        add.execute();
    }
}
