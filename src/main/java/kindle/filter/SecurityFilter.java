package kindle.filter;

import kindle.pojo.Remember;
import kindle.pojo.User;
import kindle.repository.RememberRepository;
import kindle.utils.Constants;
import kindle.utils.CookieUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;
import org.springframework.web.servlet.mvc.method.annotation.RequestResponseBodyMethodProcessor;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 * @param
 * @author hely
 * @description 过滤器
 * @date 2017-09-01
 */
public class SecurityFilter implements Filter {

    @Autowired
    RememberRepository rememberRepository;

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
        User user = (User) request.getSession().getAttribute(Constants.SESSSION_USER_KEY);
        System.out.println(">>>>>>>>>>启动过滤器");
        if (user == null) {
            String uuid = CookieUtils.getCookieValue(request, Constants.COOKIE_NAME);
            if (uuid != null) {
                uuid = uuid.substring(0, 32);
                Remember remember = rememberRepository.findByUuid(uuid);
                user = remember.getUser();
                if (user != null) {
                    request.getSession().setAttribute(Constants.SESSSION_USER_KEY, user); // Login.
                    CookieUtils.addCookie(response, Constants.COOKIE_NAME, uuid + user.getUsername(), Constants
                            .COOKIE_USERNAME_TIMEOUT);
                    request.getRequestDispatcher("/pages/admin3/index.html").forward(request,response);
                    return;
                } else {
                    CookieUtils.removeCookie(response, Constants.COOKIE_NAME);
                    response.sendRedirect("/");
                    return;
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
            request.getRequestDispatcher("/pages/admin3/index.html").forward(request,response);
            return;
        }



    }

    @Override
    public void destroy() {

    }
}
