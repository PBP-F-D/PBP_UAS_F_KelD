package com.tubespbp.petshop.UnitTest;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import androidx.navigation.Navigation;

import com.tubespbp.petshop.MainActivity;
import com.tubespbp.petshop.R;


public class ActivityUtil {
    private final Context context;
    private View view;
    public ActivityUtil(Context context) {
        this.context = context;
    }
    public void startCatalogFragment() {
//        context.startActivity(new Intent(context, MainActivity.class));
        Navigation.findNavController(view).navigate(
                R.id.action_addCatalogFragment_to_navigation_catalog);
    }

}

