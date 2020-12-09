package com.tubespbp.petshop.UnitTest;


import com.tubespbp.petshop.API.User.UserDAO;

public class CreatePresenter {
    private final CreateView view;
    private final CreateService service;
    private CreateCallback callback;

    public CreatePresenter(CreateView view, CreateService service) {
        this.view = view;
        this.service = service;
    }

    public void onCreateClicked() {
        if (view.getNamaBarang().isEmpty()) {
            view.showNamaBarangError("Nama Barang Tidak Boleh Kosong");
            return;
        } else if (view.getStok().isEmpty() ) {
            view.showStokError("Stok Tidak Boleh Kosong");
            return;
        } else if (view.getHarga().isEmpty() ) {
            view.showHargaError("Harga Tidak Boleh Kosong");
            return;
        } else if (view.getKategori().isEmpty() ) {
            view.showKategoriError("Kategori Tidak Boleh Kosong");
            return;
        } else if (view.getUrl().isEmpty() ) {
            view.showUrlError("Url Tidak Boleh Kosong");
            return;
        } else {
            service.addProduct(view, view.getNamaBarang(), Integer.parseInt(view.getStok()), Double.parseDouble(view.getHarga())
                    , view.getKategori(), view.getUrl(), new CreateCallback() {

                @Override
                public void onSuccess(boolean value, String message) {
                    view.startCatalogFragment();
                }

                @Override
                        public void onError() {
                        }
                    });
            return;
        }
    }
}
