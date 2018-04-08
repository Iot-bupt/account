package cn.edu.bupt.entity;

import javax.persistence.*;

/**
 * Created by CZX on 2018/4/8.
 */
@Entity
@Table(name = "user_credentials")
public class UserCredentials {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;
    private Integer user_id;
    private String password;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
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
