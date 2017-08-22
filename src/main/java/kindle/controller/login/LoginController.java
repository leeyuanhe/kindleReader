package kindle.controller.login;


import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("login")
@Component
public class LoginController {


    @RequestMapping("/mian")
    public String login(){

        return "login_soft.html";
    }
}
