package top.golvaje.me.model;

import java.io.Serializable;

/**
 * Created by Administrator on 7/17/2017.
 */

public class UserModel implements Serializable {

    private String name;
    private String id;
    private String email;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
