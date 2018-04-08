package cn.edu.bupt.entity;

import java.io.Serializable;

/**
 * Created by CZX on 2018/4/8.
 */
public class CustomerId implements Serializable {

    private String tenant_id;
    private String id;

    public String getTenant_id() {
        return tenant_id;
    }

    public void setTenant_id(String tenant_id) {
        this.tenant_id = tenant_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
