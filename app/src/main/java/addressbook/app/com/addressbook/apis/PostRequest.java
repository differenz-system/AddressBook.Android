package addressbook.app.com.addressbook.apis;

import android.content.Context;
import android.view.View;
import android.widget.ProgressBar;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.orhanobut.logger.Logger;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import addressbook.app.com.addressbook.R;
import addressbook.app.com.addressbook.utility.Constant;
import cc.cloudist.acplibrary.ACProgressFlower;
import cz.msebera.android.httpclient.Header;

import static java.net.HttpURLConnection.HTTP_BAD_REQUEST;

public class PostRequest {

    private OnPostServiceCallListener listener;
    private OnNoInternetListener internetListener;
    private JSONObject postData;
    private Context context;
    private String url;
    private boolean isLoaderRequired, isDialog = true;
    private ACProgressFlower dialog = null;
    private ProgressBar pb = null;

    public interface OnPostServiceCallListener {
        void onSucceedToPostCall(JSONObject response);

        void onFailedToPostCall(int statusCode, String msg);
    }

    public interface OnNoInternetListener {
        void onNoInternet(String msg);
    }


    public PostRequest(Context context, String url, JSONObject postData, boolean isLoaderRequired, OnPostServiceCallListener listener) {
        this.listener = listener;
        this.postData = postData;
        this.context = context;
        this.isLoaderRequired = isLoaderRequired;
        this.url = url;
    }

    public PostRequest(Context context, String url, JSONObject postData, boolean isLoaderRequired, boolean isDialog, OnPostServiceCallListener listener, OnNoInternetListener internetListener) {
        this.listener = listener;
        this.internetListener = internetListener;
        this.postData = postData;
        this.context = context;
        this.isLoaderRequired = isLoaderRequired;
        this.url = url;
        this.isDialog = isDialog;
    }

    public PostRequest(Context context, String url, JSONObject postData, ProgressBar pb, boolean isLoaderRequired, OnPostServiceCallListener listener) {
        this.listener = listener;
        this.postData = postData;
        this.context = context;
        this.url = url;
        this.pb = pb;
        this.isLoaderRequired = isLoaderRequired;
    }

    public PostRequest(Context context, String url, JSONObject postData, ProgressBar pb, boolean isLoaderRequired, boolean isDialog, OnPostServiceCallListener listener, OnNoInternetListener internetListener) {
        this.listener = listener;
        this.postData = postData;
        this.context = context;
        this.url = url;
        this.pb = pb;
        this.isLoaderRequired = isLoaderRequired;
        this.internetListener = internetListener;
        this.isDialog = isDialog;
    }

    public void execute() {

        if (!ConnectionDetector.internetCheck(context, isDialog)) {
            if (!isDialog && internetListener != null)
                internetListener.onNoInternet(context.getString(R.string.msg_server_error));
            return;
        }

        if (isLoaderRequired && pb == null) {
            dialog = HttpRequestHandler.getInstance().getProgressBar(context);
        }

        HttpRequestHandler.getInstance().post(context, url, postData, new JsonHttpResponseHandler() {

            @Override
            public void onStart() {
                super.onStart();
                if (dialog != null && !dialog.isShowing()) {
                    dialog.show();
                } else {
                    if (pb != null && isLoaderRequired) {
                        pb.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onFinish() {
                super.onFinish();
                if (dialog != null && dialog.isShowing())
                    dialog.dismiss();
                else {
                    if (pb != null && isLoaderRequired) {
                        pb.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Logger.json(response.toString());
                if (listener != null)
                    listener.onSucceedToPostCall(response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                try {
                    if (listener != null) {
                        if (statusCode == HTTP_BAD_REQUEST) {
                            if (errorResponse.has(Constant.AB_Message) && errorResponse.has(Constant.AB_IsSuccess)) {
                                String msg = errorResponse.getString(Constant.AB_Message);
                                if (msg != null && !msg.isEmpty())
                                    listener.onFailedToPostCall(statusCode, msg);
                                else
                                    listener.onFailedToPostCall(statusCode, context.getString(R.string.msg_server_error));

                            } else {
                                listener.onFailedToPostCall(statusCode, context.getString(R.string.msg_server_error));
                            }
                        } else {
                            listener.onFailedToPostCall(statusCode, context.getString(R.string.msg_server_error));
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                if (listener != null)
                    listener.onFailedToPostCall(statusCode, context.getString(R.string.error_msg_connection_timeout));
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                if (listener != null)
                    listener.onFailedToPostCall(statusCode, context.getString(R.string.error_msg_connection_timeout));
            }
        });
    }
}
