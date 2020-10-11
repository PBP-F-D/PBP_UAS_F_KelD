package com.tubespbp.petshop.ui.profile;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.textview.MaterialTextView;
import com.tubespbp.petshop.R;
import com.tubespbp.petshop.databinding.FragmentEditProfileBinding;
import com.tubespbp.petshop.ui.profile.database.DatabaseClientUser;
import com.tubespbp.petshop.ui.profile.model.User;
import com.tubespbp.petshop.ui.shoppingCart.database.DatabaseClient;

import java.util.List;


public class EditProfileFragment extends Fragment {
    private TextInputLayout nameLayout, phoneLayout, cityLayout, countryLayout;
    private TextInputEditText name, phone, city, country;
    private String nameEdit, phoneEdit, cityEdit, countryEdit;
    FragmentEditProfileBinding editProfileBinding;

    SharedPreferences shared;
    int idUser;
    List<User> userList;

    MaterialButton btnEdit;

    public EditProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        editProfileBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_edit_profile, container, false);
        View root = editProfileBinding.getRoot();

        //Get sharepreferences for ID user
        shared = getActivity().getSharedPreferences("getId", Context.MODE_PRIVATE);
        idUser = shared.getInt("idUser", -1);
        Log.d("ID USER Edit Profile", String.valueOf(idUser));

        //Fill the field with previous values
        getUsers();

        return root;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        //Get ID
        btnEdit = view.findViewById(R.id.btn_editSubmit);

        name = view.findViewById(R.id.ti_name);
        phone = view.findViewById(R.id.ti_phone_number);
        city = view.findViewById(R.id.ti_city);
        country = view.findViewById(R.id.ti_country);

        nameLayout = view.findViewById(R.id.til_name);
        phoneLayout = view.findViewById(R.id.til_phone_number);
        cityLayout = view.findViewById(R.id.til_city);
        countryLayout = view.findViewById(R.id.til_country);

        //Button edit profile pressed
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                update(view);
            }
        });
    }

    private void getUsers(){
        class GetUsers extends AsyncTask<Void, Void, List<User>>{

            @Override
            protected List<User> doInBackground(Void... voids) {
                userList = DatabaseClientUser
                        .getInstance(getActivity().getApplicationContext())
                        .getDatabaseUser()
                        .signUpDAO()
                        .getUser(idUser);
                return userList;
            }

            @Override
            protected void onPostExecute(List<User> users) {
                super.onPostExecute(users);
                if (users.isEmpty()){
                    Toast.makeText(getActivity(), "No logged-in user", Toast.LENGTH_SHORT).show();
                } else {
                    editProfileBinding.setUserEdit(userList.get(0));
                }
            }
        }
        GetUsers get = new GetUsers();
        get.execute();
    }

    private void update(View view) {
        //Get value from text fields
        nameEdit = name.getText().toString();
        phoneEdit = phone.getText().toString();
        cityEdit = city.getText().toString();
        countryEdit = country.getText().toString();

        //Input Edit Profile Exception
        if (nameEdit.isEmpty()) nameLayout.setError("Please enter your name");
        else nameLayout.setError(null);

        if (phoneEdit.isEmpty()) phoneLayout.setError("Please enter your phone number");
        else phoneLayout.setError(null);

        if (cityEdit.isEmpty()) cityLayout.setError("Please enter your city");
        else cityLayout.setError(null);

        if (countryEdit.isEmpty()) countryLayout.setError("Please enter your country");
        else countryLayout.setError(null);

        if (!nameEdit.isEmpty() && !phoneEdit.isEmpty()
                && !cityEdit.isEmpty() && !countryEdit.isEmpty()) {

            class UpdateUser extends AsyncTask<Void, Void, Void> {

                @Override
                protected Void doInBackground(Void... voids) {

                    DatabaseClientUser.getInstance(getActivity().getApplicationContext()).getDatabaseUser()
                            .signUpDAO()
                            .updateUser(nameEdit, phoneEdit, cityEdit, countryEdit, idUser);
                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    super.onPostExecute(aVoid);
                    Toast.makeText(getActivity().getApplicationContext(), "User updated", Toast.LENGTH_SHORT).show();
                    Navigation.findNavController(view).navigate(
                            R.id.action_editProfileFragment_to_navigation_notifications); //move to profile fragment
                }
            }

            UpdateUser update = new UpdateUser();
            update.execute();
        }
    }

}