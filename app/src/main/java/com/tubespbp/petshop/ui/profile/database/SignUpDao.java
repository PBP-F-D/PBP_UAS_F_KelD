package com.tubespbp.petshop.ui.profile.database;

import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.tubespbp.petshop.ui.profile.model.User;
import com.tubespbp.petshop.ui.shoppingCart.model.Cart;

import java.util.List;

public interface SignUpDao {

    @Query("SELECT * FROM user")
    List<Cart> getAll();

    @Insert
    void insert(User user);

    @Update
    void update(User user);

    @Delete
    void delete(User user);
}

