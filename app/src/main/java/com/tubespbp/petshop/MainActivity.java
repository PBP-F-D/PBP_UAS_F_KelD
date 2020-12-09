package com.tubespbp.petshop;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.messaging.FirebaseMessaging;
import com.tubespbp.petshop.API.ApiClient;
import com.tubespbp.petshop.API.ApiInterface;
import com.tubespbp.petshop.API.User.UserResponse;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    Constant constant;
    SharedPreferences app_preferences, shared;
    int idUser;
    int appTheme;
    int themeColor;
    int appColor;
    private ProgressDialog progressDialog;

    String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        app_preferences = PreferenceManager.getDefaultSharedPreferences(this);
        appColor = app_preferences.getInt("color", 0);
        appTheme = app_preferences.getInt("theme", 0);
        themeColor = appColor;
        constant.color = appColor;

            progressDialog = new ProgressDialog(this);

        if (themeColor == 0){
            setTheme(Constant.theme);
        }else if (appTheme == 0){
            setTheme(Constant.theme);
        }else{
            setTheme(appTheme);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        shared = getSharedPreferences("getId", Context.MODE_PRIVATE);
        idUser = shared.getInt("idUser", -1);
        token = shared.getString("token", null);

        if(idUser != -1) {
            BottomNavigationView navView = findViewById(R.id.nav_view);
            // Passing each menu ID as a set of Ids because each
            // menu should be considered as top level destinations.
            AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                    R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                    .build();
            NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
    //        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
            NavigationUI.setupWithNavController(navView, navController);

            //PUSH NOTIFICATION WITH FIREBASE
            if  (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                String CHANNEL_ID = "Channel 1";
                CharSequence name = "Channel 1";
                String description = "This is Channel 1";
                int importance = NotificationManager.IMPORTANCE_DEFAULT;
                NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
                channel.setDescription(description);
                NotificationManager notificationManager = getSystemService(NotificationManager.class);
                notificationManager.createNotificationChannel(channel);
            }

            FirebaseMessaging.getInstance().subscribeToTopic("news");

            final ImageButton dropdownMenu = findViewById(R.id.btn_dropdown);
            dropdownMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    PopupMenu popup = new PopupMenu(MainActivity.this, dropdownMenu);
                    popup.getMenuInflater()
                            .inflate(R.menu.menu_dropdown, popup.getMenu());

                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        public boolean onMenuItemClick(MenuItem item) {
                            Intent intent;
                            if(item.getItemId()==R.id.menu_location){
                                intent = new Intent(getApplicationContext(), LocationActivity.class);
                                startActivity(intent);
                            }else if (item.getItemId()==R.id.setting_theme){
                                intent = new Intent(getApplicationContext(), ThemeActivity.class);
                                startActivity(intent);
                            } else if(item.getItemId()==R.id.menu_logout) {
                                logout();
                            }
                            return true;
                        }
                    });

                    popup.show();
                }
            });
        } else {
            Intent moveToLogin = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(moveToLogin);
            finish();
        }
    }

    public void logout() {
        progressDialog.setMessage("Logging Out....");
        progressDialog.setProgressStyle(android.app.ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<UserResponse> logout = apiService.logout("Bearer " + token);
        System.out.println("Masuk call response");

        logout.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                //If response's code is 200
                if (response.isSuccessful()) {
                    //Edit sharedpreferences
                    SharedPreferences.Editor editor = shared.edit();
                    editor.putInt("idUser", -1);
                    editor.putString("token", null);
                    editor.apply();
                    Log.d("Id USER LOG OUT", String.valueOf(idUser));
                    Toast.makeText(MainActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    System.out.println("Masuk on response logout");
                    progressDialog.dismiss();

                    Intent moveToLogin = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(moveToLogin);
                    finish();
                } else {  //If response's code is 4xx (error)
                    try {
                        JSONObject error = new JSONObject(response.errorBody().string());
                        Toast.makeText(MainActivity.this, error.optString("message"), Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Logout Failed", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }

}