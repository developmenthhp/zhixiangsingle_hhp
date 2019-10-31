package cn.zhixiangsingle.controller.companyHonor;

import cn.zhixiangsingle.entity.base.IsEmptyUtils;
import cn.zhixiangsingle.entity.base.site.vo.SiteVO;
import cn.zhixiangsingle.entity.base.user.User;
import cn.zhixiangsingle.entity.companyHonor.dto.CompanyHonorDTO;
import cn.zhixiangsingle.service.companyHonor.CompanyHonorService;
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
 * @Package cn.zhixiangsingle.controller.companyHonor
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019/10/14 18:40
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Controller
@RequestMapping("/companyHonor")
public class CompanyHonorController {
    private static final Logger logger = LoggerFactory
            .getLogger(CompanyHonorController.class);
    @Reference(version = "1.0.0")
    private SiteService siteService;
    @Reference(version = "1.0.0")
    private CompanyHonorService companyHonorService;
    /**
     * @version V1.0
     * @Title: zhixiangpingtai
     * @Description:
     * @author: hhp
     * @param:  * @param
     * @date: 2019/10/14 18:47
     */
    @RequestMapping(value = "/getListPage", method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView getListPage() {
        logger.debug("跳转公司荣誉信息列表！");
        ModelAndView mav = new ModelAndView("/jygs/gsry/gsry");
        try {
            mav.addObject("msg", "ok");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("跳转公司荣誉列表异常！", e);
        }
        return mav;
    }
    /**
     * @version V1.0
     * @Title: zhixiangpingtai
     * @Description:
     * @author: hhp
     * @param:  * @param companyHonorDTO
     * @date: 2019/10/14 18:50
     */
    @PostMapping(value = "/getCompanyHonorList")
    @ResponseBody
    public ResultBean getCompanyHonorList(CompanyHonorDTO companyHonorDTO) {
        logger.debug("获取公司荣誉列表！ companyHonorDTO = "+companyHonorDTO);
        ResultBean resultBean = new ResultBean();
        try {
            User existUser = (User) SecurityUtils.getSubject().getPrincipal();
            companyHonorDTO.setUserId(existUser.getId());
            resultBean = companyHonorService.findCompanyHonor(companyHonorDTO);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("获取公司荣誉列表异常！", e);
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
     * @param:  * @param companyHonorDTO
     * @date: 2019/10/14 18:53
     */
    @RequestMapping(value = "/setCompanyHonor", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean setCompanyHonor(CompanyHonorDTO companyHonorDTO) {
        logger.debug("添加或修改公司荣誉信息 --companyHonorDTO-" + companyHonorDTO);
        ResultBean resultBean = new ResultBean();
        try {
            User existUser = (User) SecurityUtils.getSubject().getPrincipal();
            if (!IsEmptyUtils.isEmpty(companyHonorDTO)) {
                companyHonorDTO.setUserId(existUser.getId());
                if(!IsEmptyUtils.isEmpty(companyHonorDTO.getId())){
                    resultBean = companyHonorService.updateCompanyHonor(companyHonorDTO);
                }else{
                    resultBean = siteService.getUserSites(existUser.getId());
                    if(!IsEmptyUtils.isEmpty(resultBean.getResult())){
                        List<SiteVO> siteVOList = (List<SiteVO>)resultBean.getResult();
                        if(siteVOList.size()>1){
                            if(!IsEmptyUtils.isEmpty(companyHonorDTO.getSdIds())){
                                resultBean = companyHonorService.addCompanyHonor(companyHonorDTO);
                            }else{
                                resultBean.setSuccess(false);
                                resultBean.setResultCode(IStatusMessage.SystemStatus.SITE_ID_LOSE.getCode());
                                resultBean.setMsg(IStatusMessage.SystemStatus.SITE_ID_LOSE.getMessage());
                            }
                        }else{
                            companyHonorDTO.setSdId(existUser.getSdId());
                            companyHonorDTO.setSdIds("");
                            resultBean = companyHonorService.addCompanyHonor(companyHonorDTO);
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
            logger.error("添加或修改公司荣誉信息异常！", e);
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
     * @param:  * @param companyHonorDTO
     * @date: 2019/10/14 18:53
     */
    @PostMapping(value = "/getUpdCompanyHonor")
    @ResponseBody
    public ResultBean getUpdCompanyHonor(CompanyHonorDTO companyHonorDTO) {
        logger.debug("获取公司荣誉！ companyHonorDTO = "+companyHonorDTO);
        ResultBean resultBean = new ResultBean();
        try {
            User existUser = (User) SecurityUtils.getSubject().getPrincipal();
            companyHonorDTO.setUserId(existUser.getId());
            if(IsEmptyUtils.isEmpty(companyHonorDTO.getSdId())){
                companyHonorDTO.setSdId(existUser.getSdId());
            }
            resultBean = companyHonorService.findUpdCompanyHonor(companyHonorDTO);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("获取公司荣誉异常！", e);
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
     * @param:  * @param companyHonorDTO
     * @date: 2019/10/14 18:57
     */
    @PostMapping(value = "/delCompanyHonor")
    @ResponseBody
    public ResultBean delCompanyHonor(CompanyHonorDTO companyHonorDTO){
        logger.debug("删除公司荣誉信息 --companyHonorDTO-" + companyHonorDTO);
        ResultBean resultBean = new ResultBean();
        try {
            User existUser = (User) SecurityUtils.getSubject().getPrincipal();
            companyHonorDTO.setUserId(existUser.getId());
            if (!IsEmptyUtils.isEmpty(companyHonorDTO)) {
                /*if(IsEmptyUtils.isEmpty(ingredientBaseDTO.getSdId())){
                    ingredientBaseDTO
                }*/
                resultBean = companyHonorService.delCompanyHonor(companyHonorDTO);
            }else{
                resultBean.setSuccess(false);
                resultBean.setResultCode(IStatusMessage.SystemStatus.PARAMETERS_LOSE.getCode());
                resultBean.setMsg(IStatusMessage.SystemStatus.PARAMETERS_LOSE.getMessage());
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("删除公司荣誉异常！", e);
            resultBean.setSuccess(false);
            resultBean.setResultCode(IStatusMessage.SystemStatus.ERROR.getCode());
            resultBean.setMsg(IStatusMessage.SystemStatus.ERROR.getMessage());
        }
        return resultBean;
    }
}
