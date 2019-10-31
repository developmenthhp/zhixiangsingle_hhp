package cn.zhixiangsingle.controller.company;

import cn.zhixiangsingle.entity.base.common.dto.BaseDTO;
import cn.zhixiangsingle.entity.base.user.User;
import cn.zhixiangsingle.service.company.CompanyLicensesService;
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

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangpingtai
 * @Package cn.zhixiangsingle.controller.company
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019/10/9 10:30
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Controller
@RequestMapping("/companyLicenses")
public class CompanyLicensesController {
    private static final Logger logger = LoggerFactory
            .getLogger(CompanyLicensesController.class);
    @Reference(version = "1.0.0")
    private SiteService siteService;
    @Reference(version = "1.0.0")
    private CompanyLicensesService companyLicensesService;
    /**
     * @version V1.0
     * @Title: zhixiangpingtai
     * @Description:
     * @author: hhp
     * @param:  * @param
     * @date: 2019/10/9 9:36
     */
    @RequestMapping(value = "/getListPage", method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView getMapPage() {
        logger.debug("跳转公司证照信息列表！");
        ModelAndView mav = new ModelAndView("/jygs/gszz/gszz");
        try {
            mav.addObject("msg", "ok");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("跳转公司证照异常！", e);
        }
        return mav;
    }
    /**
     * @version V1.0
     * @Title: zhixiangpingtai
     * @Description:
     * @author: hhp
     * @param:  * @param baseDTO
     * @date: 2019/10/12 16:32
     */
    @PostMapping(value = "/getCompanyLicensesList")
    @ResponseBody
    public ResultBean getCompanyLicensesList(BaseDTO baseDTO) {
        logger.debug("获取公司证照列表！ baseDTO = "+baseDTO);
        ResultBean resultBean = new ResultBean();
        try {
            User existUser = (User) SecurityUtils.getSubject().getPrincipal();
            baseDTO.setUserId(existUser.getId());
            /*if(existUser.getZx()==true||existUser.getSdId()!=22){
                baseDTO.setSdId(existUser.getSdId());
            }*/
            resultBean = companyLicensesService.findCompanyLicensesList(baseDTO);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("获取公司证照列表异常！", e);
            resultBean.setSuccess(false);
            resultBean.setResultCode(IStatusMessage.SystemStatus.ERROR.getCode());
            resultBean.setMsg(IStatusMessage.SystemStatus.ERROR.getMessage());
        }
        return resultBean;
    }
}
