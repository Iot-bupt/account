package cn.edu.bupt.dao;

import cn.edu.bupt.entity.Tenant;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by CZX on 2018/4/8.
 */
public interface TenantRepository extends CrudRepository<Tenant, String> {

}
