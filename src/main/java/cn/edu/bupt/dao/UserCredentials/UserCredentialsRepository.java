package cn.edu.bupt.dao.UserCredentials;

import cn.edu.bupt.entity.User;
import cn.edu.bupt.entity.UserCredentials;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

/**
 * Created by CZX on 2018/4/8.
 */
public interface UserCredentialsRepository extends CrudRepository<UserCredentials, Integer> {

    Optional<UserCredentials> findUserCredentialsByUser(User user);

    void deleteUserCredentialsByUser(User user);

}
