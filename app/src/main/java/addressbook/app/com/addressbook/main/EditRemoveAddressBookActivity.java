package addressbook.app.com.addressbook.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import addressbook.app.com.addressbook.R;
import addressbook.app.com.addressbook.databinding.ActivityEditRemoveAddressBookBinding;
import addressbook.app.com.addressbook.greendao.db.AppDatabase;
import addressbook.app.com.addressbook.greendao.db.RoomAddressBook;
import addressbook.app.com.addressbook.greendao.db.RoomAddressBookDao;
import addressbook.app.com.addressbook.utility.BaseAppCompatActivity;
import addressbook.app.com.addressbook.utility.Constant;
import addressbook.app.com.addressbook.utility.Globals;
import addressbook.app.com.addressbook.utility.Globals.OnDialogClickListener;
import addressbook.app.com.addressbook.utility.UtilsValidation;

public class EditRemoveAddressBookActivity extends BaseAppCompatActivity {

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

    public void intentAddressBook() {
        onBackPressed();
    }

    public void logoutClick() {
        globals.setUserDetails(null);
        Globals.logoutProcess(getContext());
    }

    public void saveUpdateClick() {
        if (isUpdate) {
            updateAddressBook();
        } else {
            insertAddressBook();
        }
    }

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
        }
    }

    private void deleteAddressBook() {
        Globals.showDialog(this, new OnDialogClickListener() {
            @Override
            public void OnDialogPositiveClick(int position) {
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
