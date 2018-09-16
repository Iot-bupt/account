package cn.edu.bupt.entity;


/**
 * Created by CZX on 2018/4/8.
 */
//@Entity
public class Tenant extends IdBased{

//    @Id
//    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;
    private String address;
    private String phone;
    private String title;
    private String additional_info;
    private String email;
    private Boolean suspended = Boolean.FALSE;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAdditional_info() {
        return additional_info;
    }

    public void setAdditional_info(String additional_info) {
        this.additional_info = additional_info;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getSuspended() {
        return suspended;
    }

    public void setSuspended(Boolean suspended) {
        this.suspended = suspended;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{");
        sb.append("\"id\":")
                .append(id);
        sb.append(",\"address\":\"")
                .append(address).append('\"');
        sb.append(",\"phone\":\"")
                .append(phone).append('\"');
        sb.append(",\"title\":\"")
                .append(title).append('\"');
        sb.append(",\"additional_info\":\"")
                .append(additional_info).append('\"');
        sb.append(",\"email\":\"")
                .append(email).append('\"');
        sb.append(",\"suspendedStatus\":\"")
                .append(suspended).append('\"');
        sb.append('}');
        return sb.toString();
    }
}
