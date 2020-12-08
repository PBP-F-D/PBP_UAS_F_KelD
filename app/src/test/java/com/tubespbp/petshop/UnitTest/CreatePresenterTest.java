package com.tubespbp.petshop.UnitTest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CreatePresenterTest {

    @Mock
    private CreateView view;
    @Mock
    private CreateService service;
    private CreatePresenter presenter;
    @Before
    public void setUp() throws Exception {
        presenter = new CreatePresenter(view, service);
    }
    @Test
    public void shouldShowErrorMessageWhenNamaBarangIsEmpty() throws Exception {
        when(view.getNamaBarang()).thenReturn("");
        System.out.println("Nama Barang : "+view.getNamaBarang());
        presenter.onCreateClicked();
        verify(view).showNamaBarangError("Nama Barang Tidak Boleh Kosong");
    }
    @Test
    public void shouldShowErrorMessageWhenStokIsEmpty() throws Exception {
        when(view.getNamaBarang()).thenReturn("Daily Fresh Dog Food");
        System.out.println("Nama Barang : "+ view.getNamaBarang());
        when(view.getStok()).thenReturn("");
        System.out.println("Stok : "+view.getStok());
        presenter.onCreateClicked();
        verify(view).showStokError("Stok Tidak Boleh Kosong");
    }
    @Test
    public void shouldShowErrorMessageWhenHargaIsEmpty() throws Exception {
        when(view.getNamaBarang()).thenReturn("Daily Fresh Dog Food");
        System.out.println("Nama Barang : "+ view.getNamaBarang());
        when(view.getStok()).thenReturn("20");
        System.out.println("Stok : "+ view.getStok());
        when(view.getHarga()).thenReturn("");
        System.out.println("Harga : "+view.getHarga());
        presenter.onCreateClicked();
        verify(view).showStokError("Harga Tidak Boleh Kosong");
    }

    @Test
    public void shouldShowErrorMessageWhenKategoriIsEmpty() throws Exception {
        when(view.getNamaBarang()).thenReturn("Daily Fresh Dog Food");
        System.out.println("Nama Barang : "+ view.getNamaBarang());
        when(view.getStok()).thenReturn("20");
        System.out.println("Stok : "+ view.getStok());
        when(view.getHarga()).thenReturn("2000");
        System.out.println("Harga : "+ view.getHarga());
        when(view.getKategori()).thenReturn("");
        System.out.println("Kategori : "+view.getKategori());
        presenter.onCreateClicked();
        verify(view).showStokError("Kategori Tidak Boleh Kosong");
    }

    @Test
    public void shouldShowErrorMessageWhenUrlIsEmpty() throws Exception {
        when(view.getNamaBarang()).thenReturn("Daily Fresh Dog Food");
        System.out.println("Nama Barang : "+ view.getNamaBarang());
        when(view.getStok()).thenReturn("20");
        System.out.println("Stok : "+ view.getStok());
        when(view.getHarga()).thenReturn("20000");
        System.out.println("Harga : "+ view.getHarga());
        when(view.getKategori()).thenReturn("Dog");
        System.out.println("Kategori : "+view.getKategori());
        when(view.getUrl()).thenReturn("");
        System.out.println("Url : "+view.getUrl());
        presenter.onCreateClicked();
        verify(view).showStokError("Url Tidak Boleh Kosong");
    }
    @Test
    public void shouldStartCatalogFragmentWhenAllDataAreCorrect() throws
            Exception {
        when(view.getNamaBarang()).thenReturn("Purina Dog Chow");
        System.out.println("Nama Barang :" + view.getNamaBarang());
        when(view.getStok()).thenReturn("20");
        System.out.println("Stok : "+view.getStok());
        when(view.getHarga()).thenReturn("20000");
        System.out.println("Harga : "+view.getHarga());
        when(view.getKategori()).thenReturn("Dog");
        System.out.println("Kategori : "+view.getKategori());
        when(view.getUrl()).thenReturn("https://media.tractorsupply.com/is/image/TractorSupplyCompany/5048134?$456$");
        System.out.println("Url : "+view.getUrl());
        when(service.getValid(view, view.getNamaBarang(), Integer.parseInt(view.getStok()), Double.parseDouble(view.getHarga()), view.getKategori(),
                view.getUrl())).thenReturn(true);
        System.out.println("Hasil : "+service.getValid(view, view.getNamaBarang(), Integer.parseInt(view.getStok()), Double.parseDouble(view.getHarga()), view.getKategori(),
                view.getUrl()));
        presenter.onCreateClicked();
        //verify(view).startMainActivity();
    }

}