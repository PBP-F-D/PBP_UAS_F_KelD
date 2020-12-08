package com.tubespbp.petshop.admin.ui.user.model;

import androidx.databinding.BindingAdapter;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.bumptech.glide.Glide;

import java.io.Serializable;

import de.hdodenhof.circleimageview.CircleImageView;

@Entity
public class User implements Serializable {

    public int id;
    public String email;
    public String name;
    public String phone;
    public String city;
    public String country;
    public String img_user;

    public User(int id, String email, String name, String phone, String city, String country, String img_user) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.phone = phone;
        this.city = city;
        this.country = country;
        this.img_user = img_user;
    }

    public User(String email, String name, String phone, String city, String country, String img_user) {
        this.email = email;
        this.name = name;
        this.phone = phone;
        this.city = city;
        this.country = country;
        this.img_user = img_user;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getImg_user() {
        return img_user;
    }

    public void setImg_user(String img_user) {
        this.img_user = img_user;
    }
}


