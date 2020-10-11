package com.tubespbp.petshop.ui.home.catalog;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tubespbp.petshop.R;
import com.tubespbp.petshop.databinding.FragmentCatalogBinding;
import com.tubespbp.petshop.ui.home.HomeFragment;
import com.tubespbp.petshop.ui.home.catalog.adapter.RecyclerViewAdapterKatalog;
import com.tubespbp.petshop.ui.home.catalog.model.Barang;
import com.tubespbp.petshop.ui.home.catalog.model.DaftarBarang;
import com.tubespbp.petshop.ui.shoppingCart.model.Cart;

import java.util.ArrayList;
import java.util.List;

public class CatalogFragment extends Fragment {

    private ArrayList<Barang> ListBarang, newList;
    private RecyclerView recyclerView;
    private RecyclerViewAdapterKatalog adapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private FragmentCatalogBinding catalogBinding, binding;

    public String name;

    public CatalogFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        catalogBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_catalog, container, false);
        View view = catalogBinding.getRoot();

        //Mengambil Bundle dari HomeFragment
        if (getArguments() != null)
            name = getArguments().getString(HomeFragment.name);
        else
            name = "";

        //Daftar Semua List Barang
        ListBarang = new DaftarBarang().BARANG;

        //List barang baru yang dikosongkan
        ArrayList<Barang> newList = new ArrayList<Barang>();

        //Membandingkan nama/kategori supaya dimasukkan ke list baru
        switch(name) {
            case "Dogs":
                for (int i=0; i<ListBarang.size(); i++)
                    if(ListBarang.get(i).getKategori().equals("Dog"))
                        newList.add(ListBarang.get(i));
                break;
            case "Cats":
                for (int i=0; i<ListBarang.size(); i++)
                    if(ListBarang.get(i).getKategori().equals("Cat"))
                        newList.add(ListBarang.get(i));
                break;
            case "Other":
                for (int i=0; i<ListBarang.size(); i++)
                    if(ListBarang.get(i).getKategori().equals("Other"))
                        newList.add(ListBarang.get(i));
                break;
            default:
                break;
        }

        //Recycler View
        recyclerView = catalogBinding.rvKatalog;

        //Menampilkan list barang baru
        adapter = new RecyclerViewAdapterKatalog(newList);
        recyclerView.setAdapter(adapter);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}