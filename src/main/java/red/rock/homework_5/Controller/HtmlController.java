package red.rock.homework_5.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import red.rock.homework_5.Service.UserService;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * TODO
 *
 * @Date 2019/5/1022:07
 * @Author tudou
 */


@Controller
public class HtmlController {

    @Autowired
    private UserService userService;

    @GetMapping("/LG")
    public String login(HttpSession session, HttpServletResponse response) throws IOException {
        String code=(String)session.getAttribute("code");
        if(code!=null){
            response.setHeader("content-type","text/html;charset=UTF-8");
            String nickname=userService.getUserInformation(code);
            response.getWriter().println(nickname+"登录成功");
            return null;
        }
        return "login";
    }

    @GetMapping("/RLG")
    public String reLogin(){
        return "reLogin";
    }

    @GetMapping("/RG")
    public String register(){
        return "register";
    }
}
