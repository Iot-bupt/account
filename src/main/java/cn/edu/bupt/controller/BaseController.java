package cn.edu.bupt.controller;

import cn.edu.bupt.Security.model.SecurityUser;
import cn.edu.bupt.dao.Customer.CustomerService;
import cn.edu.bupt.dao.DataValidationException;
import cn.edu.bupt.dao.User.UserService;
import cn.edu.bupt.entity.Authority;
import cn.edu.bupt.entity.Customer;
import cn.edu.bupt.entity.User;
import cn.edu.bupt.exception.IOTErrorCode;
import cn.edu.bupt.exception.IOTException;
import cn.edu.bupt.exception.IncorrectParameterException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import java.util.Optional;

import static cn.edu.bupt.controller.UserController.YOU_DON_T_HAVE_PERMISSION_TO_PERFORM_THIS_OPERATION;

/**
 * Created by CZX on 2018/4/13.
 */
public abstract class BaseController {


    @Autowired
    private UserService userService;

    @Autowired
    private CustomerService customerService;

    IOTException handleException(Exception exception) {
        return handleException(exception, true);
    }

    private IOTException handleException(Exception exception, boolean logException) {
//        if (logException) {
//            log.error("Error [{}]", exception.getMessage());
//        }

        String cause = "";
        if (exception.getCause() != null) {
            cause = exception.getCause().getClass().getCanonicalName();
        }

        if (exception instanceof IOTException) {
            return (IOTException) exception;
        } else if (exception instanceof IllegalArgumentException || exception instanceof IncorrectParameterException
                || exception instanceof DataValidationException || cause.contains("IncorrectParameterException")) {
            return new IOTException(exception.getMessage(), IOTErrorCode.BAD_REQUEST_PARAMS);
        }  else {
            return new IOTException(exception.getMessage(), IOTErrorCode.GENERAL);
        }
    }

    protected SecurityUser getCurrentUser() throws IOTException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof SecurityUser) {
            return (SecurityUser) authentication.getPrincipal();
        } else {
            throw new IOTException("You aren't authorized to perform this operation!", IOTErrorCode.AUTHENTICATION);
        }
    }

    User checkUserId(Integer userId) throws IOTException {
        try {
            User user = userService.findUserById(userId);
            checkUser(user);
            return user;
        } catch (Exception e) {
            throw handleException(e, false);
        }
    }

    private void checkUser(User user) throws IOTException {
        checkNotNull(user);
        checkTenantId(user.getTenant().getId());
        if (user.getAuthority() == Authority.CUSTOMER_USER) {
            checkCustomerId(user.getCustomer().getId());
        }
    }

    void checkTenantId(Integer tenantId) throws IOTException {
        SecurityUser authUser = getCurrentUser();
        if (authUser.getAuthority() != Authority.SYS_ADMIN &&
                (authUser.getTenant().getId() == null || !authUser.getTenant().getId().equals(tenantId))) {
            throw new IOTException(YOU_DON_T_HAVE_PERMISSION_TO_PERFORM_THIS_OPERATION,
                    IOTErrorCode.PERMISSION_DENIED);
        }
    }

    Customer checkCustomerId(Integer customerId) throws IOTException {
        try {
            SecurityUser authUser = getCurrentUser();
            if (authUser.getAuthority() == Authority.SYS_ADMIN ||
                    (authUser.getAuthority() != Authority.TENANT_ADMIN &&
                            (authUser.getCustomer().getId() == null || !authUser.getCustomer().getId().equals(customerId)))) {
                throw new IOTException(YOU_DON_T_HAVE_PERMISSION_TO_PERFORM_THIS_OPERATION,
                        IOTErrorCode.PERMISSION_DENIED);
            }
            Customer customer = customerService.findCustomerById(customerId);
            checkCustomer(customer);
            return customer;
        } catch (Exception e) {
            throw handleException(e, false);
        }
    }

    private void checkCustomer(Customer customer) throws IOTException {
        checkNotNull(customer);
        checkTenantId(customer.getTenant().getId());
    }

    <T> T checkNotNull(T reference) throws IOTException {
        if (reference == null) {
            throw new IOTException("Requested item wasn't found!", IOTErrorCode.ITEM_NOT_FOUND);
        }
        return reference;
    }

    <T> T checkNotNull(Optional<T> reference) throws IOTException {
        if (reference.isPresent()) {
            return reference.get();
        } else {
            throw new IOTException("Requested item wasn't found!", IOTErrorCode.ITEM_NOT_FOUND);
        }
    }
}