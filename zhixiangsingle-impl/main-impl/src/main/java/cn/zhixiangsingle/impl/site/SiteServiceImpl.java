package cn.zhixiangsingle.impl.site;

import cn.zhixiangsingle.annotation.TargetDataSource;
import cn.zhixiangsingle.entity.base.IsEmptyUtils;
import cn.zhixiangsingle.entity.base.imgPath.ImgPath;
import cn.zhixiangsingle.entity.base.site.Site;
import cn.zhixiangsingle.entity.base.site.dto.SiteDTO;
import cn.zhixiangsingle.entity.base.site.vo.SiteVO;
import cn.zhixiangsingle.entity.base.siteRole.SiteRole;
import cn.zhixiangsingle.entity.base.siteRole.dto.SiteRoleDTO;
import cn.zhixiangsingle.entity.base.siteRole.vo.SiteRoleVO;
import cn.zhixiangsingle.entity.base.siteRolePermission.SiteRolePermissionKey;
import cn.zhixiangsingle.dao.roleSiteRole.RoleSiteRoleMapper;
import cn.zhixiangsingle.dao.site.SiteMapper;
import cn.zhixiangsingle.dao.siteRole.SiteRoleMapper;
import cn.zhixiangsingle.service.site.SiteService;
import cn.zhixiangsingle.web.responsive.IStatusMessage;
import cn.zhixiangsingle.web.responsive.ResultBean;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangpingtai
 * @Package cn.zhixiangsingle.impl.site
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019/8/23 12:19
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Service(version = "1.0.0", interfaceClass = SiteService.class)
public class SiteServiceImpl implements SiteService {
    private static final Logger logger = LoggerFactory
            .getLogger(SiteServiceImpl.class);
    @Autowired
    private SiteMapper siteMapper;
    @Autowired
    private SiteRoleMapper siteRoleMapper;
    @Autowired
    private RoleSiteRoleMapper roleSiteRoleMapper;

    /**
     * @version V1.0
     * @Title: zhixiangpingtai
     * @Description:
     * @author: hhp
     * @param:  * @param siteRoleDTO
     * @param page
     * @param limit
     * @date: 2019/8/23 12:28
     */
    @Override
    @TargetDataSource(name = "default")
    public ResultBean siteRoleList(SiteRoleDTO siteRoleDTO, Integer page, Integer limit) {
        ResultBean resultBean = new ResultBean();
        try {
            if (null == page) {
                page = 1;
            }
            if (null == limit) {
                limit = 10;
            }
            PageHelper.startPage(page, limit);
            List<SiteRole> roleList = this.siteRoleMapper.findSiteRoleList(siteRoleDTO);
            PageInfo<SiteRole> pageInfo = new PageInfo<>(roleList);
            resultBean.setSuccess(true);
            resultBean.setResult(roleList);
            resultBean.setTotal(Long.valueOf(pageInfo.getTotal()).intValue());
            resultBean.setResultCode(IStatusMessage.SystemStatus.SUCCESS.getCode());
            resultBean.setMsg(IStatusMessage.SystemStatus.SUCCESS.getMessage());
            logger.debug("站点角色列表查询=roleList:"+roleList);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("站点角色查询异常！", e);
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
     * @param:  * @param site
     * @date: 2019/8/23 12:30
     */
    @Override
    @TargetDataSource(name = "default")
    public int addSite(Site site) {
        return this.siteMapper.insert(site);
    }
    /**
     * @version V1.0
     * @Title: zhixiangpingtai
     * @Description:
     * @author: hhp
     * @param:  * @param
     * @date: 2019/8/23 12:30
     */
    @Override
    @TargetDataSource(name = "default")
    public ResultBean siteList(SiteVO siteVO) throws Exception {
        ResultBean resultBean = new ResultBean();
        siteVO.setId(-1);
        siteVO.setName("n");
        siteVO.setPhoto("p");
        siteVO.setAddress("a");
        StringBuffer stringBuffer = new StringBuffer();
        InputStream stream = getClass().getClassLoader().getResourceAsStream("json/imgPathData.json");
        BufferedReader br = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
        String line;
        while ((line = br.readLine()) != null) {
            stringBuffer.append(line);
        }
        JSONObject jobj = JSON.parseObject(stringBuffer.toString());
        ImgPath imgPath = null;
        if(!IsEmptyUtils.isEmpty(jobj.get(siteVO.getSdId().toString()))){
            imgPath = JSON.parseObject(jobj.get(siteVO.getSdId().toString()).toString(),ImgPath.class);
        }
        siteVO.setSdId(null);
        List<Site> sites = this.siteMapper.findAll(siteVO);
        resultBean.setSuccess(true);
        resultBean.setResult(sites);
        resultBean.setResultCode(IStatusMessage.SystemStatus.SUCCESS.getCode());
        resultBean.setMsg(IStatusMessage.SystemStatus.SUCCESS.getMessage());
        resultBean.setObj(imgPath.getImgPath());
        return resultBean;
    }
    /**
     * @version V1.0
     * @Title: zhixiangpingtai
     * @Description:
     * @author: hhp
     * @param:  * @param id
     * @date: 2019/8/23 12:31
     */
    @Override
    @TargetDataSource(name = "default")
    public Site getSite(int id) {
        return this.siteMapper.selectByPrimaryKey(id);
    }
    /**
     * @version V1.0
     * @Title: zhixiangpingtai
     * @Description:
     * @author: hhp
     * @param:  * @param id
     * @date: 2019/8/23 12:32
     */
    @Override
    @TargetDataSource(name = "default")
    public ResultBean delSite(int id) {
        ResultBean resultBean = new ResultBean();
        List<Site> childPerm = this.siteMapper.findChildSite(id);
        if(null != childPerm && childPerm.size()>0){
            resultBean.setSuccess(false);
            resultBean.setResultCode(IStatusMessage.SystemStatus.SUN_PERMISSION_NOT_DELETE.getCode());
            resultBean.setMsg(IStatusMessage.SystemStatus.SUN_PERMISSION_NOT_DELETE.getMessage());
            return resultBean;
        }
        if(this.siteMapper.deleteByPrimaryKey(id)>0){
            resultBean.setSuccess(true);
            resultBean.setResultCode(IStatusMessage.SystemStatus.SUCCESS.getCode());
            resultBean.setMsg(IStatusMessage.SystemStatus.SUCCESS.getMessage());
            return resultBean;
        }else{
            resultBean.setSuccess(false);
            resultBean.setResultCode(IStatusMessage.SystemStatus.ERROR.getCode());
            resultBean.setMsg(IStatusMessage.SystemStatus.ERROR.getMessage());
            return resultBean;
        }
    }
    /**
     * @version V1.0
     * @Title: zhixiangpingtai
     * @Description:
     * @author: hhp
     * @param:  * @param id
     * @date: 2019/8/23 12:32
     */
    @Override
    @TargetDataSource(name = "default")
    public SiteRoleVO findSiteRoleAndSites(Integer id) {
        return this.siteRoleMapper.findSiteRoleAndSites(id);
    }
    /**
     * @version V1.0
     * @Title: zhixiangpingtai
     * @Description:
     * @author: hhp
     * @param:  * @param flag
     * @param userId
     * @date: 2019/8/23 12:33
     */
    @Override
    @TargetDataSource(name = "default")
    public List<SiteVO> findSites(Integer flag, Integer userId) {
        List<SiteVO> siteVOS = null;
        if(flag==0){
            siteVOS = this.siteMapper.findSites();
        }else if(flag==1){
            siteVOS = this.siteMapper.findUserSites(userId);
        }
        return siteVOS;
    }
    /**
     * @version V1.0
     * @Title: zhixiangpingtai
     * @Description:
     * @author: hhp
     * @param:  * @param siteRole
     * @param permIds
     * @date: 2019/8/23 12:33
     */
    @Override
    @TargetDataSource(name = "default")
    @Transactional(propagation = Propagation.REQUIRED,isolation = Isolation.DEFAULT,timeout=30000,rollbackFor={RuntimeException.class, Exception.class})
    public ResultBean addSiteRole(SiteRole siteRole, String permIds) {
        ResultBean resultBean = new ResultBean();
        this.siteRoleMapper.insertSelective(siteRole);
        int roleId=siteRole.getId();
        String[] arrays=permIds.split(",");
        List<String> strsToList1= Arrays.asList(arrays);
        logger.debug("权限id =arrays="+arrays.toString());
        int inserCount = this.roleSiteRoleMapper.insertBitch(roleId,strsToList1);
        if(!IsEmptyUtils.isEmpty(arrays)&&inserCount<1){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            resultBean.setSuccess(false);
            resultBean.setResultCode(IStatusMessage.SystemStatus.ERROR.getCode());
            resultBean.setMsg(IStatusMessage.SystemStatus.ERROR.getMessage());
            return resultBean;
        }
        resultBean.setSuccess(true);
        resultBean.setResultCode(IStatusMessage.SystemStatus.SUCCESS.getCode());
        resultBean.setMsg(IStatusMessage.SystemStatus.SUCCESS.getMessage());
        return resultBean;
    }

    @Override
    @TargetDataSource(name = "default")
    @Transactional(propagation = Propagation.REQUIRED,isolation = Isolation.DEFAULT,timeout=30000,rollbackFor={RuntimeException.class, Exception.class})
    public ResultBean updateSiteRole(SiteRole siteRole, String permIds) {
        ResultBean resultBean = new ResultBean();
        int roleId=siteRole.getId();
        String[] arrays=permIds.split(",");
        logger.debug("站点id =arrays="+arrays.toString());
        int num=this.siteRoleMapper.updateByPrimaryKeySelective(siteRole);
        if(num<1){
            //事务回滚
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            resultBean.setSuccess(false);
            resultBean.setResultCode(IStatusMessage.SystemStatus.ERROR.getCode());
            resultBean.setMsg(IStatusMessage.SystemStatus.ERROR.getMessage());
            return resultBean;
        }
        this.roleSiteRoleMapper.deleteBySiteRoleId(roleId);
        List<String> strsToList1= Arrays.asList(arrays);
        int inserCount = this.roleSiteRoleMapper.insertBitch(roleId,strsToList1);
        if(!IsEmptyUtils.isEmpty(arrays)&&inserCount<1){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            resultBean.setSuccess(false);
            resultBean.setResultCode(IStatusMessage.SystemStatus.ERROR.getCode());
            resultBean.setMsg(IStatusMessage.SystemStatus.ERROR.getMessage());
            return resultBean;
        }
        resultBean.setSuccess(true);
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
     * @date: 2019/8/23 12:36
     */
    @Override
    @TargetDataSource(name = "default")
    @Transactional(propagation = Propagation.REQUIRED,isolation = Isolation.DEFAULT,timeout=30000,rollbackFor={RuntimeException.class, Exception.class})
    public ResultBean delSiteRole(int id) {
        ResultBean resultBean = new ResultBean();
        this.roleSiteRoleMapper.deleteBySiteRoleId(id);
        //2.删除角色
        int num=this.siteRoleMapper.deleteByPrimaryKey(id);
        if(num<1){
            //事务回滚
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            resultBean.setSuccess(false);
            resultBean.setResultCode(IStatusMessage.SystemStatus.ERROR.getCode());
            resultBean.setMsg(IStatusMessage.SystemStatus.ERROR.getMessage());
            return resultBean;
        }
        resultBean.setSuccess(true);
        resultBean.setResultCode(IStatusMessage.SystemStatus.SUCCESS.getCode());
        resultBean.setMsg(IStatusMessage.SystemStatus.SUCCESS.getMessage());
        return resultBean;
    }
    /**
     * @version V1.0
     * @Title: zhixiangpingtai
     * @Description:
     * @author: hhp
     * @param:  * @param sdId
     * @date: 2019/8/23 12:37
     */
    @Override
    @TargetDataSource(name = "default")
    public List<SiteRole> getSiteRoles(SiteRole siteRole) {
        return this.siteRoleMapper.findSiteRoles(siteRole);
    }
    /**
     * @version V1.0
     * @Title: zhixiangpingtai
     * @Description:
     * @author: hhp
     * @param:  * @param id
     * @date: 2019/8/23 12:37
     */
    @Override
    @TargetDataSource(name = "default")
    public ResultBean getUserSites(Integer id) throws Exception {
        ResultBean resultBean = new ResultBean();
        StringBuffer stringBufferJson = new StringBuffer();
        InputStream stream = getClass().getClassLoader().getResourceAsStream("json/siteData.json");
        BufferedReader br = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
        String line;
        while ((line = br.readLine()) != null) {
            stringBufferJson.append(line);
        }
        JSONObject jobj = JSON.parseObject(stringBufferJson.toString());
        resultBean.setSuccess(true);
        resultBean.setResult(this.siteMapper.findUserSites(id));
        resultBean.setResultCode(IStatusMessage.SystemStatus.SUCCESS.getCode());
        resultBean.setMsg(IStatusMessage.SystemStatus.SUCCESS.getMessage());
        resultBean.setObj(jobj);
        return resultBean;
    }
    /**
     * @version V1.0
     * @Title: zhixiangpingtai
     * @Description:
     * @author: hhp
     * @param:  * @param site
     * @date: 2019/8/23 12:37
     */
    @Override
    @TargetDataSource(name = "default")
    public int updateSite(Site site) {
        return this.siteMapper.updateByPrimaryKeySelective(site);
    }
    /**
     * @version V1.0
     * @Title: zhixiangpingtai
     * @Description:
     * @author: hhp
     * @param:  * @param sdId
     * @date: 2019/8/23 12:37
     */
    @Override
    @TargetDataSource(name = "default")
    public SiteVO findSiteBySdId(Integer sdId) {
        return this.siteMapper.findSiteBySdId(sdId);
    }
    /**
     * @version V1.0
     * @Title: zhixiangpingtai
     * @Description:
     * @author: hhp
     * @param:  * @param id
     * @date: 2019/8/23 12:37
     */
    @Override
    @TargetDataSource(name = "default")
    public List<SiteVO> findSitesByPId(Integer id) {
        return this.siteMapper.findSitesByPId(id);
    }
    /**
     * @version V1.0
     * @Title: zhixiangpingtai
     * @Description:
     * @author: hhp
     * @param:  * @param sdId
     * @date: 2019/8/23 12:38
     */
    @Override
    @TargetDataSource(name = "default")
    public ResultBean getPhotoBySdId(String sdId) {
        ResultBean resultBean = new ResultBean();
        String photo = this.siteMapper.findPhotoBySdId(sdId);
        resultBean.setResult(photo);
        resultBean.setSuccess(true);
        resultBean.setResultCode(IStatusMessage.SystemStatus.SUCCESS.getCode());
        return resultBean;
    }
    /**
     * @version V1.0
     * @Title: zhixiangpingtai
     * @Description: 查询系统是否到期
     * @author: hhp
     * @param:  * @param sdId 试点
     * @date: 2019/8/13 19:13
     */
    @TargetDataSource(name = "default")
    @Override
    public ResultBean isSiteOverTime(String sdId){
        ResultBean resultBean = new ResultBean();
        try {
            Map<String,Object> timesMap = this.siteMapper.findOverTimeSiteId(sdId);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date currentDate = new Date();
            if(!IsEmptyUtils.isEmpty(timesMap)){
                if(!IsEmptyUtils.isEmpty(timesMap.get("overTime"))){
                    resultBean.setObj(timesMap.get("overTime"));
                    int bigCompare = currentDate.compareTo(sdf.parse(timesMap.get("overTime").toString()));
                    switch (bigCompare){
                        case -1:
                            //比较临期时间
                            if(!IsEmptyUtils.isEmpty(timesMap.get("advanceTime"))){
                                int advanceCompare = currentDate.compareTo(sdf.parse(timesMap.get("advanceTime").toString()));
                                switch (advanceCompare){
                                    case -1:
                                        resultBean.setResultCode(200);
                                        resultBean.setResult(0);
                                        break;
                                    case 0:
                                        //两个的逻辑一样，故直接放行执行接下来的代码
                                        resultBean.setResultCode(200);
                                        resultBean.setResult(1);
                                        break;
                                    case 1:
                                        System.out.println(timesMap.get("overTime")+",22222222222222222,,"+timesMap.get("advanceTime"));
                                        resultBean.setResultCode(200);
                                        resultBean.setResult(1);
                                        break;
                                }
                            }
                            break;
                        case 0:
                            //两个的逻辑一样，故直接放行执行接下来的代码
                            resultBean.setResultCode(200);
                            resultBean.setResult(2);
                            break;
                        case 1:
                            resultBean.setResultCode(200);
                            resultBean.setResult(2);
                            break;
                    }

                }else{
                    if(!IsEmptyUtils.isEmpty(timesMap.get("advanceTime"))){
                        int advanceCompare = currentDate.compareTo(sdf.parse(timesMap.get("advanceTime").toString()));
                        switch (advanceCompare){
                            case -1:
                                resultBean.setResultCode(200);
                                resultBean.setResult(0);
                                break;
                            case 0:
                                //两个的逻辑一样，故直接放行执行接下来的代码
                                resultBean.setResultCode(200);
                                resultBean.setResult(1);
                                break;
                            case 1:
                                resultBean.setResultCode(200);
                                resultBean.setResult(1);
                                break;
                        }
                    }else{
                        resultBean.setResultCode(IStatusMessage.SystemStatus.SITE_IS_NOT_SET_WARNING_OVER_TIME.getCode());
                        resultBean.setMsg(IStatusMessage.SystemStatus.SITE_IS_NOT_SET_WARNING_OVER_TIME.getMessage());
                    }
                }
            }else{
                resultBean.setResultCode(IStatusMessage.SystemStatus.SITE_IS_NOT_SET_WARNING_OVER_TIME.getCode());
                resultBean.setMsg(IStatusMessage.SystemStatus.SITE_IS_NOT_SET_WARNING_OVER_TIME.getMessage());
            }
        } catch (ParseException e) {
            e.printStackTrace();
            resultBean.setResultCode(IStatusMessage.SystemStatus.ERROR.getCode());
            resultBean.setMsg(IStatusMessage.SystemStatus.SITE_IS_NOT_SET_WARNING_OVER_TIME.getMessage());
        }
        return resultBean;
    }
    /**
     * @version V1.0
     * @Title: zhixiangpingtai
     * @Description:
     * @author: hhp
     * @param:  * @param siteDTO
     * @param page
     * @param limit
     * @date: 2019/8/23 12:46
     */
    @TargetDataSource(name = "default")
    @Override
    public ResultBean getPageSiteList(SiteDTO siteDTO, Integer page, Integer limit) {
        ResultBean resultBean = new ResultBean();
        if (null == page) {
            page = 1;
        }
        if (null == limit) {
            limit = 10;
        }
        siteDTO.setPage(page);
        siteDTO.setLimit(limit);
        PageHelper.startPage(page, limit);
        List<Site> siteList = siteMapper.findPageSiteList(siteDTO);
        // 获取分页查询后的数据
        PageInfo<Site> pageInfo = new PageInfo<>(siteList);
        // 设置获取到的总记录数total：
        resultBean.setTotal(Long.valueOf(pageInfo.getTotal()).intValue());
        resultBean.setResult(siteList);
        return resultBean;
    }
}
