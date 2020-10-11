package com.tubespbp.petshop.ui.shoppingCart.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.tubespbp.petshop.ui.shoppingCart.model.Cart;

import java.util.List;

@Dao
public interface UserDao {

    @Query("SELECT * FROM cart")
    List<Cart> getAll();

    @Query("SELECT * FROM cart WHERE idUser = :idUser")
    List<Cart> getUserCart(int idUser);

    @Insert
    void insert(Cart cart);

    @Update
    void update(Cart cart);

    @Delete
    void delete(Cart cart);
}
