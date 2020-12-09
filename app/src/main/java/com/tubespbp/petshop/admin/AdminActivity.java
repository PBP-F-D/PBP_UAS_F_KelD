package com.tubespbp.petshop.admin;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.tubespbp.petshop.API.ApiClient;
import com.tubespbp.petshop.API.ApiInterface;
import com.tubespbp.petshop.API.User.UserResponse;
import com.tubespbp.petshop.LocationActivity;
import com.tubespbp.petshop.LoginActivity;
import com.tubespbp.petshop.MainActivity;
import com.tubespbp.petshop.R;
import com.tubespbp.petshop.ThemeActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminActivity extends AppCompatActivity {
    private SharedPreferences shared;
    private ProgressDialog progressDialog;
    String token;
    int idUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_catalog, R.id.navigation_user, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
//        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        //Progress Dialog
        progressDialog = new ProgressDialog(this);

        //GET SharedPreferences
        shared = getSharedPreferences("getId", Context.MODE_PRIVATE);
        idUser = shared.getInt("idUser", -1);
        token = shared.getString("token", null);

        final ImageButton dropdownMenu = findViewById(R.id.btn_dropdown_admin);
        dropdownMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popup = new PopupMenu(AdminActivity.this, dropdownMenu);
                popup.getMenuInflater()
                        .inflate(R.menu.menu_dropdown_admin, popup.getMenu());

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        Intent intent;
                        if(item.getItemId()==R.id.menu_logout) {
                            logout();
                        }
                        return true;
                    }
                });

                popup.show();
            }
        });
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
                    Toast.makeText(AdminActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    System.out.println("Masuk on response logout");
                    progressDialog.dismiss();

                    Intent moveToLogin = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(moveToLogin);
                    finish();
                } else {  //If response's code is 4xx (error)
                    try {
                        JSONObject error = new JSONObject(response.errorBody().string());
                        Toast.makeText(AdminActivity.this, error.optString("message"), Toast.LENGTH_SHORT).show();
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
                Toast.makeText(AdminActivity.this, "Logout Failed", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }

}