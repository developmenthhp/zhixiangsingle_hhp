package cn.zhixiangsingle.controller.jobResponsibilities;

import cn.zhixiangsingle.date.DateUtils;
import cn.zhixiangsingle.entity.base.IsEmptyUtils;
import cn.zhixiangsingle.entity.base.site.vo.SiteVO;
import cn.zhixiangsingle.entity.base.user.User;
import cn.zhixiangsingle.entity.jobResponsibilities.dto.JobResponsibilitiesDTO;
import cn.zhixiangsingle.entity.jobResponsibilities.vo.JobResponsibilitiesVO;
import cn.zhixiangsingle.excel.ExcelUtil;
import cn.zhixiangsingle.excel.ExcelWriterFactroy;
import cn.zhixiangsingle.service.jobResponsibilities.JobResponsibilitiesService;
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
 * @Package cn.zhixiangsingle.controller.jobResponsibilities
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019/10/17 10:48
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Controller
@RequestMapping("/jobResponsibilities")
public class JobResponsibilitiesController {
    private static final Logger logger = LoggerFactory
            .getLogger(JobResponsibilitiesController.class);
    @Reference(version = "1.0.0")
    private SiteService siteService;
    @Reference(version = "1.0.0")
    private JobResponsibilitiesService jobResponsibilitiesService;
    /**
     * @version V1.0
     * @Title: zhixiangpingtai
     * @Description:
     * @author: hhp
     * @param:  * @param
     * @date: 2019/10/17 11:04
     */
    @RequestMapping(value = "/getListPage", method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView getListPage() {
        logger.debug("跳转岗位职责信息列表！");
        ModelAndView mav = new ModelAndView("/jygs/gwzz/gwzz");
        try {
            mav.addObject("msg", "ok");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("跳转岗位职责列表异常！", e);
        }
        return mav;
    }
    /**
     * @version V1.0
     * @Title: zhixiangpingtai
     * @Description:
     * @author: hhp
     * @param:  * @param jobResponsibilitiesDTO
     * @date: 2019/10/17 11:05
     */
    @PostMapping(value = "/getJobResponsibilitiesList")
    @ResponseBody
    public ResultBean getJobResponsibilitiesList(JobResponsibilitiesDTO jobResponsibilitiesDTO) {
        logger.debug("获取岗位职责列表！ jobResponsibilitiesDTO = "+jobResponsibilitiesDTO);
        ResultBean resultBean = new ResultBean();
        try {
            User existUser = (User) SecurityUtils.getSubject().getPrincipal();
            jobResponsibilitiesDTO.setUserId(existUser.getId());
            resultBean = jobResponsibilitiesService.findJobResponsibilitiesList(jobResponsibilitiesDTO);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("获取岗位职责列表异常！", e);
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
     * @param:  * @param jobResponsibilitiesDTO
     * @date: 2019/10/17 11:08
     */
    @RequestMapping(value = "/setJobResponsibilities", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean setJobResponsibilities(JobResponsibilitiesDTO jobResponsibilitiesDTO) {
        logger.debug("添加或修改岗位职责信息 --jobResponsibilitiesDTO-" + jobResponsibilitiesDTO);
        ResultBean resultBean = new ResultBean();
        try {
            User existUser = (User) SecurityUtils.getSubject().getPrincipal();
            if (!IsEmptyUtils.isEmpty(jobResponsibilitiesDTO)) {
                jobResponsibilitiesDTO.setUserId(existUser.getId());
                if(!IsEmptyUtils.isEmpty(jobResponsibilitiesDTO.getId())){
                    resultBean = jobResponsibilitiesService.updateJobResponsibilities(jobResponsibilitiesDTO);
                }else{
                    resultBean = siteService.getUserSites(existUser.getId());
                    if(!IsEmptyUtils.isEmpty(resultBean.getResult())){
                        List<SiteVO> siteVOList = (List<SiteVO>)resultBean.getResult();
                        if(siteVOList.size()>1){
                            if(!IsEmptyUtils.isEmpty(jobResponsibilitiesDTO.getSdIds())){
                                resultBean = jobResponsibilitiesService.addJobResponsibilities(jobResponsibilitiesDTO);
                            }else{
                                resultBean.setSuccess(false);
                                resultBean.setResultCode(IStatusMessage.SystemStatus.SITE_ID_LOSE.getCode());
                                resultBean.setMsg(IStatusMessage.SystemStatus.SITE_ID_LOSE.getMessage());
                            }
                        }else{
                            jobResponsibilitiesDTO.setSdId(existUser.getSdId());
                            jobResponsibilitiesDTO.setSdIds("");
                            resultBean = jobResponsibilitiesService.addJobResponsibilities(jobResponsibilitiesDTO);
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
            logger.error("添加或修改岗位职责信息异常！", e);
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
     * @param:  * @param jobResponsibilitiesDTO
     * @date: 2019/10/17 11:10
     */
    @PostMapping(value = "/getUpdJobResponsibilities")
    @ResponseBody
    public ResultBean getUpdJobResponsibilities(JobResponsibilitiesDTO jobResponsibilitiesDTO) {
        logger.debug("获取岗位职责！ jobResponsibilitiesDTO = "+jobResponsibilitiesDTO);
        ResultBean resultBean = new ResultBean();
        try {
            User existUser = (User) SecurityUtils.getSubject().getPrincipal();
            jobResponsibilitiesDTO.setUserId(existUser.getId());
            if(IsEmptyUtils.isEmpty(jobResponsibilitiesDTO.getSdId())){
                jobResponsibilitiesDTO.setSdId(existUser.getSdId());
            }
            resultBean = jobResponsibilitiesService.findUpdJobResponsibilities(jobResponsibilitiesDTO);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("获取岗位职责异常！", e);
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
     * @param:  * @param jobResponsibilitiesDTO
     * @date: 2019/10/17 11:12
     */
    @PostMapping(value = "/delJobResponsibilities")
    @ResponseBody
    public ResultBean delJobResponsibilities(JobResponsibilitiesDTO jobResponsibilitiesDTO){
        logger.debug("删除岗位职责信息 --jobResponsibilitiesDTO-" + jobResponsibilitiesDTO);
        ResultBean resultBean = new ResultBean();
        try {
            User existUser = (User) SecurityUtils.getSubject().getPrincipal();
            jobResponsibilitiesDTO.setUserId(existUser.getId());
            if (!IsEmptyUtils.isEmpty(jobResponsibilitiesDTO)) {
                /*if(IsEmptyUtils.isEmpty(ingredientBaseDTO.getSdId())){
                    ingredientBaseDTO
                }*/
                resultBean = jobResponsibilitiesService.delJobResponsibilities(jobResponsibilitiesDTO);
            }else{
                resultBean.setSuccess(false);
                resultBean.setResultCode(IStatusMessage.SystemStatus.PARAMETERS_LOSE.getCode());
                resultBean.setMsg(IStatusMessage.SystemStatus.PARAMETERS_LOSE.getMessage());
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("删除岗位职责异常！", e);
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
     * @param jobResponsibilitiesDTO
     * @date: 2019/10/19 11:12
     */
    @RequestMapping(value = "/writeJobResponsibilities", method = RequestMethod.GET)
    public void writeJobResponsibilities(HttpServletResponse response, JobResponsibilitiesDTO jobResponsibilitiesDTO){
        logger.debug("导出岗位职责信息Excel！条件,jobResponsibilitiesDTO:"+jobResponsibilitiesDTO);
        try {
            User existUser = (User) SecurityUtils.getSubject().getPrincipal();
            jobResponsibilitiesDTO.setUserId(existUser.getId());
            /*if(existUser.getZx()==true||existUser.getSdId()!=22){
                managementSystemDTO.setSdId(existUser.getSdId());
            }*/
            List<ResultBean> ingredientBasesModel = jobResponsibilitiesService.getJobResponsibilities(jobResponsibilitiesDTO);
            ExcelWriterFactroy writer = new ExcelWriterFactroy(ExcelUtil.getOutputStream(DateUtils.formatDate(new Date(),"yyyy-MM-dd-HH-mm:ss-岗位职责信息"), response), ExcelTypeEnum.XLSX);
            JobResponsibilitiesVO jobResponsibilitiesVO = new JobResponsibilitiesVO();
            if(!IsEmptyUtils.isEmpty(ingredientBasesModel)){
                for(int i=0;i<ingredientBasesModel.size();i++){
                    //sheetNo 不能从0开始
                    Sheet sheet = new Sheet(i+1, 2, jobResponsibilitiesVO.getClass());
                    sheet.setSheetName(ingredientBasesModel.get(i).getMsg());
                    sheet.setColumnWidthMap((Map)ingredientBasesModel.get(i).getObj());
                    Table table1 = new Table(1);
                    table1.setClazz(JobResponsibilitiesVO.class);
                    table1.setHead((List<List<String>>)ingredientBasesModel.get(i).getRows());
                    List<JobResponsibilitiesVO> jobResponsibilitiesVOS = (List<JobResponsibilitiesVO>)ingredientBasesModel.get(i).getResult();
                    if(!IsEmptyUtils.isEmpty(jobResponsibilitiesVOS)){
                        writer.write(jobResponsibilitiesVOS, sheet, table1);
                    }
                }
            }
            writer.finish();
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("导出岗位职责Excel异常！", e);
        }
    }
}
