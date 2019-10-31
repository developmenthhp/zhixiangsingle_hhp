package cn.zhixiangsingle.controller.iwareCount;

import cn.zhixiangsingle.entity.base.IsEmptyUtils;
import cn.zhixiangsingle.entity.base.site.vo.SiteVO;
import cn.zhixiangsingle.entity.base.user.User;
import cn.zhixiangsingle.entity.iwareCount.dto.IwareCountDTO;
import cn.zhixiangsingle.service.iwareCount.IwareCountService;
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
 * @Package cn.zhixiangsingle.controller.iwareCount
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019/10/25 10:52
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Controller
@RequestMapping("/iwareCount")
public class IwareCountController {
    private static final Logger logger = LoggerFactory
            .getLogger(IwareCountController.class);
    @Reference(version = "1.0.0")
    private SiteService siteService;
    @Reference(version = "1.0.0")
    private IwareCountService iwareCountService;
    /**
     * @version V1.0
     * @Title: zhixiangpingtai
     * @Description:
     * @author: hhp
     * @param:  * @param iwareCountDTO
     * @date: 2019/10/25 13:05
     */
    @PostMapping(value = "/getIwareCountList")
    @ResponseBody
    public ResultBean getIwareCountList(IwareCountDTO iwareCountDTO) {
        logger.debug("获取已制单采购单列表！ iwareCountDTO = "+iwareCountDTO);
        ResultBean resultBean = new ResultBean();
        try {
            User existUser = (User) SecurityUtils.getSubject().getPrincipal();
            iwareCountDTO.setUserId(existUser.getId());
            resultBean = iwareCountService.findIwareCountList(iwareCountDTO);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("获取已制单采购单列表异常！", e);
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
     * @param:  * @param iwareCountDTO
     * @date: 2019/10/25 13:56
     */
    @RequestMapping(value = "/transLPurchase", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean transLPurchase(IwareCountDTO iwareCountDTO) {
        logger.debug("添加或修改已制单采购单信息 --iwareCountDTO-" + iwareCountDTO);
        ResultBean resultBean = new ResultBean();
        try {
            User existUser = (User) SecurityUtils.getSubject().getPrincipal();
            if (!IsEmptyUtils.isEmpty(iwareCountDTO)) {
                iwareCountDTO.setUserId(existUser.getId());
                iwareCountDTO.setReportPersion(existUser.getUserName());
                iwareCountDTO.setReportPersionId(existUser.getId());
                resultBean = iwareCountService.transLPurchase(iwareCountDTO);
            }else{
                resultBean.setSuccess(false);
                resultBean.setResultCode(IStatusMessage.SystemStatus.PARAMETERS_LOSE.getCode());
                resultBean.setMsg(IStatusMessage.SystemStatus.PARAMETERS_LOSE.getMessage());
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("添加或修改已制单采购单信息异常！", e);
            resultBean.setSuccess(false);
            resultBean.setResultCode(IStatusMessage.SystemStatus.ERROR.getCode());
            resultBean.setMsg(IStatusMessage.SystemStatus.ERROR.getMessage());
        }
        return resultBean;
    }
}
