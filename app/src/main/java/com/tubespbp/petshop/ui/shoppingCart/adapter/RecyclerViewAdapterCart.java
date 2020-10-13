package com.tubespbp.petshop.ui.shoppingCart.adapter;

import android.content.Context;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.tubespbp.petshop.R;
import com.tubespbp.petshop.databinding.ItemCartBinding;
import com.tubespbp.petshop.ui.shoppingCart.ShoppingCartFragment;
import com.tubespbp.petshop.ui.shoppingCart.database.DatabaseClient;
import com.tubespbp.petshop.ui.shoppingCart.model.Cart;

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
    public void onBindViewHolder(@NonNull final CartViewHolder holder, final int position) {
        cart = cartList.get(position);
        holder.itemCartBinding.setCart(cart);

        holder.removeItemBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //deletes item from database (still crashing)
                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                deleteData(cartList.get(position), holder);

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
        private ItemCartBinding itemCartBinding;
        private MaterialButton removeItemBtn;

        public CartViewHolder(ItemCartBinding itemView){
            super(itemView.getRoot());
            itemCartBinding = itemView;
            removeItemBtn = itemCartBinding.getRoot().findViewById(R.id.removeItem);
        }

    }

    public Cart getItem(int position) {
        return cartList.get(position);
    }

    private void deleteData(final Cart cart, final CartViewHolder holder){
        class DeleteItem extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {
                DatabaseClient.getInstance(holder.itemView.getContext())
                        .getDatabase()
                        .userDAO()
                        .delete(cart);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                Toast.makeText(holder.itemView.getContext(), "Item removed from cart", Toast.LENGTH_SHORT).show();
            }
        }

        DeleteItem delete = new DeleteItem();
        delete.execute();
    }

}
