package red.rock.homework_5.Entity;


import lombok.Data;

/**
 * @Date 2019/5/913:50
 * @Author tudou
 */

@Data
public class User {

    private int id;
    private String username;
    private String password;
    private String nickname;
    private String code;

    public User(){

    }

    public User(String username,String password,String nickname){
        this.username=username;
        this.password=password;
        this.nickname=nickname;
    }
}
