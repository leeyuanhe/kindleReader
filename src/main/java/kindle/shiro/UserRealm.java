package kindle.shiro;

import kindle.mapper.UserMapper;
import kindle.pojo.User;
import kindle.pojo.UserExample;
import kindle.repository.UserRepository;
import kindle.utils.CommonUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public class UserRealm extends AuthorizingRealm {

    private static final Logger logger = LoggerFactory.getLogger(UserRealm.class);
    @Autowired
    UserMapper userMapper;

    /**
     * @description 获取授权信息
     * @author hely
     * @date 2017-09-12
     * @param
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        String currentLoginName = (String)principals.getPrimaryPrincipal();
        List<String> userRoles = new ArrayList<String>();
        List<String> userPermissions = new ArrayList<String>();

        User user = userMapper.selectOne(new User(currentLoginName, null));

        if(!CommonUtils.isEmpty(user)){
            //获取当前用户下所有ACL权限列表  待续。。。
            //获取当前用户下拥有的所有角色列表
          /*  List<Role> roles = roleService.findByUserId(user.getId());
            for (int i = 0; i < roles.size(); i++) {
                userRoles.add(roles.get(i).getCode());
            }*/
        }else{
            throw new AuthorizationException();
        }
        System.out.println("#######获取角色："+userRoles);
        System.out.println("#######获取权限："+userPermissions);
        //为当前用户设置角色和权限
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        authorizationInfo.addRoles(userRoles);
        authorizationInfo.addStringPermissions(userPermissions);
        return authorizationInfo;
    }


    /**
     * @description 登陆认证
     * @author hely
     * @date 2017-09-12
     * @param
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        System.out.println("###【开始认证[SessionId]】"+ SecurityUtils.getSubject().getSession().getId());
        String loginName = (String)token.getPrincipal();
        User user = userMapper.selectOne(new User(loginName, null));
        if(user == null) {
            throw new UnknownAccountException();//没找到帐号
        }
        //交给AuthenticatingRealm使用CredentialsMatcher进行密码匹配，如果觉得人家的不好可以自定义实现
        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(
                user.getUserName(), //用户名
                user.getPassword(), //密码
                ByteSource.Util.bytes(user.getUserName()+user.getSalt()),
                getName()  //realm name
        );
        return authenticationInfo;
    }
}
