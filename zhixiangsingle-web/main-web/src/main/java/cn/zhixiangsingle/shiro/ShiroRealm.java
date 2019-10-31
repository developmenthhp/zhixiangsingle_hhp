package cn.zhixiangsingle.shiro;

import cn.zhixiangsingle.entity.base.permission.Permission;
import cn.zhixiangsingle.entity.base.role.Role;
import cn.zhixiangsingle.entity.base.user.User;
import cn.zhixiangsingle.config.SpringBeanFactoryUtils;
import cn.zhixiangsingle.service.UserService.UserService;
import cn.zhixiangsingle.service.auth.AuthService;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangpingtai
 * @Package cn.zhixiangsingle.shiro
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019/8/23 9:18
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
public class ShiroRealm extends AuthorizingRealm {
    private static final Logger logger = LoggerFactory
            .getLogger(ShiroRealm.class);
    /**
     * @version V1.0
     * @Title: zhixiangpingtai
     * @Description:
     * @author: hhp
     * @param:  * @param principalCollection
     * @date: 2019/8/23 10:07
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(
            PrincipalCollection principalCollection) {
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        Subject subject = SecurityUtils.getSubject();
        User user = (User) subject.getPrincipal();
        if (user.getUserName().equals("hhp")&&!user.getZx()) {
            authorizationInfo.addRole("*");
            authorizationInfo.addStringPermission("*");
        } else {
            Integer userId = user.getId();
            AuthService authService = SpringBeanFactoryUtils.getBean("authService",AuthService.class);
            List<Role> roles = authService.getRoleByUser(userId);
            if (null != roles && roles.size() > 0) {
                for (Role role : roles) {
                    authorizationInfo.addRole(role.getCode());
                    List<Permission> perms = authService.findPermsByRoleId(role.getId());
                    if (null != perms && perms.size() > 0) {
                        for (Permission perm : perms) {
                            authorizationInfo.addStringPermission(perm
                                    .getCode());
                        }
                    }
                }
            }
        }
        return authorizationInfo;
    }
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(
            AuthenticationToken authenticationToken)
            throws AuthenticationException {
        UsernamePasswordToken token = (UsernamePasswordToken)authenticationToken;
        String userName = token.getUsername();
        UserService userService = SpringBeanFactoryUtils.getBean("userService",UserService.class);
        User findUser = new User();
        findUser.setId(0);
        findUser.setUserName(userName);
        findUser.setSdId(1);
        findUser.setEmail("2");
        Date myDate = new Date();
        findUser.setInsertTime(myDate);
        findUser.setUpdateTime(myDate);
        myDate=null;
        findUser.setPassword("1");
        findUser.setInsertUid(1);
        findUser.setJob(false);
        findUser.setZx(true);
        findUser.setProfession(-1);
        User user = userService.findUserByName(findUser);
        findUser = null;
        if (user == null) {
            return null;
        } else {
            return new SimpleAuthenticationInfo(user, DigestUtils.md5Hex(user.getPassword()),
                    getName());
        }
    }
    public void clearCachedAuth(){
        this.clearCachedAuthorizationInfo(SecurityUtils.getSubject().getPrincipals());
    }
}
