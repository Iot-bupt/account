package cn.edu.bupt.entity;

/**
 * Created by CZX on 2018/9/3.
 */
public class Permission {

    private int id;

    private String name;

    private String description;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Permission(Integer id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{");
        sb.append("\"id\":")
                .append(id);
        sb.append(",\"name\":\"")
                .append(name).append('\"');
        sb.append(",\"description\":\"")
                .append(description).append('\"');
        sb.append('}');
        return sb.toString();
    }
}
