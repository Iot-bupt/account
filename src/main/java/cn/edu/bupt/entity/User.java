package cn.edu.bupt.entity;


/**
 * Created by CZX on 2018/4/8.
 */
//@Entity
public class User extends IdBased{

//    @Id
//    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;

//    @ManyToOne
//    @JoinColumn(name="tenant_id",referencedColumnName = "id")
    private Integer tenantId;

//    @ManyToOne
//    @JoinColumn(name="customer_id",referencedColumnName = "id")
    private Integer customerId;

//    @Enumerated(EnumType.STRING)
    private Authority authority;

    private String name;
    private String additional_info;
    private String email;
    private String phone;
    private String we_chat;

    public User() {
    }

    public User(User user) {
        this.id = user.getId();
        this.tenantId = user.getTenantId();
        this.customerId = user.getCustomerId();
        this.authority = user.getAuthority();
        this.name = user.getName();
        this.additional_info = user.getAdditional_info();
        this.email = user.getEmail();
        this.phone = user.getPhone();
        this.we_chat = user.getWe_chat();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public Integer getTenantId() {
        return tenantId;
    }

    public void setTenantId(Integer tenantId) {
        this.tenantId = tenantId;
    }

    public Authority getAuthority() {
        return authority;
    }

    public void setAuthority(Authority authority) {
        this.authority = authority;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getWe_chat() {
        return we_chat;
    }

    public void setWe_chat(String we_chat) {
        this.we_chat = we_chat;
    }

    //    @Override
//    public String toString() {
//        final StringBuilder sb = new StringBuilder("{");
//        sb.append("\"id\":")
//                .append(id);
//        sb.append(",\"tenant_id\":")
//                .append(tenantId);
//        sb.append(",\"customer_id\":")
//                .append(customerId);
//        sb.append(",\"authority\":")
//                .append(authority);
//        sb.append(",\"name\":\"")
//                .append(name).append('\"');
//        sb.append(",\"additional_info\":\"")
//                .append(additional_info).append('\"');
//        sb.append(",\"email\":\"")
//                .append(email).append('\"');
//        sb.append('}');
//        return sb.toString();
//    }


//    @Override
//    public String toString() {
//        final StringBuilder sb = new StringBuilder("{");
//        sb.append("\"id\":")
//                .append(id);
//        sb.append(",\"tenant_id\":")
//                .append(tenantId);
//        sb.append(",\"customer_id\":")
//                .append(customerId);
//        sb.append(",\"authority\":")
//                .append("\""+authority.name()+"\"");
//        sb.append(",\"name\":\"")
//                .append(name).append('\"');
//        sb.append(",\"additional_info\":\"")
//                .append(additional_info).append('\"');
//        sb.append(",\"email\":\"")
//                .append(email).append('\"');
//        sb.append(",\"phone\":\"")
//                .append(phone).append('\"');
//        sb.append(",\"we_chat\":\"")
//                .append(we_chat).append('\"');
//        sb.append('}');
//        return sb.toString();
//    }


    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{");
        sb.append("\"id\":")
                .append(id);
        sb.append(",\"tenant_id\":")
                .append(tenantId);
        sb.append(",\"customer_id\":")
                .append(customerId);
        sb.append(",\"authority\":")
                .append(authority);
        sb.append(",\"name\":\"")
                .append(name).append('\"');
        sb.append(",\"additional_info\":\"")
                .append(additional_info).append('\"');
        sb.append(",\"email\":\"")
                .append(email).append('\"');
        sb.append(",\"phone\":\"")
                .append(phone).append('\"');
        sb.append(",\"we_chat\":\"")
                .append(we_chat).append('\"');
        sb.append('}');
        return sb.toString();
    }
}
