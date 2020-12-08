package com.tubespbp.petshop.admin.ui.catalog.model;

import java.io.Serializable;

public class Barang implements Serializable {

    private int idBarang;
    public String nama;
    public double harga;
    public int stok;
    public String kategori;
    public String imgURL;

    public Barang(String nama, double harga, int stok, String kategori, String imgURL) {
        this.nama = nama;
        this.harga = harga;
        this.stok = stok;
        this.kategori = kategori;
        this.imgURL = imgURL;
    }

    public Barang(int idBarang, String nama, double harga, int stok, String kategori, String imgURL) {
        this.idBarang = idBarang;
        this.nama = nama;
        this.harga = harga;
        this.stok = stok;
        this.kategori = kategori;
        this.imgURL = imgURL;
    }

    public int getIdBarang() {
        return idBarang;
    }

    public void setIdBarang(int idBarang) {
        this.idBarang = idBarang;
    }


    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public double getHarga() {
        return harga;
    }

    public void setHarga(double harga) {
        this.harga = harga;
    }

    public int getStok() {
        return stok;
    }

    public void setStok(int stok) {
        this.stok = stok;
    }

    public String getKategori() {
        return kategori;
    }

    public void setKategori(String kategori) {
        this.kategori = kategori;
    }

    public String getImgURL() {
        return imgURL;
    }

    public void setImgURL(String imgURL) {
        this.imgURL = imgURL;
    }

//    @BindingAdapter("pImage")
//    public static void loadImage(ImageView view, String imgURL) {
//        Glide.with(view.getContext())
//                .load(imgURL)
//                .into(view);
//    }
}
