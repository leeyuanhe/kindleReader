package kindle.controller;


import kindle.pojo.User;
import kindle.pojo.result.ExceptionMsg;
import kindle.repository.UserRepository;
import kindle.utils.CommonUtils;
import kindle.utils.PasswordUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RequestMapping("user")
@RestController
public class UserController extends BaseController{

    @Autowired
    UserRepository userRepository;

    @RequestMapping("/main")
    public Object login(){

        return "forward:/templates/admin3/login_soft.html";
    }

    @RequestMapping("/register")
    public Object registerUser(@ModelAttribute User user){

        User byUserName = userRepository.findByUsername(user.getUsername());

       if (!CommonUtils.isEmpty(byUserName)){
           return result(ExceptionMsg.UserNameUsed);
       }

        User byEmail = userRepository.findByEmail(user.getEmail());

       if (!CommonUtils.isEmpty(byEmail)){
           return result(ExceptionMsg.EmailUsed);
       }
       user.setPassword(PasswordUtils.getMD5(user.getPassword()+ PasswordUtils.generateRandomSalt()));
       user.setCdate(new Date());
       userRepository.save(user);

       return result();
    }
}
