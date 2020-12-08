package com.tubespbp.petshop.admin.ui.catalog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.google.android.material.button.MaterialButton;
import com.tubespbp.petshop.R;


public class EditCatalogFragment extends Fragment {
    View view;
    MaterialButton btnCancel, btnAdd;
    String selectedCategory;

    public static EditCatalogFragment newInstance(String param1, String param2) {
        EditCatalogFragment fragment = new EditCatalogFragment();

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_edit_catalog, container, false);
        // Inflate the layout for this fragment
        btnCancel = view.findViewById(R.id.btn_editCancel);
        btnAdd = view.findViewById(R.id.btn_editSubmit);

        //Isi dropdown Categories
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.categories, android.R.layout.simple_spinner_item);
        final AutoCompleteTextView categoriesDropdown = view.findViewById(R.id.ti_kategori);
        categoriesDropdown.setText(selectedCategory);
        categoriesDropdown.setAdapter(adapter);
        categoriesDropdown.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                selectedCategory = categoriesDropdown.getEditableText().toString();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(
                        R.id.action_editCatalogFragment_to_navigation_catalog);
            }
        });
        return view;

    }
}