package com.tubespbp.petshop;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.textview.MaterialTextView;
import com.google.gson.GsonBuilder;
import com.tubespbp.petshop.API.ApiClient;
import com.tubespbp.petshop.API.ApiInterface;
import com.tubespbp.petshop.API.User.UserDAO;
import com.tubespbp.petshop.API.User.UserResponse;
import com.tubespbp.petshop.ui.profile.ProfileFragment;
import com.tubespbp.petshop.ui.profile.model.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

public class LoginActivity extends AppCompatActivity {
    MaterialTextView clickHere;
    MaterialButton login;
    TextInputLayout emailLayout, passLayout;
    TextInputEditText email, pass;
    List<User> userList;
    int idUser, currentIdUser =-1;
    private ProgressDialog progressDialog;
    private Intent i = null;

    public static final int mode = Activity.MODE_PRIVATE;
    private SharedPreferences shared;

    Constant constant;
    SharedPreferences.Editor editor;
    SharedPreferences app_preferences;
    int appTheme;
    int themeColor;
    int appColor;

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
        setContentView(R.layout.activity_login);
        shared = getSharedPreferences("getId", mode);
        idUser = shared.getInt("idUser", -1);

        Log.d("ID USER BEFORE LOGIN:", String.valueOf(idUser));
        //ID
        clickHere = findViewById(R.id.clickHere);
        login = findViewById(R.id.btn_login);
        email = findViewById(R.id.ti_login_email);
        pass = findViewById(R.id.ti_login_pass);

        emailLayout = findViewById(R.id.til_login_email);
        passLayout = findViewById(R.id.til_login_pass);

        //Click here to sign up text link
        clickHere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent moveToSignUp = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(moveToSignUp);
            }
        });

        //Button Login clicked
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });
    }

    private void login() {
        final String emailLogin = email.getText().toString();
        final String passLogin = pass.getText().toString();

        if(TextUtils.isEmpty(emailLogin)) {
            email.setError("Email field is empty!");
            return;
        } else if(TextUtils.isEmpty(passLogin)) {
            pass.setError("Password field is empty");
            return;
        } else {
            progressDialog.show();
            ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
            Call<UserResponse> req = apiService.login(
                    email.getText().toString(),
                    pass.getText().toString());

            req.enqueue(new Callback<UserResponse>() {
                @Override
                public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {

                    if(response.body().getMessage().equals("Authenticated")) {
                        Toast.makeText(LoginActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                        UserDAO user = response.body().getUsers().get(0);
                        if(user.getEmail().equalsIgnoreCase("admin")) {
                            i = new Intent(LoginActivity.this, MainActivity.class);
                        } else {
                            i = new Intent(LoginActivity.this, MainActivity.class);
                            i.putExtra("id", user.getId());
                            finish();
                        }

                        startActivity(i);
                        SharedPreferences.Editor editor = shared.edit();

                        editor.putString("id", user.getId());
                        editor.apply();
                    } else {
                        Toast.makeText(LoginActivity.this, "Username / Password Salah", Toast.LENGTH_SHORT).show();
                    }

                    progressDialog.dismiss();
                }

                @Override
                public void onFailure(Call<UserResponse> call, Throwable t) {
                    Toast.makeText(LoginActivity.this, "Gagal Login", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                    Log.i("LOGIN", "Msg: " + t.getMessage());
                }
            });
        }
    }
}