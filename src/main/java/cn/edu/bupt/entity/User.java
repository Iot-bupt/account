package cn.edu.bupt.entity;

import javax.persistence.*;

import java.io.Serializable;

/**
 * Created by CZX on 2018/4/8.
 */
@Entity
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;
    private Integer tenant_id;
    private Integer customer_id;
    private String authority;
    private String name;
    private String additional_info;
    private String email;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getTenant_id() {
        return tenant_id;
    }

    public void setTenant_id(Integer tenant_id) {
        this.tenant_id = tenant_id;
    }

    public Integer getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(Integer customer_id) {
        this.customer_id = customer_id;
    }

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
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
        return "User{" +
                "id='" + id + '\'' +
                ", tenant_id='" + tenant_id + '\'' +
                ", customer_id='" + customer_id + '\'' +
                ", authority='" + authority + '\'' +
                ", name='" + name + '\'' +
                ", additional_info='" + additional_info + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
