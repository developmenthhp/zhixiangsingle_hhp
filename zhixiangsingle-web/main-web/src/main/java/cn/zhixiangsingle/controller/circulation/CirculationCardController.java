package cn.zhixiangsingle.controller.circulation;

import cn.zhixiangsingle.entity.base.IsEmptyUtils;
import cn.zhixiangsingle.entity.base.site.vo.SiteVO;
import cn.zhixiangsingle.entity.base.user.User;
import cn.zhixiangsingle.entity.circulation.dto.CirculationCardDTO;
import cn.zhixiangsingle.service.circulation.CirculationCardService;
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
 * @Package cn.zhixiangsingle.controller.circulation
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019/10/9 10:54
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Controller
@RequestMapping("/circulationCard")
public class CirculationCardController {
    private static final Logger logger = LoggerFactory
            .getLogger(CirculationCardController.class);
    @Reference(version = "1.0.0")
    private SiteService siteService;
    @Reference(version = "1.0.0")
    private CirculationCardService circulationCardService;
    /**
     * @version V1.0
     * @Title: zhixiangpingtai
     * @Description:
     * @author: hhp
     * @param:  * @param circulationCardDTO
     * @date: 2019/10/11 12:42
     */
    @RequestMapping(value = "/setCirculationCard", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean setCirculationCard(CirculationCardDTO circulationCardDTO) {
        logger.debug("添加或修改食品流通证信息 --circulationCardDTO-" + circulationCardDTO);
        ResultBean resultBean = new ResultBean();
        try {
            User existUser = (User) SecurityUtils.getSubject().getPrincipal();
            if (!IsEmptyUtils.isEmpty(circulationCardDTO)) {
                circulationCardDTO.setUserId(existUser.getId());
                if(!IsEmptyUtils.isEmpty(circulationCardDTO.getId())){
                    resultBean = circulationCardService.updateCirculationCard(circulationCardDTO);
                }else{
                    resultBean = siteService.getUserSites(existUser.getId());
                    if(!IsEmptyUtils.isEmpty(resultBean.getResult())){
                        List<SiteVO> siteVOList = (List<SiteVO>)resultBean.getResult();
                        if(siteVOList.size()>1){
                            if(!IsEmptyUtils.isEmpty(circulationCardDTO.getSdIds())){
                                resultBean = circulationCardService.addCirculationCard(circulationCardDTO);
                            }else{
                                resultBean.setSuccess(false);
                                resultBean.setResultCode(IStatusMessage.SystemStatus.SITE_ID_LOSE.getCode());
                                resultBean.setMsg(IStatusMessage.SystemStatus.SITE_ID_LOSE.getMessage());
                            }
                        }else{
                            circulationCardDTO.setSdId(existUser.getSdId());
                            circulationCardDTO.setSdIds("");
                            resultBean = circulationCardService.addCirculationCard(circulationCardDTO);
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
            logger.error("添加或修改食品流通证信息异常！", e);
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
     * @date: 2019/10/11 12:46
     */
    @RequestMapping(value = "/delCirculationCard", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean delCirculationCard(CirculationCardDTO circulationCardDTO){
        logger.debug("删除食品流通证信息 --circulationCardDTO-" + circulationCardDTO);
        ResultBean resultBean = new ResultBean();
        try {
            User existUser = (User) SecurityUtils.getSubject().getPrincipal();
            circulationCardDTO.setUserId(existUser.getId());
            if (!IsEmptyUtils.isEmpty(circulationCardDTO)) {
                /*if(IsEmptyUtils.isEmpty(ingredientBaseDTO.getSdId())){
                    ingredientBaseDTO
                }*/
                resultBean = circulationCardService.delCirculationCard(circulationCardDTO);
            }else{
                resultBean.setSuccess(false);
                resultBean.setResultCode(IStatusMessage.SystemStatus.PARAMETERS_LOSE.getCode());
                resultBean.setMsg(IStatusMessage.SystemStatus.PARAMETERS_LOSE.getMessage());
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("删除食品流通证异常！", e);
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
    @PostMapping(value = "/getUpdCirculationCard")
    @ResponseBody
    public ResultBean getUpdCirculationCard(CirculationCardDTO circulationCardDTO) {
        logger.debug("获取食品流通证信息！ circulationCardDTO = "+circulationCardDTO);
        ResultBean resultBean = new ResultBean();
        try {
            User existUser = (User) SecurityUtils.getSubject().getPrincipal();
            circulationCardDTO.setUserId(existUser.getId());
            if(IsEmptyUtils.isEmpty(circulationCardDTO.getSdId())){
                circulationCardDTO.setSdId(existUser.getSdId());
            }
            resultBean = circulationCardService.findUpdCirculationCard(circulationCardDTO);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("获取食品流通证异常！", e);
            resultBean.setSuccess(false);
            resultBean.setResultCode(IStatusMessage.SystemStatus.ERROR.getCode());
            resultBean.setMsg(IStatusMessage.SystemStatus.ERROR.getMessage());
        }
        return resultBean;
    }
}
