package cn.edu.bupt.dao.UserCredentials;

import cn.edu.bupt.entity.UserCredentials;

/**
 * Created by CZX on 2018/4/9.
 */
public interface UserCredentialsService {

    UserCredentials findUserCredentialsByUserId(Integer userId);

    UserCredentials saveUserCredentials(UserCredentials userCredentials);

    void deleteUserCredentialsByUserId(Integer userId);
}
