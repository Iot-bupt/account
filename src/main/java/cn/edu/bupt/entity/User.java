package cn.edu.bupt.entity;


import java.util.List;

/**
 * Created by CZX on 2018/4/8.
 */
//@Entity
public class User extends IdBased{

    private Integer id;
    private Integer tenantId;
    private Integer customerId;
    private Authority authority;
    private String name;
    private String additional_info;
    private String email;
    private String phone;
    private String we_chat;
    private List<Role> roles;
    private Integer roleId;
    private List<UserField> userFields;

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
        this.roles = user.getRoles();
        this.roleId = user.getRoleId();

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
        setRoleId(authority.ordinal()+1);
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

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        for(Role role:roles){
            if(role.getId()<4){
                this.authority = Authority.parse(role.getName());
                roles.remove(role);
                break;
            }
        }
        this.roles = roles;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public List<UserField> getUserFields() {
        return userFields;
    }

    public void setUserFields(List<UserField> userFields) {
        this.userFields = userFields;
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
                .append("\""+authority.name()+"\"");
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
        for(UserField userField:this.userFields) {
            sb.append(",\""+userField.getName()+"\":\"")
                    .append(userField.getValue()).append('\"');
        }
        sb.append('}');
        return sb.toString();
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
//        sb.append(",\"phone\":\"")
//                .append(phone).append('\"');
//        sb.append(",\"we_chat\":\"")
//                .append(we_chat).append('\"');
//        sb.append('}');
//        return sb.toString();
//    }
}
