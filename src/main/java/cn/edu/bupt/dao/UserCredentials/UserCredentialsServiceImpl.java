package cn.edu.bupt.dao.UserCredentials;

import cn.edu.bupt.dao.DataValidationException;
import cn.edu.bupt.dao.DataValidator;
import cn.edu.bupt.dao.User.UserRepository;
import cn.edu.bupt.entity.UserCredentials;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Created by CZX on 2018/4/9.
 */
@Service
@Slf4j
public class UserCredentialsServiceImpl implements UserCredentialsService{

    @Autowired
    UserCredentialsRepository userCredentialsRepository;

    @Autowired
    UserRepository userRepository;

    @Override
    public Optional<UserCredentials> findUserCredentialsByUserId(Integer userId){
        log.trace("Executing findUserCredentialsByUserId [{}]", userId);
        return userCredentialsRepository.findUserCredentialsByUser(userRepository.findById(userId).get());
    }

    @Override
    public UserCredentials saveUserCredentials(UserCredentials userCredentials){
        log.trace("Executing saveUserCredentials [{}]", userCredentials);
        userCredentialsValidator.validate(userCredentials);
        return userCredentialsRepository.save(userCredentials);
    }

    @Override
    public void deleteUserCredentialsByUserId(Integer userId){
        log.trace("Executing deleteUserCredentialsByUserId [{}]", userId);
        userCredentialsRepository.deleteUserCredentialsByUser(userRepository.findById(userId).get());
    }

    private DataValidator<UserCredentials> userCredentialsValidator =
            new DataValidator<UserCredentials>() {

                @Override
                protected void validateDataImpl(UserCredentials userCredentials) {
                    if (userCredentials.getUser() == null) {
                        throw new DataValidationException("User credentials should be assigned to user!");
                    }
                }
            };
}
