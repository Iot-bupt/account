package cn.edu.bupt.dao.UserCredentials;

import cn.edu.bupt.entity.UserCredentials;

import java.util.Optional;

/**
 * Created by CZX on 2018/4/9.
 */
public interface UserCredentialsService {

    UserCredentials findUserCredentialsByUserId(Integer userId);

    void saveUserCredentials(UserCredentials userCredentials);

    void updateUserCredentials(UserCredentials userCredentials);

    void deleteUserCredentialsByUserId(Integer userId);
}
