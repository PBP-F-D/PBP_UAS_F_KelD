package com.tubespbp.petshop;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

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

    public static final int mode = Activity.MODE_PRIVATE;
    private SharedPreferences shared = getSharedPreferences("getId", mode);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
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
                }
            }

            GetUsers get = new GetUsers();
            get.execute();
            for (int i = 0; i < userList.size(); i++) {
                if (emailLogin.equals(userList.get(i).getEmail()) && passLogin.equals(userList.get(i).getPassword())) {
                    //Edit sharedpreferences (adding user id)
                    SharedPreferences.Editor editor = shared.edit();
                    editor.putInt("idUser", userList.get(i).getId());
                    editor.apply();

                    //Intent move to main activity
                    Intent moveToMain = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(moveToMain);
                }
            }
        } else {
            Toast.makeText(this, "Email/Password is wrong!",Toast.LENGTH_SHORT).show();
        }
    }
}