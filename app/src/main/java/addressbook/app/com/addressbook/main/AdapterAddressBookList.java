package addressbook.app.com.addressbook.main;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import addressbook.app.com.addressbook.databinding.ItemListAddressbookBinding;
import addressbook.app.com.addressbook.greendao.db.RoomAddressBook;
/*import butterknife.BindView;
import butterknife.ButterKnife;*/

/**
 * Created by mac on 10/4/17.
 */

public class AdapterAddressBookList extends RecyclerView.Adapter<AdapterAddressBookList.ViewHolder> {

    private ArrayList<RoomAddressBook> mValues;
    private AdapterView.OnItemClickListener onItemClickListener;
    private final Context context;

    public AdapterAddressBookList(Context context) {
        this.context = context;
    }

    public void doRefresh(ArrayList<RoomAddressBook> addressBookList) {
        this.mValues = addressBookList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        /*View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_addressbook, parent, false);
        return new ViewHolder(view, this);*/
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemListAddressbookBinding binding = ItemListAddressbookBinding.inflate(inflater, parent, false);
        return new ViewHolder(binding, this);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        AdapterAddressBookList adapterAddressBookList;
        ItemListAddressbookBinding binding;

        public ViewHolder(@NonNull ItemListAddressbookBinding binding, AdapterAddressBookList adapterAddressBookList) {
            super(binding.getRoot());
            this.binding = binding;
            this.adapterAddressBookList = adapterAddressBookList;
            itemView.setOnClickListener(this);
        }

        /*@BindView(R.id.tv_name)
        AppCompatTextView tv_name;

        @BindView(R.id.tv_email)
        AppCompatTextView tv_email;

        @BindView(R.id.tv_contact_number)
        AppCompatTextView tv_contact_number;*/

        /*public ViewHolder(View itemView, AdapterAddressBookList adapterAddressBookList) {
            super(itemView);
            *//*ButterKnife.bind(this, itemView);*//*
        }*/

        void setDataToView(RoomAddressBook addressBookItemModel) {
            binding.tvName.setText(addressBookItemModel.getName());
            binding.tvEmail.setText(addressBookItemModel.getEmail());
            binding.tvContactNumber.setText(addressBookItemModel.getContactNumber());
        }

        @Override
        public void onClick(View v) {
            adapterAddressBookList.onItemHolderClick(ViewHolder.this);
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        RoomAddressBook addressBookItemModel = mValues.get(position);
        holder.setDataToView(addressBookItemModel);
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public void setOnItemClickListener(AdapterView.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    private void onItemHolderClick(ViewHolder holder) {
        Log.e("Adapter","onclick");
        if (onItemClickListener != null)
            onItemClickListener.onItemClick(null, holder.itemView, holder.getAdapterPosition(), holder.getItemId());
    }
}
