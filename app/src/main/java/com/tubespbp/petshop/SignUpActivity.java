package com.tubespbp.petshop;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.tubespbp.petshop.ui.profile.database.DatabaseClientUser;
import com.tubespbp.petshop.ui.profile.model.User;

public class SignUpActivity extends AppCompatActivity {
    TextInputLayout numberLayout, nameLayout, ageLayout;
    TextInputEditText fullname,age, number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
    }

    private void addUser(User user){
        final String emailSign = .getText().toString();


        class AddUser extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {
                User user = new User();
                user.setFullName(name);

                DatabaseClientUser.getInstance(getActivity().getApplicationContext()).getDatabaseUser()
                        .signUpDAO()
                        .insert(user);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                Toast.makeText(getApplicationContext(), "User saved", Toast.LENGTH_SHORT).show();
                editText.setText("");
                getUsers();
            }
        }

        AddUser add = new AddUser();
        add.execute();
    }
}