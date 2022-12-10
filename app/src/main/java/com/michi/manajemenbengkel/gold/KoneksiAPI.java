package com.michi.manajemenbengkel.gold;

public class KoneksiAPI {
    //dynamic ip before production
    private static final String db ="http://192.168.1.40/ManajemenWarehouse/";
    public static final String AllItem = db+"warehouse/cuditem.php";
    public static final String ShowItem = db+"warehouse/showItem.php";
    public static final String AllUser = db+"user/cuduser.php";
    public static final String login = db+"user/auth.php";
}
