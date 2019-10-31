package cn.zhixiangsingle.controller.user;

import cn.zhixiangsingle.entity.base.IsEmptyUtils;
import cn.zhixiangsingle.entity.base.job.JobData;
import cn.zhixiangsingle.entity.base.role.Role;
import cn.zhixiangsingle.entity.base.shifts.Shifts;
import cn.zhixiangsingle.entity.base.siteRole.SiteRole;
import cn.zhixiangsingle.entity.base.user.User;
import cn.zhixiangsingle.entity.base.user.dto.UserDTO;
import cn.zhixiangsingle.entity.base.userRole.dto.UserSearchDTO;
import cn.zhixiangsingle.entity.base.userRole.vo.UserRolesVO;
import cn.zhixiangsingle.json.JsonUtil;
import cn.zhixiangsingle.service.UserService.UserService;
import cn.zhixiangsingle.service.auth.AuthService;
import cn.zhixiangsingle.service.site.SiteService;
import cn.zhixiangsingle.shiro.ShiroRealm;
import cn.zhixiangsingle.web.responsive.IStatusMessage;

import cn.zhixiangsingle.web.responsive.ResultBean;
import cn.zhixiangsingle.web.responsive.WebMassage;
import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import com.google.common.collect.Lists;
import net.sf.oval.ConstraintViolation;
import net.sf.oval.Validator;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.mgt.RealmSecurityManager;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangpingtai
 * @Package cn.zhixiangsingle.controller.user
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019/8/23 15:35
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Controller
@RequestMapping("/user")
public class UserController {
    private static final Logger logger = LoggerFactory
            .getLogger(UserController.class);
    @Reference(version = "1.0.0")
    private UserService userService;
    @Bean(name = "userService")
    public UserService getUserService(){
        return userService;
    }
    @Reference(version = "1.0.0")
    private AuthService authService;
    @Reference(version = "1.0.0")
    private SiteService siteService;
    @Autowired
    private EhCacheManager ecm;
    /**
     * @version V1.0
     * @Title: zhixiangpingtai
     * @Description:
     * @author: hhp
     * @param:  * @param
     * @date: 2019/8/23 15:40
     */
    @RequestMapping("/userList")
    public String toUserList() {
        return "/auth/userList";
    }
    /**
     * @version V1.0
     * @Title: zhixiangpingtai
     * @Description:
     * @author: hhp
     * @param:  * @param page
     * @param limit
     * @param userSearch
     * @date: 2019/8/23 15:40
     */
    @RequestMapping(value = "/getUsers", method = RequestMethod.POST)
    @ResponseBody
    @RequiresPermissions(value = "usermanage")
    public ResultBean getUsers(@RequestParam("page") Integer page,
                               @RequestParam("limit") Integer limit, UserSearchDTO userSearch) {
        logger.debug("分页查询用户列表！搜索条件：userSearch：" + userSearch + ",page:" + page
                + ",每页记录数量limit:" + limit);
        User existUser= (User) SecurityUtils.getSubject().getPrincipal();
        ResultBean resultBean = new ResultBean();
        try {
            if(existUser.getZx()==true||existUser.getSdId()!=22){
                userSearch.setSdId(existUser.getSdId());
            }
            resultBean = userService.getUsers(userSearch, page, limit);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("用户列表查询异常！", e);
            resultBean.setSuccess(false);
            resultBean.setResultCode(IStatusMessage.SystemStatus.ERROR.getCode());
            resultBean.setMsg(IStatusMessage.SystemStatus.SUCCESS.getMessage());
        }
        return resultBean;
    }
    /**
     * @version V1.0
     * @Title: zhixiangpingtai
     * @Description:
     * @author: hhp
     * @param:  * @param id
     * @param isJob
     * @param version
     * @date: 2019/8/23 15:43
     */
    @RequestMapping(value = "/setJobUser", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean setJobUser(@RequestParam("id") Integer id,
                             @RequestParam("job") Integer isJob) {
        logger.debug("设置用户是否离职！id:" + id + ",isJob:" + isJob);
        ResultBean resultBean = new ResultBean();
        try {
            if (null == id || null == isJob) {
                logger.debug("设置用户是否离职，结果=请求参数有误，请您稍后再试");
                resultBean.setSuccess(false);
                resultBean.setResultCode(IStatusMessage.SystemStatus.PARAMETERS_LOSE.getCode());
                resultBean.setMsg(IStatusMessage.SystemStatus.PARAMETERS_LOSE.getMessage());
                return resultBean;
            }
            User existUser = (User) SecurityUtils.getSubject().getPrincipal();
            if (null == existUser) {
                logger.debug("设置用户是否离职，结果=您未登录或登录超时，请您登录后再试");
                resultBean.setSuccess(false);
                resultBean.setResultCode(IStatusMessage.SystemStatus.USER_NOT_LOGIN.getCode());
                resultBean.setMsg(IStatusMessage.SystemStatus.USER_NOT_LOGIN.getMessage());
                return resultBean;
            }
            // 设置用户是否离职
            resultBean = userService.setJobUser(id, isJob, existUser.getId());
            logger.info("设置用户是否离职成功！userID=" + id + ",isJob:" + isJob
                    + "，操作的用户ID=" + existUser.getId());
        } catch (Exception e) {
            e.printStackTrace();
            resultBean.setSuccess(false);
            resultBean.setResultCode(IStatusMessage.SystemStatus.ERROR.getCode());
            resultBean.setMsg(IStatusMessage.SystemStatus.ERROR.getMessage());
            logger.error("设置用户是否离职异常！", e);
        }
        return resultBean;
    }
    /**
     * @version V1.0
     * @Title: zhixiangpingtai
     * @Description:
     * @author: hhp
     * @param:  * @param roleIds
     * @param siteRoleIds
     * @param user
     * @date: 2019/8/23 16:04
     */
    @PostMapping(value = "/setUser")
    @ResponseBody
    public ResultBean setUser(@RequestParam("roleIds") String roleIds, @RequestParam("siteRoleIds") String siteRoleIds, User user) {
        logger.debug("设置用户[新增或更新]！user:" + user + ",roleIds:" + roleIds+", siteRoleIds:"+siteRoleIds);
        ResultBean rb = new ResultBean();
        try {
            if(IsEmptyUtils.isEmpty(user.getUserName())||IsEmptyUtils.isEmpty(user.getPassword())) {
                logger.debug("设置用户[新增或更新]，结果=请您填写用户信息");
                rb.setSuccess(false);
                rb.setResultCode(IStatusMessage.SystemStatus.PARAMETERS_LOSE.getCode());
                rb.setMsg(WebMassage.PARAMETERS_REQUIRED_LOSE);
                return rb;
            }
            if (StringUtils.isEmpty(roleIds)) {
                logger.debug("设置用户[新增或更新]，结果=请您给用户设置角色");
                rb.setSuccess(false);
                rb.setResultCode(IStatusMessage.SystemStatus.ROLE_IS_EMPTY.getCode());
                rb.setMsg(IStatusMessage.SystemStatus.ROLE_IS_EMPTY.getMessage());
                return rb;
            }
            if (StringUtils.isEmpty(siteRoleIds)) {
                logger.debug("设置用户[新增或更新]，结果=请您给用户设置站点角色");
                rb.setSuccess(false);
                rb.setResultCode(IStatusMessage.SystemStatus.SITE_ROLE_IS_EMPTY.getCode());
                rb.setMsg("请您给用户设置站点角色");
                return rb;
            }
            User existUser = (User) SecurityUtils.getSubject().getPrincipal();
            user.setInsertUid(existUser.getId());
            if(!IsEmptyUtils.isEmpty(user.getSdId())){
                if(existUser.getZx()==true||existUser.getSdId()!=22){
                    user.setSdId(existUser.getSdId());
                }
            }else{
                if(existUser.getZx()==false&&existUser.getSdId()==22){
                    rb.setSuccess(false);
                    rb.setResultCode(IStatusMessage.SystemStatus.PARAMETERS_LOSE.getCode());
                    rb.setMsg("智飨科技提示："+existUser.getUserName()+"您好，请为当前”"+user.getUserName()+"“账号设置试点id");
                    return rb;
                }
                user.setSdId(existUser.getSdId());
            }
            logger.info("设置用户[新增或更新]！user=" + user + ",roleIds=" + roleIds + ",siteRoleIds="+siteRoleIds
                    + "，操作的用户ID=" + existUser.getId());
            String whichMsg = "";
            if(user.getId()!=null){
                RealmSecurityManager rsm = (RealmSecurityManager) SecurityUtils
                        .getSecurityManager();
                ShiroRealm authRealm = (ShiroRealm) rsm.getRealms().iterator()
                        .next();
                authRealm.clearCachedAuth();
                logger.debug("清除所有用户权限缓存！！！");

                whichMsg = "更新用户信息成功";

            }else{
                whichMsg = "开通用户成功";
            }

            rb =  userService.setUser(user, roleIds, existUser, siteRoleIds);

            if(!rb.isSuccess()){

            }else{

                if (user.getId() != null){
                    if (existUser != null
                            && existUser.getId().intValue() == user.getId().intValue()) {
                        logger.debug("更新自己的信息，退出重新登录！adminUser=" + existUser);
                        SecurityUtils.getSubject().logout();
                    }
                }
                rb.setSuccess(true);
                rb.setResultCode(IStatusMessage.SystemStatus.SUCCESS.getCode());//未通过校验
                rb.setMsg(whichMsg);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("设置用户[新增或更新]异常！", e);
            rb.setSuccess(false);
            rb.setResultCode(IStatusMessage.SystemStatus.ERROR.getCode());
            rb.setMsg(IStatusMessage.SystemStatus.ERROR.getMessage());
        }
        return rb;
    }

    /**
     * @version V1.0
     * @Title: zhixiangpingtai
     * @Description:
     * @author: hhp
     * @param:  * @param id
     * @date: 2019/8/31 16:44
     */
    @RequestMapping(value = "/delUser", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean delUser(@RequestParam("id") Integer id) {
        logger.debug("删除用户！id:" + id);
        ResultBean resultBean = new ResultBean();
        try {
            if (null == id) {
                logger.debug("删除用户，结果=请求参数有误，请您稍后再试");
                resultBean.setSuccess(false);
                resultBean.setResultCode(IStatusMessage.SystemStatus.PARAMETERS_LOSE.getCode());
                resultBean.setMsg(IStatusMessage.SystemStatus.PARAMETERS_LOSE.getMessage());
                return resultBean;
            }
            User existUser = (User) SecurityUtils.getSubject().getPrincipal();
            if (null == existUser) {
                logger.debug("删除用户，结果=您未登录或登录超时，请您登录后再试");
                resultBean.setSuccess(false);
                resultBean.setResultCode(IStatusMessage.SystemStatus.USER_NOT_LOGIN.getCode());
                resultBean.setMsg(IStatusMessage.SystemStatus.USER_NOT_LOGIN.getMessage());
                return resultBean;
            }
            resultBean = userService.setDelUser(id);
            logger.info("删除用户:"  + "！userId=" + id + "，操作用户id:"
                    + existUser.getId());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("删除用户异常！", e);
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
     * @date: 2019/8/30 10:17
     */
    @PostMapping(value = "/getUserAndRoles")
    @ResponseBody
    public ResultBean getUserAndRoles(@RequestParam("id") Integer id) {
        logger.debug("查询用户数据！id:" + id);
        ResultBean resultBean = new ResultBean();
        try {
            User existUser= (User) SecurityUtils.getSubject().getPrincipal();
            if(IsEmptyUtils.isEmpty(existUser)){
                resultBean.setSuccess(false);
                resultBean.setResultCode(IStatusMessage.SystemStatus.USER_NOT_LOGIN.getCode());
                resultBean.setMsg(IStatusMessage.SystemStatus.USER_NOT_LOGIN.getMessage());
            }else{
                if (null == id) {
                    logger.debug("查询用户数据==请求参数有误，请您稍后再试");
                    resultBean.setSuccess(false);
                    resultBean.setResultCode(IStatusMessage.SystemStatus.USER_NOT_EXIST.getCode());
                    resultBean.setMsg(IStatusMessage.SystemStatus.USER_NOT_EXIST.getMessage());
                }else{
                    Map<String,Object> resultMap = new HashMap<>();
                    UserRolesVO urvo = userService.getUserAndRoles(id);
                    logger.debug("查询用户数据！urvo=" + urvo);
                    if (null != urvo) {
                        resultMap.put("user", urvo);
                        Integer sdId = null;
                        if(existUser.getZx()==true||existUser.getSdId()!=22){
                            sdId = existUser.getSdId();
                        }
                        String sdIdStr = String.valueOf(existUser.getSdId());
                        String path = UserController.class.getClassLoader().getResource("json/shiftsData.json").getPath();
                        String siteJsonData = JsonUtil.readJsonFile(path);
                        JSONObject jobj = JSON.parseObject(siteJsonData);

                        String jobPath = UserController.class.getClassLoader().getResource("json/JobData.json").getPath();
                        String jobJsonData = JsonUtil.readJsonFile(jobPath);
                        JSONObject jobJob = JSON.parseObject(jobJsonData);
                        Shifts shifts = null;
                        JobData jobData = null;
                        if(!IsEmptyUtils.isEmpty(jobj.get(sdIdStr))){
                            shifts = JSON.parseObject(jobj.get(sdIdStr).toString(),Shifts.class);
                        }
                        if(!IsEmptyUtils.isEmpty(jobJob.get(sdIdStr))){
                            jobData = JSON.parseObject(jobJob.get(sdIdStr).toString(),JobData.class);
                        }
                        resultBean.setSuccess(true);
                        resultBean.setResultCode(IStatusMessage.SystemStatus.SUCCESS.getCode());
                        resultBean.setMsg(IStatusMessage.SystemStatus.SUCCESS.getMessage());
                        resultMap.put("shifts",shifts);
                        resultMap.put("jobData",jobData);

                        Role role = new Role();
                        role.setSdId(sdId);
                        role.setRoleName("r");
                        role.setId(-1);
                        List<Role> roles = this.authService.getRoles(role);
                        role = null;
                        logger.debug("查询角色数据！roles=" + roles);
                        if (null != roles && roles.size() > 0) {
                            resultMap.put("roles", roles);
                        }
                        SiteRole siteRole = new SiteRole();
                        siteRole.setId(-1);
                        siteRole.setSdId(sdId);
                        siteRole.setRoleName("r");
                        List<SiteRole> siteRoles = this.siteService.getSiteRoles(siteRole);
                        siteRole = null;
                        logger.debug("查询站点角色数据！siteRoles=" + siteRoles);
                        if (null != siteRoles && siteRoles.size() > 0) {
                            resultMap.put("siteRoles", siteRoles);
                        }
                        resultBean.setSuccess(true);
                        resultBean.setResultCode(IStatusMessage.SystemStatus.SUCCESS.getCode());
                        resultBean.setMsg(IStatusMessage.SystemStatus.SUCCESS.getMessage());
                        resultBean.setResult(resultMap);
                    } else {
                        resultBean.setSuccess(false);
                        resultBean.setResultCode(IStatusMessage.SystemStatus.USER_NOT_EXIST.getCode());
                        resultBean.setMsg(IStatusMessage.SystemStatus.USER_NOT_EXIST.getMessage());
                    }
                    logger.debug("查询用户数据成功！map=" + resultBean);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            resultBean.setSuccess(false);
            resultBean.setResultCode(IStatusMessage.SystemStatus.ERROR.getCode());
            resultBean.setMsg(IStatusMessage.SystemStatus.ERROR.getMessage());
            logger.error("查询用户数据异常！", e);
        }
        return resultBean;
    }
    /**
     * @version V1.0
     * @Title: zhixiangpingtai
     * @Description:
     * @author: hhp
     * @param:  * @param userDTO
     * @date: 2019/8/24 16:36
     */
    @PostMapping(value = "login")
    @ResponseBody
    public ResultBean login(UserDTO userDTO) {
        logger.debug("用户登录，请求参数=user:" + userDTO);
        ResultBean resultBean = new ResultBean();
        User existUser = null;
        try {
            if (null == userDTO) {
                resultBean.setSuccess(false);
                resultBean.setResultCode(IStatusMessage.SystemStatus.PARAMETERS_LOSE.getCode());
                resultBean.setMsg(IStatusMessage.SystemStatus.PARAMETERS_LOSE.getMessage());
                logger.debug("用户登录，结果=resultBean:" + resultBean);
                return resultBean;
            }
            if (!validatorRequestParam(userDTO, resultBean)) {
                logger.debug("用户登录，结果=resultBean:" + resultBean);
                return resultBean;
            }
            User user = new User();
            BeanUtils.copyProperties(userDTO, user);
            user.setSdId(1);
            user.setEmail("2");
            Date myDate = new Date();
            user.setInsertTime(myDate);
            user.setUpdateTime(myDate);
            myDate=null;
            user.setInsertUid(1);
            user.setJob(true);
            user.setZx(true);
            user.setProfession(-1);
            user.setQsType(-1);
            user.setClasses(-1);
            user.setAccessCode("a");
            existUser = userService.findUserByName(user);
            if (existUser == null) {
                resultBean.setSuccess(false);
                resultBean.setResultCode(IStatusMessage.SystemStatus.USER_NOT_EXIST.getCode());
                resultBean.setMsg(IStatusMessage.SystemStatus.USER_NOT_EXIST.getMessage());
                logger.debug("用户登录，结果=responseResult:" + resultBean);
                return resultBean;
            } else {
                if (existUser.getJob()) {
                    resultBean.setSuccess(false);
                    resultBean.setResultCode(IStatusMessage.SystemStatus.USER_HAS_QUIT.getCode());
                    resultBean.setMsg(IStatusMessage.SystemStatus.USER_HAS_QUIT.getMessage());
                    logger.debug("用户登录，结果=responseResult:" + resultBean);
                    return resultBean;
                }
            }
            AuthenticationToken token = new UsernamePasswordToken(
                    user.getUserName(), DigestUtils.md5Hex(user.getPassword()));
            Subject subject = SecurityUtils.getSubject();
            logger.debug("用户登录，用户验证开始！user=" + userDTO.getUserName());
            subject.login(token);
            resultBean.setSuccess(true);
            resultBean.setResultCode(IStatusMessage.SystemStatus.SUCCESS
                    .getCode());
            logger.info("用户登录，用户验证通过！user=" + userDTO.getUserName());
        } catch (UnknownAccountException uae) {
            logger.error("用户登录，用户验证未通过：未知用户！user=" + userDTO.getUserName(), uae);
            resultBean.setSuccess(false);
            resultBean.setResultCode(IStatusMessage.SystemStatus.USER_NOT_EXIST.getCode());
            resultBean.setMsg(IStatusMessage.SystemStatus.USER_NOT_EXIST.getMessage());
        } catch (IncorrectCredentialsException ice) {
            logger.error("用户登录，用户验证未通过：错误的凭证，密码输入错误！user=" +userDTO.getUserName(),
                    ice);
            resultBean.setSuccess(false);
            resultBean.setResultCode(IStatusMessage.SystemStatus.NAME_OR_PASSWORD_WRONG.getCode());
            resultBean.setMsg(IStatusMessage.SystemStatus.NAME_OR_PASSWORD_WRONG.getMessage());
        } catch (LockedAccountException lae) {
            logger.error("用户登录，用户验证未通过：账户已锁定！user=" + userDTO.getUserName(), lae);
            resultBean.setSuccess(false);
            resultBean.setResultCode(IStatusMessage.SystemStatus.ACCOUNT_IS_LOCKED.getCode());
            resultBean.setMsg(IStatusMessage.SystemStatus.ACCOUNT_IS_LOCKED.getMessage());
        } catch (ExcessiveAttemptsException eae) {
            logger.error(
                    "用户登录，用户验证未通过：错误次数大于5次,账户已锁定！user=.getMobile()" + userDTO, eae);
            resultBean.setSuccess(false);
            resultBean.setResultCode(IStatusMessage.SystemStatus.ACCOUINT_INVALIDATE_OVER_FIVE_TOTAL.getCode());
            resultBean.setMsg(IStatusMessage.SystemStatus.ACCOUINT_INVALIDATE_OVER_FIVE_TOTAL.getMessage());
        }catch (AuthenticationException ae) {
            logger.error("用户登录，用户验证未通过：认证异常，异常信息如下！user=" + userDTO.getUserName(),
                    ae);
            resultBean.setSuccess(false);
            resultBean.setResultCode(IStatusMessage.SystemStatus.NAME_OR_PASSWORD_WRONG.getCode());
            resultBean.setMsg(IStatusMessage.SystemStatus.NAME_OR_PASSWORD_WRONG.getMessage());
        } catch (Exception e) {
            logger.error("用户登录，用户验证未通过：操作异常，异常信息如下！user=" + userDTO.getUserName(), e);
            resultBean.setSuccess(false);
            resultBean.setResultCode(IStatusMessage.SystemStatus.NAME_OR_PASSWORD_WRONG.getCode());
            resultBean.setMsg(IStatusMessage.SystemStatus.NAME_OR_PASSWORD_WRONG.getMessage());
        }
        logger.debug("用户登录，user=" +userDTO.getUserName() + ",登录结果=resultBean:"
                + resultBean);
        Cache<String, AtomicInteger> passwordRetryCache = ecm
                .getCache("passwordRetryCache");
        if (null != passwordRetryCache) {
            int retryNum = (passwordRetryCache.get(existUser.getUserName()) == null ? 0
                    : passwordRetryCache.get(existUser.getUserName())).intValue();
            logger.debug("输错次数：" + retryNum);
            if (retryNum > 0 && retryNum < 6) {
                resultBean.setSuccess(false);
                resultBean.setResultCode(IStatusMessage.SystemStatus.USER_WRONG_LOGIN_COUNT.getCode());
                resultBean.setMsg(IStatusMessage.SystemStatus.USER_WRONG_LOGIN_COUNT.getMessage() + retryNum + "次,再输错"
                        + (6 - retryNum) + "次账号将锁定");
            }
        }
        return resultBean;
    }
    /**
     * @version V1.0
     * @Title: zhixiangpingtai
     * @Description:
     * @author: hhp
     * @param:  * @param obj
     * @param resultBean
     * @date: 2019/8/24 14:36
     */
    protected boolean validatorRequestParam(Object obj, ResultBean resultBean) {
        boolean flag = false;
        Validator validator = new Validator();
        List<ConstraintViolation> ret = validator.validate(obj);
        if (ret.size() > 0) {
            // 校验参数有误
            resultBean.setResultCode(IStatusMessage.SystemStatus.PARAM_ERROR.getCode());
            resultBean.setMsg(ret.get(0).getMessageTemplate());
        } else {
            flag = true;
        }
        return flag;
    }

    /**
     * @version V1.0
     * @Title: zhixiangpingtai
     * @Description:
     * @author: hhp
     * @param:  * @param
     * @date: 2019/8/31 16:45
     */
    @RequestMapping(value = "getShifts", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean getShifts() {
        ResultBean resultBean = new ResultBean();
        try {
            User existUser= (User) SecurityUtils.getSubject().getPrincipal();
            String sdId = String.valueOf(existUser.getSdId());
            String path = UserController.class.getClassLoader().getResource("json/shiftsData.json").getPath();
            String siteJsonData = JsonUtil.readJsonFile(path);
            JSONObject jobj = JSON.parseObject(siteJsonData);

            String jobPath = UserController.class.getClassLoader().getResource("json/JobData.json").getPath();
            String jobJsonData = JsonUtil.readJsonFile(jobPath);
            JSONObject jobJob = JSON.parseObject(jobJsonData);
            Shifts shifts = null;
            JobData jobData = null;
            if(!IsEmptyUtils.isEmpty(jobj.get(sdId))){
                shifts = JSON.parseObject(jobj.get(sdId).toString(),Shifts.class);
            }
            if(!IsEmptyUtils.isEmpty(jobJob.get(sdId))){
                jobData = JSON.parseObject(jobJob.get(sdId).toString(),JobData.class);
            }
            resultBean.setSuccess(true);
            resultBean.setResultCode(IStatusMessage.SystemStatus.SUCCESS.getCode());
            resultBean.setMsg(IStatusMessage.SystemStatus.SUCCESS.getMessage());
            Map<String,Object> map = new HashMap<String,Object>();
            map.put("shifts",shifts);
            map.put("jobData",jobData);
            resultBean.setResult(map);
        } catch (Exception e) {
            e.printStackTrace();
            resultBean.setSuccess(false);
            resultBean.setResultCode(IStatusMessage.SystemStatus.ERROR.getCode());
            resultBean.setMsg(IStatusMessage.SystemStatus.ERROR.getMessage());
            logger.error("修改密码异常！", e);
        }
        logger.debug("获取班次数据，结果=resultBean:" + resultBean);
        return resultBean;
    }
    /**
     * @version V1.0
     * @Title: zhixiangpingtai
     * @Description:
     * @author: hhp
     * @param:  * @param fieldId
     * @param userName
     * @param sdId
     * @date: 2019/9/21 17:46
     */
    @RequestMapping(value = "isNameExist", method = RequestMethod.POST)
    @ResponseBody
    public List<Object> isNameExist(@RequestParam("fieldId") String fieldId, @RequestParam("fieldValue") String userName, @RequestParam(value = "sdId",required = false)Integer sdId){
        List<Object> validateResult = Lists.newArrayList();
        try {
            User existUser= (User) SecurityUtils.getSubject().getPrincipal();
            User user = new User();
            user.setUserName(userName);
            if(existUser.getZx()==true||existUser.getSdId()!=22){
                user.setSdId(existUser.getSdId());
            }else{
                user.setSdId(sdId);
            }
            ResultBean resultBean = userService.selectCommon(user);
            if(!IsEmptyUtils.isEmpty(resultBean.getResult())){
                validateResult.add(fieldId);
                validateResult.add(false);
            }else{
                validateResult.add(fieldId);
                validateResult.add(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
            validateResult.add(fieldId);
            validateResult.add(false);
            logger.error("校验用户名异常！", e);
        }
        return validateResult;
    }

}
