package cn.edu.bupt.entity;


/**
 * Created by CZX on 2018/4/8.
 */
//@Entity
//@Table(name = "user_credentials")
public class UserCredentials extends IdBased{
//    @Id
//    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;
    private Integer userId;
    private String password;

    public UserCredentials() {
    }

    public UserCredentials(Integer userId, String password) {
        this.userId = userId;
        this.password = password;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{");
        sb.append("\"id\":")
                .append(id);
        sb.append(",\"user\":")
                .append(userId);
        sb.append(",\"password\":\"")
                .append(password).append('\"');
        sb.append('}');
        return sb.toString();
    }
}
