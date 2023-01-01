package com.michi.manajemenbengkel.gold;

public class KoneksiAPI {
    //dynamic ip before production
    private static final String db ="http://192.168.1.22/ManajemenWarehouse/";
    //private static final String db ="https://androidemployee.000webhostapp.com/ManajemenWarehouse/";
    public static final String AllItem = db+"warehouse/cuditem.php";
    public static final String ShowItem = db+"warehouse/showItem.php";
    public static final String ShowItemTek = db+"warehouse/showItemTek.php";
    public static final String AllUser = db+"user/cuduser.php";
    public static final String login = db+"user/auth.php";
    public static final String register = db+"user/cekUser.php";
}
