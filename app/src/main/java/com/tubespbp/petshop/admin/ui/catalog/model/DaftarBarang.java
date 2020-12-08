package com.tubespbp.petshop.admin.ui.catalog.model;

import java.util.ArrayList;

public class DaftarBarang {
    public ArrayList<Barang> BARANG;

    public DaftarBarang(){
        BARANG = new ArrayList();
        BARANG.add(Dog1);
        BARANG.add(Dog2);
        BARANG.add(Dog3);
        BARANG.add(Cat1);
        BARANG.add(Cat2);
        BARANG.add(Other1);
        BARANG.add(Other2);
        BARANG.add(Other3);
    }

    public static final Barang Dog1 = new Barang("DAILY DELIGHT DOG SAVORY LAMB 375 GR", 29000,
            20, "Dog",
            "http://balipetshop.co.id/upload/item/thumb/w9c1SYZlotb0gKNRRcpR.png");

    public static final Barang Dog2 = new Barang("DAILY DELIGHT DOG SAVORY LAMB AND VEGGY 375 GR", 29000,
            22, "Dog",
            "http://balipetshop.co.id/upload/item/thumb/wBk8VK30LFttWw0bVFK0.png");

    public static final Barang Dog3 = new Barang("DAILY DELIGHT DOG LUSCIOUS BEEF 375 GR", 26000,
            15, "Dog",
            "http://balipetshop.co.id/upload/item/thumb/FtnwXevO6uitBqxeAL1f.png");

    public static final Barang Cat1 = new Barang("DAILY DELIGHT MOUSSE WITH TUNA 80GR", 18000,
            12, "Cat",
            "http://balipetshop.co.id/upload/item/thumb/oGRmy7N6Tj0A9eMFVJG2.png");

    public static final Barang Cat2 = new Barang("DAILY DELIGHT MOUSSE WITH CHICKEN 80 GR", 16000,
            15, "Cat",
            "http://balipetshop.co.id/upload/item/thumb/oGRmy7N6Tj0A9eMFVJG2.png");

    public static final Barang Other1 = new Barang("SHERWOOD PET HEALTH ADULT RABBIT FOOD TIMOTHY PELLET", 15000,
            15, "Other",
            "https://images-na.ssl-images-amazon.com/images/I/61ZLYNTjabL._AC_SL300_.jpg");
    public static final Barang Other2 = new Barang("SMALL PET SELECT RABBIT FOOD PELLETS", 14000,
            20, "Other",
            "https://images-na.ssl-images-amazon.com/images/I/61yxdwChYIL._AC_SL300_.jpg");
    public static final Barang Other3 = new Barang("OMEGA ONE FRESHWATER FLAKES", 13000,
            13, "Other",
            "https://images-na.ssl-images-amazon.com/images/I/71a4GlE9auL._AC_SL1500_.jpg");

}
