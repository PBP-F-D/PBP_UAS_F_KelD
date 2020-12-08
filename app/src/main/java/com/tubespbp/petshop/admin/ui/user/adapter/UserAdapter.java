package com.tubespbp.petshop.admin.ui.user.adapter;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
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
import com.tubespbp.petshop.admin.ui.user.model.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.android.volley.Request.Method.DELETE;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {
    Context context;
    private List<User> result;
    SharedPreferences shared;
    int idUser;
    String token;
    private View view;
    private deleteItemListener mListener;


    public UserAdapter(List<User> result) {
        this.result = result;
    }

    public UserAdapter(Context context, List<User> result,
                       deleteItemListener mListener) {
        this.context            = context;
        this.result             = result;
        this.mListener          = mListener;
    }

    public UserAdapter(Context context, List<User> result) {
        this.context            = context;
        this.result             = result;
    }

    public interface deleteItemListener {
        void deleteItem( Boolean delete);
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        view = layoutInflater.inflate(R.layout.item_user, parent, false);

//      Get token from sharedpreferences
        shared = context.getSharedPreferences("getId", Context.MODE_PRIVATE);
        token = shared.getString("token", null);

        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final UserViewHolder holder, int position) {
        final User user = result.get(position);
        NumberFormat formatter = new DecimalFormat("#,###");

        holder.txtNama.setText(user.getName());
        holder.txtEmail.setText(user.getEmail());
        holder.txtCity.setText(user.getCity());
        holder.txtCountry.setText(user.getCountry());
        holder.txtPhone.setText(user.getPhone());

//        Glide.with(context)
//                .load(user.getImg_user())
//                .diskCacheStrategy(DiskCacheStrategy.NONE)
//                .skipMemoryCache(true)
//                .into(holder.ivFoto);
    }

    @Override
    public int getItemCount() {
        return result.size();
    }

    public class UserViewHolder extends RecyclerView.ViewHolder {
        private TextView txtNama, txtEmail, txtCountry, txtCity, txtPhone;
        private ImageView ivFoto;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            txtNama = itemView.findViewById(R.id.tv_nama_user);
            txtEmail = itemView.findViewById(R.id.tv_email);
            txtCountry = itemView.findViewById(R.id.tv_country);
            txtCity = itemView.findViewById(R.id.tv_city);
            txtPhone = itemView.findViewById(R.id.tv_phone);
            ivFoto = itemView.findViewById(R.id.ivFotoUser);
        }

    }
}
