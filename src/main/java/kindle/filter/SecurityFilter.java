package kindle.filter;

import kindle.pojo.Remember;
import kindle.pojo.User;
import kindle.repository.RememberRepository;
import kindle.utils.Constants;
import kindle.utils.CookieUtils;
import kindle.utils.PasswordUtils;
import kindle.utils.RandomUtils;
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
                uuid = URLDecoder.decode(uuid,"utf-8");
                String[] split = uuid.split(Constants.SEPRETOR_FLAG);
                String cookieUuid = split[0];
                Remember remember = rememberRepository.findByUuid(cookieUuid);
                user = remember.getUser();
                if (user != null) {
                    request.getSession().setAttribute(Constants.SESSSION_USER_KEY, user); // Login.

                    String invariable_series = remember.getUuid();
                    String token = PasswordUtils.getMD5(RandomUtils.generateMixString(64));
                    String key = invariable_series+Constants.SEPRETOR_FLAG+token;
                    remember.setToken(token);
                    rememberRepository.save(remember);
                    CookieUtils.addCookie(response, Constants.COOKIE_NAME, key, Constants
                            .COOKIE_USERNAME_TIMEOUT);
                    request.getRequestDispatcher("/pages/admin3/index.html").forward(request,response);

                } else {
                    CookieUtils.removeCookie(response, Constants.COOKIE_NAME);
                    request.getRequestDispatcher("/pages/admin3/index.html").forward(request,response);
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
            request.getRequestDispatcher("templates/pages/admin3/index.html").forward(request,response);
        }



    }

    @Override
    public void destroy() {

    }
}
