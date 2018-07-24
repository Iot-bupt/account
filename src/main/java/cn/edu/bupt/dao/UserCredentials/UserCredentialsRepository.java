package cn.edu.bupt.dao.UserCredentials;

import cn.edu.bupt.entity.User;
import cn.edu.bupt.entity.UserCredentials;
import org.apache.ibatis.annotations.*;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

/**
 * Created by CZX on 2018/4/8.
 */
@Mapper
public interface UserCredentialsRepository{

    @Select("select  id  as id,user_id as  userId,password as password  from user_credentials where user_id = #{userId}")
    UserCredentials findUserCredentialsByUserId(Integer userId);

    @Delete("delete from user_credentials where user_id=#{userId}")
    void deleteUserCredentialsByUserId(Integer userId);

    @Insert("insert into user_credentials (user_id,password) values (#{userId},#{password}) ")
    @Options(useGeneratedKeys = true)
    void save(UserCredentials userCredentials);

    @Update("update user_credentials set password = #{password} where id=#{id}")
    void update(UserCredentials userCredentials);
}
