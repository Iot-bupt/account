package cn.edu.bupt.dao.Tenant;

import cn.edu.bupt.entity.Tenant;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * Created by CZX on 2018/4/9.
 */
public interface TenantService {

    Tenant findTenantById(Integer tenantId);

    void updateTenant(Tenant tenant);

    void saveTenant(Tenant tenant);

    void deleteTenant(Integer tenantId);

    List<Tenant> findTenants(Integer page, Integer size);

    Integer findTenantsPageNum(Integer size);

    Boolean findSuspendedStatusById(Integer id);

    void updateSuspendedStatusById(Boolean suspended,Integer id);

}
