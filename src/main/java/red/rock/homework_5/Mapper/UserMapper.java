package red.rock.homework_5.Mapper;

import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;
import red.rock.homework_5.Entity.User;

/**
 * TODO
 *
 * @Date 2019/5/914:10
 * @Author tudou
 */

@Repository
@Mapper
public interface UserMapper {

     /**
       * 增加用户
       * @param user
       * @return void
       */
     @Insert("INSERT INTO another_users(username,password,nickname) values(#{username},#{password},#{nickname})")
     boolean addUser(User user);

      /**
        * 搜素用户
        * @param username
        * @return User
        */
      @Select("SELECT * from another_users where username=#{username}")
      @Results({
              @Result(property = "id",column = "id"),
              @Result(property = "username",column = "username"),
              @Result(property = "password",column = "password"),
              @Result(property = "nickname",column = "nickname")
      })
      User getUserInformation(String username);

       /**
         * 根据code获取用户信息
         * @param code
         * @return nickname
         */
       @Select("SELECT nickname from another_users where code=#{code}")
       String getUserReInformation(String code);

       /**
         * 生成用户code
         * @param code
         * @param username
         * @return void
         */
       @Update("UPDATE another_users SET code=#{code} where username=#{username}")
       void addUserCode(String code,String username);

       /**
        * 删除用户code
        * @param nickname
        * @return void
        */
        @Update("UPDATE another_users SET code=null where nickname=#{nickname}")
        void deleteCode(String nickname);
}
