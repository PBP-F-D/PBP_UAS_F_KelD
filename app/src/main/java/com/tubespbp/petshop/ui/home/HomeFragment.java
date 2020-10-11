package com.tubespbp.petshop.ui.home;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.Navigation;

import com.tubespbp.petshop.Constant;
import com.tubespbp.petshop.MainActivity;
import com.tubespbp.petshop.R;
import com.tubespbp.petshop.ui.home.catalog.CatalogFragment;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private Fragment fragment;
    private FragmentManager mFragmentManager;

    Constant constant;
    SharedPreferences.Editor editor;
    SharedPreferences app_preferences;
    int appTheme;
    int themeColor;
    int appColor;

    public static final String name = "category";

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        MainActivity main = (MainActivity)getActivity();

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

        View root;
        root = inflater.inflate(
                R.layout.fragment_home,
                container,
                false);

        CardView Dogs = root.findViewById(R.id.cardDogs);
        CardView Cats = root.findViewById(R.id.cardCats);
        CardView Other = root.findViewById(R.id.cardOther);

        Dogs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String category = "Dogs";

                Bundle bundle= new Bundle();
                bundle.putString(name, category);
                Navigation.findNavController(v).navigate(
                        R.id.action_navigation_home_to_catalogFragment2, bundle);
            }
        });

        Cats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String category = "Cats";

                Bundle bundle= new Bundle();
                bundle.putString(name, category);
                Navigation.findNavController(v).navigate(
                        R.id.action_navigation_home_to_catalogFragment2, bundle);
            }
        });

        Other.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String category = "Other";

                Bundle bundle= new Bundle();
                bundle.putString(name, category);
                Navigation.findNavController(v).navigate(
                        R.id.action_navigation_home_to_catalogFragment2, bundle);
            }
        });
        return root;
    }
}