package com.tubespbp.petshop.ui.shoppingCart.model;


import android.widget.ImageView;

import androidx.databinding.BindingAdapter;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.bumptech.glide.Glide;

import java.io.Serializable;

@Entity
public class Cart implements Serializable {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "namaBarang")
    public String namaB;

    @ColumnInfo(name = "hargaBarang")
    public double hargaB;

    @ColumnInfo(name = "jumlah")
    public int jumlahB;

    @ColumnInfo(name = "totalHarga")
    public double totalB;

    @ColumnInfo(name = "imgUrlCart")
    public String imgUrlC;

    @ColumnInfo(name = "pembeli")
    public String pembeliB;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNamaB() {
        return namaB;
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

    public String getPembeliB() {
        return pembeliB;
    }

    public void setPembeliB(String pembeliB) {
        this.pembeliB = pembeliB;
    }

    @BindingAdapter("cImage")
    public static void loadImage(ImageView view, String imgUrlC) {
        Glide.with(view.getContext())
                .load(imgUrlC)
                .into(view);
    }
}
