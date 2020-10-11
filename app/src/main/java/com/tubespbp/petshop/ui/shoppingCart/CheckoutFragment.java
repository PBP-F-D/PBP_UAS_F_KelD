package com.tubespbp.petshop.ui.shoppingCart;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tubespbp.petshop.R;
import com.tubespbp.petshop.ui.shoppingCart.model.Cart;

import java.util.ArrayList;
import java.util.List;

public class CheckoutFragment extends Fragment {
//    private static final String KEY_ARRAY = "arraylist";

    //Get list from bundle

    public CheckoutFragment() {
        //Empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_checkout, container, false);

        //Get ArrayList from ShoppingCartFragment's Bundle
//        ArrayList<Cart> cartList = new ArrayList<Cart>();
//        ArrayList<Cart> cartList = getArguments().getSerializable(KEY_ARRAY);
    }
}