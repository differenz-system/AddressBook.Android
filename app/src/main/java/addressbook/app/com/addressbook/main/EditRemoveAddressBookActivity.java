package addressbook.app.com.addressbook.main;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.room.Room;

import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import addressbook.app.com.addressbook.R;
import addressbook.app.com.addressbook.databinding.ActivityEditRemoveAddressBookBinding;
import addressbook.app.com.addressbook.databinding.ActivityLoginBinding;
//import addressbook.app.com.addressbook.greendao.db.AddressBook;
import addressbook.app.com.addressbook.greendao.db.AppDatabase;
//import addressbook.app.com.addressbook.greendao.db.DaoSession;
import addressbook.app.com.addressbook.greendao.db.RoomAddressBook;
import addressbook.app.com.addressbook.greendao.db.RoomAddressBookDao;
import addressbook.app.com.addressbook.utility.BaseAppCompatActivity;
import addressbook.app.com.addressbook.utility.Globals;
import addressbook.app.com.addressbook.utility.Constant;
import addressbook.app.com.addressbook.utility.Globals.OnDialogClickListener;
import addressbook.app.com.addressbook.utility.UtilsValidation;
/*import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;*/

public class EditRemoveAddressBookActivity extends BaseAppCompatActivity {

    /*@BindView(R.id.toolbar)
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
    SwitchCompat switch_active;*/

    Globals globals;

    //private DaoSession daoSession;
    private boolean isUpdate = false;
    private Bundle extra;
    private RoomAddressBook mAddressBook;
    private AppDatabase db;
    private RoomAddressBookDao addressBookDao;

    private ActivityEditRemoveAddressBookBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*setContentView(R.layout.activity_edit_remove_address_book);
        ButterKnife.bind(this);*/
        binding = ActivityEditRemoveAddressBookBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        db = AppDatabase.getInstance(getApplicationContext()); // Updated to use the singleton method

        if (db == null) {
            throw new IllegalStateException("Database initialization failed");
        }

        addressBookDao = db.addressBookDao();
        init();
    }

    private void init() {
        /*globals = (Globals) getApplicationContext();
        daoSession = Globals.getInstance().getDaoSession();
        setSupportActionBar(binding.toolbar.getRoot());
        binding.toolbar.toolbarTitle.setText(getString(R.string.lbl_detail));
        binding.toolbar.toolbarLeft.setVisibility(View.VISIBLE);
        binding.toolbar.toolbarRight.setVisibility(View.VISIBLE);
        binding.toolbar.toolbarLeft.setText(getString(R.string.lbl_address_book));
        binding.toolbar.toolbarRight.setText(getString(R.string.action_logout));

        extra = getIntent().getExtras();
        if (extra != null && extra.containsKey(Constant.Key_editAddressBook)) {
            isUpdate = true;
            mAddressBook = (AddressBook) extra.getSerializable(Constant.Key_editAddressBook);
        }

        // set the text in button based on adding or editing addressbook
        if (isUpdate) {
            // set the value from intent bundle
            binding.edtName.setText(mAddressBook.getName());
            binding.edtEmail.setText(mAddressBook.getEmail());
            binding.edtContactNo.setText(mAddressBook.getContact_number());
            binding.switchActive.setChecked(mAddressBook.getIsactive());

            binding.btnSaveUpdate.setText(getString(R.string.action_update));
            binding.btnDeleteCancel.setText(getString(R.string.action_delete));
        } else {
            binding.btnSaveUpdate.setText(getString(R.string.action_save));
            binding.btnDeleteCancel.setText(getString(R.string.action_cancel));
        }*/

        //db = Globals.getInstance().getAppDatabase();
        Bundle extra = getIntent().getExtras();
        if (extra != null && extra.containsKey(Constant.Key_editAddressBook)) {
            mAddressBook = getIntent().getParcelableExtra(Constant.Key_editAddressBook);
            isUpdate = true;
            binding.edtName.setText(mAddressBook.getName());
            binding.edtEmail.setText(mAddressBook.getEmail());
            binding.edtContactNo.setText(mAddressBook.getContactNumber());
            binding.switchActive.setChecked(mAddressBook.getIsActive());
        }

        binding.toolbar.toolbarLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intentAddressBook();
            }
        });
        binding.toolbar.toolbarRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logoutClick();
            }
        });

        binding.btnSaveUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveUpdateClick();
            }
        });
        binding.btnDeleteCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteCancelClick();
            }
        });
    }

    //@OnClick(R.id.toolbar_left)
    public void intentAddressBook() {
        onBackPressed();
    }

    //@OnClick(R.id.toolbar_right)
    public void logoutClick() {
        globals.setUserDetails(null);
        Globals.logoutProcess(getContext());
    }

    //@OnClick(R.id.btn_save_update)
    public void saveUpdateClick() {
        if (isUpdate) {
            updateAddressBook();
        } else {
            insertAddressBook();
        }
    }

    //@OnClick(R.id.btn_delete_cancel)
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


    private void insertAddressBook() {

        if (isValid()) {
            RoomAddressBook addressBook = new RoomAddressBook();
            addressBook.setName(binding.edtName.getText().toString().trim());
            addressBook.setEmail(binding.edtEmail.getText().toString().trim());
            addressBook.setContactNumber(binding.edtContactNo.getText().toString().trim());
            addressBook.setIsActive(binding.switchActive.isChecked());

            new Thread(() -> {
                db.addressBookDao().insert(addressBook);
                runOnUiThread(() -> {
                    setResult(RESULT_OK, new Intent());
                    finish();
                });
            }).start();

            /*db.addressBookDao().insert(addressBook);
            setResult(RESULT_OK, new Intent());
            finish();*/
            //daoSession.insert(addressBook);
            /*Intent resultIntent = new Intent();
            setResult(RESULT_OK, resultIntent);
            finish();*/
        }
    }


    private void updateAddressBook() {
        if (isValid()) {
            RoomAddressBook addressBook = new RoomAddressBook();
            addressBook.setName(binding.edtName.getText().toString().trim());
            addressBook.setEmail(binding.edtEmail.getText().toString().trim());
            addressBook.setContactNumber(binding.edtContactNo.getText().toString().trim());
            addressBook.setIsActive(binding.switchActive.isChecked());
            addressBook.setId(mAddressBook.getId());

            new Thread(() -> {
                db.addressBookDao().update(addressBook);
                runOnUiThread(() -> {
                    setResult(RESULT_OK, new Intent());
                    finish();
                });
            }).start();

            /*db.addressBookDao().update(mAddressBook);
            setResult(RESULT_OK, new Intent());
            finish();*/

            /*daoSession.update(addressBook);
            Intent resultIntent = new Intent();
            setResult(RESULT_OK, resultIntent);
            finish();*/
        }
    }

    private void deleteAddressBook() {
        Globals.showDialog(this, new OnDialogClickListener() {
            @Override
            public void OnDialogPositiveClick(int position) {
                /*daoSession.delete(mAddressBook);
                Intent resultIntent = new Intent();
                setResult(RESULT_OK, resultIntent);
                finish();*/

                new Thread(() -> {
                    db.addressBookDao().delete(mAddressBook);
                    runOnUiThread(() -> {
                        setResult(RESULT_OK, new Intent());
                        finish();
                    });
                }).start();
            }

            @Override
            public void OnDialogNegativeClick() {

            }
        }, getString(R.string.action_delete), getString(R.string.delete_confirmation_msg), getString(R.string.action_yes), getString(R.string.action_no), false, 0);
    }


    public boolean isValid() {
        if (UtilsValidation.validateEmptyEditText(binding.edtName)) {
            Globals.showToast(EditRemoveAddressBookActivity.this, getString(R.string.toast_err_name));
            requestFocus(binding.edtName);
            return false;
        }
        if (UtilsValidation.validateEmptyEditText(binding.edtEmail)) {
            Globals.showToast(EditRemoveAddressBookActivity.this, getString(R.string.toast_err_email));
            requestFocus(binding.edtEmail);
            return false;
        }
        if (UtilsValidation.validateEmail(binding.edtEmail)) {
            Globals.showToast(EditRemoveAddressBookActivity.this, getString(R.string.toast_err_enter_valid_email));
            requestFocus(binding.edtEmail);
            return false;
        }
        if (UtilsValidation.validateEmptyEditText(binding.edtContactNo)) {
            Globals.showToast(EditRemoveAddressBookActivity.this, getString(R.string.toast_err_contact_no));
            requestFocus(binding.edtContactNo);
            return false;
        }
        if (UtilsValidation.validatePhoneNumber(binding.edtContactNo)) {
            Globals.showToast(EditRemoveAddressBookActivity.this, getString(R.string.toast_err_enter_valid_contact_number));
            requestFocus(binding.edtContactNo);
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
