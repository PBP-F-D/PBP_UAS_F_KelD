package com.tubespbp.petshop.ui.shoppingCart.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.tubespbp.petshop.R;
import com.tubespbp.petshop.databinding.ItemCartBinding;
import com.tubespbp.petshop.databinding.KatalogBarangBinding;
import com.tubespbp.petshop.ui.home.catalog.adapter.RecyclerViewAdapterKatalog;
import com.tubespbp.petshop.ui.home.catalog.model.Barang;
import com.tubespbp.petshop.ui.shoppingCart.model.Cart;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewAdapterCart extends RecyclerView.Adapter< RecyclerViewAdapterCart.CartViewHolder>{

    private Cart cart;
    private Context context;
    private List<Cart> cartList;

    public RecyclerViewAdapterCart(Context context, List<Cart> cartList) {
        this.context = context;
        this.cartList = cartList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerViewAdapterCart.CartViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());

        ItemCartBinding binding = ItemCartBinding.inflate(layoutInflater, viewGroup, false);
        return new RecyclerViewAdapterCart.CartViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        cart = cartList.get(position);
        holder.itemCartBinding.setCart(cart);
//        holder.tvNumber.setText(Integer.toString(user.getNumberU()));
//        holder.tvName.setText(user.getNameU());
//        holder.tvAge.setText(Integer.toString(user.getAgeU()) + " " + "years old");
    }

    @Override
    public int getItemCount() {
        return cartList.size();
    }

    public class CartViewHolder extends RecyclerView.ViewHolder {
        private ItemCartBinding itemCartBinding;

        public CartViewHolder(ItemCartBinding itemView){
            super(itemView.getRoot());
            itemCartBinding = itemView;
        }

    }


}
