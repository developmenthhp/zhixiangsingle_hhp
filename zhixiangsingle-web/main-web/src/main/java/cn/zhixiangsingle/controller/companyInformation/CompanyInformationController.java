package cn.zhixiangsingle.controller.companyInformation;

import cn.zhixiangsingle.entity.base.IsEmptyUtils;
import cn.zhixiangsingle.entity.base.site.vo.SiteVO;
import cn.zhixiangsingle.entity.base.user.User;
import cn.zhixiangsingle.entity.companyInformation.dto.CompanyInformationDTO;
import cn.zhixiangsingle.service.companyInformation.CompanyInformationService;
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
 * @version V1.0
 *  @Title: zhixiangpingtai
 * @Package cn.zhixiangsingle.controller.companyInformation
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019/10/12 16:19
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Controller
@RequestMapping("/companyInformation")
public class CompanyInformationController {
    private static final Logger logger = LoggerFactory
            .getLogger(CompanyInformationController.class);
    @Reference(version = "1.0.0")
    private SiteService siteService;
    @Reference(version = "1.0.0")
    private CompanyInformationService companyInformationService;

    /**
     * @version V1.0
     * @Title: zhixiangpingtai
     * @Description:
     * @author: hhp
     * @param:  * @param
     * @date: 2019/10/12 16:33
     */
    @RequestMapping(value = "/getListPage", method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView getListPage() {
        logger.debug("跳转公司资讯信息列表！");
        ModelAndView mav = new ModelAndView("/jygs/gszx/gszx");
        try {
            mav.addObject("msg", "ok");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("跳转公司资讯异常！", e);
        }
        return mav;
    }

    /**
     * @version V1.0
     * @Title: zhixiangpingtai
     * @Description:
     * @author: hhp
     * @param:  * @param companyInformationDTO
     * @date: 2019/10/12 17:47
     */
    @PostMapping(value = "/getCompanyInformationList")
    @ResponseBody
    public ResultBean getCompanyInformationList(CompanyInformationDTO companyInformationDTO) {
        logger.debug("获取公司资讯列表！ companyInformationDTO = "+companyInformationDTO);
        ResultBean resultBean = new ResultBean();
        try {
            User existUser = (User) SecurityUtils.getSubject().getPrincipal();
            companyInformationDTO.setUserId(existUser.getId());
            resultBean = companyInformationService.findCompanyInformation(companyInformationDTO);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("获取公司资讯列表异常！", e);
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
     * @param:  * @param companyInformationDTO
     * @date: 2019/10/12 17:48
     */
    @RequestMapping(value = "/setCompanyInformation", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean setCompanyInformation(CompanyInformationDTO companyInformationDTO) {
        logger.debug("添加或修改公司资讯信息 --companyInformationDTO-" + companyInformationDTO);
        ResultBean resultBean = new ResultBean();
        try {
            User existUser = (User) SecurityUtils.getSubject().getPrincipal();
            if (!IsEmptyUtils.isEmpty(companyInformationDTO)) {
                companyInformationDTO.setUserId(existUser.getId());
                if(!IsEmptyUtils.isEmpty(companyInformationDTO.getId())){
                    resultBean = companyInformationService.updateCompanyInformation(companyInformationDTO);
                }else{
                    resultBean = siteService.getUserSites(existUser.getId());
                    if(!IsEmptyUtils.isEmpty(resultBean.getResult())){
                        List<SiteVO> siteVOList = (List<SiteVO>)resultBean.getResult();
                        if(siteVOList.size()>1){
                            if(!IsEmptyUtils.isEmpty(companyInformationDTO.getSdIds())){
                                resultBean = companyInformationService.addCompanyInformation(companyInformationDTO);
                            }else{
                                resultBean.setSuccess(false);
                                resultBean.setResultCode(IStatusMessage.SystemStatus.SITE_ID_LOSE.getCode());
                                resultBean.setMsg(IStatusMessage.SystemStatus.SITE_ID_LOSE.getMessage());
                            }
                        }else{
                            companyInformationDTO.setSdId(existUser.getSdId());
                            companyInformationDTO.setSdIds("");
                            resultBean = companyInformationService.addCompanyInformation(companyInformationDTO);
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
            logger.error("添加或修改公司资讯信息异常！", e);
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
     * @param:  * @param circulationCardDTO
     * @date: 2019/10/11 13:20
     */
    @PostMapping(value = "/getUpdCompanyInformation")
    @ResponseBody
    public ResultBean getUpdCompanyInformation(CompanyInformationDTO companyInformationDTO) {
        logger.debug("获取公司资讯！ companyInformationDTO = "+companyInformationDTO);
        ResultBean resultBean = new ResultBean();
        try {
            User existUser = (User) SecurityUtils.getSubject().getPrincipal();
            companyInformationDTO.setUserId(existUser.getId());
            if(IsEmptyUtils.isEmpty(companyInformationDTO.getSdId())){
                companyInformationDTO.setSdId(existUser.getSdId());
            }
            resultBean = companyInformationService.findUpdCompanyInformation(companyInformationDTO);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("获取公司资讯异常！", e);
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
     * @param:  * @param companyInformationDTO
     * @date: 2019/10/14 9:37
     */
    @PostMapping(value = "/delCompanyInformation")
    @ResponseBody
    public ResultBean delCompanyInformation(CompanyInformationDTO companyInformationDTO){
        logger.debug("删除公司资讯信息 --companyInformationDTO-" + companyInformationDTO);
        ResultBean resultBean = new ResultBean();
        try {
            User existUser = (User) SecurityUtils.getSubject().getPrincipal();
            companyInformationDTO.setUserId(existUser.getId());
            if (!IsEmptyUtils.isEmpty(companyInformationDTO)) {
                /*if(IsEmptyUtils.isEmpty(ingredientBaseDTO.getSdId())){
                    ingredientBaseDTO
                }*/
                resultBean = companyInformationService.delCompanyInformation(companyInformationDTO);
            }else{
                resultBean.setSuccess(false);
                resultBean.setResultCode(IStatusMessage.SystemStatus.PARAMETERS_LOSE.getCode());
                resultBean.setMsg(IStatusMessage.SystemStatus.PARAMETERS_LOSE.getMessage());
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("删除公司资讯异常！", e);
            resultBean.setSuccess(false);
            resultBean.setResultCode(IStatusMessage.SystemStatus.ERROR.getCode());
            resultBean.setMsg(IStatusMessage.SystemStatus.ERROR.getMessage());
        }
        return resultBean;
    }
}

