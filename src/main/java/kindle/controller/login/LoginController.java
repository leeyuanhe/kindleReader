package kindle.controller.login;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("login")
@Controller
public class LoginController {


    @RequestMapping("/main")
    public String login(){

        return "forward:/templates/admin3/login_soft.html";
    }
}
