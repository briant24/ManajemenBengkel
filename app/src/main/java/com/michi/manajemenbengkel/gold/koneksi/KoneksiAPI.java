package com.michi.manajemenbengkel.gold.koneksi;

public class KoneksiAPI {
    //dynamic ip before production
    private static final String db ="http://192.168.1.15/ManajemenWarehouse/";
    public static final String AllItem = db+"warehouse/cuditem.php";
    public static final String AddTrans = db+"transaksi/addTransaksi.php";
    public static final String historyTek = db+"history/historytek.php";
    public static final String deldetailtrans = db+"detailTransaksi/deleteDetailTransaksi.php";
    public static final String SearchhistoryTek = db+"history/searchhistorytek.php";
    public static final String AddDetailTrans = db+"detailTransaksi/addDetailTransaksi.php";
    public static final String DelTrans = db+"transaksi/deleteTransaksi.php";
    public static final String UpdateTrans = db+"transaksi/updateTransaksi.php";
    public static final String Updatestatus = db+"transaksi/updatestatus.php";
    public static final String ShowTransaksi = db+"transaksi/showTransaksi.php";
    public static final String ShowTransaksibyUser = db+"transaksi/showTransaksibyuser.php";
    public static final String ShowItem = db+"warehouse/showItem.php";
    public static final String ShowListItem = db+"warehouse/showItemTek.php";
    public static final String ShowStokItem = db+"warehouse/showstok.php";
    public static final String ShowDetailTrans = db+"detailTransaksi/showdetailbyuser.php";
    public static final String ShowPaidTemTek = db+"transTemp/showpaidtemp.php";
    public static final String updatestok = db+"warehouse/updatestok.php";
    public static final String AllUser = db+"user/cuduser.php";
    public static final String ShowUser = db+"user/showUser.php";
    public static final String login = db+"user/auth.php";
    public static final String showProfile = db+"user/showprofile.php";
    public static final String register = db+"user/cekUser.php";
}
