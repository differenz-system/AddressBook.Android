package addressbook.app.com.addressbook.main;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import addressbook.app.com.addressbook.R;
import addressbook.app.com.addressbook.greendao.db.AddressBook;
import addressbook.app.com.addressbook.greendao.db.DaoSession;
import addressbook.app.com.addressbook.utility.BaseAppCompatActivity;
import addressbook.app.com.addressbook.utility.Globals;
import addressbook.app.com.addressbook.utility.Constant;
import addressbook.app.com.addressbook.utility.Globals.OnDialogClickListener;
import addressbook.app.com.addressbook.utility.UtilsValidation;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EditRemoveAddressBookActivity extends BaseAppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.toolbar_title)
    AppCompatTextView toolbar_title;

    @BindView(R.id.toolbar_left)
    AppCompatTextView toolbar_left;

    @BindView(R.id.toolbar_right)
    AppCompatTextView toolbar_right;

    @BindView(R.id.btn_save_update)
    Button btn_save_update;

    @BindView(R.id.btn_delete_cancel)
    Button btn_delete_cancel;

    @BindView(R.id.edt_name)
    AppCompatEditText edt_name;

    @BindView(R.id.edt_email)
    AppCompatEditText edt_email;

    @BindView(R.id.edt_contact_no)
    AppCompatEditText edt_contact_no;

    @BindView(R.id.switch_active)
    SwitchCompat switch_active;

    Globals globals;

    private DaoSession daoSession;
    private boolean isUpdate = false;
    private Bundle extra;
    private AddressBook mAddressBook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_remove_address_book);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        globals = (Globals) getApplicationContext();
        daoSession = Globals.getInstance().getDaoSession();
        setSupportActionBar(toolbar);
        toolbar_title.setText(getString(R.string.lbl_detail));
        toolbar_left.setVisibility(View.VISIBLE);
        toolbar_right.setVisibility(View.VISIBLE);
        toolbar_left.setText(getString(R.string.lbl_address_book));
        toolbar_right.setText(getString(R.string.action_logout));

        extra = getIntent().getExtras();
        if (extra != null && extra.containsKey(Constant.Key_editAddressBook)) {
            isUpdate = true;
            mAddressBook = (AddressBook) extra.getSerializable(Constant.Key_editAddressBook);
        }

        // set the text in button based on adding or editing addressbook
        if (isUpdate) {
            // set the value from intent bundle
            edt_name.setText(mAddressBook.getName());
            edt_email.setText(mAddressBook.getEmail());
            edt_contact_no.setText(mAddressBook.getContact_number());
            switch_active.setChecked(mAddressBook.getIsactive());

            btn_save_update.setText(getString(R.string.action_update));
            btn_delete_cancel.setText(getString(R.string.action_delete));
        } else {
            btn_save_update.setText(getString(R.string.action_save));
            btn_delete_cancel.setText(getString(R.string.action_cancel));
        }
    }

    @OnClick(R.id.toolbar_left)
    public void intentAddressBook() {
        onBackPressed();
    }

    @OnClick(R.id.toolbar_right)
    public void logoutClick() {
        globals.setUserDetails(null);
        Globals.logoutProcess(getContext());
    }

    @OnClick(R.id.btn_save_update)
    public void saveUpdateClick() {

        if (isUpdate) {
            updateAddressBook();
        } else {
            insertAddressBook();
        }
    }

    @OnClick(R.id.btn_delete_cancel)
    public void deleteCancelClick() {

        if (isUpdate) {
            deleteAddressBook();
        } else {
            onBackPressed();
        }
    }

    private Context getContext() {
        return EditRemoveAddressBookActivity.this;
    }

    private Activity getContextActivity() {
        return EditRemoveAddressBookActivity.this;
    }

    private void insertAddressBook() {

        if (isValid()) {
            AddressBook addressBook = new AddressBook();
            addressBook.setName(edt_name.getText().toString().trim());
            addressBook.setEmail(edt_email.getText().toString().trim());
            addressBook.setContact_number(edt_contact_no.getText().toString().trim());
            addressBook.setIsactive(switch_active.isChecked());

            daoSession.insert(addressBook);
            Intent resultIntent = new Intent();
            setResult(RESULT_OK, resultIntent);
            finish();
        }
    }


    private void updateAddressBook() {
        if (isValid()) {
            AddressBook addressBook = new AddressBook();
            addressBook.setName(edt_name.getText().toString().trim());
            addressBook.setEmail(edt_email.getText().toString().trim());
            addressBook.setContact_number(edt_contact_no.getText().toString().trim());
            addressBook.setIsactive(switch_active.isChecked());
            addressBook.setId(mAddressBook.getId());

            daoSession.update(addressBook);
            Intent resultIntent = new Intent();
            setResult(RESULT_OK, resultIntent);
            finish();
        }
    }

    private void deleteAddressBook() {
        Globals.showDialog(this, new OnDialogClickListener() {
            @Override
            public void OnDialogPositiveClick(int position) {
                daoSession.delete(mAddressBook);
                Intent resultIntent = new Intent();
                setResult(RESULT_OK, resultIntent);
                finish();
            }

            @Override
            public void OnDialogNegativeClick() {

            }
        }, getString(R.string.action_delete), getString(R.string.delete_confirmation_msg), getString(R.string.action_yes), getString(R.string.action_no), false, 0);

    }


    public boolean isValid() {
        if (UtilsValidation.validateEmptyEditText(edt_name)) {
            Globals.showToast(EditRemoveAddressBookActivity.this, getString(R.string.toast_err_name));
            requestFocus(edt_name);
            return false;
        }
        if (UtilsValidation.validateEmptyEditText(edt_email)) {
            Globals.showToast(EditRemoveAddressBookActivity.this, getString(R.string.toast_err_email));
            requestFocus(edt_email);
            return false;
        }
        if (UtilsValidation.validateEmail(edt_email)) {
            Globals.showToast(EditRemoveAddressBookActivity.this, getString(R.string.toast_err_enter_valid_email));
            requestFocus(edt_email);
            return false;
        }
        if (UtilsValidation.validateEmptyEditText(edt_contact_no)) {
            Globals.showToast(EditRemoveAddressBookActivity.this, getString(R.string.toast_err_contact_no));
            requestFocus(edt_contact_no);
            return false;
        }
        if (UtilsValidation.validatePhoneNumber(edt_contact_no)) {
            Globals.showToast(EditRemoveAddressBookActivity.this, getString(R.string.toast_err_enter_valid_contact_number));
            requestFocus(edt_contact_no);
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
