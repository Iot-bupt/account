package cn.edu.bupt.dao.Tenant;

import cn.edu.bupt.Security.HttpUtil;
import cn.edu.bupt.dao.Customer.CustomerService;
import cn.edu.bupt.dao.DataValidationException;
import cn.edu.bupt.dao.DataValidator;
import cn.edu.bupt.dao.User.UserService;
import cn.edu.bupt.entity.Tenant;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    private static String delete_device_url;

    private static String activate_device_url;

    private static String suspend_device_url;

    @Value("${device-access.delete_device_url}")
    private void getDeviceAccessHost(String delete_device_url) {
        this.delete_device_url = delete_device_url ;
    }

    @Value("${device-access.activate_device_url}")
    private void getDeviceActivateHost(String activate_device_url) {
        this.activate_device_url = activate_device_url ;
    }

    @Value("${device-access.suspend_device_url}")
    private void getDeviceSuspendHost(String suspend_device_url) {
        this.suspend_device_url = suspend_device_url ;
    }



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
        try {
            HttpUtil.sendDeletToThingsboard(delete_device_url + tenantId);
        }catch (Exception e){
            System.out.println("删除设备失败");
        }
        //TODO:deleteRulesByTenantId, deletePluginsByTenantId
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
        // 第二个-1代表默认的保留租户，该租户id为1，且不显示出来。
        Integer num = ((tenantRepository.findAllCount()+size-1)-1)/size;
        return num;
    }

    @Override
    public Boolean findSuspendedStatusById(Integer id) {
        log.trace("Executing findSuspendedStatusById [{}]", id);
        return tenantRepository.findSuspendedStatusById(id);
    }

    @Override
    public void updateSuspendedStatusById(Boolean suspended, Integer id) {
        log.trace("Executing updateSuspendedStatusById,suspended [{}],id [{}]", suspended,id);
        try {
            if (suspended.equals(Boolean.FALSE)) {
                HttpUtil.sendPutToThingsboard(activate_device_url+id, null, null);
            }else if(suspended.equals(Boolean.TRUE)){
                HttpUtil.sendPutToThingsboard(suspend_device_url+id, null, null);
            }else{
                log.error("updating device suspending status failed!");
            }
        }catch (Exception e){
            log.error("updating device suspending status failed!");
        }
        tenantRepository.updateSuspendedStatus(suspended,id);
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
