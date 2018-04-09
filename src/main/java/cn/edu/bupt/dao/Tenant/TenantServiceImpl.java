package cn.edu.bupt.dao.Tenant;

import cn.edu.bupt.entity.Tenant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

/**
 * Created by CZX on 2018/4/9.
 */
@Service
public class TenantServiceImpl implements TenantService{

    @Autowired
    private TenantRepository tenantRepository;

    @Override
    public Tenant findTenantById(Integer tenantId){
        return tenantRepository.findById(tenantId).get();
    }

    @Override
    public Tenant saveTenant(Tenant tenant){
        return tenantRepository.save(tenant);
    }

    @Override
    public void deleteTenant(Integer tenantId){
        tenantRepository.deleteById(tenantId);
    }

    @Override
    public Page<Tenant> findTenants(Integer page, Integer size){
        Pageable pageable = new PageRequest(page, size, Sort.Direction.ASC, "id");
        Page<Tenant> tenantPage = tenantRepository.findAll(pageable);
        return tenantPage;
    }
}
