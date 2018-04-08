package cn.edu.bupt.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by CZX on 2018/4/8.
 */
@Entity
@Table(name = "user_credentials")
public class UserCredentials {
    @Id
    private String id;
    private String user_id;
    private String password;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "UserCredentials{" +
                "id='" + id + '\'' +
                ", user_id='" + user_id + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
