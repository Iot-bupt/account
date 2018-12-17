package cn.edu.bupt.dao.Field;

import cn.edu.bupt.entity.User;
import cn.edu.bupt.entity.UserField;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface FieldRepository {

    @Select("select name,`value` from user_fields inner join user_fields_relation ufr on user_fields.id = ufr.field_id and ufr.user_id=#{id}")
    List<UserField> findFieldsByUserId(int id);

    @Insert("insert into user_fields (tenant_id,name,`desc`) values (#{tenant_id},#{name},#{desc})")
    @Options(useGeneratedKeys = true,keyProperty="id")
    void save(UserField userField);

    @Insert("insert into user_fields_relation (user_id,field_id,`value`) values (#{user_id},#{field_id},#{value})")
    void saveRelation(@Param("user_id")Integer user_id, @Param("field_id")Integer field_id, @Param("value")String value);

    @Select("select * from user_fields where tenant_id=#{tenant_id}")
    List<UserField> findFieldsByTenantId(int tenant_id);

    @Select("select id from user_fields where tenant_id = #{tenant_id} and name = #{name}")
    Integer findFieldId(@Param("tenant_id")Integer tenant_id,@Param("name")String name);

    @Delete("delete from user_fields where id = #{id}")
    void deleteById(Integer id);

    @Delete("delete from user_fields_relation where field_id = #{field_id}")
    void deleteRelationByFieldId(Integer field_id);

    @Delete("delete from user_fields_relation where user_id = #{user_id}")
    void deleteRelationByUserId(Integer user_id);

    @Update("update user_fields set `desc` = #{desc} where id=#{id}")
    void updateField(@Param("id")Integer id,@Param("desc")String desc);

    @Update("update user_fields_relation set `value` = #{value} where user_id = #{user_id} and field_id = #{field_id}")
    void updateValue(@Param("value")String value,@Param("user_id")Integer user_id,@Param("field_id")Integer field_id);



}
