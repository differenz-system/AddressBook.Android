package addressbook.app.com.addressbook.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by mac on 9/28/17.
 */

public class UserLoginDetail implements Serializable {

    @SerializedName("data")
    public Data data;

    public static class Data implements Serializable {
        @SerializedName("Password")
        public String password;
        @SerializedName("Email")
        public String email;
    }



}
