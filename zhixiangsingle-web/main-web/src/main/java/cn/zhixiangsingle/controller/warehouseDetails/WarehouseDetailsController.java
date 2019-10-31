package cn.zhixiangsingle.controller.warehouseDetails;

import cn.zhixiangsingle.entity.base.IsEmptyUtils;
import cn.zhixiangsingle.entity.base.site.vo.SiteVO;
import cn.zhixiangsingle.entity.base.user.User;
import cn.zhixiangsingle.entity.warehouseDetails.dto.WarehouseDetailsDTO;
import cn.zhixiangsingle.service.site.SiteService;
import cn.zhixiangsingle.service.warehouseDetails.WarehouseDetailsService;
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
 * @Package cn.zhixiangsingle.controller.warehouseDetails
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019/10/21 11:25
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Controller
@RequestMapping("/warehouseDetails")
public class WarehouseDetailsController {
    private static final Logger logger = LoggerFactory
            .getLogger(WarehouseDetailsController.class);
    @Reference(version = "1.0.0")
    private SiteService siteService;
    @Reference(version = "1.0.0")
    private WarehouseDetailsService warehouseDetailsService;
    /**
     * @version V1.0
     * @Title: zhixiangpingtai
     * @Description:
     * @author: hhp
     * @param:  * @param
     * @date: 2019/10/21 14:13
     */
    @RequestMapping(value = "/getListPage", method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView getListPage() {
        logger.debug("跳转库存明细信息列表！");
        ModelAndView mav = new ModelAndView("/cgcc/sckc/sckcgl");
        try {
            mav.addObject("msg", "ok");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("跳转库存明细消息列表异常！", e);
        }
        return mav;
    }
    /**
     * @version V1.0
     * @Title: zhixiangpingtai
     * @Description:
     * @author: hhp
     * @param:  * @param warehouseDetailsDTO
     * @date: 2019/10/21 14:15
     */
    @PostMapping(value = "/getByIngId")
    @ResponseBody
    public ResultBean getByIngId(WarehouseDetailsDTO warehouseDetailsDTO) {
        logger.debug("获取库存明细信息列表！ warehouseDetailsDTO = "+warehouseDetailsDTO);
        ResultBean resultBean = new ResultBean();
        try {
            User existUser = (User) SecurityUtils.getSubject().getPrincipal();
            warehouseDetailsDTO.setUserId(existUser.getId());
            resultBean = warehouseDetailsService.findByIngredientId(warehouseDetailsDTO);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("获取库存明细信息异常！", e);
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
     * @param:  * @param warehouseDetailsDTO
     * @date: 2019/10/22 16:34
     */
    @PostMapping(value = "/getWarehouseDetailsList")
    @ResponseBody
    public ResultBean getWarehouseDetailsList(WarehouseDetailsDTO warehouseDetailsDTO) {
        logger.debug("获取库存明细信息列表！ warehouseDetailsDTO = "+warehouseDetailsDTO);
        ResultBean resultBean = new ResultBean();
        try {
            User existUser = (User) SecurityUtils.getSubject().getPrincipal();
            warehouseDetailsDTO.setUserId(existUser.getId());
            resultBean = warehouseDetailsService.findWarehouseDetailsList(warehouseDetailsDTO);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("获取库存明细异常！", e);
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
     * @param:  * @param warehouseDetailsDTO
     * @date: 2019/10/22 16:36
     */
    @RequestMapping(value = "/setWarehouseDetails", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean setWarehouseDetails(WarehouseDetailsDTO warehouseDetailsDTO) {
        logger.debug("添加或修改库存明细信息 --warehouseDetailsDTO-" + warehouseDetailsDTO);
        ResultBean resultBean = new ResultBean();
        try {
            User existUser = (User) SecurityUtils.getSubject().getPrincipal();
            if (!IsEmptyUtils.isEmpty(warehouseDetailsDTO)) {
                warehouseDetailsDTO.setUserId(existUser.getId());
                if(!IsEmptyUtils.isEmpty(warehouseDetailsDTO.getId())){
                    resultBean = warehouseDetailsService.updateWarehouseDetails(warehouseDetailsDTO);
                }else{
                    resultBean = siteService.getUserSites(existUser.getId());
                    if(!IsEmptyUtils.isEmpty(resultBean.getResult())){
                        List<SiteVO> siteVOList = (List<SiteVO>)resultBean.getResult();
                        if(siteVOList.size()>1){
                            if(!IsEmptyUtils.isEmpty(warehouseDetailsDTO.getSdIds())){
                                resultBean = warehouseDetailsService.addWarehouseDetails(warehouseDetailsDTO);
                            }else{
                                resultBean.setSuccess(false);
                                resultBean.setResultCode(IStatusMessage.SystemStatus.SITE_ID_LOSE.getCode());
                                resultBean.setMsg(IStatusMessage.SystemStatus.SITE_ID_LOSE.getMessage());
                            }
                        }else{
                            warehouseDetailsDTO.setSdId(existUser.getSdId());
                            warehouseDetailsDTO.setSdIds("");
                            resultBean = warehouseDetailsService.addWarehouseDetails(warehouseDetailsDTO);
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
            logger.error("添加或修改库存明细信息异常！", e);
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
     * @param:  * @param warehouseDetailsDTO
     * @date: 2019/10/22 16:38
     */
    @PostMapping(value = "/getUpdWarehouseDetails")
    @ResponseBody
    public ResultBean getUpdWarehouseDetails(WarehouseDetailsDTO warehouseDetailsDTO) {
        logger.debug("获取食材库存明细信息！ warehouseDetailsDTO = "+warehouseDetailsDTO);
        ResultBean resultBean = new ResultBean();
        try {
            User existUser = (User) SecurityUtils.getSubject().getPrincipal();
            warehouseDetailsDTO.setUserId(existUser.getId());
            if(IsEmptyUtils.isEmpty(warehouseDetailsDTO.getSdId())){
                warehouseDetailsDTO.setSdId(existUser.getSdId());
            }
            resultBean = warehouseDetailsService.findUpdWarehouseDetails(warehouseDetailsDTO);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("获取食材库存明细信息！", e);
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
     * @param:  * @param warehouseDetailsDTO
     * @date: 2019/10/22 16:40
     */
    @PostMapping(value = "/delWarehouseDetails")
    @ResponseBody
    public ResultBean delWarehouseDetails(WarehouseDetailsDTO warehouseDetailsDTO){
        logger.debug("删除库存明细信息 --warehouseDetailsDTO-" + warehouseDetailsDTO);
        ResultBean resultBean = new ResultBean();
        try {
            User existUser = (User) SecurityUtils.getSubject().getPrincipal();
            warehouseDetailsDTO.setUserId(existUser.getId());
            if (!IsEmptyUtils.isEmpty(warehouseDetailsDTO)) {
                /*if(IsEmptyUtils.isEmpty(ingredientBaseDTO.getSdId())){
                    ingredientBaseDTO
                }*/
                resultBean = warehouseDetailsService.delWarehouseDetails(warehouseDetailsDTO);
            }else{
                resultBean.setSuccess(false);
                resultBean.setResultCode(IStatusMessage.SystemStatus.PARAMETERS_LOSE.getCode());
                resultBean.setMsg(IStatusMessage.SystemStatus.PARAMETERS_LOSE.getMessage());
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("删除库存明细信息异常！", e);
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
     * @param:  * @param warehouseDetailsDTO
     * @date: 2019/10/23 14:49
     */
    @PostMapping(value = "/getPrintQRCodeData")
    @ResponseBody
    public ResultBean getPrintQRCodeData(WarehouseDetailsDTO warehouseDetailsDTO){
        logger.debug("获取打印二维码信息 --warehouseDetailsDTO-" + warehouseDetailsDTO);
        ResultBean resultBean = new ResultBean();
        try {
            User existUser = (User) SecurityUtils.getSubject().getPrincipal();
            warehouseDetailsDTO.setUserId(existUser.getId());
            resultBean = warehouseDetailsService.findPrintQRCodeData(warehouseDetailsDTO);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("获取打印二维码数据异常！", e);
            resultBean.setSuccess(false);
            resultBean.setResultCode(IStatusMessage.SystemStatus.ERROR.getCode());
            resultBean.setMsg(IStatusMessage.SystemStatus.ERROR.getMessage());
        }
        return resultBean;
    }
}
