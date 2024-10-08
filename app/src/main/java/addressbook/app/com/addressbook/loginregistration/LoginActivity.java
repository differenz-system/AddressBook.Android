package addressbook.app.com.addressbook.loginregistration;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

import addressbook.app.com.addressbook.R;
import addressbook.app.com.addressbook.apis.HttpRequestHandler;
import addressbook.app.com.addressbook.apis.PostRequest;
import addressbook.app.com.addressbook.apis.PostRequest.OnPostServiceCallListener;
import addressbook.app.com.addressbook.databinding.ActivityLoginBinding;
import addressbook.app.com.addressbook.main.AddressBookListingActivity;
import addressbook.app.com.addressbook.model.UserLoginDetail;
import addressbook.app.com.addressbook.utility.BaseAppCompatActivity;
import addressbook.app.com.addressbook.utility.ConnectionDetector;
import addressbook.app.com.addressbook.utility.Constant;
import addressbook.app.com.addressbook.utility.Globals;
import addressbook.app.com.addressbook.utility.UtilsValidation;
/*import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;*/

public class LoginActivity extends BaseAppCompatActivity {

    Globals globals;
    /*@BindView(R.id.toolbar_title)
    AppCompatTextView toolbar_title;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.btn_login)
    AppCompatButton btn_login;
    @BindView(R.id.btn_login_fb)
    AppCompatButton btn_login_fb;
    @BindView(R.id.et_email)
    AppCompatEditText et_email;
    @BindView(R.id.et_password)
    AppCompatEditText et_password;
    @BindView(R.id.pg)
    ProgressBar pg;*/
    //Facebook
    private CallbackManager callbackmanager;
    private ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        //setContentView(R.layout.activity_login);
        //ButterKnife.bind(this);

        init();

        binding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginClick();
            }
        });

        binding.btnLoginFb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginFBClick();
            }
        });
    }

    private void init() {
        globals = (Globals) getApplicationContext();
        setSupportActionBar(binding.toolbar.getRoot());
        binding.toolbar.toolbarTitle.setText(getString(R.string.title_login));
    }

    //@OnClick(R.id.btn_login)
    public void loginClick() {
        Globals.hideKeyboard(getContextActivity());
        if (isValid()) {
            if (ConnectionDetector.internetCheck(getContext(), true))
                doRequestForLoginUser();
        }
    }

    //@OnClick(R.id.btn_login_fb)
    public void loginFBClick() {
        Globals.hideKeyboard(getContextActivity());
        FBLogin();
    }

    public void FBLogin() {
        callbackmanager = CallbackManager.Factory.create();
        // Set permissions
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "email"));
        LoginManager.getInstance().registerCallback(callbackmanager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                intentAddressBookListing();
            }

            @Override
            public void onCancel() {
                Log.d("cancel", "On cancel");
            }

            @Override
            public void onError(FacebookException error) {
                Log.d("error", error.toString());
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            callbackmanager.onActivityResult(requestCode, resultCode, data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Context getContext() {
        return LoginActivity.this;
    }

    private Activity getContextActivity() {
        return LoginActivity.this;
    }

    private void intentAddressBookListing() {
        Intent i_address_book_listing = new Intent(LoginActivity.this, AddressBookListingActivity.class);
        startActivity(i_address_book_listing);
        finish();
    }

    private void doRequestForLoginUser() {
        String url = getString(R.string.server_url) + getString(R.string.login_url);
        JSONObject postData = HttpRequestHandler.getInstance().getLoginUserJson(Globals.trimString(binding.etEmail), Globals.trimString(binding.etPassword));

        if (postData != null) {
            new PostRequest(this, url, postData, true, new OnPostServiceCallListener() {
                @Override
                public void onSucceedToPostCall(JSONObject response) {
                    UserLoginDetail userLoginDetail = new Gson().fromJson(response.toString(), UserLoginDetail.class);
                    if (userLoginDetail.data != null) {
                        Log.d(Constant.AB_Email, " : " + userLoginDetail.data.email);
                        Log.d(Constant.AB_Password, " : " + userLoginDetail.data.password);
                        globals.setUserDetails(userLoginDetail);
                        intentAddressBookListing();
                    } else {
                        Globals.showToast(LoginActivity.this, getString(R.string.msg_server_error));
                    }
                }

                @Override
                public void onFailedToPostCall(int statusCode, String msg) {
                    Globals.showToast(LoginActivity.this, msg);
                }
            }).execute();
        }
        Globals.hideKeyboard(this);
    }

    public JSONObject getUserInputAsJson() {
        JSONObject jsonData = new JSONObject();
        try {
            // Get user input
            String email = Globals.trimString(binding.etEmail);
            String password = Globals.trimString(binding.etPassword);

            // Add input data to the JSON object
            jsonData.put("email", email);
            jsonData.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonData;
    }

    public boolean isValid() {
        if (UtilsValidation.validateEmptyEditText(binding.etEmail)) {
            Globals.showToast(LoginActivity.this, getString(R.string.toast_err_email));
            requestFocus(binding.etEmail);
            return false;
        }
        if (UtilsValidation.validateEmail(binding.etEmail)) {
            Globals.showToast(LoginActivity.this, getString(R.string.toast_err_enter_valid_email));
            requestFocus(binding.etEmail);
            return false;
        }
        if (UtilsValidation.validateEmptyEditText(binding.etPassword)) {
            Globals.showToast(LoginActivity.this, getString(R.string.toast_err_password));
            requestFocus(binding.etPassword);
            return false;
        }
        return true;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }
}
