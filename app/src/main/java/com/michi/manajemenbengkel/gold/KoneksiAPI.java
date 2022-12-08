package com.michi.manajemenbengkel.gold;

public class KoneksiAPI {
    //dynamic ip before production
    private static final String db ="http://192.168.39.84/ManajemenWarehouse/";
    public static final String AllItem = db+"warehouse/cuditem.php";
    public static final String lihatBarang = db+"warehouse/showItem.php";
}
