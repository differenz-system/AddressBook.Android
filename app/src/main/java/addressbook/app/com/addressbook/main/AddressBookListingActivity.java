package addressbook.app.com.addressbook.main;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.AdapterView;

import java.io.Serializable;
import java.util.ArrayList;

import addressbook.app.com.addressbook.R;
import addressbook.app.com.addressbook.greendao.db.AddressBook;
import addressbook.app.com.addressbook.greendao.db.AddressBookDao;
import addressbook.app.com.addressbook.greendao.db.DaoSession;
import addressbook.app.com.addressbook.utility.BaseAppCompatActivity;
import addressbook.app.com.addressbook.utility.Globals;
import addressbook.app.com.addressbook.utility.Constant;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddressBookListingActivity extends BaseAppCompatActivity implements AdapterView.OnItemClickListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.toolbar_title)
    AppCompatTextView toolbar_title;

    @BindView(R.id.toolbar_left)
    AppCompatTextView toolbar_left;

    @BindView(R.id.toolbar_right)
    AppCompatTextView toolbar_right;

    @BindView(R.id.rv_address_list)
    RecyclerView rv_address_list;

    @BindView(R.id.tv_no_list)
    AppCompatTextView tv_no_list;

    private DaoSession daoSession;

    private ArrayList<AddressBook> addressbookList;
    private AdapterAddressBookList adapterAddressBookList;
    Globals globals;
    public static int ADD_EDIT_ADDRESS_REQ_CODE = 1010;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_book_listing);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        globals = (Globals) getApplicationContext();
        daoSession = Globals.getInstance().getDaoSession();
        setSupportActionBar(toolbar);
        toolbar_title.setText(getString(R.string.lbl_address_book));
        toolbar_left.setVisibility(View.VISIBLE);
        toolbar_right.setVisibility(View.VISIBLE);
        toolbar_left.setText(getString(R.string.action_logout));
        toolbar_right.setText(getString(R.string.action_add));

        setUpList();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_EDIT_ADDRESS_REQ_CODE && resultCode == RESULT_OK) {
            setUpList();
        }
    }

    @OnClick(R.id.toolbar_right)
    public void addCLick() {
        saveAddressBook();
    }

    private void saveAddressBook() {
        Intent i_edit_remove_address_book = new Intent(getContext(), EditRemoveAddressBookActivity.class);
        someActivityResultLauncher.launch(i_edit_remove_address_book);
    }

    private void editAddressBook(int pos) {
        Intent i_edit_remove_address_book = new Intent(getContext(), EditRemoveAddressBookActivity.class);
        i_edit_remove_address_book.putExtra(Constant.Key_editAddressBook, (Serializable) addressbookList.get(pos));
        someActivityResultLauncher.launch(i_edit_remove_address_book);
    }


    // You can do the assignment inside onAttach or onCreate, i.e, before the activity is displayed
    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // There are no request codes
                        setUpList();
                    }
                }
            });
    private Context getContext() {
        return AddressBookListingActivity.this;
    }

    @OnClick(R.id.toolbar_left)
    public void logoutClick() {
        globals.setUserDetails(null);
        Globals.logoutProcess(getContext());
    }

    private void setUpList() {
        AddressBookDao addressBookDao = daoSession.getAddressBookDao();
        addressbookList = (ArrayList<AddressBook>) addressBookDao.loadAll();
        setAdapter();
    }

    public void setAdapter() {
        if (addressbookList != null && !addressbookList.isEmpty()) {
            if (adapterAddressBookList == null) {
                adapterAddressBookList = new AdapterAddressBookList(getContext());
                adapterAddressBookList.setOnItemClickListener(this);
            }
            adapterAddressBookList.doRefresh(addressbookList);

            if (rv_address_list.getAdapter() == null) {
                rv_address_list.setHasFixedSize(false);
                rv_address_list.setLayoutManager(new LinearLayoutManager(getContext()));
                rv_address_list.setAdapter(adapterAddressBookList);
                rv_address_list.setNestedScrollingEnabled(false);
                rv_address_list.setFocusable(false);
            }
        }
        handleEmptyList();
    }

    public void handleEmptyList() {
        if (tv_no_list != null) {
            if (addressbookList == null || addressbookList.isEmpty()) {
                tv_no_list.setVisibility(View.VISIBLE);
                rv_address_list.setVisibility(View.GONE);
            } else {
                tv_no_list.setVisibility(View.GONE);
                rv_address_list.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
        editAddressBook(pos);
    }
}
