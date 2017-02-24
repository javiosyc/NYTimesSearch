package javio.com.nytimessearch.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

/**
 * Created by javiosyc on 2017/2/25.
 */
public class NetworkUtils {

    private static final String NETWORK_IS_NOT_AVAILABLE_MESSAGE = "Network is not available";

    private NetworkUtils() {
    }

    public static Boolean isNetworkAvailable(Context context, boolean showMessage) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        boolean isNetworkAvailable = (activeNetworkInfo != null && activeNetworkInfo.isConnected());

        if (showMessage && !isNetworkAvailable)
            Toast.makeText(context, NETWORK_IS_NOT_AVAILABLE_MESSAGE, Toast.LENGTH_LONG).show();

        return isNetworkAvailable;
    }

    public static Boolean isNetworkAvailable(Context context) {
        return isNetworkAvailable(context, false);
    }
}
