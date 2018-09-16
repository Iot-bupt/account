package cn.edu.bupt.dao.Tenant;

import cn.edu.bupt.entity.Tenant;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * Created by CZX on 2018/4/8.
 */
@Mapper
public interface TenantRepository{

    @Insert("insert into tenant (additional_info,address,email,phone,title) values (#{additional_info},#{address},#{email},#{phone},#{title}) ")
    @Options(useGeneratedKeys = true)
    void save(Tenant tenant);

    @Update("update tenant set address = #{address},phone = #{phone},title = #{title},email = #{email},additional_info = #{additional_info} where id=#{id}")
    void update(Tenant tenant);

    @Delete("delete from tenant where id=#{id}")
    void deleteById(int id);

    @Select("select  id  as id,additional_info as  additional_info,address as address, email as email,phone as phone,title as title,suspended as suspended from tenant where id > 1 limit #{index},#{pageSize} ")
    List<Tenant> findAll(@Param("index")Integer index,@Param("pageSize")Integer pageSize);

    @Select("select  count(*) from tenant")
    Integer findAllCount();

    @Select("select  id  as id,additional_info as  additional_info,address as address, email as email,phone as phone,title as title,suspended as suspended from tenant where id = #{id}")
    Tenant findById(int id);

    @Select("select suspended from tenant where id = #{id}")
    Boolean findSuspendedStatusById(int id);

    @Update("update tenant set suspended = #{suspended} where id=#{id}")
    void updateSuspendedStatus(@Param("suspended")Boolean suspended,@Param("id")Integer id);

}
