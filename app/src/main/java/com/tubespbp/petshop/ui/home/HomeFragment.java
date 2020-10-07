package com.tubespbp.petshop.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.Navigation;

import com.tubespbp.petshop.R;
import com.tubespbp.petshop.ui.home.catalog.CatalogFragment;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private Fragment fragment;
    private FragmentManager mFragmentManager;

    public static final String name = "category";

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

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