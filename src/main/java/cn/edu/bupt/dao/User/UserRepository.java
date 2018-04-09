package cn.edu.bupt.dao.User;

import cn.edu.bupt.entity.Customer;
import cn.edu.bupt.entity.Tenant;
import cn.edu.bupt.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * Created by CZX on 2018/4/8.
 */
public interface UserRepository extends JpaRepository<User, Integer>,JpaSpecificationExecutor<User> {

    User findUserByEmail(String email);

    void deleteAllByCustomer(Customer customer);

}
