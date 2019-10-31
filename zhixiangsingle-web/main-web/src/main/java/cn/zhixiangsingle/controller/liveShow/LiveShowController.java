package cn.zhixiangsingle.controller.liveShow;

import cn.zhixiangsingle.entity.base.IsEmptyUtils;
import cn.zhixiangsingle.entity.base.site.vo.SiteVO;
import cn.zhixiangsingle.entity.base.user.User;
import cn.zhixiangsingle.entity.liveShow.dto.LiveShowDTO;
import cn.zhixiangsingle.service.liveShow.LiveShowService;
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
 * @Package cn.zhixiangsingle.controller.liveShow
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019/10/15 11:56
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Controller
@RequestMapping("/liveShow")
public class LiveShowController {
    private static final Logger logger = LoggerFactory
            .getLogger(LiveShowController.class);
    @Reference(version = "1.0.0")
    private SiteService siteService;
    @Reference(version = "1.0.0")
    private LiveShowService liveShowService;
    /**
     * @version V1.0
     * @Title: zhixiangpingtai
     * @Description:
     * @author: hhp
     * @param:  * @param
     * @date: 2019/10/15 12:02
     */
    @RequestMapping(value = "/getListPage", method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView getListPage() {
        logger.debug("跳转实景展示信息列表！");
        ModelAndView mav = new ModelAndView("/jygs/sjzs/sjzs");
        try {
            mav.addObject("msg", "ok");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("跳转实景展示列表异常！", e);
        }
        return mav;
    }
    @PostMapping(value = "/getLiveShowList")
    @ResponseBody
    public ResultBean getLiveShowList(LiveShowDTO liveShowDTO) {
        logger.debug("获取实景展示列表！ liveShowDTO = "+liveShowDTO);
        ResultBean resultBean = new ResultBean();
        try {
            User existUser = (User) SecurityUtils.getSubject().getPrincipal();
            liveShowDTO.setUserId(existUser.getId());
            resultBean = liveShowService.findLiveShows(liveShowDTO);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("获取实景展示列表异常！", e);
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
     * @param:  * @param liveShowDTO
     * @date: 2019/10/15 12:10
     */
    @RequestMapping(value = "/setLiveShow", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean setLiveShow(LiveShowDTO liveShowDTO) {
        logger.debug("添加或修改实景展示信息 --liveShowDTO-" + liveShowDTO);
        ResultBean resultBean = new ResultBean();
        try {
            User existUser = (User) SecurityUtils.getSubject().getPrincipal();
            if (!IsEmptyUtils.isEmpty(liveShowDTO)) {
                liveShowDTO.setUserId(existUser.getId());
                if(!IsEmptyUtils.isEmpty(liveShowDTO.getId())){
                    resultBean = liveShowService.updateLiveShow(liveShowDTO);
                }else{
                    resultBean = siteService.getUserSites(existUser.getId());
                    if(!IsEmptyUtils.isEmpty(resultBean.getResult())){
                        List<SiteVO> siteVOList = (List<SiteVO>)resultBean.getResult();
                        if(siteVOList.size()>1){
                            if(IsEmptyUtils.isEmpty(liveShowDTO.getAreaId())||IsEmptyUtils.isEmpty(liveShowDTO.getSdId())){
                                resultBean.setSuccess(false);
                                resultBean.setResultCode(IStatusMessage.SystemStatus.PARAMETERS_LOSE.getCode());
                                resultBean.setMsg(IStatusMessage.SystemStatus.PARAMETERS_LOSE.getMessage());
                            }else{
                                resultBean = liveShowService.addLiveShow(liveShowDTO);
                            }
                        }else{
                            liveShowDTO.setSdId(existUser.getSdId());
                            liveShowDTO.setSdIds("");
                            resultBean = liveShowService.addLiveShow(liveShowDTO);
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
            logger.error("添加或修改实景展示信息异常！", e);
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
     * @param:  * @param liveShowDTO
     * @date: 2019/10/15 12:40
     */
    @PostMapping(value = "/getUpdLiveShow")
    @ResponseBody
    public ResultBean getUpdLiveShow(LiveShowDTO liveShowDTO) {
        logger.debug("获取实景展示！ liveShowDTO = "+liveShowDTO);
        ResultBean resultBean = new ResultBean();
        try {
            User existUser = (User) SecurityUtils.getSubject().getPrincipal();
            liveShowDTO.setUserId(existUser.getId());
            if(IsEmptyUtils.isEmpty(liveShowDTO.getSdId())){
                liveShowDTO.setSdId(existUser.getSdId());
            }
            resultBean = liveShowService.findUpdLiveShow(liveShowDTO);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("获取实景展示异常！", e);
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
     * @param:  * @param liveShowDTO
     * @date: 2019/10/15 12:42
     */
    @PostMapping(value = "/delLiveShow")
    @ResponseBody
    public ResultBean delCompanyHonor(LiveShowDTO liveShowDTO){
        logger.debug("删除实景展示信息 --liveShowDTO-" + liveShowDTO);
        ResultBean resultBean = new ResultBean();
        try {
            User existUser = (User) SecurityUtils.getSubject().getPrincipal();
            liveShowDTO.setUserId(existUser.getId());
            if (!IsEmptyUtils.isEmpty(liveShowDTO)) {
                /*if(IsEmptyUtils.isEmpty(ingredientBaseDTO.getSdId())){
                    ingredientBaseDTO
                }*/
                resultBean = liveShowService.delLiveShow(liveShowDTO);
            }else{
                resultBean.setSuccess(false);
                resultBean.setResultCode(IStatusMessage.SystemStatus.PARAMETERS_LOSE.getCode());
                resultBean.setMsg(IStatusMessage.SystemStatus.PARAMETERS_LOSE.getMessage());
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("删除实景展示异常！", e);
            resultBean.setSuccess(false);
            resultBean.setResultCode(IStatusMessage.SystemStatus.ERROR.getCode());
            resultBean.setMsg(IStatusMessage.SystemStatus.ERROR.getMessage());
        }
        return resultBean;
    }
}
