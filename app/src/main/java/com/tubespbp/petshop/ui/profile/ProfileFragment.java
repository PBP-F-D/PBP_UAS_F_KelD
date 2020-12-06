package com.tubespbp.petshop.ui.profile;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;
import com.google.android.material.textview.MaterialTextView;
import com.tubespbp.petshop.Constant;
import com.tubespbp.petshop.MainActivity;
import com.tubespbp.petshop.R;
import com.tubespbp.petshop.databinding.FragmentProfileBinding;
import com.tubespbp.petshop.ui.profile.database.DatabaseClientUser;
import com.tubespbp.petshop.ui.profile.model.User;

import java.nio.ByteBuffer;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment {

    private ProfileViewModel profileViewModel;
    private MaterialTextView email, name, username, phone, city, country;
    private CircleImageView image;
    private List<User> userList;
    private String sIdUser, sName, sEmail, sCountry, sCity, sPhone, sGambar;
    Bitmap[] array;

    SharedPreferences shared;
    int idUser;

    Constant constant;
    SharedPreferences.Editor editor;
    SharedPreferences app_preferences;
    int appTheme;
    int themeColor;
    int appColor;

    FragmentProfileBinding profileBinding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        profileViewModel = ViewModelProviders.of(this).get(ProfileViewModel.class);
        profileBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false);
        View root = profileBinding.getRoot();

        MainActivity main = (MainActivity)getActivity();

        //Get Theme
        app_preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        appColor = app_preferences.getInt("color", 0);
        appTheme = app_preferences.getInt("theme", 0);
        themeColor = appColor;
        constant.color = appColor;

        if (themeColor == 0){
            main.setTheme(Constant.theme);
        }else if (appTheme == 0){
            main.setTheme(Constant.theme);
        }else{
            main.setTheme(appTheme);
        }

        //Get sharepreferences for ID user
        shared = getActivity().getSharedPreferences("getId", Context.MODE_PRIVATE);
        idUser = shared.getInt("idUser", -1);
        Log.d("ID USER Profile", String.valueOf(idUser));

//        name = root.findViewById(R.id.tv_nama);
//        email = root.findViewById(R.id.et_email);
//        phone = root.findViewById(R.id.et_phone);
//        country = root.findViewById(R.id.et_country);
//        city = root.findViewById(R.id.et_city);
        image = root.findViewById(R.id.profile_image_profile);

//        sIdUser = getArguments().getString("id", "");
//        sName = getArguments().getString("name", "");
//        sEmail = getArguments().getString("email", "");
//        sCountry = getArguments().getString("country", "");
//        sCity = getArguments().getString("city", "");
//        sPhone = getArguments().getString("phone", "");
//        sGambar = getArguments().getString("image", "");
//
//        name.setText(sName);
//        email.setText(sEmail);
//        country.setText(sCountry);
//        city.setText(sCity);
//        phone.setText(sPhone);
//
//        Glide.with(getContext())
//                .load(Uri.parse(sGambar))
//                .into(image);

        getUsers();

        Button btnEdit = root.findViewById(R.id.btn_editProfile);
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(
                        R.id.action_navigation_notifications_to_editProfileFragment);
            }
        });
        return root;
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

                    profileBinding.setUser(userList.get(0));

                }
            }
        }
        GetUsers get = new GetUsers();
        get.execute();
    }
}