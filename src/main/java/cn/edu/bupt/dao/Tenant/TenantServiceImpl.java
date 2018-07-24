package cn.edu.bupt.dao.Tenant;

import cn.edu.bupt.dao.Customer.CustomerService;
import cn.edu.bupt.dao.DataValidationException;
import cn.edu.bupt.dao.DataValidator;
import cn.edu.bupt.dao.User.UserService;
import cn.edu.bupt.entity.Tenant;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by CZX on 2018/4/9.
 */
@Service
@Slf4j
public class TenantServiceImpl implements TenantService{

    @Autowired
    private TenantRepository tenantRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private CustomerService customerService;

    @Override
    public Tenant findTenantById(Integer tenantId){
        log.trace("Executing findTenantById [{}]", tenantId);
        return tenantRepository.findById(tenantId);
    }

    @Override
    public void saveTenant(Tenant tenant){
        log.trace("Executing saveTenant [{}]", tenant);
        tenantValidator.validate(tenant);
        tenantRepository.save(tenant);
    }

    @Override
    public void updateTenant(Tenant tenant){
        log.trace("Executing updateTenant [{}]", tenant);
        tenantValidator.validate(tenant);
        tenantRepository.update(tenant);
    }

    @Override
    public void deleteTenant(Integer tenantId){
        log.trace("Executing deleteTenant [{}]", tenantId);
        customerService.deleteCustomersByTenantId(tenantId);
        userService.deleteTenantAdmins(tenantId);
        //TODO:deleteDevicesByTenantId, deleteRulesByTenantId, deletePluginsByTenantId
        tenantRepository.deleteById(tenantId);
    }

    @Override
    public List<Tenant> findTenants(Integer page, Integer size){
        log.trace("Executing findTenants size [{}], page [{}]", size, page);
//        Pageable pageable = new PageRequest(page, size, Sort.Direction.ASC, "id");
        Integer index = page*size;
        List<Tenant> tenantPage = tenantRepository.findAll(index,size);
        return tenantPage;
    }

    @Override
    public Integer findTenantsPageNum(Integer size){
        log.trace("Executing findTenantsPageNum size [{}]", size);
        Integer num = (tenantRepository.findAllCount()+size-1)/size;
        return num;
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
