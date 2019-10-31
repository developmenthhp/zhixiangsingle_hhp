package cn.zhixiangsingle.controller.displayArea;

import cn.zhixiangsingle.entity.base.IsEmptyUtils;
import cn.zhixiangsingle.entity.base.site.vo.SiteVO;
import cn.zhixiangsingle.entity.base.user.User;
import cn.zhixiangsingle.entity.displayArea.dto.DisplayAreaDTO;
import cn.zhixiangsingle.service.displayArea.DisplayAreaService;
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
 * @Package cn.zhixiangsingle.controller.displayArea
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019/10/15 17:01
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Controller
@RequestMapping("/displayArea")
public class DisplayAreaController {
    private static final Logger logger = LoggerFactory
            .getLogger(DisplayAreaController.class);
    @Reference(version = "1.0.0")
    private SiteService siteService;
    @Reference(version = "1.0.0")
    private DisplayAreaService displayAreaService;
    /**
     * @version V1.0
     * @Title: zhixiangpingtai
     * @Description:
     * @author: hhp
     * @param:  * @param
     * @date: 2019/10/15 17:12
     */
    @RequestMapping(value = "/getListPage", method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView getMapPage() {
        logger.debug("跳转区域管理列表！");
        ModelAndView mav = new ModelAndView("/jygs/qygl/qygl");
        try {
            mav.addObject("msg", "ok");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("跳转区域管理异常！", e);
        }
        return mav;
    }
    /**
     * @version V1.0
     * @Title: zhixiangpingtai
     * @Description:
     * @author: hhp
     * @param:  * @param displayAreaDTO
     * @date: 2019/10/15 17:19
     */
    @PostMapping(value = "/getDisplayAreaList")
    @ResponseBody
    public ResultBean getDisplayAreaList(DisplayAreaDTO displayAreaDTO) {
        logger.debug("获取区域管理信息列表！ displayAreaDTO = "+displayAreaDTO);
        ResultBean resultBean = new ResultBean();
        try {
            User existUser = (User) SecurityUtils.getSubject().getPrincipal();
            displayAreaDTO.setUserId(existUser.getId());
            if(existUser.getZx()==true||existUser.getSdId()!=22){
                displayAreaDTO.setSdId(existUser.getSdId());
            }
            resultBean = displayAreaService.findDisplayAreaList(displayAreaDTO);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("获取区域管理信息列表异常！", e);
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
     * @param:  * @param displayAreaDTO
     * @date: 2019/10/15 17:19
     */
    @PostMapping(value = "/getAll")
    @ResponseBody
    public ResultBean getAll(DisplayAreaDTO displayAreaDTO) {
        logger.debug("获取所有区域管理信息列表！ displayAreaDTO = "+displayAreaDTO);
        ResultBean resultBean = new ResultBean();
        try {
            User existUser = (User) SecurityUtils.getSubject().getPrincipal();
            displayAreaDTO.setUserId(existUser.getId());
            if(existUser.getZx()==true||existUser.getSdId()!=22){
                displayAreaDTO.setSdId(existUser.getSdId());
            }
            resultBean = displayAreaService.findAll(displayAreaDTO);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("获取所有区域管理信息列表异常！", e);
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
     * @param:  * @param displayAreaDTO
     * @date: 2019/10/17 14:00
     */
    @RequestMapping(value = "/setDisplayArea", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean setDisplayArea(DisplayAreaDTO displayAreaDTO) {
        logger.debug("添加或修改区域信息 --displayAreaDTO-" + displayAreaDTO);
        ResultBean resultBean = new ResultBean();
        try {
            User existUser = (User) SecurityUtils.getSubject().getPrincipal();
            if (!IsEmptyUtils.isEmpty(displayAreaDTO)) {
                displayAreaDTO.setUserId(existUser.getId());
                if(!IsEmptyUtils.isEmpty(displayAreaDTO.getId())){
                    resultBean = displayAreaService.updateDisplayArea(displayAreaDTO);
                }else{
                    resultBean = siteService.getUserSites(existUser.getId());
                    if(!IsEmptyUtils.isEmpty(resultBean.getResult())){
                        List<SiteVO> siteVOList = (List<SiteVO>)resultBean.getResult();
                        if(siteVOList.size()>1){
                            if(displayAreaDTO.getPid()==0){
                                if(!IsEmptyUtils.isEmpty(displayAreaDTO.getSdIds())){
                                    resultBean = displayAreaService.addDisplayArea(displayAreaDTO);
                                }else{
                                    resultBean.setSuccess(false);
                                    resultBean.setResultCode(IStatusMessage.SystemStatus.SITE_ID_LOSE.getCode());
                                    resultBean.setMsg(IStatusMessage.SystemStatus.SITE_ID_LOSE.getMessage());
                                }

                            }else{
                                resultBean = displayAreaService.addDisplayArea(displayAreaDTO);
                            }
                        }else{
                            displayAreaDTO.setSdId(existUser.getSdId());
                            displayAreaDTO.setSdIds("");
                            resultBean = displayAreaService.addDisplayArea(displayAreaDTO);
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
            logger.error("添加或修改区域信息异常！", e);
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
     * @param:  * @param displayAreaDTO
     * @date: 2019/10/17 14:02
     */
    @PostMapping(value = "/getUpdDisplayArea")
    @ResponseBody
    public ResultBean getUpdDisplayArea(DisplayAreaDTO displayAreaDTO) {
        logger.debug("获取区域信息！ displayAreaDTO = "+displayAreaDTO);
        ResultBean resultBean = new ResultBean();
        try {
            User existUser = (User) SecurityUtils.getSubject().getPrincipal();
            displayAreaDTO.setUserId(existUser.getId());
            if(IsEmptyUtils.isEmpty(displayAreaDTO.getSdId())){
                displayAreaDTO.setSdId(existUser.getSdId());
            }
            resultBean = displayAreaService.findUpdDisplayArea(displayAreaDTO);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("获取区域信息异常！", e);
            resultBean.setSuccess(false);
            resultBean.setResultCode(IStatusMessage.SystemStatus.ERROR.getCode());
            resultBean.setMsg(IStatusMessage.SystemStatus.ERROR.getMessage());
        }
        return resultBean;
    }
    @PostMapping(value = "/delDisplayArea")
    @ResponseBody
    public ResultBean delDisplayArea(DisplayAreaDTO displayAreaDTO){
        logger.debug("删除区域信息 --displayAreaDTO-" + displayAreaDTO);
        ResultBean resultBean = new ResultBean();
        try {
            User existUser = (User) SecurityUtils.getSubject().getPrincipal();
            displayAreaDTO.setUserId(existUser.getId());
            if (!IsEmptyUtils.isEmpty(displayAreaDTO)) {
                /*if(IsEmptyUtils.isEmpty(ingredientBaseDTO.getSdId())){
                    ingredientBaseDTO
                }*/
                resultBean = displayAreaService.delDisplayArea(displayAreaDTO);
            }else{
                resultBean.setSuccess(false);
                resultBean.setResultCode(IStatusMessage.SystemStatus.PARAMETERS_LOSE.getCode());
                resultBean.setMsg(IStatusMessage.SystemStatus.PARAMETERS_LOSE.getMessage());
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("删除区域异常！", e);
            resultBean.setSuccess(false);
            resultBean.setResultCode(IStatusMessage.SystemStatus.ERROR.getCode());
            resultBean.setMsg(IStatusMessage.SystemStatus.ERROR.getMessage());
        }
        return resultBean;
    }
}
