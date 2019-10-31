package cn.zhixiangsingle.controller.license;

import cn.zhixiangsingle.entity.base.IsEmptyUtils;
import cn.zhixiangsingle.entity.base.site.vo.SiteVO;
import cn.zhixiangsingle.entity.base.user.User;
import cn.zhixiangsingle.entity.license.dto.LicenseDTO;
import cn.zhixiangsingle.service.license.LicenseService;
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

import java.util.List;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangpingtai
 * @Package cn.zhixiangsingle.controller.license
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019/10/9 10:47
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Controller
@RequestMapping("/license")
public class LicenseController {
    private static final Logger logger = LoggerFactory
            .getLogger(LicenseController.class);
    @Reference(version = "1.0.0")
    private SiteService siteService;
    @Reference(version = "1.0.0")
    private LicenseService licenseService;
    /**
     * @version V1.0
     * @Title: zhixiangpingtai
     * @Description:
     * @author: hhp
     * @param:  * @param licenseDTO
     * @date: 2019/10/11 12:41
     */
    @RequestMapping(value = "/setLicense", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean setLicense(LicenseDTO licenseDTO) {
        logger.debug("添加或修改餐饮许可证信息 --licenseDTO-" + licenseDTO);
        ResultBean resultBean = new ResultBean();
        try {
            User existUser = (User) SecurityUtils.getSubject().getPrincipal();
            if (!IsEmptyUtils.isEmpty(licenseDTO)) {
                licenseDTO.setUserId(existUser.getId());
                if(!IsEmptyUtils.isEmpty(licenseDTO.getId())){
                    resultBean = licenseService.updateLicense(licenseDTO);
                }else{
                    resultBean = siteService.getUserSites(existUser.getId());
                    if(!IsEmptyUtils.isEmpty(resultBean.getResult())){
                        List<SiteVO> siteVOList = (List<SiteVO>)resultBean.getResult();
                        if(siteVOList.size()>1){
                            if(!IsEmptyUtils.isEmpty(licenseDTO.getSdIds())){
                                resultBean = licenseService.addLicense(licenseDTO);
                            }else{
                                resultBean.setSuccess(false);
                                resultBean.setResultCode(IStatusMessage.SystemStatus.SITE_ID_LOSE.getCode());
                                resultBean.setMsg(IStatusMessage.SystemStatus.SITE_ID_LOSE.getMessage());
                            }
                        }else{
                            licenseDTO.setSdId(existUser.getSdId());
                            licenseDTO.setSdIds("");
                            resultBean = licenseService.addLicense(licenseDTO);
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
            logger.error("添加或修改餐饮许可证信息异常！", e);
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
     * @param:  * @param licenseDTO
     * @date: 2019/10/11 12:41
     */
    @RequestMapping(value = "/delLicense", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean delLicense(LicenseDTO licenseDTO){
        logger.debug("删除餐饮许可证信息 --licenseDTO-" + licenseDTO);
        ResultBean resultBean = new ResultBean();
        try {
            User existUser = (User) SecurityUtils.getSubject().getPrincipal();
            licenseDTO.setUserId(existUser.getId());
            if (!IsEmptyUtils.isEmpty(licenseDTO)) {
                /*if(IsEmptyUtils.isEmpty(ingredientBaseDTO.getSdId())){
                    ingredientBaseDTO
                }*/
                resultBean = licenseService.delLicense(licenseDTO);
            }else{
                resultBean.setSuccess(false);
                resultBean.setResultCode(IStatusMessage.SystemStatus.PARAMETERS_LOSE.getCode());
                resultBean.setMsg(IStatusMessage.SystemStatus.PARAMETERS_LOSE.getMessage());
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("删除餐饮许可证异常！", e);
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
     * @param:  * @param licenseDTO
     * @date: 2019/10/11 13:21
     */
    @PostMapping(value = "/getUpdLicense")
    @ResponseBody
    public ResultBean getUpdLicense(LicenseDTO licenseDTO) {
        logger.debug("获取餐饮许可证信息！ licenseDTO = "+licenseDTO);
        ResultBean resultBean = new ResultBean();
        try {
            User existUser = (User) SecurityUtils.getSubject().getPrincipal();
            licenseDTO.setUserId(existUser.getId());
            if(IsEmptyUtils.isEmpty(licenseDTO.getSdId())){
                licenseDTO.setSdId(existUser.getSdId());
            }
            resultBean = licenseService.findLicense(licenseDTO);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("获取餐饮许可证信息异常！", e);
            resultBean.setSuccess(false);
            resultBean.setResultCode(IStatusMessage.SystemStatus.ERROR.getCode());
            resultBean.setMsg(IStatusMessage.SystemStatus.ERROR.getMessage());
        }
        return resultBean;
    }
}
