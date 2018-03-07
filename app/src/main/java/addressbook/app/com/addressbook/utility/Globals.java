package addressbook.app.com.addressbook.utility;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import android.support.v7.widget.AppCompatEditText;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;

import org.greenrobot.greendao.database.Database;

import java.lang.reflect.Type;

import addressbook.app.com.addressbook.R;
import addressbook.app.com.addressbook.greendao.db.DaoMaster;
import addressbook.app.com.addressbook.greendao.db.DaoSession;
import addressbook.app.com.addressbook.loginregistration.LoginActivity;
import addressbook.app.com.addressbook.model.UserLoginDetail;
import es.dmoral.toasty.Toasty;

/**
 * Created by mac on 9/20/17.
 */

public class Globals extends MultiDexApplication {
    SharedPreferences sp;
    SharedPreferences.Editor editor;
    public static String TAG = "Globals";
    private static Globals instance;
    private DaoSession daoSession;


    @Override
    public void onCreate() {
        super.onCreate();
        MultiDex.install(this);
        instance = this;
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "users-db"); //The users-db here is the name of our database.
        Database db = helper.getWritableDb();
        daoSession = new DaoMaster(db).newSession();
        Logger.addLogAdapter(new AndroidLogAdapter() {
            @Override
            public boolean isLoggable(int priority, String tag) {
                return true;
            }
        });
    }

    public static synchronized Globals getInstance() {
        return instance;
    }

    public static void hideKeyboard(Activity activity) {
        try {
            InputMethodManager inputManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public DaoSession getDaoSession() {
        return daoSession;
    }

    private static Toast toast;

    /**
     * @param context
     * @param message
     */
    public static void showToast(Context context, String message) {
        if (message == null || message.isEmpty() || context == null)
            return;

        //1st way to instantly update Toast message: with toasty library
        if (toast == null) {
            toast = Toasty.normal(context, message);
        }
        View v = toast.getView();
        if (v != null) {
            TextView tv = (TextView) v.findViewById(R.id.toast_text);
            if (tv != null)
                tv.setText(message);
        }
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();

        //with native toast
        /*if (toast == null) {
            toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        }
        toast.setText(message);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();*/

        //2nd way You can cache current Toast in Activity's variable, and then cancel it just before showing next toast
        /*if (toast != null) toast.cancel();
        toast = Toasty.normal(context, message);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();*/
    }

    public SharedPreferences getSharedPref() {
        return sp = (sp == null) ? getSharedPreferences(Constant.MM_secrets, Context.MODE_PRIVATE) : sp;
    }

    public SharedPreferences.Editor getEditor() {
        return editor = (editor == null) ? getSharedPref().edit() : editor;
    }



    // storing model class in prefrence
    public static String toJsonString(UserLoginDetail params) {
        if (params == null) {
            return null;
        }
        Type mapType = new TypeToken<UserLoginDetail>() {
        }.getType();
        Gson gson = new Gson();
        return gson.toJson(params, mapType);
    }

    public static UserLoginDetail toUserDetails(String params) {
        if (params == null)
            return null;

        Type mapType = new TypeToken<UserLoginDetail>() {
        }.getType();
        Gson gson = new Gson();
        return gson.fromJson(params, mapType);
    }

    public void setUserDetails(UserLoginDetail userMap) {
        getEditor().putString(Constant.AB_USER_MAP, toJsonString(userMap));
        getEditor().commit();
    }

    public UserLoginDetail getUserDetails() {
        return toUserDetails(getSharedPref().getString(Constant.AB_USER_MAP, null));
    }
    public static String trimString(AppCompatEditText textView) {
        return textView.getText().toString().trim();
    }

    public static void logoutProcess(Context context) {
        Intent i_logout = new Intent(context, LoginActivity.class);
        // set the new task and clear flags
        i_logout.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(i_logout);
    }

    /**
     * @param listener           listener to get click event of dialog
     * @param title              title of dialog
     * @param desc               description of dialog
     * @param positiveButtonText text of positive button
     * @param negativeButtonText text of negative
     * @param isCancelable       set true if you want cancelable dialog
     */
    public static void showDialog(final Context context, final OnDialogClickListener listener,
                                  String title, String desc, String positiveButtonText,
                                  String negativeButtonText,
                                  boolean isCancelable, final int position) {
        if (desc == null || desc.isEmpty())
            return;
        final AlertDialog.Builder dialog = new Builder(context);
        dialog.setCancelable(isCancelable);
        dialog.setTitle(title).setMessage(desc);

        if (positiveButtonText != null && !positiveButtonText.isEmpty())
            dialog.setPositiveButton(positiveButtonText, new OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                    listener.OnDialogPositiveClick(position);
                }
            });
        else
            dialog.setPositiveButton("", null);
        if (negativeButtonText != null && !negativeButtonText.isEmpty())
            dialog.setNegativeButton(negativeButtonText, new OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                    listener.OnDialogNegativeClick();
                }
            });
        else {
            dialog.setNegativeButton("", null);
        }


        dialog.create().show();
    }

    public interface OnDialogClickListener {
        void OnDialogPositiveClick(int position);

        void OnDialogNegativeClick();
    }
}

