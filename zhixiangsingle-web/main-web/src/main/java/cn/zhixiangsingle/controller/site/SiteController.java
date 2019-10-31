package cn.zhixiangsingle.controller.site;

import cn.zhixiangsingle.controller.IndexController;
import cn.zhixiangsingle.entity.base.IsEmptyUtils;
import cn.zhixiangsingle.entity.base.OpslabConfig;
import cn.zhixiangsingle.entity.base.imgPath.ImgPath;
import cn.zhixiangsingle.entity.base.site.Site;
import cn.zhixiangsingle.entity.base.site.vo.SiteVO;
import cn.zhixiangsingle.entity.base.siteRole.SiteRole;
import cn.zhixiangsingle.entity.base.siteRole.dto.SiteRoleDTO;
import cn.zhixiangsingle.entity.base.siteRole.vo.SiteRoleVO;
import cn.zhixiangsingle.entity.base.siteRolePermission.SiteRolePermissionKey;
import cn.zhixiangsingle.entity.base.user.User;
import cn.zhixiangsingle.json.JsonUtil;
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
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Date;
import java.util.List;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangpingtai
 * @Package cn.zhixiangsingle.controller.site
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019/8/23 17:00
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Controller
@RequestMapping("/site")
public class SiteController {
    private static final Logger logger = LoggerFactory
            .getLogger(SiteController.class);
    @Reference(version = "1.0.0")
    private SiteService siteService;
    /**
     * @version V1.0
     * @Title: zhixiangpingtai
     * @Description:
     * @author: hhp
     * @param:  * @param
     * @date: 2019/9/10 8:50
     */
    @RequestMapping(value = "/getMapPage", method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView getMapPage() {
        logger.debug("站点列表！");
        ModelAndView mav = new ModelAndView("/jcxxpt/zdjcxx/zdBase");
        try {
            mav.addObject("msg", "ok");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("站点查询异常！", e);
        }
        return mav;
    }
    /**
     * @version V1.0
     * @Title: zhixiangpingtai
     * @Description:
     * @author: hhp
     * @param:  * @param site
     * @date: 2019/9/10 8:51
     */
    @RequestMapping(value = "/addSite", method = RequestMethod.POST)
    @ResponseBody
    public ModelAndView toPage(Site site) {
        logger.debug("新增站点--site-" + site);
        ModelAndView mav = new ModelAndView("/home");
        try {
            if (null != site) {
                siteService.addSite(site);
                logger.debug("新增站点成功！-site-" + site);
                mav.addObject("msg", "ok");
            }
        } catch (Exception e) {
            e.printStackTrace();
            mav.addObject("msg", "fail");
            logger.error("新增站点异常！", e);
        }
        return mav;
    }

    /**
     * @version V1.0
     * @Title: zhixiangpingtai
     * @Description:
     * @author: hhp
     * @param:  * @param
     * @date: 2019/9/10 8:51
     */
    @RequestMapping(value = "/siteList", method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView permList() {
        logger.debug("站点列表！");
        ModelAndView mav = new ModelAndView("/auth/siteList");
        try {
            mav.addObject("msg", "ok");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("站点查询异常！", e);
        }
        return mav;
    }
    /**
     * @version V1.0
     * @Title: zhixiangpingtai
     * @Description:
     * @author: hhp
     * @param:  * @param
     * @date: 2019/9/10 9:55
     */
    @RequestMapping(value = "/siteListBfe", method = RequestMethod.GET)
    public ModelAndView permListCge() {
        return new ModelAndView("/auth/permList");
    }
    /**
     * @version V1.0
     * @Title: zhixiangpingtai
     * @Description:
     * @author: hhp
     * @param:  * @param
     * @date: 2019/9/10 9:56
     */
    @PostMapping(value = "/getSiteList")
    @ResponseBody
    public ResultBean getPermList(SiteVO siteVO) {
        logger.debug("站点列表！查询条件 siteVO:"+siteVO);
        ResultBean rb = new ResultBean();
        try {
            User existUser = (User) SecurityUtils.getSubject().getPrincipal();
            siteVO.setSdId(existUser.getSdId());
            rb = siteService.siteList(siteVO);
            logger.debug("站点列表查询=siteList:" + rb);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("站点查询异常！", e);
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
     * @param site
     * @date: 2019/9/19 9:25
     */
    @RequestMapping(value = "/setSite", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean setPerm(
            @RequestParam("type") int type, Site site) {
        logger.debug("设置权限--区分type-" + type + "【0：编辑；1：新增子节点站点】，权限--site-"
                + site);
        ResultBean rb = new ResultBean();
        try {
            if (null != site) {
                Date date = new Date();
                if (0 == type) {
                    rb.setMsg("更新站点成功");
                    //编辑权限
                    this.siteService.updateSite(site);
                } else if (1 == type) {
                    //增加子节点权限
                    rb.setMsg("新增成功");
                    this.siteService.addSite(site);
                }
                logger.debug("设置站点成功！-site-" + site);
                rb.setResultCode(IStatusMessage.SystemStatus.SUCCESS.getCode());
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("设置站点异常！", e);
            rb.setMsg("设置站点出错，请您稍后再试");
            rb.setResultCode(IStatusMessage.SystemStatus.ERROR.getCode());
        }
        return rb;
    }
    /**
     * @version V1.0
     * @Title: zhixiangpingtai
     * @Description:
     * @author: hhp
     * @param:  * @param id
     * @date: 2019/9/19 9:24
     */
    @PostMapping(value = "/getSite")
    @ResponseBody
    public ResultBean getPerm(
            @RequestParam("id") int id) {
        logger.debug("获取站点--id-" + id);
        ResultBean resultBean = new ResultBean();
        try {
            User existUser= (User) SecurityUtils.getSubject().getPrincipal();
            String imgPathStr = IndexController.class.getClassLoader().getResource("json/imgPathData.json").getPath();
            String jobJsonData = JsonUtil.readJsonFile(imgPathStr);
            JSONObject jobJob = JSON.parseObject(jobJsonData);
            ImgPath imgPathData = null;
            if(!IsEmptyUtils.isEmpty(jobJob.get(existUser.getSdId().toString()))){
                imgPathData = JSON.parseObject(jobJob.get(existUser.getSdId().toString()).toString(),ImgPath.class);
            }
            if (id > 0) {
                resultBean.setResult(this.siteService.getSite(id));
                resultBean.setResultCode(IStatusMessage.SystemStatus.SUCCESS.getCode());
                resultBean.setMsg(IStatusMessage.SystemStatus.SUCCESS.getMessage());
                resultBean.setObj(imgPathData.getImgPath());
                logger.debug("获取站点成功！-site-");
                return resultBean;
            }else{
                resultBean.setSuccess(false);
                resultBean.setResultCode(IStatusMessage.SystemStatus.PARAM_ERROR.getCode());
                resultBean.setMsg(IStatusMessage.SystemStatus.PARAM_ERROR.getMessage());
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("获取站点异常！", e);
            resultBean.setSuccess(false);
            resultBean.setResultCode(IStatusMessage.SystemStatus.ERROR.getCode());
            resultBean.setMsg(IStatusMessage.SystemStatus.ERROR.getMessage());
        }
        return resultBean;
    }
    /**
     * 删除权限
     * @param id
     * @return
     */
    @PostMapping(value = "/del")
    @ResponseBody
    public ResultBean del(
            @RequestParam("id") int id) {
        logger.debug("删除站点--id-" + id);
        ResultBean resultBean = new ResultBean();
        try {
            if (id > 0) {
                return this.siteService.delSite(id);
            }else{
                resultBean.setSuccess(false);
                resultBean.setResultCode(IStatusMessage.SystemStatus.PARAM_ERROR.getCode());
                resultBean.setMsg(IStatusMessage.SystemStatus.PARAM_ERROR.getMessage());
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("删除站点异常！", e);
            resultBean.setSuccess(false);
            resultBean.setResultCode(IStatusMessage.SystemStatus.ERROR.getCode());
            resultBean.setMsg(IStatusMessage.SystemStatus.ERROR.getMessage());
        }
        return resultBean;
    }

    /**
     * 跳转到角色列表
     * @return
     */
    @RequestMapping("/siteRoleManage")
    public ModelAndView toPage() {
        return new ModelAndView("/auth/siteRoleManage");
    }

    /**
     * 角色列表
     * @return ok/fail
     */
    @PostMapping(value = "/getSiteRoleList")
    @ResponseBody
    public ResultBean getRoleList(@RequestParam("page") Integer page,
                                  @RequestParam("limit") Integer limit, SiteRoleDTO siteRoleDTO) {
        logger.debug("站点角色列表！");
        User existUser= (User) SecurityUtils.getSubject().getPrincipal();
        if(existUser.getZx()==true||existUser.getSdId()!=22){
            siteRoleDTO.setSdId(existUser.getSdId());
        }
        return siteService.siteRoleList(siteRoleDTO,page,limit);
    }

    /**
     * 查询权限树数据
     * @return PermTreeDTO
     */
    @PostMapping(value = "/findSites")
    @ResponseBody
    public List<SiteVO> findPerms() {
        logger.debug("站点权限树列表！");
        List<SiteVO> svo = null;
        try {

            User existUser= (User) SecurityUtils.getSubject().getPrincipal();
            Integer flag = 1;
            if(existUser.getZx()==false&&existUser.getSdId()==22){
                flag = 0;
            }/*else{
				SiteVO siteVo = siteService.findSiteBySdId(existUser.getSdid());
				svo = siteService.findSitesByPId(siteVo.getId());
				svo.add(siteVo);
			}*/
            //如果不是智飨查询该角色下的所有站点
            svo = siteService.findSites(flag,existUser.getId());

            //生成页面需要的json格式
            logger.debug("站点权限树列表查询=svo:" + svo);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("站点权限树列表查询异常！", e);
        }
        return svo;
    }
    /**
     * 根据id查询角色
     * @return PermTreeDTO
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
            SiteRoleVO srvo=this.siteService.findSiteRoleAndSites(id);
            //角色下的权限
            List<SiteRolePermissionKey> rpks=srvo.getSiteRolePerms();
            //获取全部权限数据
            User existUser= (User) SecurityUtils.getSubject().getPrincipal();
            Integer flag = 1;
            if(existUser.getZx()==false&&existUser.getSdId()==22){
                flag = 0;
            }
            List<SiteVO> pvos = siteService.findSites(flag,existUser.getId());
            for (SiteRolePermissionKey srpk : rpks) {
                //设置角色下的权限checked状态为：true
                for (SiteVO pvo : pvos) {
                    if(String.valueOf(srpk.getSiteId()).equals(String.valueOf(pvo.getId()))){
                        pvo.setChecked(true);
                    }
                }
            }
            mv.addObject("sites", pvos.toArray());
            //角色详情
            mv.addObject("roleDetail",srvo);
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
     * @date: 2019/9/19 13:46
     */
    @PostMapping(value = "/getUpdSiteRoles")
    @ResponseBody
    public ResultBean getUpdRoles(@RequestParam("id") Integer id) {
        logger.debug("根据id查询角色id："+id);
        ResultBean rb = new ResultBean();
        try {
            if(null==id){
                rb.setSuccess(false);
                rb.setResultCode(IStatusMessage.SystemStatus.PARAMETERS_LOSE.getCode());
                rb.setMsg(IStatusMessage.SystemStatus.PARAMETERS_LOSE.getMessage());
                return rb;
            }
            SiteRoleVO rvo=this.siteService.findSiteRoleAndSites(id);
            rb.setSuccess(true);
            rb.setResultCode(IStatusMessage.SystemStatus.SUCCESS.getCode());
            rb.setMsg(IStatusMessage.SystemStatus.SUCCESS.getMessage());
            rb.setResult(rvo);
            logger.debug("根据id查询角色数据："+rb);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("添加获取用户角色信息！异常！", e);
            rb.setSuccess(false);
            rb.setResultCode(IStatusMessage.SystemStatus.ERROR.getCode());
            rb.setMsg(IStatusMessage.SystemStatus.ERROR.getMessage());
        }
        return rb;
    }
    /**
     * 更新角色并授权
     * @return PermTreeDTO
     */
    @PostMapping(value = "/setSiteRole")
    @ResponseBody
    public ResultBean setRole(@RequestParam("roleSiteRole") String roleSiteRole, SiteRole siteRole) {
        logger.debug("更新站点角色并授权！站点角色数据siteSole："+siteRole+"，站点数据roleSiteRole："+roleSiteRole);
        ResultBean rb = new ResultBean();//采购
        try {
            if(StringUtils.isEmpty(roleSiteRole)){
                rb.setSuccess(false);
                rb.setResultCode(IStatusMessage.SystemStatus.PERMISSION_IS_EMPTY.getCode());
                rb.setMsg(IStatusMessage.SystemStatus.PERMISSION_IS_EMPTY.getMessage());
                return rb;
            }
            if(null == siteRole){
                rb.setSuccess(false);
                rb.setResultCode(IStatusMessage.SystemStatus.PARAMETERS_LOSE.getCode());
                rb.setMsg(WebMassage.PARAMETERS_REQUIRED_LOSE);
                return rb;
            }
            User existUser= (User) SecurityUtils.getSubject().getPrincipal();
            if(!IsEmptyUtils.isEmpty(siteRole.getSdId())){
                if(existUser.getZx()==true||existUser.getSdId()!=22){
                    siteRole.setSdId(existUser.getSdId());
                }
            }else{
                siteRole.setSdId(existUser.getSdId());
            }
            if(!IsEmptyUtils.isEmpty(siteRole.getId())){
                siteRole.setUpdateTime(new Date());
                rb = siteService.updateSiteRole(siteRole,roleSiteRole);
            }else{
                siteRole.setInsertTime(new Date());
                rb = siteService.addSiteRole(siteRole,roleSiteRole);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("更新或添加站点角色并授权！异常！", e);
            rb.setSuccess(false);
            rb.setResultCode(IStatusMessage.SystemStatus.ERROR.getCode());
            rb.setMsg(IStatusMessage.SystemStatus.ERROR.getMessage());
        }
        return rb;
    }

    /**
     * 删除角色以及它对应的权限
     * @param id
     * @return
     */
    @PostMapping(value = "/delRole")
    @ResponseBody
    public ResultBean delRole(
            @RequestParam("id") int id) {
        logger.debug("删除角色以及它对应的权限--id-" + id);
        ResultBean resultBean = new ResultBean();
        try {
            if (id > 0) {
                resultBean = this.siteService.delSiteRole(id);
            }else{
                resultBean.setSuccess(false);
                resultBean.setResultCode(IStatusMessage.SystemStatus.PARAM_ERROR.getCode());
                resultBean.setMsg(IStatusMessage.SystemStatus.PARAM_ERROR.getMessage());
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("删除站点角色异常！", e);
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
     * @date: 2019/8/29 9:13
     */
    @PostMapping(value = "/getSiteRoles")
    @ResponseBody
    public ResultBean getRoles() {
        logger.debug("查找所有角色!");
        ResultBean resultBean = new ResultBean();
        try {
            Integer sdId = null;
            User existUser= (User) SecurityUtils.getSubject().getPrincipal();
            if(existUser.getZx()==true||existUser.getSdId()!=22){
                //不是智飨内部人员添加的角色 将试点id设置成登录人员的sdid
                sdId = existUser.getSdId();
            }
            SiteRole siteRole = new SiteRole();
            siteRole.setId(-1);
            siteRole.setRoleName("r");
            siteRole.setSdId(sdId);
            siteRole.setCode("c");
            List<SiteRole> siteRoles = this.siteService.getSiteRoles(siteRole);
            siteRole = null;
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
     * 根据用户id查询站点权限树数据
     * @return PermTreeDTO
     */
    @PostMapping(value = "/getUserSites")
    @ResponseBody
    public ResultBean getUserPerms() {
        logger.debug("根据用户id查询站点！");
        ResultBean resultBean = new ResultBean();
        User existUser= (User) SecurityUtils.getSubject().getPrincipal();
        try {
            resultBean = siteService.getUserSites(existUser.getId());
            logger.debug("根据用户id查询站点查询=pvo:" + resultBean.getResult());
        } catch (Exception e) {
            e.printStackTrace();
            resultBean.setSuccess(false);
            resultBean.setResultCode(IStatusMessage.SystemStatus.ERROR.getCode());
            resultBean.setMsg(IStatusMessage.SystemStatus.ERROR.getMessage());
            logger.error("根据用户id查询站点查询异常！", e);
        }
        return resultBean;
    }

    /**
     *  得到该站点的站点图标
     * @param sdId 试点
     * @return
     */
    @PostMapping(value = "getPhotoBySdId")
    @ResponseBody
    public ResultBean getPhotoBySdId(@RequestParam(value = "sdId") String sdId) {
        logger.debug("得到该站点的站点图标！sdId:"+sdId);
        ResultBean resultBean = new ResultBean();
        try {
            resultBean = siteService.getPhotoBySdId(sdId);
        } catch (Exception e) {
            resultBean.setSuccess(false);
            e.printStackTrace();
            resultBean.setResultCode(IStatusMessage.SystemStatus.ERROR.getCode());
            logger.error("获取站点图标异常！", e);
        }
        resultBean.setObj(OpslabConfig.get("UPLOAD_PREFIX"));
        return resultBean;
    }

    /**
     * 添加权限【test】
     * @param
     * @return ok/fail
     */
    @RequestMapping(value = "/isSiteOverTime", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean isSiteOverTime(@RequestParam(value = "sdId") String sdId) {
        logger.debug("获取系统是否到期-参数sdId" + sdId);
        ResultBean resultBean = new ResultBean();
        try {
            resultBean = siteService.isSiteOverTime(sdId);
        } catch (Exception e) {
            resultBean.setSuccess(false);
            e.printStackTrace();
            resultBean.setResultCode(IStatusMessage.SystemStatus.ERROR.getCode());
            logger.error("获取站点图标异常！", e);
        }
        return resultBean;
    }
}
