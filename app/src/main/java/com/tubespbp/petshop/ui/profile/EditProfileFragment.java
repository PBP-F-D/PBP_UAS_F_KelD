package com.tubespbp.petshop.ui.profile;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.Navigation;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
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

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;


public class EditProfileFragment extends Fragment {
    private TextInputLayout nameLayout, phoneLayout, cityLayout, countryLayout;
    private TextInputEditText name, phone, city, country;
    private String nameEdit, phoneEdit, cityEdit, countryEdit;
    private CircleImageView image;
    FragmentEditProfileBinding editProfileBinding;

    SharedPreferences shared;
    int idUser;
    List<User> userList;

    MaterialButton btnEdit;

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    Uri imgUri;

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

        image = root.findViewById(R.id.profile_image_edit);

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

        //Profile image pressed
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                capturePhoto();
            }
        });

        //Button edit profile pressed
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                update(view);
            }
        });
    }

    //Get thumbnail from the photo taken and show it
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Glide.with(getContext())
                    .load(imgUri)
                    .into(image);
        }
    }

    //Camera
    public void capturePhoto() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE,"New Picture");
        values.put(MediaStore.Images.Media.DESCRIPTION, "From the Camera");
        imgUri = getActivity().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        // Create the camera_intent ACTION_IMAGE_CAPTURE. it will open the camera for capture the image
        Intent camera_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        camera_intent.putExtra(MediaStore.EXTRA_OUTPUT, imgUri);
        // Start the activity with camera_intent, and request pic id
        startActivityForResult(camera_intent, REQUEST_IMAGE_CAPTURE);
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
                    Glide.with(getContext())
                            .load(Uri.parse(userList.get(0).getImage()))
                            .into(image);
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
                            .updateUser(nameEdit, phoneEdit, cityEdit, countryEdit, imgUri.toString(), idUser);
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