package cn.edu.bupt.dao.Tenant;

import cn.edu.bupt.entity.Tenant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by CZX on 2018/4/8.
 */
public interface TenantRepository extends JpaRepository<Tenant, Integer>,JpaSpecificationExecutor<Tenant> {

}
