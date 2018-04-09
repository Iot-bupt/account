package cn.edu.bupt.dao.Tenant;

import cn.edu.bupt.entity.Tenant;
import org.springframework.data.domain.Page;

/**
 * Created by CZX on 2018/4/9.
 */
public interface TenantService {

    Tenant findTenantById(Integer tenantId);

    Tenant saveTenant(Tenant tenant);

    void deleteTenant(Integer tenantId);

    Page<Tenant> findTenants(Integer page, Integer size);

}
