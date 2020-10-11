package com.tubespbp.petshop.ui.shoppingCart.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.tubespbp.petshop.ui.shoppingCart.model.Cart;

@Database(entities = {Cart.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract UserDao userDAO();
}
