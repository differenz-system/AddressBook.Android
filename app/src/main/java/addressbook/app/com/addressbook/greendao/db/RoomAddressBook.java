package addressbook.app.com.addressbook.greendao.db;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "address_book")
public class RoomAddressBook implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    private long id;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "email")
    private String email;

    @ColumnInfo(name = "contact_number")
    private String contactNumber;

    @ColumnInfo(name = "is_active")
    private boolean isActive;

    public RoomAddressBook() {
    }

    protected RoomAddressBook(Parcel in) {
        id = in.readInt();
        name = in.readString();
        email = in.readString();
        contactNumber = in.readString();
        isActive = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt((int) id);
        dest.writeString(name);
        dest.writeString(email);
        dest.writeString(contactNumber);
        dest.writeByte((byte) (isActive ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<RoomAddressBook> CREATOR = new Creator<RoomAddressBook>() {
        @Override
        public RoomAddressBook createFromParcel(Parcel in) {
            return new RoomAddressBook(in);
        }

        @Override
        public RoomAddressBook[] newArray(int size) {
            return new RoomAddressBook[size];
        }
    };

    // Getters and Setters
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(boolean active) {
        isActive = active;
    }
}
