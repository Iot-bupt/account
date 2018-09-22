package cn.edu.bupt.dao.permission;

import cn.edu.bupt.entity.Permission;
import cn.edu.bupt.entity.User;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Set;

/**
 * Created by CZX on 2018/9/3.
 */
@Mapper
public interface PermissionRepository {

    @Select("select  id  as id,name as name,description as description from permission")
    List<Permission> findAll();

    @Select("select permission_id from role_permission_relation where role_id = #{role_id}")
    List<Integer> findAllIDsByRoleId(int role_id);

    @Select("select name from permission where id in (select permission_id from role_permission_relation where role_id = #{role_id})")
    Set<String> findAllNamesByRoleId(int role_id);

    @Select("select id  as id,name as name,description as description from permission where id in (select permission_id from role_permission_relation where role_id = #{role_id})")
    List<Permission> findAllByRoleId(int role_id);

    @Select("select id  as id,name as name,description as description from permission where id not in (select permission_id from role_permission_relation where role_id = #{role_id})")
    List<Permission> findAllNotOwnedByRoleId(int role_id);

    @Select("select  id  as id,name as name,description as description from permission where id = #{id}")
    Permission findById(int id);

    @Insert("insert into role_permission_relation (role_id,permission_id) values (#{role_id},#{permission_id}) ")
    void saveRolePermissionRelation(@Param("role_id")int role_id, @Param("permission_id")int permission_id);

    @Delete("delete from role_permission_relation where role_id = #{role_id} and permission_id = #{permission_id}")
    void deleteARelation(@Param("role_id")int role_id, @Param("permission_id")int permission_id);

    @Select("select id  as id,name as name,description as description from permission where id in (select permission_id from role_permission_relation where role_id in (select role_id from role_user_relation where user_id = #{user_id}))")
    Set<Permission> findAllByUserId(int user_id);

    @Select("select id  as id,name as name,description as description from permission where id in (select permission_id from role_permission_relation where role_id in (select id from role where name = #{authority}))")
    Set<Permission> findBaseByUserAuthority(String authority);
}
