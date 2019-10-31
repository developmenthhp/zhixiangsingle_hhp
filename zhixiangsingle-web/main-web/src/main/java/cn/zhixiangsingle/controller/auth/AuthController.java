package cn.zhixiangsingle.controller.auth;

import cn.zhixiangsingle.entity.base.IsEmptyUtils;
import cn.zhixiangsingle.entity.base.job.JobData;
import cn.zhixiangsingle.entity.base.permission.Permission;
import cn.zhixiangsingle.entity.base.permission.vo.PermissionVO;
import cn.zhixiangsingle.entity.base.role.Role;
import cn.zhixiangsingle.entity.base.role.dto.RoleDTO;
import cn.zhixiangsingle.entity.base.role.vo.RoleVO;
import cn.zhixiangsingle.entity.base.rolePermission.RolePermissionKey;
import cn.zhixiangsingle.entity.base.shifts.Shifts;
import cn.zhixiangsingle.entity.base.siteRole.SiteRole;
import cn.zhixiangsingle.entity.base.user.User;
import cn.zhixiangsingle.json.JsonUtil;
import cn.zhixiangsingle.service.auth.AuthService;
import cn.zhixiangsingle.service.site.SiteService;
import cn.zhixiangsingle.web.responsive.IStatusMessage;
import cn.zhixiangsingle.web.responsive.ResultBean;
import cn.zhixiangsingle.web.responsive.WebMassage;
import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangpingtai
 * @Package cn.zhixiangsingle.controller.auth
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019/8/23 16:35
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Controller
@RequestMapping("/auth")
public class AuthController {
    private static final Logger logger = LoggerFactory
            .getLogger(AuthController.class);
    @Reference(version = "1.0.0")
    private AuthService authService;
    @Bean(name = "authService")
    public AuthService getAuthService(){
        return authService;
    }
    @Reference(version = "1.0.0")
    private SiteService siteService;
    /**
     * @version V1.0
     * @Title: zhixiangpingtai
     * @Description:
     * @author: hhp
     * @param:  * @param permission
     * @date: 2019/8/27 17:25
     */
    @RequestMapping(value = "/addPermission", method = RequestMethod.POST)
    @ResponseBody
    public ModelAndView toPage(
            Permission permission) {
        logger.debug("新增权限--permission-" + permission);
        ModelAndView mav = new ModelAndView("/home");
        try {
            if (null != permission) {
                permission.setInsertTime(new Date());
                authService.addPermission(permission);
                logger.debug("新增权限成功！-permission-" + permission);
                mav.addObject("msg", "ok");
            }
        } catch (Exception e) {
            e.printStackTrace();
            mav.addObject("msg", "fail");
            logger.error("新增权限异常！", e);
        }
        return mav;
    }

    /**
     * @version V1.0
     * @Title: zhixiangpingtai
     * @Description:
     * @author: hhp
     * @param:  * @param
     * @date: 2019/8/27 17:25
     */
    @RequestMapping(value = "/permList", method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView permList() {
        logger.debug("权限列表！");
        ModelAndView mav = new ModelAndView("/auth/permList");
        try {
            mav.addObject("msg", "ok");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("权限查询异常！", e);
        }
        return mav;
    }

    /**
     * @version V1.0
     * @Title: zhixiangpingtai
     * @Description:
     * @author: hhp
     * @param:  * @param
     * @date: 2019/8/27 17:36
     */
    @RequestMapping(value = "/permListBfe", method = RequestMethod.GET)
    public ModelAndView permListCge() {
        return new ModelAndView("/auth/permList");
    }

    /**
     * @version V1.0
     * @Title: zhixiangpingtai
     * @Description:
     * @author: hhp
     * @param:  * @param
     * @date: 2019/8/27 17:36
     */
    @PostMapping(value = "/getPermList")
    @ResponseBody
    public ResultBean getPermList() {
        logger.debug("权限列表！");
        ResultBean rb = new ResultBean();
        try {
            User existUser = (User) SecurityUtils.getSubject().getPrincipal();
            if (IsEmptyUtils.isEmpty(existUser)) {
                rb.setSuccess(false);
                rb.setResultCode(IStatusMessage.SystemStatus.USER_NOT_LOGIN.getCode());
                rb.setMsg(IStatusMessage.SystemStatus.USER_NOT_LOGIN.getMessage());
            }else{
                List<Permission> permList = authService.permList();
                logger.debug("权限列表查询=permList:" + permList);
                rb.setSuccess(true);
                rb.setResultCode(IStatusMessage.SystemStatus.SUCCESS.getCode());
                rb.setMsg(IStatusMessage.SystemStatus.SUCCESS.getCode());
                rb.setResult(permList);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("权限查询异常！", e);
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
     * @param:  * @param type
     * @param permission
     * @date: 2019/8/26 15:59
     */
    @RequestMapping(value = "/setPerm", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean setPerm(
            @RequestParam("type") int type, Permission permission) {
        logger.debug("设置权限--区分type-" + type + "【0：编辑；1：新增子节点权限】，权限--permission-"
                + permission);
        ResultBean resultBean = new ResultBean();
        try {
            User existUser = (User) SecurityUtils.getSubject().getPrincipal();
            if (IsEmptyUtils.isEmpty(existUser)) {
                resultBean.setSuccess(false);
                resultBean.setResultCode(IStatusMessage.SystemStatus.USER_NOT_LOGIN.getCode());
                resultBean.setMsg(IStatusMessage.SystemStatus.USER_NOT_LOGIN.getMessage());
            }else{
                if (null != permission) {
                    Date date = new Date();
                    if (0 == type) {
                        permission.setUpdateTime(date);
                        //编辑权限
                        this.authService.updatePerm(permission);
                    } else if (1 == type) {
                        permission.setInsertTime(date);
                        //增加子节点权限
                        this.authService.addPermission(permission);
                    }
                    logger.debug("设置权限成功！-permission-" + permission);
                    resultBean.setSuccess(true);
                    resultBean.setResultCode(IStatusMessage.SystemStatus.SUCCESS.getCode());
                    resultBean.setMsg(IStatusMessage.SystemStatus.SUCCESS.getMessage());
                }else{
                    resultBean.setSuccess(false);
                    resultBean.setResultCode(IStatusMessage.SystemStatus.PARAMETERS_LOSE.getCode());
                    resultBean.setMsg(IStatusMessage.SystemStatus.PARAMETERS_LOSE.getMessage());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("设置权限异常！", e);
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
     * @date: 2019/8/27 17:36
     */
    @PostMapping(value = "/getPerm")
    @ResponseBody
    public ResultBean getPerm(
            @RequestParam("id") int id) {
        logger.debug("获取权限--id-" + id);
        ResultBean resultBean = new ResultBean();
        try {
            User existUser = (User) SecurityUtils.getSubject().getPrincipal();
            if (IsEmptyUtils.isEmpty(existUser)) {
                resultBean.setSuccess(false);
                resultBean.setResultCode(IStatusMessage.SystemStatus.USER_NOT_LOGIN.getCode());
                resultBean.setMsg(IStatusMessage.SystemStatus.USER_NOT_LOGIN.getMessage());
            }else{
                if (id > 0) {
                    resultBean = this.authService.getPermission(id);
                    logger.debug("获取权限成功！-resultBean-" + resultBean);
                }else{
                    resultBean.setSuccess(false);
                    resultBean.setResultCode(IStatusMessage.SystemStatus.PARAM_ERROR.getCode());
                    resultBean.setMsg(IStatusMessage.SystemStatus.PARAM_ERROR.getMessage());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("获取权限异常！", e);
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
     * @date: 2019/8/27 17:37
     */
    @RequestMapping(value = "/del", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean del(
            @RequestParam("id") int id) {
        logger.debug("删除权限--id-" + id);
        ResultBean resultBean = new ResultBean();
        try {
            User existUser = (User) SecurityUtils.getSubject().getPrincipal();
            if (id > 0) {
                resultBean.setSuccess(true);
                resultBean.setResultCode(IStatusMessage.SystemStatus.SUCCESS.getCode());
                String msg = this.authService.delPermission(id);
                resultBean.setMsg(msg);
                msg = null;
            }else{
                resultBean.setSuccess(false);
                resultBean.setResultCode(IStatusMessage.SystemStatus.PARAM_ERROR.getCode());
                resultBean.setMsg(IStatusMessage.SystemStatus.PARAM_ERROR.getMessage());
            }
        } catch (Exception e) {
            e.printStackTrace();
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
     * @param:  * @param
     * @date: 2019/8/27 17:37
     */
    @RequestMapping("/roleManage")
    public ModelAndView toPage() {
        return new ModelAndView("/auth/roleManage");
    }

    /**
     * @version V1.0
     * @Title: zhixiangpingtai
     * @Description:
     * @author: hhp
     * @param:  * @param page
     * @param limit
     * @param roleDTO
     * @date: 2019/8/27 17:38
     */
    @PostMapping(value = "/getRoleList")
    @ResponseBody
    public ResultBean getRoleList(@RequestParam("page") Integer page,
                                  @RequestParam("limit") Integer limit,RoleDTO roleDTO) {
        logger.debug("获取权限角色列表！");
        ResultBean resultBean = new ResultBean();
        try {
            User existUser = (User) SecurityUtils.getSubject().getPrincipal();
            if (IsEmptyUtils.isEmpty(existUser)) {
                resultBean.setSuccess(false);
                resultBean.setResultCode(IStatusMessage.SystemStatus.USER_NOT_LOGIN.getCode());
                resultBean.setMsg(IStatusMessage.SystemStatus.USER_NOT_LOGIN.getMessage());
            }else{
                Integer sdId = null;
                if(existUser.getZx()==true||existUser.getSdId()!=22){
                    sdId = existUser.getSdId();
                    roleDTO.setSdId(sdId);
                }
                resultBean = authService.roleList(roleDTO,page,limit);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("角色查询异常！", e);
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
     * @param:  * @param
     * @date: 2019/8/27 17:38
     */
    @PostMapping(value = "/findPerms")
    @ResponseBody
    public List<PermissionVO> findPerms() {
        logger.debug("权限树列表！");
        List<PermissionVO> pvo = null;
        try {
            User existUser= (User) SecurityUtils.getSubject().getPrincipal();
            Integer flag = 1;
            if(existUser.getZx()==false&&existUser.getSdId()==22){
                flag = 0;
            }
            pvo = authService.findPerms(flag,existUser.getId());
            logger.debug("权限树列表查询=pvo:" + pvo);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("权限树列表查询异常！", e);
        }
        return pvo;
    }

    /**
     * @version V1.0
     * @Title: zhixiangpingtai
     * @Description:
     * @author: hhp
     * @param:  * @param permIds
     * @param role
     * @date: 2019/8/27 17:40
     */
    @PostMapping(value = "/addRole")
    @ResponseBody
    public ResultBean addRole(@RequestParam("rolePermIds") String permIds, Role role) {
        logger.debug("添加角色并授权！角色数据role："+role+"，权限数据permIds："+permIds);
        ResultBean rb = new ResultBean();
        try {
            if(StringUtils.isEmpty(permIds)){
                rb.setResultCode(IStatusMessage.SystemStatus.PERMISSION_IS_EMPTY.getCode());
                rb.setMsg(IStatusMessage.SystemStatus.PERMISSION_IS_EMPTY.getMessage());
                return rb;
            }
            if(null == role){
                rb.setResultCode(IStatusMessage.SystemStatus.PARAMETERS_LOSE.getCode());
                rb.setMsg(WebMassage.PARAMETERS_REQUIRED_LOSE);
                return rb;
            }else if(IsEmptyUtils.isEmpty(role.getRoleName())||IsEmptyUtils.isEmpty(role.getCode())){
                rb.setResultCode(IStatusMessage.SystemStatus.PARAMETERS_LOSE.getCode());
                rb.setMsg(WebMassage.PARAMETERS_REQUIRED_LOSE);
                return rb;
            }

            role.setInsertTime(new Date());

            User existUser= (User) SecurityUtils.getSubject().getPrincipal();
            if(!IsEmptyUtils.isEmpty(role.getSdId())){
                if(existUser.getZx()==true||existUser.getSdId()!=22){
                    //不是智飨内部人员添加的角色 将试点id设置成登录人员的sdid
                    role.setSdId(existUser.getSdId());
                }
            }else{
                role.setSdId(existUser.getSdId());
            }
            String msg = authService.addRole(role,permIds);
            if(IsEmptyUtils.isEmpty(msg)||!msg.equals("ok")){
                msg = "新增角色并授权权限失败";
                rb.setResultCode(IStatusMessage.SystemStatus.ERROR.getCode());
            }else{
                msg = "新增角色并授权权限成功";
                rb.setResultCode(IStatusMessage.SystemStatus.SUCCESS.getCode());
            }
            rb.setMsg(msg);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("添加角色并授权！异常！", e);
            rb.setResultCode(IStatusMessage.SystemStatus.ERROR.getCode());
            rb.setMsg("未授权，请您给该角色授权");
        }
        return rb;
    }
    /**
     * @version V1.0
     * @Title: zhixiangpingtai
     * @Description:
     * @author: hhp
     * @param:  * @param id
     * @date: 2019/8/27 17:40
     */
    @RequestMapping(value = "/updateRole/{id}", method = RequestMethod.GET)
    //@ResponseBody
    public ModelAndView updateRole(@PathVariable("id") Integer id) {
        logger.debug("根据id查询角色id："+id);
        ModelAndView mv=new ModelAndView("/auth/roleManage");
        try {
            if(null==id){
                mv.addObject("msg","请求参数有误，请您稍后再试");
                return mv;
            }
            mv.addObject("flag","updateRole");
            RoleVO rvo=this.authService.findRoleAndPerms(id);
            //角色下的权限
            List<RolePermissionKey> rpks=rvo.getRolePerms();
            //获取全部权限数据
            User existUser= (User) SecurityUtils.getSubject().getPrincipal();
            Integer flag = 1;
            if(existUser.getZx()==false&&existUser.getSdId()==22){
                flag = 0;
            }
            List<PermissionVO> pvos = authService.findPerms(flag,existUser.getId());
            for (RolePermissionKey rpk : rpks) {
                //设置角色下的权限checked状态为：true
                for (PermissionVO pvo : pvos) {
                    if(String.valueOf(rpk.getPermitId()).equals(String.valueOf(pvo.getId()))){
                        pvo.setChecked(true);
                    }
                }
            }
            mv.addObject("perms", pvos.toArray());
            //角色详情
            mv.addObject("roleDetail",rvo);
            logger.debug("根据id查询角色数据："+mv);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("添加角色并授权！异常！", e);
            mv.addObject("msg","请求异常，请您稍后再试");
        }
        return mv;
    }

    /**
     * @version V1.0
     * @Title: zhixiangpingtai
     * @Description:
     * @author: hhp
     * @param:  * @param id
     * @date: 2019/8/27 17:41
     */
    @PostMapping(value = "/getUpdRoles")
    @ResponseBody
    public ResultBean getUpdRoles(@RequestParam("id") Integer id) {
        logger.debug("根据id查询角色id："+id);
        ResultBean rb = new ResultBean();
        try {
            if(null==id){
                rb.setSuccess(false);
                rb.setResultCode(IStatusMessage.SystemStatus.PARAMETERS_LOSE.getCode());
                rb.setMsg(IStatusMessage.SystemStatus.PARAMETERS_LOSE.getMessage());
            }else{
                RoleVO rvo=this.authService.findRoleAndPerms(id);
                //角色下的权限
                rb.setSuccess(true);
                rb.setResultCode(IStatusMessage.SystemStatus.SUCCESS.getCode());
                rb.setResult(rvo);
            }
            logger.debug("根据id查询角色数据："+rb);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("添加角色并授权！异常！", e);
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
     * @param:  * @param permIds
     * @param role
     * @date: 2019/8/27 17:41
     */
    @PostMapping(value = "/setRole")
    @ResponseBody
    public ResultBean setRole(@RequestParam("rolePermIds") String permIds, Role role) {
        logger.debug("更新角色并授权！角色数据role："+role+"，权限数据permIds："+permIds);
        ResultBean rb = new ResultBean();
        try {
            if(StringUtils.isEmpty(permIds)){
                rb.setSuccess(false);
                rb.setResultCode(IStatusMessage.SystemStatus.PERMISSION_IS_EMPTY.getCode());
                rb.setMsg(IStatusMessage.SystemStatus.PERMISSION_IS_EMPTY.getMessage());
                return rb;
            }
            if(null == role){
                rb.setSuccess(false);
                rb.setResultCode(IStatusMessage.SystemStatus.PARAMETERS_LOSE.getCode());
                rb.setMsg(WebMassage.PARAMETERS_REQUIRED_LOSE);
                return rb;
            }
            role.setUpdateTime(new Date());
            User existUser= (User) SecurityUtils.getSubject().getPrincipal();
            if(!IsEmptyUtils.isEmpty(role.getSdId())){
                if(existUser.getZx()==true||existUser.getSdId()!=22){
                    role.setSdId(existUser.getSdId());
                }
            }else{
                role.setSdId(existUser.getSdId());
            }
            rb = authService.updateRole(role,permIds);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("更新角色并授权！异常！", e);
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
     * @date: 2019/8/27 17:42
     */
    @PostMapping(value = "/delRole")
    @ResponseBody
    public ResultBean delRole(
            @RequestParam("id") int id) {
        logger.debug("删除角色以及它对应的权限--id-" + id);
        ResultBean resultBean = new ResultBean();
        try {
            if (id > 0) {
                resultBean = this.authService.delRole(id);
            }else{
                resultBean.setSuccess(false);
                resultBean.setResultCode(IStatusMessage.SystemStatus.PARAM_ERROR.getCode());
                resultBean.setMsg(IStatusMessage.SystemStatus.PARAM_ERROR.getMessage());
            }
        } catch (Exception e) {
            e.printStackTrace();
            resultBean.setSuccess(false);
            resultBean.setResultCode(IStatusMessage.SystemStatus.ERROR.getCode());
            resultBean.setMsg(IStatusMessage.SystemStatus.ERROR.getMessage());
            logger.error("删除角色异常！", e);
        }
        return resultBean;
    }

    /**
     * @version V1.0
     * @Title: zhixiangpingtai
     * @Description:
     * @author: hhp
     * @param:  * @param
     * @date: 2019/8/27 17:42
     */
    @PostMapping(value = "/getRoles")
    @ResponseBody
    public ResultBean getRoles() {
        logger.debug("查找所有角色!");
        ResultBean resultBean = new ResultBean();
        Map<String, Object> map = new HashMap<>();
        List<Role> roles=null;
        List<SiteRole> siteRoles = null;
        try {

            Integer sdId = null;
            User existUser= (User) SecurityUtils.getSubject().getPrincipal();
            if(existUser.getZx()==true||existUser.getSdId()!=22){
                //不是智飨内部人员添加的角色 将试点id设置成登录人员的sdid
                sdId = existUser.getSdId();
            }
            String sdIdStr = String.valueOf(existUser.getSdId());
            String path = AuthController.class.getClassLoader().getResource("json/shiftsData.json").getPath();
            String siteJsonData = JsonUtil.readJsonFile(path);
            JSONObject jobj = JSON.parseObject(siteJsonData);

            String jobPath = AuthController.class.getClassLoader().getResource("json/JobData.json").getPath();
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
            map.put("shifts",shifts);
            map.put("jobData",jobData);

            Role role = new Role();
            role.setRoleName("r");
            role.setId(-1);
            role.setSdId(sdId);
            roles = this.authService.getRoles(role);
            role = null;
            SiteRole siteRole = new SiteRole();
            siteRole.setSdId(sdId);
            siteRole.setId(-1);
            siteRole.setRoleName("r");
            siteRoles = this.siteService.getSiteRoles(siteRole);
            siteRole = null;
            map.put("roles",roles);
            map.put("siteRoles",siteRoles);
            resultBean.setResult(map);
            resultBean.setSuccess(true);
            resultBean.setResultCode(IStatusMessage.SystemStatus.SUCCESS.getCode());
            resultBean.setMsg(IStatusMessage.SystemStatus.SUCCESS.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            resultBean.setSuccess(false);
            resultBean.setResultCode(IStatusMessage.SystemStatus.ERROR.getCode());
            resultBean.setMsg(IStatusMessage.SystemStatus.ERROR.getMessage());
            logger.error("查找所有角色异常！", e);
        }
        return resultBean;
    }
    /**
     * @version V1.0
     * @Title: zhixiangpingtai
     * @Description:
     * @author: hhp
     * @param:  * @param
     * @date: 2019/8/24 16:57
     */
    @PostMapping(value = "/getUserPerms")
    @ResponseBody
    public ResultBean getUserPerms() {
        ResultBean resultBean = new ResultBean();
        User existUser= (User) SecurityUtils.getSubject().getPrincipal();
        try {
            if(null==existUser){
                logger.debug("根据用户id查询限树列表！用户未登录");
                resultBean.setSuccess(false);
                resultBean.setResultCode(IStatusMessage.SystemStatus.USER_NOT_LOGIN.getCode());
                resultBean.setMsg(IStatusMessage.SystemStatus.USER_NOT_LOGIN.getMessage());
                return resultBean;
            }
            resultBean = authService.getUserPerms(existUser.getId());
            logger.debug("根据用户id查询限树列表查询=resultBean:" + resultBean);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("根据用户id查询权限树列表查询异常！", e);
            resultBean.setSuccess(false);
            resultBean.setResultCode(IStatusMessage.SystemStatus.ERROR.getCode());
            resultBean.setMsg(IStatusMessage.SystemStatus.ERROR.getMessage());
        }
        return resultBean;
    }
}
