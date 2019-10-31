package cn.zhixiangsingle.controller.purchase.scjcxx;

import cn.zhixiangsingle.date.DateUtils;
import cn.zhixiangsingle.entity.base.IsEmptyUtils;

import cn.zhixiangsingle.entity.base.user.User;
import cn.zhixiangsingle.entity.purchase.scjcxx.dto.IngredientBaseDTO;
import cn.zhixiangsingle.entity.purchase.scjcxx.vo.IngredientBaseVO;
import cn.zhixiangsingle.excel.ExcelUtil;
import cn.zhixiangsingle.excel.ExcelWriterFactroy;
import cn.zhixiangsingle.service.purchase.scjcxx.IngredientBaseService;
import cn.zhixiangsingle.service.site.SiteService;
import cn.zhixiangsingle.web.responsive.IStatusMessage;
import cn.zhixiangsingle.web.responsive.ResultBean;
import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.excel.metadata.Sheet;
import com.alibaba.excel.metadata.Table;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.google.common.collect.Lists;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangpingtai
 * @Package cn.zhixiangsingle.controller.purchase.scjcxx
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019/9/20 9:20
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Controller
@RequestMapping("/ingredientBase")
public class IngredientBaseController {
    private static final Logger logger = LoggerFactory
            .getLogger(IngredientBaseController.class);
    @Reference(version = "1.0.0")
    private SiteService siteService;
    @Reference(version = "1.0.0")
    private IngredientBaseService ingredientBaseService;
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
        logger.debug("跳转食材基础信息列表！");
        ModelAndView mav = new ModelAndView("/cgcc/scjcxxgl/scjcxxgl");
        try {
            mav.addObject("msg", "ok");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("跳转食材基础信息异常！", e);
        }
        return mav;
    }
    /**
     * @version V1.0
     * @Title: zhixiangpingtai
     * @Description:
     * @author: hhp
     * @param:  * @param ingredientBaseDTO
     * @date: 2019/9/20 10:40
     */
    @RequestMapping(value = "/setIngredientBase", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean setIngredientBase(IngredientBaseDTO ingredientBaseDTO) {
        logger.debug("添加或修改食材基础信息 --ingredientBaseDTO-" + ingredientBaseDTO);
        ResultBean resultBean = new ResultBean();
        try {
            User existUser = (User) SecurityUtils.getSubject().getPrincipal();
            if (!IsEmptyUtils.isEmpty(ingredientBaseDTO)) {
                ingredientBaseDTO.setUserId(existUser.getId());

                if(!IsEmptyUtils.isEmpty(ingredientBaseDTO.getGgxh())){
                    if(IsEmptyUtils.isEmpty(ingredientBaseDTO.getGgxhdw())){
                        resultBean.setSuccess(false);
                        resultBean.setResultCode(IStatusMessage.SystemStatus.GGXHDW_LOSE.getCode());
                        resultBean.setMsg(IStatusMessage.SystemStatus.GGXHDW_LOSE.getMessage());
                        return resultBean;
                    }
                }
                if(ingredientBaseDTO.getNeedCleaning().equals("1")){
                    if(IsEmptyUtils.isEmpty(ingredientBaseDTO.getCleaningTime())&&IsEmptyUtils.isEmpty(ingredientBaseDTO.getCleanWater())){
                        resultBean.setSuccess(false);
                        resultBean.setResultCode(IStatusMessage.SystemStatus.CLEAN_TIME_WATER_LOSE.getCode());
                        resultBean.setMsg(IStatusMessage.SystemStatus.CLEAN_TIME_WATER_LOSE.getMessage());
                        return resultBean;
                    }else if(IsEmptyUtils.isEmpty(ingredientBaseDTO.getCleaningTime())){
                        resultBean.setSuccess(false);
                        resultBean.setResultCode(IStatusMessage.SystemStatus.CLEAN_TIME_LOSE.getCode());
                        resultBean.setMsg(IStatusMessage.SystemStatus.CLEAN_TIME_LOSE.getMessage());
                        return resultBean;
                    }else if(IsEmptyUtils.isEmpty(ingredientBaseDTO.getCleanWater())){
                        resultBean.setSuccess(false);
                        resultBean.setResultCode(IStatusMessage.SystemStatus.CLEAN_WATER_LOSE.getCode());
                        resultBean.setMsg(IStatusMessage.SystemStatus.CLEAN_WATER_LOSE.getMessage());
                        return resultBean;
                    }
                }
                if(!IsEmptyUtils.isEmpty(ingredientBaseDTO.getBoomTypeStr())){
                    String[] bomTypes = ingredientBaseDTO.getBoomTypeStr().split(",");
                    if(bomTypes.length==1){
                        ingredientBaseDTO.setBoomType(Integer.parseInt(bomTypes[0]));
                    }else if(bomTypes.length==2){
                        if(bomTypes[0].equals("1")){
                            if(!bomTypes[1].equals("2")){
                                resultBean.setSuccess(false);
                                resultBean.setResultCode(IStatusMessage.SystemStatus.BOM_TYPE_TOTAL_ERROR.getCode());
                                resultBean.setMsg(IStatusMessage.SystemStatus.BOM_TYPE_TOTAL_ERROR.getMessage());
                                return resultBean;
                            }else{
                                ingredientBaseDTO.setBoomType(3);
                            }
                        }else if(bomTypes[0].equals("2")){
                            if(!bomTypes[1].equals("1")){
                                resultBean.setSuccess(false);
                                resultBean.setResultCode(IStatusMessage.SystemStatus.BOM_TYPE_TOTAL_ERROR.getCode());
                                resultBean.setMsg(IStatusMessage.SystemStatus.BOM_TYPE_TOTAL_ERROR.getMessage());
                                return resultBean;
                            }else{
                                ingredientBaseDTO.setBoomType(3);
                            }
                        }else{
                            resultBean.setSuccess(false);
                            resultBean.setResultCode(IStatusMessage.SystemStatus.BOM_TYPE_TOTAL_ERROR.getCode());
                            resultBean.setMsg(IStatusMessage.SystemStatus.BOM_TYPE_TOTAL_ERROR.getMessage());
                            return resultBean;
                        }
                    }else if(bomTypes.length>2){
                        resultBean.setSuccess(false);
                        resultBean.setResultCode(IStatusMessage.SystemStatus.BOM_TYPE_TOTAL_ERROR.getCode());
                        resultBean.setMsg(IStatusMessage.SystemStatus.BOM_TYPE_TOTAL_ERROR.getMessage());
                        return resultBean;
                    }
                }

                if(!IsEmptyUtils.isEmpty(ingredientBaseDTO.getId())){
                    resultBean = ingredientBaseService.updateIngredientBase(ingredientBaseDTO);
                }else{
                    resultBean = ingredientBaseService.addIngredientBase(ingredientBaseDTO);
                }
            }else{
                resultBean.setSuccess(false);
                resultBean.setResultCode(IStatusMessage.SystemStatus.PARAMETERS_LOSE.getCode());
                resultBean.setMsg(IStatusMessage.SystemStatus.PARAMETERS_LOSE.getMessage());
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("添加获取修改食材基础信息异常！", e);
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
     * @param:  * @param ingredientBaseDTO
     * @date: 2019/10/7 14:01
     */
    @RequestMapping(value = "/delIngredientBase", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean delIngredientBase(IngredientBaseDTO ingredientBaseDTO){
        logger.debug("逻辑删除食材基础信息 --ingredientBaseDTO-" + ingredientBaseDTO);
        ResultBean resultBean = new ResultBean();
        try {
            User existUser = (User) SecurityUtils.getSubject().getPrincipal();
            if (!IsEmptyUtils.isEmpty(ingredientBaseDTO)) {
                /*if(IsEmptyUtils.isEmpty(ingredientBaseDTO.getSdId())){
                    ingredientBaseDTO
                }*/
                resultBean = ingredientBaseService.delIngredientBase(ingredientBaseDTO);
            }else{
                resultBean.setSuccess(false);
                resultBean.setResultCode(IStatusMessage.SystemStatus.PARAMETERS_LOSE.getCode());
                resultBean.setMsg(IStatusMessage.SystemStatus.PARAMETERS_LOSE.getMessage());
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("逻辑删除食材基础信息异常！", e);
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
     * @param:  * @param ingredientBaseDTO
     * @date: 2019/9/20 10:53
     */
    @PostMapping(value = "/getIngredientBaseList")
    @ResponseBody
    public ResultBean getIngredientBaseList(IngredientBaseDTO ingredientBaseDTO) {
        logger.debug("获取食材基础信息列表！ ingredientBaseDTO = "+ingredientBaseDTO);
        ResultBean resultBean = new ResultBean();
        try {
            User existUser = (User) SecurityUtils.getSubject().getPrincipal();
            ingredientBaseDTO.setUserId(existUser.getId());
            if(existUser.getZx()==true||existUser.getSdId()!=22){
                ingredientBaseDTO.setSdId(existUser.getSdId());
            }
            resultBean = ingredientBaseService.findIngredientBaseList(ingredientBaseDTO);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("获取食材基础信息列表异常！", e);
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
     * @param:  * @param ingredientBaseDTO
     * @date: 2019/10/9 9:34
     */
    @PostMapping(value = "/getUpdIngredientBase")
    @ResponseBody
    public ResultBean getUpdIngredientBase(IngredientBaseDTO ingredientBaseDTO) {
        logger.debug("获取食材基础信息！ ingredientBaseDTO = "+ingredientBaseDTO);
        ResultBean resultBean = new ResultBean();
        try {
            User existUser = (User) SecurityUtils.getSubject().getPrincipal();
            ingredientBaseDTO.setUserId(existUser.getId());
            if(IsEmptyUtils.isEmpty(ingredientBaseDTO.getSdId())){
                ingredientBaseDTO.setSdId(existUser.getSdId());
            }
            resultBean = ingredientBaseService.findUpdIngredientBase(ingredientBaseDTO);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("获取食材基础信息异常！", e);
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
     * @param:  * @param fieldId
     * @param ingredientName
     * @param sdIdStr
     * @date: 2019/9/21 17:47
     */
    @RequestMapping(value = "isNameExist", method = RequestMethod.POST)
    @ResponseBody
    public List<Object> isNameExist(@RequestParam("fieldId") String fieldId, @RequestParam("fieldValue") String ingredientName,
                                    @RequestParam(value = "sdId",required = false)String sdIdStr, @RequestParam(value = "id",required = false)Integer id){
        List<Object> validateResult = Lists.newArrayList();
        try {
            if(!IsEmptyUtils.isEmpty(id)){
                validateResult.add(fieldId);
                validateResult.add(true);
                return validateResult;
            }
            User existUser= (User) SecurityUtils.getSubject().getPrincipal();
            IngredientBaseDTO ingredientBaseDTO = new IngredientBaseDTO();
            ingredientBaseDTO.setUserId(existUser.getId());
            ingredientBaseDTO.setSdIds(sdIdStr);
            ingredientBaseDTO.setIngredientName(ingredientName);
            //前台传站点id，不管是单个还是多个站点角色权限
            ResultBean resultBean = ingredientBaseService.selectCommon(ingredientBaseDTO);
            if(!IsEmptyUtils.isEmpty(resultBean.getMsg())){
                validateResult.add(fieldId);
                validateResult.add(false);
                validateResult.add(resultBean.getMsg());
            }else{
                validateResult.add(fieldId);
                validateResult.add(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
            validateResult.add(fieldId);
            validateResult.add(false);
            logger.error("校验食材名异常！", e);
        }
        return validateResult;
    }
    /**
     * @version V1.0
     * @Title: zhixiangpingtai
     * @Description:
     * @author: hhp
     * @param:  * @param response
     * @param ingredientBaseDTO
     * @date: 2019/10/9 9:34
     */
    @RequestMapping(value = "/writeIngredientBases", method = RequestMethod.GET)
    public void writeIngredientBases(HttpServletResponse response,IngredientBaseDTO ingredientBaseDTO){
        logger.debug("导出食材基础信息Excel！条件:"+ingredientBaseDTO);
        try {
            User existUser = (User) SecurityUtils.getSubject().getPrincipal();
            ingredientBaseDTO.setUserId(existUser.getId());
            if(existUser.getZx()==true||existUser.getSdId()!=22){
                ingredientBaseDTO.setSdId(existUser.getSdId());
            }
            List<ResultBean> ingredientBasesModel = ingredientBaseService.getIngredientBases(ingredientBaseDTO);
            ExcelWriterFactroy writer = new ExcelWriterFactroy(ExcelUtil.getOutputStream(DateUtils.formatDate(new Date(),"yyyy-MM-dd-HH-mm:ss-食材基础信息"), response), ExcelTypeEnum.XLSX);
            IngredientBaseVO ingredientBaseVO = new IngredientBaseVO();
            if(!IsEmptyUtils.isEmpty(ingredientBasesModel)){
                for(int i=0;i<ingredientBasesModel.size();i++){
                    //sheetNo 不能从0开始
                    Sheet sheet = new Sheet(i+1, 2, ingredientBaseVO.getClass());
                    sheet.setSheetName(ingredientBasesModel.get(i).getMsg());
                    sheet.setColumnWidthMap((Map)ingredientBasesModel.get(i).getObj());
                    Table table1 = new Table(1);
                    table1.setClazz(IngredientBaseVO.class);
                    table1.setHead((List<List<String>>)ingredientBasesModel.get(i).getRows());
                    List<IngredientBaseVO> ingredientBaseVOS = (List<IngredientBaseVO>)ingredientBasesModel.get(i).getResult();
                    if(!IsEmptyUtils.isEmpty(ingredientBaseVOS)){
                        writer.write(ingredientBaseVOS, sheet, table1);
                    }
                }
            }
            writer.finish();
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("出 采购单Excel异常！", e);
        }
    }
    /**
     * @version V1.0
     * @Title: zhixiangpingtai
     * @Description:
     * @author: hhp
     * @param:  * @param ingredientBaseDTO
     * @date: 2019/10/26 12:51
     */
    @PostMapping(value = "/getAll")
    @ResponseBody
    public ResultBean getAllIngredientBase(IngredientBaseDTO ingredientBaseDTO) {
        logger.debug("获取所有食材信息列表！ ingredientBaseDTO = "+ingredientBaseDTO);
        ResultBean resultBean = new ResultBean();
        try {
            User existUser = (User) SecurityUtils.getSubject().getPrincipal();
            ingredientBaseDTO.setUserId(existUser.getId());
            resultBean = ingredientBaseService.findAllIngredientBase(ingredientBaseDTO);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("获取所有食材信息列表异常！", e);
            resultBean.setSuccess(false);
            resultBean.setResultCode(IStatusMessage.SystemStatus.ERROR.getCode());
            resultBean.setMsg(IStatusMessage.SystemStatus.ERROR.getMessage());
        }
        return resultBean;
    }
}
