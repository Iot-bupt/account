package cn.edu.bupt.dao.User;

import cn.edu.bupt.entity.Authority;
import cn.edu.bupt.entity.Customer;
import cn.edu.bupt.entity.Tenant;
import cn.edu.bupt.entity.User;
import org.apache.ibatis.annotations.*;


import java.util.List;

/**
 * Created by CZX on 2018/4/8.
 */
@Mapper
public interface UserRepository{

    @Select("select u.id as id,u.tenant_id as tenantId,u.customer_id as customerId,r.name as authority,u.name as name,u.additional_info as additional_info,u.email as email,u.phone as phone,u.we_chat as we_chat from user u,role r where u.base_role_id = r.id and email = #{email}")
    User findUserByEmail(String email);

    @Select("select u.id as id,u.tenant_id as tenantId,u.customer_id as customerId,r.name as authority,u.name as name,u.additional_info as additional_info,u.email as email,u.phone as phone,u.we_chat as we_chat from user u,role r where u.base_role_id = r.id and u.id = #{id}")
    User findById(Integer id);

    @Select("select u.id as id,u.tenant_id as tenantId,u.customer_id as customerId,r.name as authority,u.name as name,u.additional_info as additional_info,u.email as email,u.phone as phone,u.we_chat as we_chat from user u,role r where u.base_role_id = r.id and u.tenant_id = #{tenantId}")
    List<User> findByTenantId(Integer tenantId);

    @Select("select u.id as id,u.tenant_id as tenantId,u.customer_id as customerId,r.name as authority,u.name as name,u.additional_info as additional_info,u.email as email,u.phone as phone,u.we_chat as we_chat from user u,role r where u.base_role_id = r.id and tenant_id = #{tenant_id} and base_role_id = 2 limit #{index},#{pageSize}")
    List<User> findTenantAdmins(@Param("index")Integer index,@Param("pageSize")Integer pageSize,@Param("tenant_id")Integer tenant_id);

    @Select("select count(*) from user where tenant_id = #{tenant_id} and base_role_id = 2")
    Integer findTenantAdminsCount(Integer tenant_id);

    @Select("select u.id as id,u.tenant_id as tenantId,u.customer_id as customerId,r.name as authority,u.name as name,u.additional_info as additional_info,u.email as email,u.phone as phone,u.we_chat as we_chat from user u,role r where u.base_role_id = r.id and base_role_id = 3 and tenant_id = #{tenant_id} and customer_id = #{customer_id} limit #{index},#{pageSize}")
    List<User> findCustomerUsers(@Param("index")Integer index,@Param("pageSize")Integer pageSize,@Param("tenant_id")Integer tenant_id,@Param("customer_id")Integer customer_id);

    @Select("select count(*) from user where tenant_id = #{tenant_id} and customer_id = #{customer_id}")
    Integer findCustomerUsersCount(@Param("tenant_id")Integer tenant_id,@Param("customer_id")Integer customer_id);

    @Delete("delete from user where customer_id = #{customerId}")
    void deleteAllByCustomerId(Integer customerId);

    @Delete("delete from user where id = #{id}")
    void deleteById(Integer id);

//    @Delete("delete from user where tenant_id = #{tenantId} and authority = #{authority}")
//    void deleteAllByTenantIdAndAuthority(@Param("tenantId")Integer tenantId, @Param("authority")Authority authority);

    @Insert("insert into user (tenant_id,customer_id,name,additional_info,email,phone,we_chat,base_role_id) values (#{tenantId},#{customerId},#{name},#{additional_info},#{email},#{phone},#{we_chat},#{roleId}) ")
    @Options(useGeneratedKeys = true,keyProperty="id")
    Integer save(User user);

    @Update("update user set customer_id = #{customerId},name = #{name},additional_info = #{additional_info},email = #{email},phone = #{phone},we_chat = #{we_chat} where id=#{id}")
    void update(User user);

}
