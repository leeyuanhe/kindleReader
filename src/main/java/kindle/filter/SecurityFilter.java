package kindle.filter;

import kindle.mapper.RememberMapper;
import kindle.pojo.Remember;
import kindle.pojo.User;
import kindle.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;


/**
 * @param
 * @author hely
 * @description 过滤器
 * @date 2017-09-01
 */
public class SecurityFilter implements Filter {

    @Autowired
    RememberMapper rememberMapper;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this,
                filterConfig.getServletContext());
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException,
            ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        String rememberCheckbox = request.getParameter("remember");

        User user = (User) request.getSession().getAttribute(Constants.SESSSION_USER_KEY);
        System.out.println(">>>>>>>>>>启动过滤器");
        if (user == null) {
            String uuid = CookieUtils.getCookieValue(request, Constants.COOKIE_NAME);
            if (uuid != null) {
                uuid = URLDecoder.decode(uuid,"utf-8");
                String[] split = uuid.split(Constants.SEPRETOR_FLAG);
                String cookieUuid = split[0];

                Remember remember = rememberMapper.selectByCookUuid(cookieUuid);
                User dbUser = remember.getUser();
                if (remember != null) {
                    request.getSession().setAttribute(Constants.SESSSION_USER_KEY, dbUser); // Login.
                    String invariable_series = remember.getInvariableSeries();
                    String token = PasswordUtils.getMD5(RandomUtils.generateMixString(64));
                    String key = URLEncoder.encode(invariable_series+Constants.SEPRETOR_FLAG+dbUser.getUserName()
                            +Constants.SEPRETOR_FLAG+token, "utf-8");
                    remember.setToken(token);
                    rememberMapper.updateByPrimaryKey(remember);
                    CookieUtils.addCookie(response, Constants.COOKIE_NAME, key, Constants
                            .COOKIE_USERNAME_TIMEOUT);
                    clearUserCookie(request, response, rememberCheckbox);
                    response.sendRedirect("/index");
                    return;

                } else {
                    CookieUtils.removeCookie(response, Constants.COOKIE_NAME);
                }
            }else{
                chain.doFilter(req, res);
                return;
            }

        }

        if (user == null) {
            response.sendRedirect("/");
            return;
        } else {
            clearUserCookie(request, response, rememberCheckbox);
            response.sendRedirect("/index");
            return;
        }
    }

    /**
     * @description 已记住密码的用户 不再勾选记住我  清除cookie信息
     * @author hely
     * @date 2017-09-14
     * @param
     */
    public void clearUserCookie(HttpServletRequest request, HttpServletResponse response, String remember) {
        if (CommonUtils.isEmpty(remember)) {
            String cookieValue = CookieUtils.getCookieValue(request, Constants.COOKIE_NAME);
            if(!CommonUtils.isEmpty(cookieValue)){
                String[] split = cookieValue.split(Constants.SEPRETOR_FLAG);
                String cookieUuid = split[0];
                rememberMapper.delete(new Remember(cookieUuid,null,null));
                CookieUtils.removeCookie(response, Constants.COOKIE_NAME);
            }
        }
    }

    @Override
    public void destroy() {

    }
}
