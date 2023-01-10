package addressbook.app.com.addressbook.apis;

import android.app.ProgressDialog;
import android.content.Context;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.orhanobut.logger.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import addressbook.app.com.addressbook.utility.Constant;
import addressbook.app.com.addressbook.utility.Globals;

import cz.msebera.android.httpclient.entity.StringEntity;

public class HttpRequestHandler {

    private Globals globals = (Globals) Globals.getInstance();
    private static final String HEADER_TYPE_JSON = "application/json";

    private static final String AUTHORIZATION_HEADER = "authorization";
    private static final String AUTHORIZATION_HEADER_VALUE = "Bearer b04a4a68ee5bf04d9582e27011601ac8";

    private static HttpRequestHandler mInstance = null;
    private final AsyncHttpClient client;
    public String TAG = getClass().getName();

    private HttpRequestHandler() {
        client = new AsyncHttpClient();
        client.setTimeout(30000);
    }

    public static HttpRequestHandler getInstance() {
        if (mInstance == null) {
            mInstance = new HttpRequestHandler();
        }
        return mInstance;
    }

    public void get(Context context, String url, AsyncHttpResponseHandler responseHandler) {
        client.addHeader(AUTHORIZATION_HEADER, AUTHORIZATION_HEADER_VALUE);
        StringEntity entity = new StringEntity("", "UTF-8");
        client.get(context, url, entity, HEADER_TYPE_JSON, responseHandler);
    }

    void post(Context context, String url, JSONObject params, AsyncHttpResponseHandler responseHandler) {
        try {
            Logger.e(url);
            Logger.json(params.toString());
            StringEntity entity = new StringEntity(params.toString(), "UTF-8");
            client.post(context, url, entity, HEADER_TYPE_JSON, responseHandler);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void postWithRequestParam(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        Logger.e("Server Url:=> ", url);
        Logger.json(params.toString());
        client.post(url, params, responseHandler);
    }

    public void cancelRequest(Context context) {
        client.cancelRequests(context, true);
    }

    public ProgressDialog getProgressBar(Context context) {
        final ProgressDialog dialog = new ProgressDialog(context);
        dialog.setMessage("Please wait...");
        dialog.setCancelable(false);
        return dialog;
    }

    /*public ACProgressFlower getProgressBar(Context context) {
        final ACProgressFlower dialog = new ACProgressFlower.Builder(context)
                .direction(ACProgressConstant.DIRECT_CLOCKWISE)
                .petalThickness(5)
                .themeColor(Color.WHITE)
                .fadeColor(Color.DKGRAY).build();
        dialog.setCancelable(false);
        return dialog;
    }

    public ACProgressFlower getProgressBarWithText(Context context, String msg) {
        final ACProgressFlower dialog = new ACProgressFlower.Builder(context)
                .direction(ACProgressConstant.DIRECT_CLOCKWISE)
                .petalThickness(5)
                .themeColor(Color.WHITE)
                .text(msg)
                .fadeColor(Color.DKGRAY).build();
        dialog.setCancelable(false);
        return dialog;
    }*/

    public JSONObject getLoginUserJson(String email, String password) {
        // we are using this JSON for demo purpose
        JSONObject params = new JSONObject();
        try {
            params.put(Constant.AB_Email, email);
            params.put(Constant.AB_Password, password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return params;
    }
}
