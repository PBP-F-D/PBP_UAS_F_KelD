package com.tubespbp.petshop.API.User;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UserResponse {
    @SerializedName("user")
    @Expose
    private UserDAO users = null;

    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("access_token")
    @Expose
    private String token;

    public UserDAO getUsers()
    {
        return users;
    }

    public String getMessage()
    {
        return message;
    }

    public void setUsers(UserDAO users)
    {
        this.users = users;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
