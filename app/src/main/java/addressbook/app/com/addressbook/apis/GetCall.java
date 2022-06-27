package addressbook.app.com.addressbook.apis;

import android.content.Context;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import cc.cloudist.acplibrary.ACProgressFlower;
import cz.msebera.android.httpclient.Header;

/**
 * Created by mac on 6/8/17.
 */

public class GetCall {

    private OnGetServiceCallListener listener;
    private JSONObject postData;
    private Context context;
    private String url;
    private boolean isLoaderRequired;
    ACProgressFlower dialog;

    public interface OnGetServiceCallListener {
        void onSucceedToGetCall(JSONObject response);

        void onFailedToGetCall();
    }

    public GetCall(Context context, String url, JSONObject postData, OnGetServiceCallListener listener, boolean isLoaderRequired) {
        this.listener = listener;
        this.postData = postData;
        this.context = context;
        this.url = url;
        this.isLoaderRequired = isLoaderRequired;
    }

    public void doRequest() {

        if (!ConnectionDetector.internetCheck(context, true)) {
            return;
        }

        if (isLoaderRequired && context != null) {
            dialog = HttpRequestHandler.getInstance().getProgressBar(context);
        }
        HttpRequestHandler.getInstance().get(context, url, new JsonHttpResponseHandler() {

            @Override
            public void onStart() {
                super.onStart();
                if (dialog != null && !dialog.isShowing()) {
                    dialog.show();
                }
            }

            @Override
            public void onFinish() {
                super.onFinish();
                if (dialog != null && dialog.isShowing())
                    dialog.dismiss();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                listener.onSucceedToGetCall(response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                listener.onFailedToGetCall();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                listener.onFailedToGetCall();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                listener.onFailedToGetCall();
            }
        });
    }
}
