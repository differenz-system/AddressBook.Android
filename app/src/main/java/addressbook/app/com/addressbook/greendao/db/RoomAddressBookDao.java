package addressbook.app.com.addressbook.greendao.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface RoomAddressBookDao {

    @Insert
    void insert(RoomAddressBook addressBook);

    @Update
    void update(RoomAddressBook addressBook);

    @Delete
    void delete(RoomAddressBook addressBook);

    @Query("SELECT * FROM address_book WHERE id = :id")
    RoomAddressBook getAddressBookById(long id);

    @Query("SELECT * FROM address_book")
    List<RoomAddressBook> getAllAddressBooks();

    @Query("DELETE FROM address_book")
    void deleteAll();
}
