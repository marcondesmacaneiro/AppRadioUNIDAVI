package br.com.marcondesmacaneiro.appradiounidavi2;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by marcondes on 25/11/16.
 */
public class VerificarConexao {
    private static ConnectivityManager cm;
    private static NetworkInfo info;

    public static boolean conexaoDisponivel(Context context) {
        cm = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);
        info = cm.getActiveNetworkInfo();

        if (info != null && info.isConnectedOrConnecting()) {
            return true;
        } else return false;
    }
}
