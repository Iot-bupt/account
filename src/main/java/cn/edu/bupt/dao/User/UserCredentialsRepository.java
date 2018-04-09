package cn.edu.bupt.dao.User;

import cn.edu.bupt.entity.User;
import cn.edu.bupt.entity.UserCredentials;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by CZX on 2018/4/8.
 */
public interface UserCredentialsRepository extends CrudRepository<UserCredentials, Integer> {
    UserCredentials findUserCredentialsByUser(User user);
}
