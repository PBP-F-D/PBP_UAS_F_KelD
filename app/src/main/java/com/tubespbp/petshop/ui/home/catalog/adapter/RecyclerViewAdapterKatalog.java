package com.tubespbp.petshop.ui.home.catalog.adapter;

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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tubespbp.petshop.Constant;
import com.tubespbp.petshop.databinding.KatalogBarangBinding;
import com.tubespbp.petshop.ui.home.catalog.model.Barang;
import com.tubespbp.petshop.ui.shoppingCart.database.DatabaseClient;
import com.tubespbp.petshop.ui.shoppingCart.model.Cart;

import java.util.List;

public class RecyclerViewAdapterKatalog extends RecyclerView.Adapter<RecyclerViewAdapterKatalog.KatalogViewHolder> {
    Context context;
    private List<Barang> result;
    SharedPreferences shared;
    int idUser;

    public RecyclerViewAdapterKatalog(List<Barang> result) {
        this.result = result;
    }

    @NonNull
    @Override
    public KatalogViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        context = viewGroup.getContext();

        //Get idUser from sharedpreferences
        shared = context.getSharedPreferences("getId", Context.MODE_PRIVATE);
        idUser = shared.getInt("idUser", -1);

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
                            addBarangToCart(holder, value);
                            if(value == 1)
                                Toast.makeText(holder.itemView.getContext(),value.toString() + "x " + brg.getNama()+" has been added to cart", Toast.LENGTH_SHORT).show();
                            else
                                Toast.makeText(holder.itemView.getContext(),value.toString() + "x " + brg.getNama()+"(s) have been added to cart", Toast.LENGTH_SHORT).show();
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

    private void addBarangToCart(@NonNull final KatalogViewHolder holder, final int value){

        class addBarangToCart extends AsyncTask<Void, Void, Void> {
            String name = holder.katalogBarangBinding.namaBarang.getText().toString();
            double harga =  Double.parseDouble(holder.katalogBarangBinding.hargaBarang.getText().toString());
            Cart cart;
            int jml =  value;
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
                cart.setIdUser(idUser);
                cart.setHargaB(harga);
                cart.setJumlahB(jml);
                cart.setTotalB(total);
                cart.setImgUrlC(gmbr);

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
