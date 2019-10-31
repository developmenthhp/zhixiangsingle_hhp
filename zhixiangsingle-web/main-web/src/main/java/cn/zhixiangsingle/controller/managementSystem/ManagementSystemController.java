package cn.zhixiangsingle.controller.managementSystem;

import cn.zhixiangsingle.date.DateUtils;
import cn.zhixiangsingle.entity.base.IsEmptyUtils;
import cn.zhixiangsingle.entity.base.site.vo.SiteVO;
import cn.zhixiangsingle.entity.base.user.User;
import cn.zhixiangsingle.entity.managementSystem.dto.ManagementSystemDTO;
import cn.zhixiangsingle.entity.managementSystem.vo.ManagementSystemVO;
import cn.zhixiangsingle.excel.ExcelUtil;
import cn.zhixiangsingle.excel.ExcelWriterFactroy;
import cn.zhixiangsingle.service.managementSystem.ManagementSystemService;
import cn.zhixiangsingle.service.site.SiteService;
import cn.zhixiangsingle.web.responsive.IStatusMessage;
import cn.zhixiangsingle.web.responsive.ResultBean;
import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.excel.metadata.Sheet;
import com.alibaba.excel.metadata.Table;
import com.alibaba.excel.support.ExcelTypeEnum;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
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
 * @Package cn.zhixiangsingle.controller.managementSystem
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019/10/16 14:19
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Controller
@RequestMapping("/managementSystem")
public class ManagementSystemController {
    private static final Logger logger = LoggerFactory
            .getLogger(ManagementSystemController.class);
    @Reference(version = "1.0.0")
    private SiteService siteService;
    @Reference(version = "1.0.0")
    private ManagementSystemService managementSystemService;
    /**
     * @version V1.0
     * @Title: zhixiangpingtai
     * @Description:
     * @author: hhp
     * @param:  * @param
     * @date: 2019/10/16 14:35
     */
    @RequestMapping(value = "/getListPage", method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView getListPage() {
        logger.debug("跳转管理制度信息列表！");
        ModelAndView mav = new ModelAndView("/jygs/glzd/glzd");
        try {
            mav.addObject("msg", "ok");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("跳转管理制度列表异常！", e);
        }
        return mav;
    }
    /**
     * @version V1.0
     * @Title: zhixiangpingtai
     * @Description:
     * @author: hhp
     * @param:  * @param managementSystemDTO
     * @date: 2019/10/16 14:39
     */
    @PostMapping(value = "/getManagementSystemList")
    @ResponseBody
    public ResultBean getCompanyHonorList(ManagementSystemDTO managementSystemDTO) {
        logger.debug("获取管理制度列表！ managementSystemDTO = "+managementSystemDTO);
        ResultBean resultBean = new ResultBean();
        try {
            User existUser = (User) SecurityUtils.getSubject().getPrincipal();
            managementSystemDTO.setUserId(existUser.getId());
            resultBean = managementSystemService.findManagementSystemList(managementSystemDTO);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("获取管理制度列表异常！", e);
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
     * @param:  * @param managementSystemDTO
     * @date: 2019/10/16 14:39
     */
    @RequestMapping(value = "/setManagementSystem", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean setManagementSystem(ManagementSystemDTO managementSystemDTO) {
        logger.debug("添加或修改管理制度信息 --managementSystemDTO-" + managementSystemDTO);
        ResultBean resultBean = new ResultBean();
        try {
            User existUser = (User) SecurityUtils.getSubject().getPrincipal();
            if (!IsEmptyUtils.isEmpty(managementSystemDTO)) {
                managementSystemDTO.setUserId(existUser.getId());
                if(!IsEmptyUtils.isEmpty(managementSystemDTO.getId())){
                    resultBean = managementSystemService.updateManagementSystem(managementSystemDTO);
                }else{
                    resultBean = siteService.getUserSites(existUser.getId());
                    if(!IsEmptyUtils.isEmpty(resultBean.getResult())){
                        List<SiteVO> siteVOList = (List<SiteVO>)resultBean.getResult();
                        if(siteVOList.size()>1){
                            if(!IsEmptyUtils.isEmpty(managementSystemDTO.getSdIds())){
                                resultBean = managementSystemService.addManagementSystem(managementSystemDTO);
                            }else{
                                resultBean.setSuccess(false);
                                resultBean.setResultCode(IStatusMessage.SystemStatus.SITE_ID_LOSE.getCode());
                                resultBean.setMsg(IStatusMessage.SystemStatus.SITE_ID_LOSE.getMessage());
                            }
                        }else{
                            managementSystemDTO.setSdId(existUser.getSdId());
                            managementSystemDTO.setSdIds("");
                            resultBean = managementSystemService.addManagementSystem(managementSystemDTO);
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
            logger.error("添加或修改管理制度信息异常！", e);
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
     * @param:  * @param managementSystemDTO
     * @date: 2019/10/16 14:43
     */
    @PostMapping(value = "/getUpdManagementSystem")
    @ResponseBody
    public ResultBean getUpdManagementSystem(ManagementSystemDTO managementSystemDTO) {
        logger.debug("获取管理制度！ managementSystemDTO = "+managementSystemDTO);
        ResultBean resultBean = new ResultBean();
        try {
            User existUser = (User) SecurityUtils.getSubject().getPrincipal();
            managementSystemDTO.setUserId(existUser.getId());
            if(IsEmptyUtils.isEmpty(managementSystemDTO.getSdId())){
                managementSystemDTO.setSdId(existUser.getSdId());
            }
            resultBean = managementSystemService.findUpdManagementSystem(managementSystemDTO);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("获取管理制度异常！", e);
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
     * @param:  * @param managementSystemDTO
     * @date: 2019/10/16 14:47
     */
    @PostMapping(value = "/delManagementSystem")
    @ResponseBody
    public ResultBean delManagementSystem(ManagementSystemDTO managementSystemDTO){
        logger.debug("删除管理制度信息 --managementSystemDTO-" + managementSystemDTO);
        ResultBean resultBean = new ResultBean();
        try {
            User existUser = (User) SecurityUtils.getSubject().getPrincipal();
            managementSystemDTO.setUserId(existUser.getId());
            if (!IsEmptyUtils.isEmpty(managementSystemDTO)) {
                /*if(IsEmptyUtils.isEmpty(ingredientBaseDTO.getSdId())){
                    ingredientBaseDTO
                }*/
                resultBean = managementSystemService.delManagementSystem(managementSystemDTO);
            }else{
                resultBean.setSuccess(false);
                resultBean.setResultCode(IStatusMessage.SystemStatus.PARAMETERS_LOSE.getCode());
                resultBean.setMsg(IStatusMessage.SystemStatus.PARAMETERS_LOSE.getMessage());
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("删除管理制度异常！", e);
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
     * @param:  * @param response
     * @param managementSystemDTO
     * @date: 2019/10/19 10:54
     */
    @RequestMapping(value = "/writeManagementSystems", method = RequestMethod.GET)
    public void writeManagementSystems(HttpServletResponse response, ManagementSystemDTO managementSystemDTO){
        logger.debug("导出管理制度信息Excel！条件managementSystemDTO:"+managementSystemDTO);
        try {
            User existUser = (User) SecurityUtils.getSubject().getPrincipal();
            managementSystemDTO.setUserId(existUser.getId());
            /*if(existUser.getZx()==true||existUser.getSdId()!=22){
                managementSystemDTO.setSdId(existUser.getSdId());
            }*/
            List<ResultBean> ingredientBasesModel = managementSystemService.getManagementSystems(managementSystemDTO);
            ExcelWriterFactroy writer = new ExcelWriterFactroy(ExcelUtil.getOutputStream(DateUtils.formatDate(new Date(),"yyyy-MM-dd-HH-mm:ss-管理制度信息"), response), ExcelTypeEnum.XLSX);
            ManagementSystemVO managementSystemVO = new ManagementSystemVO();
            if(!IsEmptyUtils.isEmpty(ingredientBasesModel)){
                for(int i=0;i<ingredientBasesModel.size();i++){
                    //sheetNo 不能从0开始
                    Sheet sheet = new Sheet(i+1, 2, managementSystemVO.getClass());
                    sheet.setSheetName(ingredientBasesModel.get(i).getMsg());
                    sheet.setColumnWidthMap((Map)ingredientBasesModel.get(i).getObj());
                    Table table1 = new Table(1);
                    table1.setClazz(ManagementSystemVO.class);
                    table1.setHead((List<List<String>>)ingredientBasesModel.get(i).getRows());
                    List<ManagementSystemVO> managementSystemVOS = (List<ManagementSystemVO>)ingredientBasesModel.get(i).getResult();
                    if(!IsEmptyUtils.isEmpty(managementSystemVOS)){
                        writer.write(managementSystemVOS, sheet, table1);
                    }
                }
            }
            writer.finish();
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("导出管理制度Excel异常！", e);
        }
    }
}
