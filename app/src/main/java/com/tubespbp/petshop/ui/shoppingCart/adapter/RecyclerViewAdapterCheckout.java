package com.tubespbp.petshop.ui.shoppingCart.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tubespbp.petshop.databinding.ItemCheckoutBinding;
import com.tubespbp.petshop.ui.shoppingCart.model.Cart;

import java.util.List;

public class RecyclerViewAdapterCheckout extends RecyclerView.Adapter<RecyclerViewAdapterCheckout.CheckoutViewHolder>{

    private Cart cart;
    private Context context;
    private List<Cart> cartList;

    public RecyclerViewAdapterCheckout(Context context, List<Cart> cartList) {
        this.context = context;
        this.cartList = cartList;
    }

    @NonNull
    @Override
    public RecyclerViewAdapterCheckout.CheckoutViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());

        ItemCheckoutBinding binding = ItemCheckoutBinding.inflate(layoutInflater, viewGroup, false);
        return new RecyclerViewAdapterCheckout.CheckoutViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull final CheckoutViewHolder holder, final int position) {
        cart = cartList.get(position);
        holder.itemCheckoutBinding.setCart(cart);
    }

    public class CheckoutViewHolder extends RecyclerView.ViewHolder {
        private ItemCheckoutBinding itemCheckoutBinding;

        public CheckoutViewHolder(ItemCheckoutBinding itemView){
            super(itemView.getRoot());
            itemCheckoutBinding = itemView;
        }
    }

    @Override
    public int getItemCount() {
        return cartList.size();
    }

}
