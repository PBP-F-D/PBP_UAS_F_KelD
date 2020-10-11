package com.tubespbp.petshop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Activity;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

public class SplashActivity extends Activity {

    Handler handler;
    SharedPreferences shared = getSharedPreferences("getId", Context.MODE_PRIVATE);
    int idUser = shared.getInt("idUser", -1);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splashfile);

        Log.d("ID USER Splash", String.valueOf(idUser));

        handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(idUser == -1) {
                    Intent intent= new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Intent intent= new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        },1500);

    }
}