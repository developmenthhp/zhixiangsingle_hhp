package cn.zhixiangsingle.controller.classification;

import cn.zhixiangsingle.entity.base.IsEmptyUtils;
import cn.zhixiangsingle.entity.base.site.vo.SiteVO;
import cn.zhixiangsingle.entity.base.user.User;
import cn.zhixiangsingle.entity.classification.dto.CategoryBaseDTO;
import cn.zhixiangsingle.service.classification.CategoryBaseService;
import cn.zhixiangsingle.service.site.SiteService;
import cn.zhixiangsingle.web.responsive.IStatusMessage;
import cn.zhixiangsingle.web.responsive.ResultBean;
import com.alibaba.dubbo.config.annotation.Reference;
import com.google.common.collect.Lists;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangpingtai
 * @Package cn.zhixiangsingle.controller.classification
 * @Description: 食材分类
 * @author: hhp
 * @date: 2019/9/23 18:08
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Controller
@RequestMapping("/categoryBase")
public class CategoryBaseController {
    private static final Logger logger = LoggerFactory
            .getLogger(CategoryBaseController.class);
    @Reference(version = "1.0.0")
    private SiteService siteService;
    @Reference(version = "1.0.0")
    private CategoryBaseService categoryBaseService;
    /**
     * @version V1.0
     * @Title: zhixiangpingtai
     * @Description:
     * @author: hhp
     * @param:  * @param
     * @date: 2019/9/27 16:11
     */
    @RequestMapping(value = "/getListPage", method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView getMapPage() {
        logger.debug("跳转食材分类信息列表！");
        ModelAndView mav = new ModelAndView("/cgcc/scflgl/scflgl");
        try {
            mav.addObject("msg", "ok");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("跳转食材岑类信息异常！", e);
        }
        return mav;
    }
    /**
     * @version V1.0
     * @Title: zhixiangpingtai
     * @Description:
     * @author: hhp
     * @param:  * @param categoryBaseDTO
     * @date: 2019/9/27 17:40
     */
    @PostMapping(value = "/getCategoryBaseList")
    @ResponseBody
    public ResultBean getCategoryBaseList(CategoryBaseDTO categoryBaseDTO) {
        logger.debug("获取分类信息列表！ categoryBaseDTO = "+categoryBaseDTO);
        ResultBean resultBean = new ResultBean();
        try {
            User existUser = (User) SecurityUtils.getSubject().getPrincipal();
            categoryBaseDTO.setUserId(existUser.getId());
            resultBean = categoryBaseService.findCategoryBaseList(categoryBaseDTO);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("获取食材分类列表异常！", e);
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
     * @param:  * @param categoryBaseDTO
     * @date: 2019/9/27 17:39
     */
    @RequestMapping(value = "/setCategoryBase", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean setCategoryBase(CategoryBaseDTO categoryBaseDTO) {
        logger.debug("添加或修改分类信息 --categoryBaseDTO-" + categoryBaseDTO);
        ResultBean resultBean = new ResultBean();
        try {
            User existUser = (User) SecurityUtils.getSubject().getPrincipal();
            if (!IsEmptyUtils.isEmpty(categoryBaseDTO)) {
                categoryBaseDTO.setUserId(existUser.getId());
                if(!IsEmptyUtils.isEmpty(categoryBaseDTO.getId())){
                    resultBean = categoryBaseService.updateCategoryBase(categoryBaseDTO);
                }else{
                    resultBean = siteService.getUserSites(existUser.getId());
                    if(!IsEmptyUtils.isEmpty(resultBean.getResult())){
                        List<SiteVO> siteVOList = (List<SiteVO>)resultBean.getResult();
                        if(siteVOList.size()>1){
                            if(categoryBaseDTO.getPId()==0){
                                if(!IsEmptyUtils.isEmpty(categoryBaseDTO.getSdIds())){
                                    resultBean = categoryBaseService.addCategoryBase(categoryBaseDTO);
                                }else{
                                    resultBean.setSuccess(false);
                                    resultBean.setResultCode(IStatusMessage.SystemStatus.SITE_ID_LOSE.getCode());
                                    resultBean.setMsg(IStatusMessage.SystemStatus.SITE_ID_LOSE.getMessage());
                                }
                            }else{
                                resultBean = categoryBaseService.addCategoryBase(categoryBaseDTO);
                            }
                        }else{
                            categoryBaseDTO.setSdId(existUser.getSdId());
                            categoryBaseDTO.setSdIds("");
                            resultBean = categoryBaseService.addCategoryBase(categoryBaseDTO);
                        }
                    }else{
                        resultBean.setSuccess(false);
                        resultBean.setResultCode(IStatusMessage.SystemStatus.SITE_ROLE_IS_EMPTY.getCode());
                        resultBean.setMsg(IStatusMessage.SystemStatus.SITE_ROLE_IS_EMPTY.getMessage());
                    }
                }
            }else{
                resultBean.setSuccess(false);
                resultBean.setResultCode(IStatusMessage.SystemStatus.PARAMETERS_LOSE.getCode());
                resultBean.setMsg(IStatusMessage.SystemStatus.PARAMETERS_LOSE.getMessage());
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("添加或修改分类信息异常！", e);
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
     * @param:  * @param categoryBaseDTO
     * @date: 2019/10/19 14:20
     */
    @PostMapping(value = "/getUpdCategoryBase")
    @ResponseBody
    public ResultBean getUpdCategoryBase(CategoryBaseDTO categoryBaseDTO) {
        logger.debug("获取分类！ categoryBaseDTO = "+categoryBaseDTO);
        ResultBean resultBean = new ResultBean();
        try {
            User existUser = (User) SecurityUtils.getSubject().getPrincipal();
            categoryBaseDTO.setUserId(existUser.getId());
            if(IsEmptyUtils.isEmpty(categoryBaseDTO.getSdId())){
                categoryBaseDTO.setSdId(existUser.getSdId());
            }
            resultBean = categoryBaseService.findUpdCategoryBase(categoryBaseDTO);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("获取分类！", e);
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
     * @param:  * @param categoryBaseDTO
     * @date: 2019/10/19 15:05
     */
    @PostMapping(value = "/delCategoryBase")
    @ResponseBody
    public ResultBean delCategoryBase(CategoryBaseDTO categoryBaseDTO){
        logger.debug("删除分类信息 --categoryBaseDTO-" + categoryBaseDTO);
        ResultBean resultBean = new ResultBean();
        try {
            User existUser = (User) SecurityUtils.getSubject().getPrincipal();
            categoryBaseDTO.setUserId(existUser.getId());
            if (!IsEmptyUtils.isEmpty(categoryBaseDTO)) {
                /*if(IsEmptyUtils.isEmpty(ingredientBaseDTO.getSdId())){
                    ingredientBaseDTO
                }*/
                resultBean = categoryBaseService.delCategoryBase(categoryBaseDTO);
            }else{
                resultBean.setSuccess(false);
                resultBean.setResultCode(IStatusMessage.SystemStatus.PARAMETERS_LOSE.getCode());
                resultBean.setMsg(IStatusMessage.SystemStatus.PARAMETERS_LOSE.getMessage());
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("删除分类异常！", e);
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
     * @param:  * @param categoryBaseDTO
     * @date: 2019/9/27 17:39
     */
    @PostMapping(value = "/getAll")
    @ResponseBody
    public ResultBean getAll(CategoryBaseDTO categoryBaseDTO) {
        logger.debug("获取所有分类信息列表！ categoryBaseDTO = "+categoryBaseDTO);
        ResultBean resultBean = new ResultBean();
        try {
            User existUser = (User) SecurityUtils.getSubject().getPrincipal();
            categoryBaseDTO.setUserId(existUser.getId());
            /*if(existUser.getZx()==true||existUser.getSdId()!=22){
                categoryBaseDTO.setSdId(existUser.getSdId());
            }*/
            resultBean = categoryBaseService.findAll(categoryBaseDTO);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("获取所有分类信息列表异常！", e);
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
     * @param:  * @param fieldId
     * @param categoryName
     * @param sdId
     * @date: 2019/9/27 17:39
     */
    @RequestMapping(value = "isNameExist", method = RequestMethod.POST)
    @ResponseBody
    public List<Object> isNameExist(@RequestParam("fieldId") String fieldId, @RequestParam("fieldValue") String categoryName, @RequestParam(value = "sdId",required = false)Integer sdId){
        List<Object> validateResult = Lists.newArrayList();
        try {
            User existUser= (User) SecurityUtils.getSubject().getPrincipal();
            CategoryBaseDTO categoryBaseDTO = new CategoryBaseDTO();
            categoryBaseDTO.setCategoryName(categoryName);
            if(existUser.getZx()==true||existUser.getSdId()!=22){
                ResultBean resultBean = siteService.getUserSites(existUser.getId());
                List<SiteVO> siteVOS = null;
                if(!IsEmptyUtils.isEmpty(resultBean.getResult())){
                    siteVOS = (List<SiteVO>)resultBean.getResult();
                    if(siteVOS.size()==1){
                        categoryBaseDTO.setSdId(existUser.getSdId());
                    }else{
                        categoryBaseDTO.setSdId(sdId);
                    }
                }else{
                    categoryBaseDTO.setSdId(existUser.getSdId());
                }
            }else{
                categoryBaseDTO.setSdId(sdId);
            }
            /*ResultBean resultBean = userService.selectCommon(user);*/
            ResultBean resultBean = null;
            resultBean.setSuccess(false);
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
            logger.error("校验分类重复名异常！", e);
        }
        return validateResult;
    }
}
