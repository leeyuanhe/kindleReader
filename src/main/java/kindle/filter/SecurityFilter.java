package kindle.filter;

import kindle.mapper.RememberMapper;
import kindle.pojo.Remember;
import kindle.pojo.RememberExample;
import kindle.pojo.User;
import kindle.repository.RememberRepository;
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
        User user = (User) request.getSession().getAttribute(Constants.SESSSION_USER_KEY);
        System.out.println(">>>>>>>>>>启动过滤器");
        if (user == null) {
            String uuid = CookieUtils.getCookieValue(request, Constants.COOKIE_NAME);
            if (uuid != null) {
                uuid = URLDecoder.decode(uuid,"utf-8");
                String[] split = uuid.split(Constants.SEPRETOR_FLAG);
                String cookieUuid = split[0];
                Remember remember = rememberMapper.selectOne(new Remember(cookieUuid, null, null));

                if (remember != null) {
                    request.getSession().setAttribute(Constants.SESSSION_USER_KEY, user); // Login.
                    String invariable_series = remember.getInvariableSeries();
                    String token = PasswordUtils.getMD5(RandomUtils.generateMixString(64));
                    String key = URLEncoder.encode(invariable_series+Constants.SEPRETOR_FLAG+user.getUserName()
                            +Constants.SEPRETOR_FLAG+token, "utf-8");
                    remember.setToken(token);
                    rememberMapper.insert(remember);
                    CookieUtils.addCookie(response, Constants.COOKIE_NAME, key, Constants
                            .COOKIE_USERNAME_TIMEOUT);
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
            String remember = request.getParameter("remember");
            if (CommonUtils.isEmpty(remember)) {
                String cookieValue = CookieUtils.getCookieValue(request, Constants.COOKIE_NAME);
                String[] split = cookieValue.split(Constants.SEPRETOR_FLAG);
                String cookieUuid = split[0];
              /*  RememberExample rememberExample = new RememberExample();
                RememberExample.Criteria rememberCri = rememberExample.createCriteria();
                rememberCri.andInvariableSeriesEqualTo(cookieUuid);
                rememberMapper.deleteByExample(rememberExample);*/
                rememberMapper.delete(new Remember(cookieUuid,null,null));
                CookieUtils.removeCookie(response, Constants.COOKIE_NAME);
            }
            response.sendRedirect("/index");
            return;
        }



    }

    @Override
    public void destroy() {

    }
}
