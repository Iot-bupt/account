package cn.edu.bupt.entity;

import javax.persistence.*;

/**
 * Created by CZX on 2018/4/8.
 */
@Entity
@Table(name = "user_credentials")
public class UserCredentials extends IdBased{
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;

    @OneToOne
    @JoinColumn(name="user_id",referencedColumnName = "id")
    private User user;
    private String password;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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
                "id=" + id +
                ", user=" + user +
                ", password='" + password + '\'' +
                '}';
    }
}
