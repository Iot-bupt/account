package cn.edu.bupt.controller;

import cn.edu.bupt.Security.model.SecurityUser;
import cn.edu.bupt.dao.User.UserService;
import cn.edu.bupt.dao.UserCredentials.UserCredentialsService;
import cn.edu.bupt.entity.UserCredentials;
import cn.edu.bupt.exception.IOTErrorCode;
import cn.edu.bupt.exception.IOTException;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

/**
 * Created by CZX on 2018/4/16.
 */
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController extends BaseController{

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private UserService userService;

    @Autowired
    private UserCredentialsService userCredentialsService;

//    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/changePassword", method = RequestMethod.PUT)
    @ResponseStatus(value = HttpStatus.OK)
    public void changePassword (
            @RequestBody JsonNode changePasswordRequest) throws IOTException{
        try {
            String currentPassword = changePasswordRequest.get("currentPassword").asText();
            String newPassword = changePasswordRequest.get("newPassword").asText();
            SecurityUser securityUser = getCurrentUser();
            UserCredentials userCredentials = userCredentialsService.findUserCredentialsByUserId(securityUser.getId()).get();
            if (!passwordEncoder.matches(currentPassword, userCredentials.getPassword())) {
                throw new IOTException("Current password doesn't match!", IOTErrorCode.BAD_REQUEST_PARAMS);
            }
            userCredentials.setPassword(passwordEncoder.encode(newPassword));
            userCredentialsService.saveUserCredentials(userCredentials);
        } catch (Exception e) {
            throw handleException(e);
        }
    }
}
