package cn.edu.bupt.dao.UserCredentials;

import cn.edu.bupt.entity.UserCredentials;

import java.util.Optional;

/**
 * Created by CZX on 2018/4/9.
 */
public interface UserCredentialsService {

    Optional<UserCredentials> findUserCredentialsByUserId(Integer userId);

    UserCredentials saveUserCredentials(UserCredentials userCredentials);

    void deleteUserCredentialsByUserId(Integer userId);
}
