package red.rock.homework_5.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import red.rock.homework_5.Entity.User;
import red.rock.homework_5.Mapper.UserMapper;
import red.rock.homework_5.Util.CodeUtil;

/**
 * TODO
 *
 * @Date 2019/5/914:41
 * @Author tudou
 */

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    /**
     * 注册用户
     * @param user
     * @return boolean
     */
    public boolean registerUser(User user){
        User selectUser=userMapper.getUserInformation(user.getUsername());
        if (selectUser!=null&&user.getNickname().equals(selectUser.getNickname())){
            return false;
        }
        return userMapper.addUser(user);
    }

    /**
     * 登录用户
     * @param username
     * @param password
     * @return boolean
     */
    public boolean loginUser(String username,String password){
        User user=userMapper.getUserInformation(username);
        if(user==null){
            return false;
        }
        boolean flag=false;
        if(user.getPassword().equals(password)){
            flag=true;
        }
        return flag;
    }

    /**
     * 生成用户code
     * @param username
     * @return boolean
     */
    public String createCode(String username){
        String code= CodeUtil.getCode();
        userMapper.addUserCode(code,username);
        return code;
    }

    /**
     * 获取用户信息后删除code
     * @param
     * @return
     */
    @Transactional
    public String getUserInformation(String code){
        String nickname=userMapper.getUserReInformation(code);
        userMapper.deleteCode(nickname);
        return nickname;
    }
}
