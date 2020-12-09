package com.tubespbp.petshop.ui.shoppingCart.model;


import android.widget.ImageView;

import androidx.databinding.BindingAdapter;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.bumptech.glide.Glide;

import java.io.Serializable;

import de.hdodenhof.circleimageview.CircleImageView;

public class Cart implements Serializable {
    public int id;
    public String idUser;
    public String namaB;
    public double hargaB;
    public int jumlahB;
    public double totalB;

    public String statusB;
    public String imgUrlC;

    public Cart() {
        //
    }

    public Cart(int id, String idUser, String namaB, Double hargaB, int jumlahB, String statusB, String imgUrlC) {
        this.id = id;
        this.idUser = idUser;
        this.namaB = namaB;
        this.hargaB = hargaB;
        this.jumlahB = jumlahB;
        this.statusB = statusB;
        this.imgUrlC = imgUrlC;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) { this.idUser = idUser; }

    public String getNamaB() {
        return namaB;
    }

    public String getStatusB() {
        return statusB;
    }

    public void setStatusB(String statusB) {
        this.statusB = statusB;
    }

    public void setNamaB(String namaB) {
        this.namaB = namaB;
    }

    public double getHargaB() {
        return hargaB;
    }

    public void setHargaB(double hargaB) {
        this.hargaB = hargaB;
    }

    public int getJumlahB() {
        return jumlahB;
    }

    public void setJumlahB(int jumlahB) {
        this.jumlahB = jumlahB;
    }

    public double getTotalB() {
        return totalB;
    }

    public void setTotalB(double totalB) {
        this.totalB = totalB;
    }

    public String getImgUrlC() {
        return imgUrlC;
    }

    public void setImgUrlC(String imgUrlC) {
        this.imgUrlC = imgUrlC;
    }

    @BindingAdapter("cImage")
    public static void loadImage(CircleImageView circleImageView, String imgUrlC) {
        Glide.with(circleImageView.getContext())
                .load(imgUrlC)
                .into(circleImageView);
    }
}
