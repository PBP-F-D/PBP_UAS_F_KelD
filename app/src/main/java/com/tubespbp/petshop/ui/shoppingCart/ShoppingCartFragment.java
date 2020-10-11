package com.tubespbp.petshop.ui.shoppingCart;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.tubespbp.petshop.R;
import com.tubespbp.petshop.databinding.FragmentShoppingCartBinding;
import com.tubespbp.petshop.ui.shoppingCart.adapter.RecyclerViewAdapterCart;
import com.tubespbp.petshop.ui.shoppingCart.database.DatabaseClient;
import com.tubespbp.petshop.ui.shoppingCart.model.Cart;

import java.util.ArrayList;
import java.util.List;

public class ShoppingCartFragment extends Fragment {

    private ArrayList<Cart> ListCart, newList;
    private RecyclerView recyclerView;
    private RecyclerViewAdapterCart adapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private FragmentShoppingCartBinding shoppingCartBinding;
    private MaterialButton checkOutBtn;

    private ShoppingCartViewModel shoppingCartViewModel;

    public ShoppingCartFragment() {
        // Required empty public constructor
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        shoppingCartBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_shopping_cart, container, false);
        View view = shoppingCartBinding.getRoot();

        recyclerView = shoppingCartBinding.rvFollow;

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);

        getCart();

        checkOutBtn = view.findViewById(R.id.btn_checkout);
        checkOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Get cart list when clicked
                ArrayList<Cart> items = new ArrayList<Cart>();
                for(int i=0; i<adapter.getItemCount(); i++){
                    items.add(adapter.getItem(i));
                }

                //Create a new ArrayList bundle
                Bundle bundle= new Bundle();
                bundle.putSerializable("arraylist", items);

                //Navigate to CheckoutFragment
                if(adapter.getItemCount()>0)
                    Navigation.findNavController(view).navigate(
                            R.id.action_navigation_cart_to_checkoutFragment, bundle);
                else
                    Toast.makeText(getContext(), "Cart is Empty",
                            Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    public void getCart(){
        class GetCart extends AsyncTask<Void, Void, List<Cart>> {

            @Override
            protected void onPostExecute(List<Cart> carts) {
                super.onPostExecute(carts);
                adapter = new RecyclerViewAdapterCart(getContext(), carts);
                recyclerView.setAdapter(adapter);
                if (carts.isEmpty()){
                    Toast.makeText(getContext(), "Empty List", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            protected List<Cart> doInBackground(Void... voids) {
                List<Cart> userCart = DatabaseClient
                        .getInstance(getContext())
                        .getDatabase()
                        .userDAO()
                        .getAll();
                return userCart;
            }
        }

        GetCart get = new GetCart();
        get.execute();
    }

}