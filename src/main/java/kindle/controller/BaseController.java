package kindle.controller;


import kindle.pojo.result.ExceptionMsg;
import kindle.pojo.result.Response;
import kindle.utils.CommonUtils;
import kindle.utils.Constants;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class BaseController {

    protected Logger logger = Logger.getLogger(this.getClass());
    
    protected Response result(ExceptionMsg msg){
    	return new Response(msg);
    }
    protected Response result(){
    	return new Response();
    }
    
    protected HttpServletRequest getRequest() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    }
    
    protected HttpSession getSession() {
        return getRequest().getSession();
    }
    

    
    protected String getUserIp() {
        String value = getRequest().getHeader("X-Real-IP");
        if (!CommonUtils.isEmpty(value) && !"unknown".equalsIgnoreCase(value)) {
            return value;
        } else {
            return getRequest().getRemoteAddr();
        }
    }

    /**
     * @description 登陆后首页
     * @author hely
     * @date 2017-08-30
     * @param
     */
    @RequestMapping(value = "/index",method = RequestMethod.GET)
    public ModelAndView index(){
        ModelAndView mv = new ModelAndView();
        mv.setViewName("pages/admin3/index");
        return mv;
    }

    /**
     * @description 注销账号
     * @author hely
     * @date 2017-09-12
     * @param
     */
    @RequestMapping(value = "/logout",method = RequestMethod.GET)
    public ModelAndView LogOut(HttpServletRequest request){
        request.getSession().removeAttribute(Constants.SESSSION_USER_KEY);
        ModelAndView mv = new ModelAndView();
        mv.setViewName("pages/admin3/login_soft");
        return mv;
    }

}
