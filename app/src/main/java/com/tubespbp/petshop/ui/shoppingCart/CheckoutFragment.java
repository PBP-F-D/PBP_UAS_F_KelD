package com.tubespbp.petshop.ui.shoppingCart;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.tubespbp.petshop.Constant;
import com.tubespbp.petshop.MainActivity;
import com.tubespbp.petshop.R;
import com.tubespbp.petshop.databinding.FragmentCheckoutBinding;
import com.tubespbp.petshop.ui.profile.database.DatabaseClientUser;
import com.tubespbp.petshop.ui.profile.model.User;
import com.tubespbp.petshop.ui.shoppingCart.adapter.RecyclerViewAdapterCart;
import com.tubespbp.petshop.ui.shoppingCart.adapter.RecyclerViewAdapterCheckout;
import com.tubespbp.petshop.ui.shoppingCart.model.Cart;

import java.util.ArrayList;
import java.util.List;

public class CheckoutFragment extends Fragment {
    private static final String KEY_ARRAY = "arraylist";
    private RecyclerView recyclerView;
    private RecyclerViewAdapterCheckout adapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private FragmentCheckoutBinding checkoutBinding;
    private ShoppingCartFragment shoppingCartFragment = new ShoppingCartFragment();
    private TextView totalprice, customername;
    private MaterialButton cancelBtn, confirmBtn;
    private double total = 0;

    SharedPreferences shared;
    private List<User> userList;
    int idUser;

    Constant constant;
    SharedPreferences.Editor editor;
    SharedPreferences app_preferences;
    int appTheme;
    int themeColor;
    int appColor;

    public CheckoutFragment() {
        //Empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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


        //Get sharepreferences for ID user
        shared = getActivity().getSharedPreferences("getId", Context.MODE_PRIVATE);
        idUser = shared.getInt("idUser", -1);
        getUsers();

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

        cancelBtn = view.findViewById(R.id.cancelBtn);
        confirmBtn = view.findViewById(R.id.confirmBtn);

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(
                        R.id.action_checkoutFragment_to_navigation_dashboard);
            }
        });

        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Check-out Successful", Toast.LENGTH_SHORT).show();
                for(int i=0; i<cartList.size(); i++) {
                    deleteData(cartList.get(i));
                }
                Navigation.findNavController(view).navigate(
                        R.id.action_checkoutFragment_to_navigation_dashboard);
            }
        });

        return view;
    }

    private void getUsers(){
        class GetUsers extends AsyncTask<Void, Void, List<User>> {

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
                    checkoutBinding.setUser(userList.get(0));
                }
            }
        }
        GetUsers get = new GetUsers();
        get.execute();
    }

    private void deleteData(final Cart cart){
//        class DeleteItem extends AsyncTask<Void, Void, Void> {
//
//            @Override
//            protected Void doInBackground(Void... voids) {
//                DatabaseClient.getInstance(getContext())
//                        .getDatabase()
//                        .userDAO()
//                        .delete(cart);
//                return null;
//            }
//
//            @Override
//            protected void onPostExecute(Void aVoid) {
//                super.onPostExecute(aVoid);
//            }
//        }
//
//        DeleteItem delete = new DeleteItem();
//        delete.execute();
    }

}