package cn.zhixiangsingle.controller.supplier;

import cn.zhixiangsingle.entity.base.IsEmptyUtils;
import cn.zhixiangsingle.entity.base.user.User;
import cn.zhixiangsingle.entity.supplier.dto.IngredientSupplierDTO;
import cn.zhixiangsingle.service.site.SiteService;
import cn.zhixiangsingle.service.supplier.IngredientSupplierService;
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
 * @Package cn.zhixiangsingle.controller.supplier
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019/10/5 13:23
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Controller
@RequestMapping("/ingredientSupp")
public class IngredientSupplierController {
    private static final Logger logger = LoggerFactory
            .getLogger(IngredientSupplierController.class);
    @Reference(version = "1.0.0")
    private SiteService siteService;
    @Reference(version = "1.0.0")
    private IngredientSupplierService ingredientSupplierService;
    /**
     * @version V1.0
     * @Title: zhixiangpingtai
     * @Description:
     * @author: hhp
     * @param:  * @param
     * @date: 2019/9/10 8:50
     */
    @RequestMapping(value = "/getListPage", method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView getMapPage() {
        logger.debug("跳转供应商信息列表！");
        ModelAndView mav = new ModelAndView("/cgcc/ghsjczlgl/ghsjczlgl");
        try {
            mav.addObject("msg", "ok");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("跳转供应商列表异常！", e);
        }
        return mav;
    }
    /**
     * @version V1.0
     * @Title: zhixiangpingtai
     * @Description:
     * @author: hhp
     * @param:  * @param ingredientSupplierDTO
     * @date: 2019/9/27 17:39
     */
    @RequestMapping(value = "/setIngredientSupplier", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean setIngredientSupplier(IngredientSupplierDTO ingredientSupplierDTO) {
        logger.debug("添加或修改供应商信息 --ingredientSupplierDTO-" + ingredientSupplierDTO);
        ResultBean resultBean = new ResultBean();
        try {
            User existUser = (User) SecurityUtils.getSubject().getPrincipal();
            if (!IsEmptyUtils.isEmpty(ingredientSupplierDTO)) {
                ingredientSupplierDTO.setUserId(existUser.getId());
                if(!IsEmptyUtils.isEmpty(ingredientSupplierDTO.getId())){
                    resultBean = ingredientSupplierService.updateIngredientSupplier(ingredientSupplierDTO);
                }else{
                    resultBean = ingredientSupplierService.addIngredientSupplier(ingredientSupplierDTO);
                }
            }else{
                resultBean.setSuccess(false);
                resultBean.setResultCode(IStatusMessage.SystemStatus.PARAMETERS_LOSE.getCode());
                resultBean.setMsg(IStatusMessage.SystemStatus.PARAMETERS_LOSE.getMessage());
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("添加或修改供应商信息异常！", e);
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
     * @param:  * @param ingredientSupplierDTO
     * @date: 2019/9/27 17:40
     */
    @PostMapping(value = "/getIngredientSupplierList")
    @ResponseBody
    public ResultBean getIngredientSupplierList(IngredientSupplierDTO ingredientSupplierDTO) {
        logger.debug("获取供应商信息列表！ ingredientSupplierDTO = "+ingredientSupplierDTO);
        ResultBean resultBean = new ResultBean();
        try {
            User existUser = (User) SecurityUtils.getSubject().getPrincipal();
            ingredientSupplierDTO.setUserId(existUser.getId());
            if(existUser.getZx()==true||existUser.getSdId()!=22){
                ingredientSupplierDTO.setSdId(existUser.getSdId());
            }
            resultBean = ingredientSupplierService.findIngredientSupplierList(ingredientSupplierDTO);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("获取供应商信息列表异常！", e);
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
     * @param:  * @param ingredientSupplierDTO
     * @date: 2019/9/27 17:39
     */
    @PostMapping(value = "/getAll")
    @ResponseBody
    public ResultBean getAll(IngredientSupplierDTO ingredientSupplierDTO) {
        logger.debug("获取所有供应商信息列表！ ingredientSupplierDTO = "+ingredientSupplierDTO);
        ResultBean resultBean = new ResultBean();
        try {
            User existUser = (User) SecurityUtils.getSubject().getPrincipal();
            ingredientSupplierDTO.setUserId(existUser.getId());
            if(existUser.getZx()==true||existUser.getSdId()!=22){
                ingredientSupplierDTO.setSdId(existUser.getSdId());
            }
            resultBean = ingredientSupplierService.findAll(ingredientSupplierDTO);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("获取所有供应商信息列表异常！", e);
            resultBean.setSuccess(false);
            resultBean.setResultCode(IStatusMessage.SystemStatus.ERROR.getCode());
            resultBean.setMsg(IStatusMessage.SystemStatus.ERROR.getMessage());
        }
        return resultBean;
    }
}
