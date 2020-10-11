package com.tubespbp.petshop.ui.profile.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.tubespbp.petshop.ui.profile.model.User;

@Database(entities = {User.class}, version = 1, exportSchema = false)
public abstract class AppDatabaseUser extends RoomDatabase {
    public abstract SignUpDao signUpDAO();
}
