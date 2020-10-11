package com.tubespbp.petshop.ui.profile.database;

import android.content.Context;

import androidx.room.Room;

public class DatabaseClientUser {
    private Context context;
    private static DatabaseClientUser databaseClientUser;

    private AppDatabaseUser database;

    private DatabaseClientUser(Context context){
        this.context = context;
        database = Room.databaseBuilder(context, AppDatabaseUser.class, "user").build();
    }

    public static synchronized DatabaseClientUser getInstance(Context context){
        if (databaseClientUser ==null){
            databaseClientUser = new DatabaseClientUser(context);
        }
        return databaseClientUser;
    }

    public AppDatabaseUser getDatabaseUser() {return database; }
}
