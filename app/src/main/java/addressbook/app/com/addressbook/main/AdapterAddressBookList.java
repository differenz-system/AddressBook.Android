package addressbook.app.com.addressbook.main;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import java.util.ArrayList;

import addressbook.app.com.addressbook.R;
import addressbook.app.com.addressbook.greendao.db.AddressBook;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by mac on 10/4/17.
 */

public class AdapterAddressBookList extends RecyclerView.Adapter<AdapterAddressBookList.ViewHolder> {

    private ArrayList<AddressBook> mValues;
    private AdapterView.OnItemClickListener onItemClickListener;
    private Context context;

    public AdapterAddressBookList(Context context) {
        this.context = context;
    }

    public void doRefresh(ArrayList<AddressBook> addressbookList) {
        this.mValues = addressbookList;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_addressbook, parent, false);
        return new ViewHolder(view, this);
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private AdapterAddressBookList adapterAddressBookList;

        @BindView(R.id.tv_name)
        AppCompatTextView tv_name;

        @BindView(R.id.tv_email)
        AppCompatTextView tv_email;

        @BindView(R.id.tv_contact_number)
        AppCompatTextView tv_contact_number;

        public ViewHolder(View itemView, AdapterAddressBookList adapterAddressBookList) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            this.adapterAddressBookList = adapterAddressBookList;
            itemView.setOnClickListener(this);
        }

        void setDataToView(AddressBook addressBookItemModel, ViewHolder viewHolder) {

            viewHolder.tv_name.setText(addressBookItemModel.getName());
            viewHolder.tv_email.setText(addressBookItemModel.getEmail());
            viewHolder.tv_contact_number.setText(addressBookItemModel.getContact_number());
        }

        @Override
        public void onClick(View v) {
            adapterAddressBookList.onItemHolderClick(ViewHolder.this);
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        AddressBook addressBookItemModel = mValues.get(position);
        holder.setDataToView(addressBookItemModel, holder);
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public void setOnItemClickListener(AdapterView.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    private void onItemHolderClick(ViewHolder holder) {
        if (onItemClickListener != null)
            onItemClickListener.onItemClick(null, holder.itemView, holder.getAdapterPosition(), holder.getItemId());
    }
}
