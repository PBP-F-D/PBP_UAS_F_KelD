package com.tubespbp.petshop.ui.shoppingCart;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tubespbp.petshop.R;
import com.tubespbp.petshop.databinding.FragmentCheckoutBinding;
import com.tubespbp.petshop.ui.home.catalog.adapter.RecyclerViewAdapterKatalog;
import com.tubespbp.petshop.ui.shoppingCart.adapter.RecyclerViewAdapterCart;
import com.tubespbp.petshop.ui.shoppingCart.adapter.RecyclerViewAdapterCheckout;
import com.tubespbp.petshop.ui.shoppingCart.model.Cart;

import java.util.ArrayList;

public class CheckoutFragment extends Fragment {
    private static final String KEY_ARRAY = "arraylist";
    private RecyclerView recyclerView;
    private RecyclerViewAdapterCheckout adapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private FragmentCheckoutBinding checkoutBinding;
    private ShoppingCartFragment shoppingCartFragment = new ShoppingCartFragment();
    private TextView totalprice;
    private double total = 0;

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

        checkoutBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_checkout, container, false);
        View view = checkoutBinding.getRoot();

        //Get ArrayList from Bundle
        ArrayList<Cart> cartList = (ArrayList<Cart>) getArguments().getSerializable(KEY_ARRAY);

        //Declare total price textview
        totalprice = view.findViewById(R.id.tv_totalprice);
        for(int i=0; i<cartList.size(); i++) {
            total += cartList.get(i).getTotalB();
        }
        totalprice.setText(Double.toString(total));

        recyclerView = checkoutBinding.rvCheckout;

        adapter = new RecyclerViewAdapterCheckout(getContext(), cartList);
        recyclerView.setAdapter(adapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext()) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };

        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);

        // Inflate the layout for this fragment
        return view;
    }
}