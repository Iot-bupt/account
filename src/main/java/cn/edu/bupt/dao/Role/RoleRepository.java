package cn.edu.bupt.dao.Role;

import cn.edu.bupt.entity.Role;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * Created by CZX on 2018/9/4.
 */
@Mapper
public interface RoleRepository {

    @Select("select  id  as id,name as name,description as description from role")
    List<Role> findAll();

    @Select("select id  as id,name as name,description as description from role where id in (select role_id from role_user_relation where user_id = #{user_id})")
    List<Role> findAllByUserId(int user_id);

    @Select("select id  as id,name as name,description as description from role where id in (select role_id from role_user_relation where user_id = #{user_id}) and id > 3")
    List<Role> findExtraByUserId(int user_id);

    @Select("select  id  as id,name as name,description as description from role where id=#{id}")
    Role findById(Integer id);

    @Insert("insert into role (name,description) values (#{name},#{description}) ")
    @Options(useGeneratedKeys = true,keyProperty="id")
    Integer saveRole(Role role);

    @Delete("delete from role where id = #{id}")
    void deleteById(Integer id);

    @Update("update role set name = #{name},description = #{description} where id=#{id}")
    void update(Role role);

    @Delete("delete from role_user_relation where role_id = #{role_id}")
    void deleteRoleUserRelationByRoleId(Integer role_id);

    @Delete("delete from role_permission_relation where role_id = #{role_id}")
    void deleteRolePermissionRelation(Integer role_id);

    @Insert("insert into role_user_relation (role_id,user_id) values (#{role_id},#{user_id}) ")
    void saveRoleUserRelation(@Param("role_id")Integer role_id,@Param("user_id")Integer user_id);

    @Delete("delete from role_user_relation where role_id = #{role_id} and user_id = #{user_id}")
    void deleteRoleUserRelation(@Param("role_id")Integer role_id,@Param("user_id")Integer user_id);

}
