package cn.edu.bupt.dao;

import cn.edu.bupt.entity.User;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by CZX on 2018/4/8.
 */
public interface UserRepository extends CrudRepository<User, String> {
}
