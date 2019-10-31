package cn.zhixiangsingle.controller.warehouse;

import cn.zhixiangsingle.entity.base.IsEmptyUtils;
import cn.zhixiangsingle.entity.base.user.User;
import cn.zhixiangsingle.entity.warehouse.dto.IngredientWhouseDTO;
import cn.zhixiangsingle.service.site.SiteService;
import cn.zhixiangsingle.service.warehouse.IngredientWhouseService;
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
 * @Package cn.zhixiangsingle.controller.warehouse
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019/10/5 11:16
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Controller
@RequestMapping("/ingredientWhouse")
public class IngredientWhouseController {
    private static final Logger logger = LoggerFactory
            .getLogger(IngredientWhouseController.class);
    @Reference(version = "1.0.0")
    private SiteService siteService;
    @Reference(version = "1.0.0")
    private IngredientWhouseService ingredientWhouseService;
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
        logger.debug("跳转仓库类别信息列表！");
        ModelAndView mav = new ModelAndView("/cgcc/cklbgl/cklbgl");
        try {
            mav.addObject("msg", "ok");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("跳转仓库类别信息异常！", e);
        }
        return mav;
    }
    /**
     * @version V1.0
     * @Title: zhixiangpingtai
     * @Description:
     * @author: hhp
     * @param:  * @param ingredientWhouseDTO
     * @date: 2019/9/27 17:39
     */
    @RequestMapping(value = "/setIngredientWhouse", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean setIngredientWhouse(IngredientWhouseDTO ingredientWhouseDTO) {
        logger.debug("添加或修改仓库类别信息 --ingredientWhouseDTO-" + ingredientWhouseDTO);
        ResultBean resultBean = new ResultBean();
        try {
            User existUser = (User) SecurityUtils.getSubject().getPrincipal();
            if (!IsEmptyUtils.isEmpty(ingredientWhouseDTO)) {
                ingredientWhouseDTO.setUserId(existUser.getId());
                if(!IsEmptyUtils.isEmpty(ingredientWhouseDTO.getId())){
                    resultBean = ingredientWhouseService.updateIngredientWhouse(ingredientWhouseDTO);
                }else{
                    resultBean = ingredientWhouseService.addIngredientWhouse(ingredientWhouseDTO);
                }
            }else{
                resultBean.setSuccess(false);
                resultBean.setResultCode(IStatusMessage.SystemStatus.PARAMETERS_LOSE.getCode());
                resultBean.setMsg(IStatusMessage.SystemStatus.PARAMETERS_LOSE.getMessage());
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("添加或修改仓库类别信息异常！", e);
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
     * @param:  * @param ingredientWhouseDTO
     * @date: 2019/9/27 17:40
     */
    @PostMapping(value = "/getIngredientWhouseList")
    @ResponseBody
    public ResultBean getIngredientWhouseList(IngredientWhouseDTO ingredientWhouseDTO) {
        logger.debug("获取仓库类别信息列表！ ingredientWhouseDTO = "+ingredientWhouseDTO);
        ResultBean resultBean = new ResultBean();
        try {
            User existUser = (User) SecurityUtils.getSubject().getPrincipal();
            ingredientWhouseDTO.setUserId(existUser.getId());
            if(existUser.getZx()==true||existUser.getSdId()!=22){
                ingredientWhouseDTO.setSdId(existUser.getSdId());
            }
            resultBean = ingredientWhouseService.findIngredientWhouseList(ingredientWhouseDTO);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("获取仓库类别信息列表异常！", e);
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
     * @param:  * @param ingredientWhouseDTO
     * @date: 2019/9/27 17:39
     */
    @PostMapping(value = "/getAll")
    @ResponseBody
    public ResultBean getAll(IngredientWhouseDTO ingredientWhouseDTO) {
        logger.debug("获取所有仓库类别信息列表！ ingredientWhouseDTO = "+ingredientWhouseDTO);
        ResultBean resultBean = new ResultBean();
        try {
            User existUser = (User) SecurityUtils.getSubject().getPrincipal();
            ingredientWhouseDTO.setUserId(existUser.getId());
            if(existUser.getZx()==true||existUser.getSdId()!=22){
                ingredientWhouseDTO.setSdId(existUser.getSdId());
            }
            resultBean = ingredientWhouseService.findAll(ingredientWhouseDTO);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("获取所有仓库类别信息列表异常！", e);
            resultBean.setSuccess(false);
            resultBean.setResultCode(IStatusMessage.SystemStatus.ERROR.getCode());
            resultBean.setMsg(IStatusMessage.SystemStatus.ERROR.getMessage());
        }
        return resultBean;
    }
}
