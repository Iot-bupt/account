package cn.edu.bupt.dao.Tenant;

import cn.edu.bupt.dao.Customer.CustomerService;
import cn.edu.bupt.dao.DataValidationException;
import cn.edu.bupt.dao.DataValidator;
import cn.edu.bupt.dao.User.UserService;
import cn.edu.bupt.entity.Tenant;
import org.apache.commons.lang3.StringUtils;
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

    @Autowired
    private UserService userService;

    @Autowired
    private CustomerService customerService;

    @Override
    public Tenant findTenantById(Integer tenantId){
        return tenantRepository.findById(tenantId).get();
    }

    @Override
    public Tenant saveTenant(Tenant tenant){
        tenantValidator.validate(tenant);
        return tenantRepository.save(tenant);
    }

    @Override
    public void deleteTenant(Integer tenantId){
        customerService.deleteCustomersByTenantId(tenantId);
        userService.deleteTenantAdmins(tenantId);
        //TODO:deleteDevicesByTenantId, deleteRulesByTenantId, deletePluginsByTenantId
        tenantRepository.deleteById(tenantId);
    }

    @Override
    public Page<Tenant> findTenants(Integer page, Integer size){
        Pageable pageable = new PageRequest(page, size, Sort.Direction.ASC, "id");
        Page<Tenant> tenantPage = tenantRepository.findAll(pageable);
        return tenantPage;
    }

    private DataValidator<Tenant> tenantValidator =
            new DataValidator<Tenant>() {
                @Override
                protected void validateDataImpl(Tenant tenant) {
                    if (StringUtils.isEmpty(tenant.getTitle())) {
                        throw new DataValidationException("Tenant title should be specified!");
                    }
                    if (!StringUtils.isEmpty(tenant.getEmail())) {
                        validateEmail(tenant.getEmail());
                    }
                }
            };
}
