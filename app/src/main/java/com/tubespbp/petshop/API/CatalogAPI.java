package com.tubespbp.petshop.API;

public class CatalogAPI {
    public static final String ROOT_URL   = "http://nugaskuy.site/";
    public static final String ROOT_API   = ROOT_URL+ "api/";
    public static final String URL_IMAGE  = ROOT_URL+"images/catalogs/";

    public static final String URL_ADD    = ROOT_API+"catalog";  //POST
    public static final String URL_SELECT = ROOT_API+"catalog";  //GET
    public static final String URL_UPDATE = ROOT_API+"catalog/"; //PUT
    public static final String URL_DELETE = ROOT_API+"catalog/"; //DELETE
}
