package cn.edu.bupt.entity;

/**
 * @Description: UserField
 * @Author: czx
 * @CreateDate: 2018-12-17 15:58
 * @Version: 1.0
 */
public class UserField {

    private Integer id;

    private Integer tenant_id;

    private String name;

    private String desc;

    private String value;

    public UserField(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public UserField(Integer tenant_id, String name, String desc) {
        this.tenant_id = tenant_id;
        this.name = name;
        this.desc = desc;
    }

    public UserField(Integer id, Integer tenant_id, String name, String desc) {
        this.id = id;
        this.tenant_id = tenant_id;
        this.name = name;
        this.desc = desc;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

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

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{");
        sb.append("\"id\":")
                .append(id);
        sb.append(",\"tenant_id\":")
                .append(tenant_id);
        sb.append(",\"name\":\"")
                .append(name).append('\"');
        sb.append(",\"desc\":\"")
                .append(desc).append('\"');
        sb.append('}');
        return sb.toString();
    }
}
