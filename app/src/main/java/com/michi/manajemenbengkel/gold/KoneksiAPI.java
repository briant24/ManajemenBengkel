package com.michi.manajemenbengkel.gold;

public class KoneksiAPI {
    //dynamic ip before production
    private static final String db ="http://192.168.1.22/ManajemenWarehouse/";
    //private static final String db ="https://androidemployee.000webhostapp.com/ManajemenWarehouse/";
    public static final String AllItem = db+"warehouse/cuditem.php";
    public static final String AddTrans = db+"transaksi/addTransaksi.php";
    public static final String tempTrans = db+"transTemp/addtranstemp.php";
    public static final String deltempTrans = db+"transTemp/deltranstemp.php";
    public static final String delAlltempTrans = db+"transTemp/delalltranstemp.php";
    public static final String ShowItem = db+"warehouse/showItem.php";
    public static final String ShowItemTek = db+"transTemp/showtranstemp.php";
    public static final String ShowPaidTemTek = db+"transTemp/showpaidtemp.php";
    public static final String AllUser = db+"user/cuduser.php";
    public static final String login = db+"user/auth.php";
    public static final String register = db+"user/cekUser.php";
}
