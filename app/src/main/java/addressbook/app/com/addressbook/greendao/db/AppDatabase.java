package addressbook.app.com.addressbook.greendao.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {RoomAddressBook.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase INSTANCE;
    public abstract RoomAddressBookDao addressBookDao();

    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "address_book_db")
                    .fallbackToDestructiveMigration()
                    //.allowMainThreadQueries() // For simplicity in examples, remove this in production
                    .build();
        }
        return INSTANCE;
    }
}