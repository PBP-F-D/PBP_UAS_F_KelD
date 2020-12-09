package com.tubespbp.petshop.admin.ui.notifications;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Configuration;
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
import androidx.appcompat.view.menu.MenuView;
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
import com.tubespbp.petshop.admin.ui.user.adapter.UserAdapter;
import com.tubespbp.petshop.databinding.ItemCartBinding;
import com.tubespbp.petshop.ui.shoppingCart.ShoppingCartFragment;
import com.tubespbp.petshop.ui.shoppingCart.model.Cart;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.android.volley.Request.Method.DELETE;

public class AdapterCart extends RecyclerView.Adapter<AdapterCart.CartViewHolder> {

    private Cart cart;
    private Context context;
    private List<Cart> cartList;
    SharedPreferences shared;
    int idUser;
    private View view;
    String token;

    public AdapterCart(Context context, List<Cart> cartList) {
        this.context = context;
        this.cartList = cartList;
        notifyDataSetChanged();

    }

    @NonNull
    @Override
    public AdapterCart.CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        view = layoutInflater.inflate(R.layout.item_cart_admin, parent, false);

//      Get token from sharedpreferences
        shared = context.getSharedPreferences("getId", Context.MODE_PRIVATE);
        token = shared.getString("token", null);

        return new AdapterCart.CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final CartViewHolder holder, final int position) {
        final Cart cart = cartList.get(position);

        holder.txtNama.setText(cart.getNamaB());
        holder.txtJml.setText(String.valueOf(cart.getJumlahB()));
        holder.txtId.setText(cart.getIdUser());
        holder.txtTotal.setText(String.valueOf(cart.getTotalB()));
        holder.txtStatus.setText(cart.getStatusB());

        Glide.with(context)
                .load(cart.getImgUrlC())
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(holder.ivImage);
    }

    @Override
    public int getItemCount() {
        return cartList.size();
    }

    public class CartViewHolder extends RecyclerView.ViewHolder {
        private TextView txtId, txtNama, txtJml, txtTotal, txtStatus;
        private ImageView ivImage;

        public CartViewHolder(View itemView) {
            super(itemView);
            txtNama = itemView.findViewById(R.id.tv_namaProduk);
            txtId = itemView.findViewById(R.id.tv_id);
            txtJml = itemView.findViewById(R.id.tv_quantity);
            txtTotal = itemView.findViewById(R.id.tv_total);
            txtStatus = itemView.findViewById(R.id.tv_status);
            ivImage = itemView.findViewById(R.id.img_cart);
        }
    }

    public Cart getItem(int position) {
        return cartList.get(position);
    }

}
