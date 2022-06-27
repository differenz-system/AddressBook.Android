package addressbook.app.com.addressbook.apis;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by mac on 9/8/17.
 */

public class GetDownloadableUrl extends AsyncTask<String, String, String> {
    private String url;
    private Context context;
    private OnGetURLListener listener;

    public GetDownloadableUrl(Context context, OnGetURLListener listener, String url) {
        this.url = url;
        this.context = context;
        this.listener = listener;
    }

    public interface OnGetURLListener {
        public void onSucceedGetURL(String download_URL);
    }

    /**
     * Before starting background thread Show Progress Bar Dialog
     */
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    /**
     * Downloading file in background thread
     */
    @Override
    protected String doInBackground(String... f_url) {
        String mURL = null;
        try {
            URL url1 = new URL(url);
            HttpURLConnection Icon = (HttpURLConnection) url1.openConnection();
            Icon.setInstanceFollowRedirects(true);
            HttpURLConnection.setFollowRedirects(true);
            URL secondURL = new URL(Icon.getHeaderField("Location"));
            mURL = secondURL.toString();
        } catch (Exception e) {
            Log.e("Error: ", e.getMessage());
        }

        return mURL;
    }

    /**
     * After completing background task Dismiss the progress dialog
     **/
    @Override
    protected void onPostExecute(String file_url) {
        // dismiss the dialog after the file was downloaded
        listener.onSucceedGetURL(file_url);
    }
}
