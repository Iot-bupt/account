package cn.edu.bupt.dao.Customer;

import cn.edu.bupt.entity.Customer;
import cn.edu.bupt.entity.Tenant;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Optional;

/**
 * Created by CZX on 2018/4/8.
 */
@Mapper
public interface CustomerRepository{

//    void deleteAllByTenant(Tenant tenant);

    @Select("select id as id,tenant_id as tenantId,additional_info as additional_info,address as address, email as email,phone as phone,title as title from customer where tenant_id = #{tenantId}")
    List<Customer> findAllByTenantId(@Param("tenantId")Integer tenantId);

    @Select("select id as id,tenant_id as tenantId,additional_info as additional_info,address as address, email as email,phone as phone,title as title from customer where tenant_id = #{tenantId} limit #{index},#{pageSize}")
    List<Customer> findAllByTenantIdAndPage(@Param("index")Integer index,@Param("pageSize")Integer pageSize,@Param("tenantId")Integer tenantId);

    @Select("select count(*) from customer where tenant_id = #{tenantId}")
    Integer findAllByTenantIdCount(Integer tenantId);

    @Select("select title from customer where tenant_id = #{tenantId} and id = #{customerId}")
    String findNameByTenantAndCustomer(@Param("tenantId")Integer tenantId,@Param("customerId")Integer customerId);

    @Insert("insert into customer (tenant_id,additional_info,address,email,phone,title) values (#{tenantId},#{additional_info},#{address},#{email},#{phone},#{title})")
    @Options(useGeneratedKeys = true,keyProperty="id")
    void save(Customer customer);

    @Update("update customer set address = #{address},phone = #{phone},title = #{title},email = #{email},additional_info = #{additional_info} where id=#{id}")
    void update(Customer customer);

    @Delete("delete from customer where id=#{id}")
    void deleteById(int id);

    @Select("select id as id,tenant_id as tenantId,additional_info as additional_info,address as address, email as email,phone as phone,title as title from customer where id = #{id}")
    Customer findById(Integer id);

    @Select("select id as id,tenant_id as tenantId,additional_info as additional_info,address as address, email as email,phone as phone,title as title from customer where id > 1")
    List<Customer> findAll();
}
