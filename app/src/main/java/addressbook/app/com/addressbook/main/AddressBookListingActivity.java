package addressbook.app.com.addressbook.main;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import addressbook.app.com.addressbook.R;
import addressbook.app.com.addressbook.databinding.ActivityAddressBookListingBinding;
import addressbook.app.com.addressbook.greendao.db.AppDatabase;
import addressbook.app.com.addressbook.greendao.db.RoomAddressBook;
import addressbook.app.com.addressbook.utility.BaseAppCompatActivity;
import addressbook.app.com.addressbook.utility.Constant;
import addressbook.app.com.addressbook.utility.Globals;
/*import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;*/

public class AddressBookListingActivity extends BaseAppCompatActivity implements AdapterView.OnItemClickListener {

    /*@BindView(R.id.toolbar)
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
    AppCompatTextView tv_no_list;*/

    //private DaoSession daoSession;

    private ArrayList<RoomAddressBook> addressbookList;
    private AdapterAddressBookList adapterAddressBookList;
    Globals globals;
    public static int ADD_EDIT_ADDRESS_REQ_CODE = 1010;
    private ActivityAddressBookListingBinding binding;
    private AppDatabase addressBookDatabase;

    private ExecutorService executorService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddressBookListingBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        /*setContentView(R.layout.activity_address_book_listing);
        ButterKnife.bind(this);*/

        executorService = Executors.newSingleThreadExecutor();

        init();
    }

    private void init() {
        globals = (Globals) getApplicationContext();
        //daoSession = Globals.getInstance().getDaoSession();
        addressBookDatabase = AppDatabase.getInstance(this);

        setSupportActionBar(binding.toolbar.getRoot());
        binding.toolbar.toolbarTitle.setText(getString(R.string.lbl_address_book));
        binding.toolbar.toolbarLeft.setVisibility(View.VISIBLE);
        binding.toolbar.toolbarRight.setVisibility(View.VISIBLE);
        binding.toolbar.toolbarLeft.setText(getString(R.string.action_logout));
        binding.toolbar.toolbarRight.setText(getString(R.string.action_add));

        binding.toolbar.toolbarRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addCLick();
            }
        });

        binding.toolbar.toolbarLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logoutClick();
            }
        });

        setUpList();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_EDIT_ADDRESS_REQ_CODE && resultCode == RESULT_OK) {
            setUpList();
        }
    }

    //@OnClick(R.id.toolbar_right)
    public void addCLick() {
        saveAddressBook();
    }

    private void saveAddressBook() {
        Intent i_edit_remove_address_book = new Intent(getContext(), EditRemoveAddressBookActivity.class);
        someActivityResultLauncher.launch(i_edit_remove_address_book);
    }

    private void editAddressBook(int pos) {
        Intent i_edit_remove_address_book = new Intent(getContext(), EditRemoveAddressBookActivity.class);
        i_edit_remove_address_book.putExtra(Constant.Key_editAddressBook, addressbookList.get(pos));
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

    //@OnClick(R.id.toolbar_left)
    public void logoutClick() {
        globals.setUserDetails(null);
        Globals.logoutProcess(getContext());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        executorService.shutdown();
    }

    private void setUpList() {
        /*AddressBookDao addressBookDao = daoSession.getAddressBookDao();
        addressbookList = (ArrayList<AddressBook>) addressBookDao.loadAll();
        setAdapter();*/
        /*List<RoomAddressBook> dbAddressBookList = addressBookDatabase.addressBookDao().getAllAddressBooks();
        addressbookList = new ArrayList<>(dbAddressBookList);  // Convert List to ArrayList if necessary
        setAdapter();*/
        executorService.execute(() -> {
            List<RoomAddressBook> dbAddressBookList = addressBookDatabase.addressBookDao().getAllAddressBooks();
            runOnUiThread(() -> {
                addressbookList = new ArrayList<>(dbAddressBookList);  // Convert List to ArrayList if necessary
                Log.e("AddressBookListingActivity"," -- " + addressbookList);
                setAdapter();
            });
        });
    }

    public void setAdapter() {
        if (addressbookList != null && !addressbookList.isEmpty()) {
            if (adapterAddressBookList == null) {
                adapterAddressBookList = new AdapterAddressBookList(getContext());
                adapterAddressBookList.setOnItemClickListener(this);
            }
            adapterAddressBookList.doRefresh(addressbookList);

            if (binding.rvAddressList.getAdapter() == null) {
                binding.rvAddressList.setHasFixedSize(false);
                binding.rvAddressList.setLayoutManager(new LinearLayoutManager(getContext()));
                binding.rvAddressList.setAdapter(adapterAddressBookList);
                binding.rvAddressList.setNestedScrollingEnabled(false);
                binding.rvAddressList.setFocusable(false);
            }
        }
        handleEmptyList();
    }

    public void handleEmptyList() {
        if (binding.tvNoList != null) {
            if (addressbookList == null || addressbookList.isEmpty()) {
                binding.tvNoList.setVisibility(View.VISIBLE);
                binding.rvAddressList.setVisibility(View.GONE);
            } else {
                binding.tvNoList.setVisibility(View.GONE);
                binding.rvAddressList.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
        Log.e("AddressBookListingActivity"," -- onItemClick ID = " + l);
        editAddressBook(pos);
    }
}
