package com.tubespbp.petshop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Activity;
import android.os.Handler;
import android.util.Log;

import com.tubespbp.petshop.admin.AdminActivity;

public class SplashActivity extends Activity {

    Handler handler;
    SharedPreferences shared;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splashfile);

        shared = getSharedPreferences("getId", Context.MODE_PRIVATE);
        int idUser = shared.getInt("idUser", -1);

        handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(idUser == -1) { //if there isn't any logged-in user (idUser == -1), then go to login
                    Log.d("ID USER Splash -1", String.valueOf(idUser));
                    Intent intent= new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                } else if(idUser == 21) {
                    Log.d("ID USER Splash else if ADMIN", String.valueOf(idUser));
                    Intent intent= new Intent(SplashActivity.this, AdminActivity.class);
                    startActivity(intent);
                    finish();

                } else { ////if there's logged-in user (idUser != -1), then go to main
                    Log.d("ID USER Splash else", String.valueOf(idUser));
                    Intent intent= new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        },1500);

    }
}