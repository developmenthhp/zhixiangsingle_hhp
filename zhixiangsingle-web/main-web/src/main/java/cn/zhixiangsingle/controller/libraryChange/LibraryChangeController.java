package cn.zhixiangsingle.controller.libraryChange;

import cn.zhixiangsingle.entity.base.IsEmptyUtils;
import cn.zhixiangsingle.entity.base.site.vo.SiteVO;
import cn.zhixiangsingle.entity.base.user.User;
import cn.zhixiangsingle.entity.libraryChange.dto.LibraryChangeDTO;
import cn.zhixiangsingle.service.libraryChange.LibraryChangeService;
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
 * @Package cn.zhixiangsingle.controller.libraryChange
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019/10/22 13:42
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Controller
@RequestMapping("/libraryChange")
public class LibraryChangeController {
    private static final Logger logger = LoggerFactory
            .getLogger(LibraryChangeController.class);
    @Reference(version = "1.0.0")
    private SiteService siteService;
    @Reference(version = "1.0.0")
    private LibraryChangeService libraryChangeService;
    /**
     * @version V1.0
     * @Title: zhixiangpingtai
     * @Description:
     * @author: hhp
     * @param:  * @param
     * @date: 2019/10/22 13:47
     */
    @RequestMapping(value = "/getListPage", method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView getListPage() {
        logger.debug("跳转库存核准信息列表！");
        ModelAndView mav = new ModelAndView("/cgcc/schz/schzjl");
        try {
            mav.addObject("msg", "ok");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("跳转库存核准消息列表异常！", e);
        }
        return mav;
    }
    /**
     * @version V1.0
     * @Title: zhixiangpingtai
     * @Description:
     * @author: hhp
     * @param:  * @param libraryChangeDTO
     * @date: 2019/10/22 13:56
     */
    @PostMapping(value = "/getLibraryChangeList")
    @ResponseBody
    public ResultBean getLibraryChangeList(LibraryChangeDTO libraryChangeDTO) {
        logger.debug("获取库存核准信息列表！ libraryChangeDTO = "+libraryChangeDTO);
        ResultBean resultBean = new ResultBean();
        try {
            User existUser = (User) SecurityUtils.getSubject().getPrincipal();
            libraryChangeDTO.setUserId(existUser.getId());
            resultBean = libraryChangeService.findLibraryChangeList(libraryChangeDTO);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("获取库存核准信息异常！", e);
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
     * @param:  * @param libraryChangeDTO
     * @date: 2019/10/22 15:34
     */
    @RequestMapping(value = "/setLibraryChange", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean setLibraryChange(LibraryChangeDTO libraryChangeDTO) {
        logger.debug("添加或修改库存核准信息 --libraryChangeDTO-" + libraryChangeDTO);
        ResultBean resultBean = new ResultBean();
        try {
            User existUser = (User) SecurityUtils.getSubject().getPrincipal();
            if (!IsEmptyUtils.isEmpty(libraryChangeDTO)) {
                libraryChangeDTO.setUserId(existUser.getId());
                libraryChangeDTO.setChangePeopleId(existUser.getId());
                libraryChangeDTO.setChangePeople(existUser.getUserName());
                if(!IsEmptyUtils.isEmpty(libraryChangeDTO.getId())){
                    resultBean = libraryChangeService.updateLibraryChange(libraryChangeDTO);
                }else{
                    resultBean = libraryChangeService.addLibraryChange(libraryChangeDTO);
                }
            }else{
                resultBean.setSuccess(false);
                resultBean.setResultCode(IStatusMessage.SystemStatus.PARAMETERS_LOSE.getCode());
                resultBean.setMsg(IStatusMessage.SystemStatus.PARAMETERS_LOSE.getMessage());
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("添加或修改库存核准信息异常！", e);
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
     * @param:  * @param libraryChangeDTO
     * @date: 2019/10/22 18:46
     */
    @RequestMapping(value = "/addByDetails", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean addByDetails(LibraryChangeDTO libraryChangeDTO) {
        logger.debug("添加库存核准信息 --libraryChangeDTO-" + libraryChangeDTO);
        ResultBean resultBean = new ResultBean();
        try {
            if(!IsEmptyUtils.isEmpty(libraryChangeDTO)){
                if(IsEmptyUtils.isEmpty(libraryChangeDTO.getChangeNumber())||
                        IsEmptyUtils.isEmpty(libraryChangeDTO.getChangePrice())||
                        IsEmptyUtils.isEmpty(libraryChangeDTO.getChangeRemarks())){
                    resultBean.setSuccess(false);
                    resultBean.setResultCode(IStatusMessage.SystemStatus.PARAMETERS_LOSE.getCode());
                    resultBean.setMsg(IStatusMessage.SystemStatus.PARAMETERS_LOSE.getMessage());
                    return resultBean;
                }
                if(libraryChangeDTO.getChangeMode().equals("2")){
                    if(libraryChangeDTO.getBitchCount()-libraryChangeDTO.getChangeNumber()<0){
                        resultBean.setSuccess(false);
                        resultBean.setResultCode(IStatusMessage.SystemStatus.PARAM_BIG.getCode());
                        resultBean.setMsg(IStatusMessage.SystemStatus.PARAM_BIG.getMessage());
                        return resultBean;
                    }
                }

                User existUser = (User) SecurityUtils.getSubject().getPrincipal();
                if (!IsEmptyUtils.isEmpty(libraryChangeDTO)) {
                    libraryChangeDTO.setUserId(existUser.getId());
                    libraryChangeDTO.setChangePeopleId(existUser.getId());
                    libraryChangeDTO.setChangePeople(existUser.getUserName());
                    resultBean = libraryChangeService.addByDetails(libraryChangeDTO);
                }else{
                    resultBean.setSuccess(false);
                    resultBean.setResultCode(IStatusMessage.SystemStatus.PARAMETERS_LOSE.getCode());
                    resultBean.setMsg(IStatusMessage.SystemStatus.PARAMETERS_LOSE.getMessage());
                }
            }else{
                resultBean.setSuccess(false);
                resultBean.setResultCode(IStatusMessage.SystemStatus.PARAMETERS_LOSE.getCode());
                resultBean.setMsg(IStatusMessage.SystemStatus.PARAMETERS_LOSE.getMessage());
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("添加库存核准信息异常！", e);
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
     * @param:  * @param libraryChangeDTO
     * @date: 2019/10/22 15:36
     */
    @PostMapping(value = "/getUpdLibraryChange")
    @ResponseBody
    public ResultBean getUpdLibraryChange(LibraryChangeDTO libraryChangeDTO) {
        logger.debug("获取库存核准信息！ libraryChangeDTO = "+libraryChangeDTO);
        ResultBean resultBean = new ResultBean();
        try {
            User existUser = (User) SecurityUtils.getSubject().getPrincipal();
            libraryChangeDTO.setUserId(existUser.getId());
            if(IsEmptyUtils.isEmpty(libraryChangeDTO.getSdId())){
                libraryChangeDTO.setSdId(existUser.getSdId());
            }
            resultBean = libraryChangeService.findUpdLibraryChange(libraryChangeDTO);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("获取库存核准信息！", e);
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
     * @param: * @param null
     * @date: 2019/10/22 15:38
     */
    @PostMapping(value = "/delLibraryChange")
    @ResponseBody
    public ResultBean delLibraryChange(LibraryChangeDTO libraryChangeDTO){
        logger.debug("删除库存核准信息 --libraryChangeDTO-" + libraryChangeDTO);
        ResultBean resultBean = new ResultBean();
        try {
            User existUser = (User) SecurityUtils.getSubject().getPrincipal();
            libraryChangeDTO.setUserId(existUser.getId());
            if (!IsEmptyUtils.isEmpty(libraryChangeDTO)) {
                /*if(IsEmptyUtils.isEmpty(ingredientBaseDTO.getSdId())){
                    ingredientBaseDTO
                }*/
                resultBean = libraryChangeService.delLibraryChange(libraryChangeDTO);
            }else{
                resultBean.setSuccess(false);
                resultBean.setResultCode(IStatusMessage.SystemStatus.PARAMETERS_LOSE.getCode());
                resultBean.setMsg(IStatusMessage.SystemStatus.PARAMETERS_LOSE.getMessage());
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("删除库存核准信息异常！", e);
            resultBean.setSuccess(false);
            resultBean.setResultCode(IStatusMessage.SystemStatus.ERROR.getCode());
            resultBean.setMsg(IStatusMessage.SystemStatus.ERROR.getMessage());
        }
        return resultBean;
    }
}
