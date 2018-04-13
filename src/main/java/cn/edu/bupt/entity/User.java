package cn.edu.bupt.entity;

import javax.persistence.*;

/**
 * Created by CZX on 2018/4/8.
 */
@Entity
public class User extends IdBased{

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name="tenant_id",referencedColumnName = "id")
    private Tenant tenant;

    @ManyToOne
    @JoinColumn(name="customer_id",referencedColumnName = "id")
    private Customer customer;

    @Enumerated(EnumType.STRING)
    private Authority authority;

    private String name;
    private String additional_info;
    private String email;

    public User() {
    }

    public User(User user) {
        this.id = user.getId();
        this.tenant = user.getTenant();
        this.customer = user.getCustomer();
        this.authority = user.getAuthority();
        this.name = user.getName();
        this.additional_info = user.getAdditional_info();
        this.email = user.getEmail();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Tenant getTenant() {
        return tenant;
    }

    public void setTenant(Tenant tenant) {
        this.tenant = tenant;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
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

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{");
        sb.append("\"id\":")
                .append(id);
        sb.append(",\"tenant_id\":")
                .append(tenant.getId());
        sb.append(",\"customer_id\":")
                .append(customer.getId());
        sb.append(",\"authority\":")
                .append(authority);
        sb.append(",\"name\":\"")
                .append(name).append('\"');
        sb.append(",\"additional_info\":\"")
                .append(additional_info).append('\"');
        sb.append(",\"email\":\"")
                .append(email).append('\"');
        sb.append('}');
        return sb.toString();
    }
}
