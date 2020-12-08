package com.tubespbp.petshop.UnitTest;


public interface CreateView {
    String getNamaBarang();
    String getStok();
    String getHarga();
    String getKategori();
    String getUrl();

    void showNamaBarangError(String message);
    void showStokError(String message);
    void showHargaError(String message);
    void showKategoriError(String message);
    void showUrlError(String message);

    void startCatalogFragment();

    void showCreateError(String message);
    void showErrorResponse(String message);
}
