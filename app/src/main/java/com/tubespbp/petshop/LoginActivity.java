package com.tubespbp.petshop;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.textview.MaterialTextView;
import com.tubespbp.petshop.ui.profile.database.DatabaseClientUser;
import com.tubespbp.petshop.ui.profile.model.User;

import java.util.List;

public class LoginActivity extends AppCompatActivity {
    MaterialTextView clickHere;
    MaterialButton login;
    TextInputLayout emailLayout, passLayout;
    TextInputEditText email, pass;
    List<User> userList;
    int idUser, currentIdUser =-1;

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

        if(!emailLogin.isEmpty() || !passLogin.isEmpty()) {
            class GetUsers extends AsyncTask<Void, Void, List<User>> {

                @Override
                protected List<User> doInBackground(Void... voids) {
                    userList = DatabaseClientUser
                            .getInstance(getApplicationContext())
                            .getDatabaseUser()
                            .signUpDAO()
                            .getAll();
                    return userList;
                }

                @Override
                protected void onPostExecute(List<User> users) {
                    super.onPostExecute(users);
                    for (int i = 0; i < userList.size(); i++) {
                        if (emailLogin.equals(userList.get(i).getEmail()) && passLogin.equals(userList.get(i).getPassword())) {
                            //Edit sharedpreferences (adding user id)
                            SharedPreferences.Editor editor = shared.edit();
                            editor.putInt("idUser", userList.get(i).getId());
                            currentIdUser = userList.get(i).getId(); //save id user to compare it with the next if
                            editor.apply();

                            Log.d("ID USER LOGIN:", String.valueOf(userList.get(i).getId()));

                            //Intent move to main activity
                            Intent moveToMain = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(moveToMain);
                            finish();
                        }
                    }
                    if(currentIdUser == -1) { //if currentIdUser still -1, that means no user matched
                        Toast.makeText(getApplicationContext(), "Email/Password is wrong!",Toast.LENGTH_SHORT).show();
                    }
                }
            }

            GetUsers get = new GetUsers();
            get.execute();

        } else {
            Toast.makeText(this, "Email/Password is wrong!",Toast.LENGTH_SHORT).show();
        }
    }
}