package kindle.controller;


import kindle.mapper.RememberMapper;
import kindle.mapper.UserMapper;
import kindle.pojo.Remember;
import kindle.pojo.RememberExample;
import kindle.pojo.User;
import kindle.pojo.result.ExceptionMsg;
import kindle.pojo.result.Response;
import kindle.service.UserService;
import kindle.utils.*;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.util.Date;

@RestController
@RequestMapping("user")
public class UserController extends BaseController {

    @Autowired
    UserMapper userMapper;
    @Autowired
    RememberMapper rememberMapper;
    @Autowired
    UserService userService;


    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public Response login(@ModelAttribute User user, HttpServletRequest request, HttpServletResponse response) {

        Response result = new Response();
        String rememberStr = request.getParameter("remember");
        try {
            User byUsernameOrEmail = userMapper.selectOne(user);

            String username = user.getUserName();
            UsernamePasswordToken token = new UsernamePasswordToken(user.getUserName(), user.getPassword());
            //获取当前的Subject
            Subject currentUser = SecurityUtils.getSubject();
            try {
                //在调用了login方法后,SecurityManager会收到AuthenticationToken,并将其发送给已配置的Realm执行必须的认证检查
                //每个Realm都能在必要时对提交的AuthenticationTokens作出反应
                //所以这一步在调用login(token)方法时,它会走到MyRealm.doGetAuthenticationInfo()方法中,具体验证方式详见此方法
                logger.info("对用户[" + username + "]进行登录验证..验证开始");
                currentUser.login(token);
                logger.info("对用户[" + username + "]进行登录验证..验证通过");
            }catch(UnknownAccountException uae){
                logger.info("对用户[" + username + "]进行登录验证..验证未通过,未知账户");
                result = result(ExceptionMsg.LoginNameNotExist);
            }catch(IncorrectCredentialsException ice){
                logger.info("对用户[" + username + "]进行登录验证..验证未通过,错误的凭证");
                result =  result(ExceptionMsg.LoginNameOrPassWordError);
            }catch(LockedAccountException lae){
                logger.info("对用户[" + username + "]进行登录验证..验证未通过,账户已锁定");
                result =  result(ExceptionMsg.UserLocked);
            }catch(ExcessiveAttemptsException eae){
                logger.info("对用户[" + username + "]进行登录验证..验证未通过,错误次数过多");
                result =  result(ExceptionMsg.IncorrectAuthOverBoundary);
            }catch(AuthenticationException ae){
                //通过处理Shiro的运行时AuthenticationException就可以控制用户登录失败或密码错误时的情景
                logger.info("对用户[" + username + "]进行登录验证..验证未通过,堆栈轨迹如下");
                ae.printStackTrace();
                result =  result(ExceptionMsg.LoginNameOrPassWordError);
            }
            //验证是否登录成功
            if(currentUser.isAuthenticated()){
                logger.info("用户[" + username + "]登录认证通过(这里可以进行一些认证通过后的一些系统参数初始化操作)");
                request.getSession().setAttribute(Constants.SESSSION_USER_KEY, user);
                //记住我功能cookie处理
                String cookieValue = CookieUtils.getCookieValue(request, Constants.COOKIE_NAME);

                Remember remember ;
                if (Constants.REMEMBER_FLAG.equals(rememberStr)) {
                    String invariable_series = PasswordUtils.getMD5(RandomUtils.generateMixString(32));
                    String usertoken = PasswordUtils.getMD5(RandomUtils.generateMixString(64));
                    String key = URLEncoder.encode(invariable_series+Constants.SEPRETOR_FLAG+user.getUserName()+Constants
                            .SEPRETOR_FLAG+token, "utf-8");

                    remember = rememberMapper.selectOne(new Remember(null,null,byUsernameOrEmail.getId()));
                    if (CommonUtils.isEmpty(remember)){
                        remember = new Remember(invariable_series,usertoken,user.getId());
                        rememberMapper.insert(remember);
                    }else{
                        remember.setInvariableSeries(invariable_series);
                        remember.setToken(usertoken);
                        remember.setUserId(user.getId());
                        rememberMapper.updateByPrimaryKey(remember);
                    }
                    CookieUtils.addCookie(response,Constants.COOKIE_NAME,key,Constants
                            .COOKIE_USERNAME_TIMEOUT);
                }else if (!Constants.REMEMBER_FLAG.equals(rememberStr) && !CommonUtils.isEmpty(cookieValue)){
                    String[] split = cookieValue.split(Constants.SEPRETOR_FLAG);
                    String cookieUuid = split[0];
                   /* RememberExample rememberExample = new RememberExample();
                    RememberExample.Criteria rememberCri = rememberExample.createCriteria();
                    rememberCri.andInvariableSeriesEqualTo(cookieUuid);
                    rememberMapper.deleteByExample(rememberExample);*/
                    rememberMapper.delete(new Remember(cookieUuid,null,null));
                    CookieUtils.removeCookie(response, Constants.COOKIE_NAME);
                }

            }else{
                token.clear();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public Response registerUser(User user, HttpServletRequest request) {

        String str = userService.checkNameAndEmail(user);
        if (ExceptionMsg.UserNameUsed.getCode().equals(str)){
            return result(ExceptionMsg.UserNameUsed);
        }
        if (ExceptionMsg.EmailUsed.getCode().equals(str)) {
            return result(ExceptionMsg.EmailUsed);
        }

//        String salt = PasswordUtils.generateRandomSalt();
//        user.setPassword(PasswordUtils.getMD5(user.getPassword() + salt));
        String salt = new SecureRandomNumberGenerator().nextBytes().toHex();
        String codePassword = PasswordUtils.codePassword(user.getUserName(), user.getPassword(), salt);
        user.setPassword(codePassword);
        user.setSalt(salt);
        user.setIpAddress(CommonUtils.getRemoteAddr(request));
        user.setCreateTime(new Date());
        userMapper.insert(user);
        return result();
    }
}
