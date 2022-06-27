package addressbook.app.com.addressbook.greendao;


import org.greenrobot.greendao.generator.DaoGenerator;
import org.greenrobot.greendao.generator.Entity;
import org.greenrobot.greendao.generator.Schema;


/**
 * Created by mac on 10/5/17.
 */

public class ABDaoGenerator {

    public static void main(String[] args) {
        Schema schema = new Schema(1, "addressbook.app.com.addressbook.greendao.db"); // Your app package name and the (.db) is the folder where the DAO files will be generated into.
        schema.enableKeepSectionsByDefault();

        addTables(schema);

        try {
            new DaoGenerator().generateAll(schema,"./app/src/main/java");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void addTables(final Schema schema) {
        addAddressBookEntities(schema);
    }

    // This is use to describe the colums of your table
    private static Entity addAddressBookEntities(final Schema schema) {
        Entity addressBook = schema.addEntity("AddressBook");
        addressBook.addIdProperty().primaryKey().autoincrement();
        addressBook.addStringProperty("name");
        addressBook.addStringProperty("email");
        addressBook.addStringProperty("contact_number");
        addressBook.addBooleanProperty("isactive");
        return addressBook;
    }

}
