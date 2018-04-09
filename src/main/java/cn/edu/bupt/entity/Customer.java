package cn.edu.bupt.entity;

import javax.persistence.*;
import java.io.Serializable;
/**
 * Created by CZX on 2018/4/8.
 */
@Entity
public class Customer implements Serializable{

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name="tenant_id",referencedColumnName = "id")
    private Tenant tenant;
    private String address;
    private String phone;
    private String title;
    private String additional_info;
    private String email;
//
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

    @Override
    public String toString() {
        return "Customer{" +
                "id=" + id +
                ", tenant=" + tenant +
                ", address='" + address + '\'' +
                ", phone='" + phone + '\'' +
                ", title='" + title + '\'' +
                ", additional_info='" + additional_info + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
