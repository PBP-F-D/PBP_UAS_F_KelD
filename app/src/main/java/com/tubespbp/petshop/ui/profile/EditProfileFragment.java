package com.tubespbp.petshop.ui.profile;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.PermissionChecker;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.Navigation;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.textview.MaterialTextView;
import com.google.gson.GsonBuilder;
import com.tubespbp.petshop.API.ApiClient;
import com.tubespbp.petshop.API.ApiInterface;
import com.tubespbp.petshop.API.User.UserResponse;
import com.tubespbp.petshop.LoginActivity;
import com.tubespbp.petshop.R;
import com.tubespbp.petshop.databinding.FragmentEditProfileBinding;
import com.tubespbp.petshop.ui.profile.database.DatabaseClientUser;
import com.tubespbp.petshop.ui.profile.model.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;


public class EditProfileFragment extends Fragment {
    private TextInputLayout nameLayout, phoneLayout, cityLayout, countryLayout;
    private TextInputEditText name, phone, city, country;
    private String nameEdit, phoneEdit, cityEdit, countryEdit;
    private CircleImageView image;
    FragmentEditProfileBinding editProfileBinding;
    private String sIdUser, sName, sEmail, sCountry, sCity, sPhone, sImage;

    SharedPreferences shared;
    int idUser;
    String token;
    List<User> userList;

    MaterialButton btnEdit, btnCancel;

    //Camera
    private static final int PERMISSION_CODE = 1000;
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    Uri imgUri;

    private ProgressDialog progressDialog;

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
        token = shared.getString("token", null);
        Log.d("ID USER Edit Profile", String.valueOf(idUser));

        progressDialog = new ProgressDialog(getContext());

        Intent i = getActivity().getIntent();
        sIdUser = i.getStringExtra("id");
        sName = i.getStringExtra("name");
        sEmail = i.getStringExtra("email");
        sCountry = i.getStringExtra("country");
        sCity = i.getStringExtra("city");
        sPhone = i.getStringExtra("phone");
        sImage = i.getStringExtra("image");

        image = root.findViewById(R.id.profile_image_edit);

        //Fill the field with previous values
        getUsers();

        return root;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        //Get ID
        btnEdit = view.findViewById(R.id.btn_editSubmitProfile);

        name = view.findViewById(R.id.ti_name);
        phone = view.findViewById(R.id.ti_phone_number);
        city = view.findViewById(R.id.ti_city);
        country = view.findViewById(R.id.ti_country);

        nameLayout = view.findViewById(R.id.til_name);
        phoneLayout = view.findViewById(R.id.til_phone_number);
        cityLayout = view.findViewById(R.id.til_city);
        countryLayout = view.findViewById(R.id.til_country);

        btnCancel = view.findViewById(R.id.btn_editCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(
                        R.id.action_editProfileFragment_to_navigation_notifications); //move to profile fragment
            }
        });

        //Profile image pressed
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //if system os is >= marshmallow, request runtime permission
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ActivityCompat.checkSelfPermission(getContext(),
                            android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                            ActivityCompat.checkSelfPermission(getContext(),
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        //request enabling permission
                        String[] permission = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
                        //show popup to request permission
                        requestPermissions(permission, PERMISSION_CODE);
                    } else {
                        //permission granted
                        capturePhoto();
                    }
                } else {
                    //system os < marshmallow
                    capturePhoto();
                }
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
    //handling permission result
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_CODE: {
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    capturePhoto(); // permission from popup was granted
                } else {
                    Toast.makeText(this.getContext(), "Permission Denied", Toast.LENGTH_SHORT).show();
                }
            }
        }
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

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<UserResponse> load = apiService.getUser("Bearer " + token);
        load.enqueue(new Callback<UserResponse>()
        {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                sName = response.body().getUsers().getName();
                sEmail = response.body().getUsers().getEmail();
                sPhone = response.body().getUsers().getPhone();
                sCity = response.body().getUsers().getCity();
                sCountry = response.body().getUsers().getCountry();
                sImage = response.body().getUsers().getPhoto();
                name.setText(sName);
//                email.setText(sEmail);
                phone.setText(sPhone);
                city.setText(sCity);
                country.setText(sCountry);

//                Glide.with(getContext())
//                        .load(Uri.parse(sImage))
//                        .into(image);
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Kesalahan Jaringan", Toast.LENGTH_SHORT).show();
            }
        });

//        class GetUsers extends AsyncTask<Void, Void, List<User>>{
//
//            @Override
//            protected List<User> doInBackground(Void... voids) {
//                userList = DatabaseClientUser
//                        .getInstance(getActivity().getApplicationContext())
//                        .getDatabaseUser()
//                        .signUpDAO()
//                        .getUser(idUser);
//                return userList;
//            }
//
//            @Override
//            protected void onPostExecute(List<User> users) {
//                super.onPostExecute(users);
//                if (users.isEmpty()){
//                    Toast.makeText(getActivity(), "No logged-in user", Toast.LENGTH_SHORT).show();
//                } else {
//                    Glide.with(getContext())
//                            .load(Uri.parse(userList.get(0).getImage()))
//                            .into(image);
//                    editProfileBinding.setUserEdit(userList.get(0));
//                    imgUri = Uri.parse(userList.get(0).getImage());
//                }
//            }
//        }
//        GetUsers get = new GetUsers();
//        get.execute();
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

            progressDialog.setMessage("Updating....");
            progressDialog.setProgressStyle(android.app.ProgressDialog.STYLE_SPINNER);
            progressDialog.show();

            ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
            Call<UserResponse> req = apiService.updateUser(String.valueOf(idUser), nameEdit,
                    sEmail, countryEdit, cityEdit, phoneEdit, "", //TODO: Change to imgUri after photo is replaced with img_user
                    //String.valueOf(imgUri),
                    "Bearer " + token);

            req.enqueue(new Callback<UserResponse>() {
                @Override
                public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                    //If response's code is 200
                    if (response.isSuccessful()) {
                        Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                        Navigation.findNavController(view).navigate(
                                R.id.action_editProfileFragment_to_navigation_notifications); //move to profile fragment
                    } else { //If response's code is 4xx (error)
                        try {
                            JSONObject error = new JSONObject(response.errorBody().string());
                            Toast.makeText(getContext(), error.optString("message"), Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(getContext(), "Update data failed", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                    Log.i("UPDATE", "Msg: " + t.getMessage());
                }
            });
        }
    }

}