package cn.zhixiangsingle.controller.morningMeeting;

import cn.zhixiangsingle.entity.base.IsEmptyUtils;
import cn.zhixiangsingle.entity.base.site.vo.SiteVO;
import cn.zhixiangsingle.entity.base.user.User;
import cn.zhixiangsingle.entity.morningMeeting.dto.MorningMeetingDTO;
import cn.zhixiangsingle.service.morningMeeting.MorningMeetingService;
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
 * @Package cn.zhixiangsingle.controller.morningMeeting
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019/10/18 10:10
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Controller
@RequestMapping("/morningMeeting")
public class MorningMeetingController {
    private static final Logger logger = LoggerFactory
            .getLogger(MorningMeetingController.class);
    @Reference(version = "1.0.0")
    private SiteService siteService;
    @Reference(version = "1.0.0")
    private MorningMeetingService morningMeetingService;
    /**
     * @version V1.0
     * @Title: zhixiangpingtai
     * @Description:
     * @author: hhp
     * @param:  * @param
     * @date: 2019/10/18 10:22
     */
    @RequestMapping(value = "/getListPage", method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView getListPage() {
        logger.debug("跳转晨会信息列表！");
        ModelAndView mav = new ModelAndView("/jygs/chgl/chgl");
        try {
            mav.addObject("msg", "ok");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("跳转晨会列表异常！", e);
        }
        return mav;
    }
    /**
     * @version V1.0
     * @Title: zhixiangpingtai
     * @Description:
     * @author: hhp
     * @param:  * @param morningMeetingDTO
     * @date: 2019/10/18 10:24
     */
    @PostMapping(value = "/getMorningMeetingList")
    @ResponseBody
    public ResultBean getMorningMeetingList(MorningMeetingDTO morningMeetingDTO) {
        logger.debug("获取晨会列表！ morningMeetingDTO = "+morningMeetingDTO);
        ResultBean resultBean = new ResultBean();
        try {
            User existUser = (User) SecurityUtils.getSubject().getPrincipal();
            morningMeetingDTO.setUserId(existUser.getId());
            resultBean = morningMeetingService.findMorningMeetingList(morningMeetingDTO);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("获取晨会列表异常！", e);
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
     * @param:  * @param morningMeetingDTO
     * @date: 2019/10/18 10:27
     */
    @RequestMapping(value = "/setMorningMeeting", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean setMorningMeeting(MorningMeetingDTO morningMeetingDTO) {
        logger.debug("添加或修改晨会信息 --morningMeetingDTO-" + morningMeetingDTO);
        ResultBean resultBean = new ResultBean();
        try {
            User existUser = (User) SecurityUtils.getSubject().getPrincipal();
            if (!IsEmptyUtils.isEmpty(morningMeetingDTO)) {
                morningMeetingDTO.setUserId(existUser.getId());
                if(!IsEmptyUtils.isEmpty(morningMeetingDTO.getId())){
                    resultBean = morningMeetingService.updateMorningMeeting(morningMeetingDTO);
                }else{
                    resultBean = siteService.getUserSites(existUser.getId());
                    if(!IsEmptyUtils.isEmpty(resultBean.getResult())){
                        List<SiteVO> siteVOList = (List<SiteVO>)resultBean.getResult();
                        if(siteVOList.size()>1){
                            if(!IsEmptyUtils.isEmpty(morningMeetingDTO.getSdIds())){
                                resultBean = morningMeetingService.addMorningMeeting(morningMeetingDTO);
                            }else{
                                resultBean.setSuccess(false);
                                resultBean.setResultCode(IStatusMessage.SystemStatus.SITE_ID_LOSE.getCode());
                                resultBean.setMsg(IStatusMessage.SystemStatus.SITE_ID_LOSE.getMessage());
                            }
                        }else{
                            morningMeetingDTO.setSdId(existUser.getSdId());
                            morningMeetingDTO.setSdIds("");
                            resultBean = morningMeetingService.addMorningMeeting(morningMeetingDTO);
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
            logger.error("添加或修改晨会信息异常！", e);
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
     * @param:  * @param morningMeetingDTO
     * @date: 2019/10/18 10:29
     */
    @PostMapping(value = "/getUpdMorningMeeting")
    @ResponseBody
    public ResultBean getUpdMorningMeeting(MorningMeetingDTO morningMeetingDTO) {
        logger.debug("获取晨会！ morningMeetingDTO = "+morningMeetingDTO);
        ResultBean resultBean = new ResultBean();
        try {
            User existUser = (User) SecurityUtils.getSubject().getPrincipal();
            morningMeetingDTO.setUserId(existUser.getId());
            if(IsEmptyUtils.isEmpty(morningMeetingDTO.getSdId())){
                morningMeetingDTO.setSdId(existUser.getSdId());
            }
            resultBean = morningMeetingService.findUpdMorningMeeting(morningMeetingDTO);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("获取晨会！", e);
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
     * @param:  * @param morningMeetingDTO
     * @date: 2019/10/18 10:31
     */
    @PostMapping(value = "/delMorningMeeting")
    @ResponseBody
    public ResultBean delMorningMeeting(MorningMeetingDTO morningMeetingDTO){
        logger.debug("删除晨会信息 --morningMeetingDTO-" + morningMeetingDTO);
        ResultBean resultBean = new ResultBean();
        try {
            User existUser = (User) SecurityUtils.getSubject().getPrincipal();
            morningMeetingDTO.setUserId(existUser.getId());
            if (!IsEmptyUtils.isEmpty(morningMeetingDTO)) {
                /*if(IsEmptyUtils.isEmpty(ingredientBaseDTO.getSdId())){
                    ingredientBaseDTO
                }*/
                resultBean = morningMeetingService.delMorningMeeting(morningMeetingDTO);
            }else{
                resultBean.setSuccess(false);
                resultBean.setResultCode(IStatusMessage.SystemStatus.PARAMETERS_LOSE.getCode());
                resultBean.setMsg(IStatusMessage.SystemStatus.PARAMETERS_LOSE.getMessage());
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("删除晨会异常！", e);
            resultBean.setSuccess(false);
            resultBean.setResultCode(IStatusMessage.SystemStatus.ERROR.getCode());
            resultBean.setMsg(IStatusMessage.SystemStatus.ERROR.getMessage());
        }
        return resultBean;
    }
}
