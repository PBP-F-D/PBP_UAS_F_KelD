package com.tubespbp.petshop.UnitTest;
import com.tubespbp.petshop.API.User.UserDAO;
import com.tubespbp.petshop.admin.ui.catalog.model.Barang;

public interface CreateCallback {
    void onSuccess(boolean value, String message);
    void onError();
}
