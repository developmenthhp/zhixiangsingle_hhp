package cn.zhixiangsingle.controller.business;

import cn.zhixiangsingle.entity.base.IsEmptyUtils;
import cn.zhixiangsingle.entity.base.site.vo.SiteVO;
import cn.zhixiangsingle.entity.base.user.User;
import cn.zhixiangsingle.entity.business.dto.BusinessDTO;
import cn.zhixiangsingle.service.business.BusinessService;
import cn.zhixiangsingle.service.site.SiteService;
import cn.zhixiangsingle.web.responsive.IStatusMessage;
import cn.zhixiangsingle.web.responsive.ResultBean;
import com.alibaba.dubbo.config.annotation.Reference;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangpingtai
 * @Package cn.zhixiangsingle.controller.business
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019/10/8 19:31
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Controller
@RequestMapping("/business")
public class BusinessController {
    private static final Logger logger = LoggerFactory
            .getLogger(BusinessController.class);
    @Reference(version = "1.0.0")
    private SiteService siteService;
    @Reference(version = "1.0.0")
    private BusinessService businessService;
    /**
     * @version V1.0
     * @Title: zhixiangpingtai
     * @Description:
     * @author: hhp
     * @param:  * @param businessDTO
     * @date: 2019/10/9 10:32
     */
    @RequestMapping(value = "/setBusiness", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean setBusiness(BusinessDTO businessDTO) {
        logger.debug("添加或修改营业执照信息 --businessDTO-" + businessDTO);
        ResultBean resultBean = new ResultBean();
        try {
            User existUser = (User) SecurityUtils.getSubject().getPrincipal();
            if (!IsEmptyUtils.isEmpty(businessDTO)) {
                businessDTO.setUserId(existUser.getId());
                if(!IsEmptyUtils.isEmpty(businessDTO.getId())){
                    resultBean = businessService.updateBusiness(businessDTO);
                }else{
                    resultBean = siteService.getUserSites(existUser.getId());
                    if(!IsEmptyUtils.isEmpty(resultBean.getResult())){
                        List<SiteVO> siteVOList = (List<SiteVO>)resultBean.getResult();
                        if(siteVOList.size()>1){
                            if(!IsEmptyUtils.isEmpty(businessDTO.getSdIds())){
                                resultBean = businessService.addBusiness(businessDTO);
                            }else{
                                resultBean.setSuccess(false);
                                resultBean.setResultCode(IStatusMessage.SystemStatus.SITE_ID_LOSE.getCode());
                                resultBean.setMsg(IStatusMessage.SystemStatus.SITE_ID_LOSE.getMessage());
                            }
                        }else{
                            businessDTO.setSdId(existUser.getSdId());
                            businessDTO.setSdIds("");
                            resultBean = businessService.addBusiness(businessDTO);
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
            logger.error("添加或修改营业执照信息异常！", e);
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
     * @param:  * @param businessDTO
     * @date: 2019/10/9 9:41
     */
    @RequestMapping(value = "/delBusiness", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean delBusiness(BusinessDTO businessDTO){
        logger.debug("逻辑删除营业执照信息 --businessDTO-" + businessDTO);
        ResultBean resultBean = new ResultBean();
        try {
            User existUser = (User) SecurityUtils.getSubject().getPrincipal();
            if (!IsEmptyUtils.isEmpty(businessDTO)) {
                /*if(IsEmptyUtils.isEmpty(ingredientBaseDTO.getSdId())){
                    ingredientBaseDTO
                }*/
                resultBean = businessService.delBusiness(businessDTO);
            }else{
                resultBean.setSuccess(false);
                resultBean.setResultCode(IStatusMessage.SystemStatus.PARAMETERS_LOSE.getCode());
                resultBean.setMsg(IStatusMessage.SystemStatus.PARAMETERS_LOSE.getMessage());
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("删除营业执照异常！", e);
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
     * @param:  * @param businessDTO
     * @date: 2019/10/9 9:46
     */
    @PostMapping(value = "/getBusinessList")
    @ResponseBody
    public ResultBean getBusinessList(BusinessDTO businessDTO) {
        logger.debug("获取营业执照列表！ businessDTO = "+businessDTO);
        ResultBean resultBean = new ResultBean();
        try {
            User existUser = (User) SecurityUtils.getSubject().getPrincipal();
            businessDTO.setUserId(existUser.getId());
            if(existUser.getZx()==true||existUser.getSdId()!=22){
                businessDTO.setSdId(existUser.getSdId());
            }
            resultBean = businessService.findBusinessList(businessDTO);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("获取营业执照列表异常！", e);
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
     * @param:  * @param businessDTO
     * @date: 2019/10/9 9:46
     */
    @PostMapping(value = "/getUpdBusiness")
    @ResponseBody
    public ResultBean getUpdBusiness(BusinessDTO businessDTO) {
        logger.debug("获取营业执照信息！ businessDTO = "+businessDTO);
        ResultBean resultBean = new ResultBean();
        try {
            User existUser = (User) SecurityUtils.getSubject().getPrincipal();
            businessDTO.setUserId(existUser.getId());
            resultBean = businessService.findUpdBusiness(businessDTO);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("获取营业执照信息异常！", e);
            resultBean.setSuccess(false);
            resultBean.setResultCode(IStatusMessage.SystemStatus.ERROR.getCode());
            resultBean.setMsg(IStatusMessage.SystemStatus.ERROR.getMessage());
        }
        return resultBean;
    }

}
