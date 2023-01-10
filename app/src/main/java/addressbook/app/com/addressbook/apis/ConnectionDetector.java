package addressbook.app.com.addressbook.apis;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;

import addressbook.app.com.addressbook.R;


public class ConnectionDetector {

    public static boolean isConnectingToInternet(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = null;
        if (connectivityManager != null) {
            activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        }
        return activeNetworkInfo != null && activeNetworkInfo.isAvailable() && activeNetworkInfo.isConnected();
    }

    public static boolean internetCheck(Context context, boolean showDialog) {
        if (isConnectingToInternet(context))
            return true;
        if (showDialog) {
            //showInternetAlertDialog(context);
            showAlertDialog(context, context.getString(R.string.msg_NO_INTERNET_TITLE), context.getString(R.string.msg_NO_INTERNET_MSG), false);
        }
        return false;
    }

    public static void showAlertDialog(final Context context, String pTitle, final String pMsg, Boolean status) {
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);

            builder.setTitle(pTitle);
            builder.setMessage(pMsg);
            builder.setCancelable(true);
            builder.setPositiveButton(context.getString(R.string.msg_goto_settings),
                    (dialog, which) -> {
                        dialog.dismiss();
                        Intent intent = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
                        context.startActivity(intent);
                    });
            AlertDialog alert = builder.create();
            alert.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
