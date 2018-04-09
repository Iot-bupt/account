package cn.edu.bupt.dao.UserCredentials;

import cn.edu.bupt.dao.User.UserRepository;
import cn.edu.bupt.entity.UserCredentials;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by CZX on 2018/4/9.
 */
@Service
public class UserCredentialsServiceImpl implements UserCredentialsService{

    @Autowired
    UserCredentialsRepository userCredentialsRepository;

    @Autowired
    UserRepository userRepository;

    @Override
    public UserCredentials findUserCredentialsByUserId(Integer userId){
        return userCredentialsRepository.findUserCredentialsByUser(userRepository.findById(userId).get());
    }

    @Override
    public UserCredentials saveUserCredentials(UserCredentials userCredentials){
        return userCredentialsRepository.save(userCredentials);
    }

    @Override
    public void deleteUserCredentialsByUser(Integer userId){
        userCredentialsRepository.deleteUserCredentialsByUser(userRepository.findById(userId).get());
    }
}
