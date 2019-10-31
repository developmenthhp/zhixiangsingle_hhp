package cn.zhixiangsingle.controller.library;

import cn.zhixiangsingle.entity.base.IsEmptyUtils;
import cn.zhixiangsingle.entity.base.site.vo.SiteVO;
import cn.zhixiangsingle.entity.base.user.User;
import cn.zhixiangsingle.entity.library.dto.LibraryDTO;
import cn.zhixiangsingle.service.library.LibraryService;
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
 * @Package cn.zhixiangsingle.controller.library
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019/10/19 15:35
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Controller
@RequestMapping("/library")
public class LibraryController {
    private static final Logger logger = LoggerFactory
            .getLogger(LibraryController.class);
    @Reference(version = "1.0.0")
    private SiteService siteService;
    @Reference(version = "1.0.0")
    private LibraryService libraryService;
    /**
     * @version V1.0
     * @Title: zhixiangpingtai
     * @Description:
     * @author: hhp
     * @param:  * @param
     * @date: 2019/10/19 15:49
     */
    @RequestMapping(value = "/getListPage", method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView getListPage() {
        logger.debug("跳转食材库存信息列表！");
        ModelAndView mav = new ModelAndView("/cgcc/sckc/sckcgl");
        try {
            mav.addObject("msg", "ok");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("跳转食材库存消息列表异常！", e);
        }
        return mav;
    }
    /**
     * @version V1.0
     * @Title: zhixiangpingtai
     * @Description:
     * @author: hhp
     * @param:  * @param morningMeetingDTO
     * @date: 2019/10/19 15:49
     */
    @PostMapping(value = "/getLibraryList")
    @ResponseBody
    public ResultBean getLibraryList(LibraryDTO libraryDTO) {
        logger.debug("获取食材库存信息列表！ libraryDTO = "+libraryDTO);
        ResultBean resultBean = new ResultBean();
        try {
            User existUser = (User) SecurityUtils.getSubject().getPrincipal();
            libraryDTO.setUserId(existUser.getId());
            resultBean = libraryService.findLibraryList(libraryDTO);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("获取食材库存信息异常！", e);
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
     * @param:  * @param libraryDTO
     * @date: 2019/10/19 15:53
     */
    @RequestMapping(value = "/setLibrary", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean setLibrary(LibraryDTO libraryDTO) {
        logger.debug("添加或修改晨会信息 --libraryDTO-" + libraryDTO);
        ResultBean resultBean = new ResultBean();
        try {
            User existUser = (User) SecurityUtils.getSubject().getPrincipal();
            if (!IsEmptyUtils.isEmpty(libraryDTO)) {
                libraryDTO.setUserId(existUser.getId());
                if(!IsEmptyUtils.isEmpty(libraryDTO.getId())){
                    resultBean = libraryService.updateLibrary(libraryDTO);
                }else{
                    resultBean = siteService.getUserSites(existUser.getId());
                    if(!IsEmptyUtils.isEmpty(resultBean.getResult())){
                        List<SiteVO> siteVOList = (List<SiteVO>)resultBean.getResult();
                        if(siteVOList.size()>1){
                            if(!IsEmptyUtils.isEmpty(libraryDTO.getSdIds())){
                                resultBean = libraryService.addLibrary(libraryDTO);
                            }else{
                                resultBean.setSuccess(false);
                                resultBean.setResultCode(IStatusMessage.SystemStatus.SITE_ID_LOSE.getCode());
                                resultBean.setMsg(IStatusMessage.SystemStatus.SITE_ID_LOSE.getMessage());
                            }
                        }else{
                            libraryDTO.setSdId(existUser.getSdId());
                            libraryDTO.setSdIds("");
                            resultBean = libraryService.addLibrary(libraryDTO);
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
            logger.error("添加或修改食材库存信息异常！", e);
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
     * @param:  * @param libraryDTO
     * @date: 2019/10/19 15:55
     */
    @PostMapping(value = "/getUpdLibrary")
    @ResponseBody
    public ResultBean getUpdLibrary(LibraryDTO libraryDTO) {
        logger.debug("获取食材库存信息！ libraryDTO = "+libraryDTO);
        ResultBean resultBean = new ResultBean();
        try {
            User existUser = (User) SecurityUtils.getSubject().getPrincipal();
            libraryDTO.setUserId(existUser.getId());
            if(IsEmptyUtils.isEmpty(libraryDTO.getSdId())){
                libraryDTO.setSdId(existUser.getSdId());
            }
            resultBean = libraryService.findUpdLibrary(libraryDTO);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("获取食材库存信息！", e);
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
     * @param:  * @param libraryDTO
     * @date: 2019/10/19 15:56
     */
    @PostMapping(value = "/delLibrary")
    @ResponseBody
    public ResultBean delLibrary(LibraryDTO libraryDTO){
        logger.debug("删除食材库存信息 --libraryDTO-" + libraryDTO);
        ResultBean resultBean = new ResultBean();
        try {
            User existUser = (User) SecurityUtils.getSubject().getPrincipal();
            libraryDTO.setUserId(existUser.getId());
            if (!IsEmptyUtils.isEmpty(libraryDTO)) {
                /*if(IsEmptyUtils.isEmpty(ingredientBaseDTO.getSdId())){
                    ingredientBaseDTO
                }*/
                resultBean = libraryService.delLibrary(libraryDTO);
            }else{
                resultBean.setSuccess(false);
                resultBean.setResultCode(IStatusMessage.SystemStatus.PARAMETERS_LOSE.getCode());
                resultBean.setMsg(IStatusMessage.SystemStatus.PARAMETERS_LOSE.getMessage());
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("删除食材库存信息异常！", e);
            resultBean.setSuccess(false);
            resultBean.setResultCode(IStatusMessage.SystemStatus.ERROR.getCode());
            resultBean.setMsg(IStatusMessage.SystemStatus.ERROR.getMessage());
        }
        return resultBean;
    }
}
