package com.tubespbp.petshop;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.tubespbp.petshop.ui.profile.database.DatabaseClientUser;
import com.tubespbp.petshop.ui.profile.model.User;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUpActivity extends AppCompatActivity {
    TextInputLayout emailLayout, nameLayout, passLayout, phoneLayout, cityLayout, countryLayout;
    TextInputEditText email, name, pass, phone, city, country;
    MaterialButton signUpBtn, cancelBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        //ID
        email = findViewById(R.id.ti_signUp_email);
        name = findViewById(R.id.ti_signUp_name);
        pass = findViewById(R.id.ti_signUp_pass);
        phone = findViewById(R.id.ti_signUp_phone_number);
        city = findViewById(R.id.ti_signUp_city);
        country = findViewById(R.id.ti_signUp_country);
        signUpBtn = findViewById(R.id.btn_signUp_submit);
        cancelBtn = findViewById(R.id.btn_signUp_cancel);


        emailLayout = findViewById(R.id.til_signUp_email);
        nameLayout = findViewById(R.id.til_signUp_name);
        passLayout = findViewById(R.id.til_signUp_pass);
        phoneLayout = findViewById(R.id.til_signUp_phone_number);
        cityLayout = findViewById(R.id.til_signUp_city);
        countryLayout = findViewById(R.id.til_signUp_country);


        //Button Pressed
        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addUser();
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    public static boolean isEmailValid(String email) {
        boolean isValid = false;

        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }

    //Database add user
    private void addUser(){
        final String emailSign = email.getText().toString();
        final String nameSign = name.getText().toString();
        final String passSign = pass.getText().toString();
        final String phoneSign = phone.getText().toString();
        final String citySign = city.getText().toString();
        final String countrySign = country.getText().toString();

        //Input Sign Up Exception
        if(emailSign.isEmpty()) emailLayout.setError("Please enter your email");
        else if (!isEmailValid(emailSign)) emailLayout.setError("Please enter a valid email");
        else emailLayout.setError(null);

        if(nameSign.isEmpty()) nameLayout.setError("Please enter your name");
        else nameLayout.setError(null);

        if(passSign.isEmpty()) passLayout.setError("Please enter your password");
        else passLayout.setError(null);

        if(phoneSign.isEmpty()) phoneLayout.setError("Please enter your phone number");
        else phoneLayout.setError(null);

        if(citySign.isEmpty()) cityLayout.setError("Please enter your city");
        else cityLayout.setError(null);

        if(countrySign.isEmpty()) countryLayout.setError("Please enter your country");
        else countryLayout.setError(null);

        if(isEmailValid(emailSign) && !emailSign.isEmpty() && !nameSign.isEmpty() && !passSign.isEmpty()
                && !phoneSign.isEmpty() && !citySign.isEmpty() && !countrySign.isEmpty()) {
            class AddUser extends AsyncTask<Void, Void, Void> {

                @Override
                protected Void doInBackground(Void... voids) {
                    User user = new User();
                    user.setEmail(emailSign);
                    user.setFullName(nameSign);
                    user.setCity(citySign);
                    user.setCountry(countrySign);
                    user.setPassword(passSign);
                    user.setPhone_number(phoneSign);

                    DatabaseClientUser.getInstance(getApplicationContext()).getDatabaseUser()
                            .signUpDAO()
                            .insert(user);
                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    super.onPostExecute(aVoid);
                    Toast.makeText(getApplicationContext(), "User saved", Toast.LENGTH_SHORT).show();

                }
            }

            AddUser add = new AddUser();
            add.execute();
            Intent moveToLogin = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(moveToLogin);
            finish();
        }
    }
}