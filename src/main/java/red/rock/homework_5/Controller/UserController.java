package red.rock.homework_5.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import red.rock.homework_5.Entity.User;
import red.rock.homework_5.Service.UserService;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * TODO
 *
 * @Date 2019/5/915:05
 * @Author tudou
 */


@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping("/login")
    public String login(@RequestParam(name="username",required = false)String username,@RequestParam(name = "password",required = false)String password,HttpSession session){
        return "登录失败";
    }

    @RequestMapping("/reLogin")
    public void reLogin(@RequestParam(name="username",required = false)String username, @RequestParam(name = "password",required = false)String password,
                        @RequestParam(name="redirect_url",required = false)String redirectUrl, HttpSession session, HttpServletResponse response)throws  Exception{
        if(username==null||password==null||username==""||password==""){
            response.getWriter().println( "登录失败,请输入账号或密码");
        }
        boolean flag=userService.loginUser(username,password);
        if(!flag){
            response.getWriter().println("登录失败,请检查账号或密码是否输入正确");
        }else{
            String code=userService.createCode(username);
            session.setAttribute("code",code);
            response.sendRedirect(redirectUrl);
        }

    }

    @RequestMapping("/register")
    public String register(@RequestParam(name="username",required = false)String username,@RequestParam(name = "password",required = false)String password,
                           @RequestParam(name = "nickname",required = false)String nickname){
        if(username==null||password==null||nickname==null||username==""||nickname==""||password==""){
            return "注册失败,请输入相关信息";
        }
        User user=new User(username,password,nickname);
        if(userService.registerUser(user)){
            return "注册成功";
        }
        return "注册失败,请检查用户名或昵称可能已被使用";
    }
}
