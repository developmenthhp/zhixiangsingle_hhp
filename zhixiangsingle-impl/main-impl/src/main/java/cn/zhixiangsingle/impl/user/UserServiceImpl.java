package cn.zhixiangsingle.impl.user;

import cn.zhixiangsingle.annotation.TargetDataSource;
import cn.zhixiangsingle.entity.base.role.Role;
import cn.zhixiangsingle.entity.base.user.User;
import cn.zhixiangsingle.entity.base.userRole.UserRoleKey;
import cn.zhixiangsingle.entity.base.userRole.dto.UserRoleDTO;
import cn.zhixiangsingle.entity.base.userRole.dto.UserSearchDTO;
import cn.zhixiangsingle.entity.base.userRole.vo.UserRolesVO;
import cn.zhixiangsingle.entity.base.userSiteRole.UserSiteRoleKey;
import cn.zhixiangsingle.dao.role.RoleMapper;
import cn.zhixiangsingle.dao.user.UserMapper;
import cn.zhixiangsingle.dao.userRole.UserRoleMapper;
import cn.zhixiangsingle.dao.userSiteRole.UserSiteRoleMapper;
import cn.zhixiangsingle.service.UserService.UserService;
import cn.zhixiangsingle.web.responsive.IStatusMessage;
import cn.zhixiangsingle.web.responsive.ResultBean;
import cn.zhixiangsingle.web.result.CommonResultMethod;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangpingtai
 * @Package cn.zhixiangsingle.impl.user
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019/8/23 12:48
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Service(version = "1.0.0", interfaceClass = UserService.class)
public class UserServiceImpl implements UserService {
    private static final org.slf4j.Logger logger = LoggerFactory
            .getLogger(UserServiceImpl.class);
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private UserRoleMapper userRoleMapper;
    @Autowired
    private UserSiteRoleMapper userSiteRoleMapper;

    /**
     * @version V1.0
     * @Title: zhixiangpingtai
     * @Description:
     * @author: hhp
     * @param:  * @param userSearch
     * @param page
     * @param limit
     * @date: 2019/8/23 12:51
     */
    @Override
    @TargetDataSource(name = "default")
    public ResultBean getUsers(UserSearchDTO userSearch, Integer page, Integer limit) {
        ResultBean resultBean = new ResultBean();
        if (null == page) {
            page = 1;
        }
        if (null == limit) {
            limit = 10;
        }
        if (null != userSearch) {
            if (StringUtils.isNotEmpty(userSearch.getInsertTimeStart())
                    && StringUtils.isEmpty(userSearch.getInsertTimeEnd())) {
                userSearch.setInsertTimeEnd(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            } else if (StringUtils.isEmpty(userSearch.getInsertTimeStart())
                    && StringUtils.isNotEmpty(userSearch.getInsertTimeEnd())) {
                userSearch.setInsertTimeStart(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            }
            if (StringUtils.isNotEmpty(userSearch.getInsertTimeStart())
                    && StringUtils.isNotEmpty(userSearch.getInsertTimeEnd())) {
                if (userSearch.getInsertTimeEnd().compareTo(
                        userSearch.getInsertTimeStart()) < 0) {
                    String temp = userSearch.getInsertTimeStart();
                    userSearch
                            .setInsertTimeStart(userSearch.getInsertTimeEnd());
                    userSearch.setInsertTimeEnd(temp);
                }
            }
        }
        PageHelper.startPage(page, limit);
        List<UserRoleDTO> urList = userMapper.getUsers(userSearch);
        PageInfo<UserRoleDTO> pageInfo = new PageInfo<>(urList);
        resultBean.setTotal(Long.valueOf(pageInfo.getTotal()).intValue());
        if (null != urList && urList.size() > 0) {
            for (UserRoleDTO ur : urList) {
                List<Role> roles = roleMapper.getRoleByUserId(ur.getId());
                if (null != roles && roles.size() > 0) {
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < roles.size(); i++) {
                        Role r = roles.get(i);
                        sb.append(r.getRoleName());
                        if (i != (roles.size() - 1)) {
                            sb.append("，");
                        }
                    }
                    ur.setRoleNames(sb.toString());
                }
            }
        }
        resultBean.setSuccess(true);
        resultBean.setResult(urList);
        resultBean.setResultCode(IStatusMessage.SystemStatus.SUCCESS.getCode());
        resultBean.setMsg(IStatusMessage.SystemStatus.SUCCESS.getMessage());
        return resultBean;
    }
    /**
     * @version V1.0
     * @Title: zhixiangpingtai
     * @Description:
     * @author: hhp
     * @param:  * @param id
     * @param isDel
     * @param insertUid
     * @param version
     * @date: 2019/8/23 12:51
     */
    @Override
    @TargetDataSource(name = "default")
    public ResultBean setDelUser(Integer id) {
        int resultCount = this.userMapper.deleteByPrimaryKey(id);
        return CommonResultMethod.getUpdOrInsertResultBean(resultCount);
    }
    /**
     * @version V1.0
     * @Title: zhixiangpingtai
     * @Description:
     * @author: hhp
     * @param:  * @param user
     * @param roleIds
     * @param adminUser
     * @param siteRoleIds
     * @date: 2019/8/23 12:53
     */
    @Override
    @TargetDataSource(name = "default")
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, timeout = 30000, rollbackFor = {
            RuntimeException.class, Exception.class })
    public ResultBean setUser(User user, String roleIds, User adminUser,String siteRoleIds) {
        ResultBean resultBean = new ResultBean();

        int userId;
        if (user.getId() != null) {
            User exist = null;
            if (null != exist
                    && !String.valueOf(exist.getId()).equals(
                    String.valueOf(user.getId()))) {
                resultBean.setSuccess(false);
                resultBean.setResultCode(IStatusMessage.SystemStatus.ERROR.getCode());
                resultBean.setMsg(IStatusMessage.SystemStatus.THE_NAME_EXIST.getMessage());
                return resultBean;
            }
            userId = user.getId();
            user.setUpdateTime(new Date());
            if (StringUtils.isNotBlank(user.getPassword())) {
                user.setPassword(DigestUtils.md5Hex(user.getPassword()));
            }
            this.userMapper.updateByPrimaryKey(user);
            List<UserRoleKey> urs = this.userRoleMapper.findByUserId(userId);
            if (null != urs && urs.size() > 0) {
                for (UserRoleKey ur : urs) {
                    this.userRoleMapper.deleteByPrimaryKey(ur);
                }
            }
            List<UserSiteRoleKey> usrk = this.userSiteRoleMapper.findByUserId(userId);
            if (null != usrk && urs.size() > 0) {
                for (UserSiteRoleKey usr : usrk) {
                    this.userSiteRoleMapper.deleteByPrimaryKey(usr);
                }
            }
            logger.debug("清除所有用户权限缓存！！！");
        } else {
            User exist = null;
            if (null != exist) {
                resultBean.setSuccess(false);
                resultBean.setResultCode(IStatusMessage.SystemStatus.ERROR.getCode());
                resultBean.setMsg(IStatusMessage.SystemStatus.THE_NAME_EXIST.getMessage());
                return resultBean;
            }
            user.setInsertTime(new Date());
            user.setJob(false);
            if (StringUtils.isNotBlank(user.getPassword())) {
                user.setPassword(DigestUtils.md5Hex(user.getPassword()));
            } else {
                user.setPassword(DigestUtils.md5Hex("654321"));
            }
            this.userMapper.insertSelective(user);
            userId = user.getId();
        }
        String[] arrays = roleIds.split(",");
        for (String roleId : arrays) {
            UserRoleKey urk = new UserRoleKey();
            urk.setRoleId(Integer.valueOf(roleId));
            urk.setUserId(userId);
            this.userRoleMapper.insert(urk);
        }
        String[] siteRoleArrays = siteRoleIds.split(",");
        for (String siteRoleId : siteRoleArrays) {
            UserSiteRoleKey usrk = new UserSiteRoleKey();
            usrk.setSiteRoleId(Integer.valueOf(siteRoleId));
            usrk.setUserId(userId);
            this.userSiteRoleMapper.insert(usrk);
        }
        resultBean.setSuccess(true);
        resultBean.setResultCode(IStatusMessage.SystemStatus.SUCCESS.getCode());
        resultBean.setMsg(IStatusMessage.SystemStatus.SUCCESS.getMessage());
        return resultBean;
    }
    public static void main(String[] args) {
        System.out.println(DigestUtils.md5Hex("654321"));
    }
    /**
     * @version V1.0
     * @Title: zhixiangpingtai
     * @Description:
     * @author: hhp
     * @param:  * @param id
     * @param isJob
     * @param insertUid
     * @date: 2019/10/14 19:07
     */
    @Override
    @TargetDataSource(name = "default")
    public ResultBean setJobUser(Integer id, Integer isJob, Integer insertUid) {
        ResultBean resultBean = new ResultBean();
        int resultCount = this.userMapper.setJobUser(id, isJob, insertUid);
        if(resultCount == 1){
            resultBean.setSuccess(true);
            resultBean.setResultCode(IStatusMessage.SystemStatus.SUCCESS.getCode());
            resultBean.setMsg(IStatusMessage.SystemStatus.ERROR.getMessage());
        }else{
            resultBean.setSuccess(false);
            resultBean.setResultCode(IStatusMessage.SystemStatus.ERROR.getCode());
            resultBean.setMsg(IStatusMessage.SystemStatus.ERROR.getMessage());
        }
        return resultBean;
    }
    /**
     * @version V1.0
     * @Title: zhixiangpingtai
     * @Description:
     * @author: hhp
     * @param:  * @param id
     * @date: 2019/10/14 19:07
     */
    @Override
    @TargetDataSource(name = "default")
    public UserRolesVO getUserAndRoles(Integer id) {
        // 获取用户及他对应的roleIds
        return this.userMapper.getUserAndRoles(id);
    }
    /**
     * @version V1.0
     * @Title: zhixiangpingtai
     * @Description:
     * @author: hhp
     * @param:  * @param id
     * @param password
     * @date: 2019/8/23 13:04
     */
    @Override
    @TargetDataSource(name = "default")
    public int updatePwd(Integer id, String password) {
        return this.userMapper.updatePwd(id, password);
    }
    /**
     * @version V1.0
     * @Title: zhixiangpingtai
     * @Description:
     * @author: hhp
     * @param:  * @param userDTO
     * @param ecm
     * @date: 2019/8/24 12:36
     */
    @Override
    public User findUserByName(User user) {
        return userMapper.findUserByName(user);
    }
    /**
     * @version V1.0
     * @Title: zhixiangpingtai
     * @Description:
     * @author: hhp
     * @param:  * @param user
     * @date: 2019/9/7 14:02
     */
    @Override
    public ResultBean selectCommon(User user) {
        ResultBean resultBean = new ResultBean();
        resultBean.setSuccess(true);
        resultBean.setResultCode(IStatusMessage.SystemStatus.SUCCESS.getCode());
        resultBean.setResult(userMapper.selectCommon(user));
        return resultBean;
    }
}
