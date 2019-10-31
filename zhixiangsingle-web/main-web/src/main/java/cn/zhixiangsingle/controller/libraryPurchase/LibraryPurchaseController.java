package cn.zhixiangsingle.controller.libraryPurchase;

import cn.zhixiangsingle.date.DateUtils;
import cn.zhixiangsingle.entity.base.IsEmptyUtils;
import cn.zhixiangsingle.entity.base.site.vo.SiteVO;
import cn.zhixiangsingle.entity.base.user.User;
import cn.zhixiangsingle.entity.libraryPurchase.dto.LibraryPurchaseDTO;
import cn.zhixiangsingle.entity.libraryPurchase.vo.LibraryPurchaseVO;
import cn.zhixiangsingle.excel.ExcelUtil;
import cn.zhixiangsingle.excel.ExcelWriterFactroy;
import cn.zhixiangsingle.service.libraryPurchase.LibraryPurchaseService;
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
 * @Package cn.zhixiangsingle.controller.libraryPurchase
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019/10/24 10:17
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Controller
@RequestMapping("/libraryPurchase")
public class LibraryPurchaseController {
    private static final Logger logger = LoggerFactory
            .getLogger(LibraryPurchaseController.class);
    @Reference(version = "1.0.0")
    private SiteService siteService;
    @Reference(version = "1.0.0")
    private LibraryPurchaseService libraryPurchaseService;
    /**
     * @version V1.0
     * @Title: zhixiangpingtai
     * @Description:
     * @author: hhp
     * @param:  * @param
     * @date: 2019/10/24 10:37
     */
    @RequestMapping(value = "/getListPage", method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView getListPage() {
        logger.debug("跳转食材采购信息列表！");
        ModelAndView mav = new ModelAndView("/cgcc/sccg/sccggl");
        try {
            mav.addObject("msg", "ok");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("跳转食材采购列表异常！", e);
        }
        return mav;
    }
    /**
     * @version V1.0
     * @Title: zhixiangpingtai
     * @Description:
     * @author: hhp
     * @param:  * @param
     * @date: 2019/10/26 11:18
     */
    @RequestMapping(value = "/getCreatePage", method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView getCreatePage() {
        logger.debug("跳转添加食材采购列表！");
        ModelAndView mav = new ModelAndView("/cgcc/sccg/cjsccg");
        try {
            mav.addObject("msg", "ok");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("跳转添加食材采购异常！", e);
        }
        return mav;
    }
    /**
     * @version V1.0
     * @Title: zhixiangpingtai
     * @Description:
     * @author: hhp
     * @param:  * @param libraryPurchaseDTO
     * @date: 2019/10/24 10:40
     */
    @PostMapping(value = "/getLibraryPurchaseList")
    @ResponseBody
    public ResultBean getLibraryPurchaseList(LibraryPurchaseDTO libraryPurchaseDTO) {
        logger.debug("获取食材采购列表！ libraryPurchaseDTO = "+libraryPurchaseDTO);
        ResultBean resultBean = new ResultBean();
        try {
            User existUser = (User) SecurityUtils.getSubject().getPrincipal();
            libraryPurchaseDTO.setUserId(existUser.getId());
            resultBean = libraryPurchaseService.findLibraryPurchaseList(libraryPurchaseDTO);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("获取食材采购列表异常！", e);
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
     * @param:  * @param libraryPurchaseDTO
     * @date: 2019/10/24 14:52
     */
    @RequestMapping(value = "/setLibraryPurchase", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean setLibraryPurchase(LibraryPurchaseDTO libraryPurchaseDTO) {
        logger.debug("添加或修改食材采购信息 --libraryPurchaseDTO-" + libraryPurchaseDTO);
        ResultBean resultBean = new ResultBean();
        try {
            User existUser = (User) SecurityUtils.getSubject().getPrincipal();
            if (!IsEmptyUtils.isEmpty(libraryPurchaseDTO)) {
                libraryPurchaseDTO.setUserId(existUser.getId());
                if(!IsEmptyUtils.isEmpty(libraryPurchaseDTO.getId())){
                    resultBean = libraryPurchaseService.updateLibraryPurchase(libraryPurchaseDTO);
                }else{
                    resultBean = siteService.getUserSites(existUser.getId());
                    if(!IsEmptyUtils.isEmpty(resultBean.getResult())){
                        List<SiteVO> siteVOList = (List<SiteVO>)resultBean.getResult();
                        if(siteVOList.size()>1){
                            resultBean = libraryPurchaseService.addLibraryPurchase(libraryPurchaseDTO);
                        }else{
                            libraryPurchaseDTO.setSdId(existUser.getSdId());
                            libraryPurchaseDTO.setSdIds("");
                            resultBean = libraryPurchaseService.addLibraryPurchase(libraryPurchaseDTO);
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
            logger.error("添加或修改食材采购信息异常！", e);
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
     * @param:  * @param libraryPurchaseDTO
     * @date: 2019/10/24 14:56
     */
    @PostMapping(value = "/delLibraryPurchase")
    @ResponseBody
    public ResultBean delLibraryPurchase(LibraryPurchaseDTO libraryPurchaseDTO){
        logger.debug("删除食材采购信息 --libraryPurchaseDTO-" + libraryPurchaseDTO);
        ResultBean resultBean = new ResultBean();
        try {
            User existUser = (User) SecurityUtils.getSubject().getPrincipal();
            libraryPurchaseDTO.setUserId(existUser.getId());
            if (!IsEmptyUtils.isEmpty(libraryPurchaseDTO)) {
                /*if(IsEmptyUtils.isEmpty(ingredientBaseDTO.getSdId())){
                    ingredientBaseDTO
                }*/
                resultBean = libraryPurchaseService.delLibraryPurchase(libraryPurchaseDTO);
            }else{
                resultBean.setSuccess(false);
                resultBean.setResultCode(IStatusMessage.SystemStatus.PARAMETERS_LOSE.getCode());
                resultBean.setMsg(IStatusMessage.SystemStatus.PARAMETERS_LOSE.getMessage());
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("删除食材采购异常！", e);
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
     * @param ingredientBaseDTO
     * @date: 2019/10/25 15:55
     */
    @RequestMapping(value = "/writeLibraryPurchases", method = RequestMethod.GET)
    public void writeLibraryPurchases(HttpServletResponse response,LibraryPurchaseDTO libraryPurchaseDTO){
        logger.debug("导出采购单信息Excel！条件:"+libraryPurchaseDTO);
        try {
            User existUser = (User) SecurityUtils.getSubject().getPrincipal();
            libraryPurchaseDTO.setUserId(existUser.getId());
            List<ResultBean> ingredientBasesModel = libraryPurchaseService.getLibraryPurchases(libraryPurchaseDTO);
            ExcelWriterFactroy writer = new ExcelWriterFactroy(ExcelUtil.getOutputStream(DateUtils.formatDate(new Date(),"yyyy-MM-dd-HH-mm:ss-采购单"), response), ExcelTypeEnum.XLSX);
            LibraryPurchaseVO libraryPurchaseVO = new LibraryPurchaseVO();
            if(!IsEmptyUtils.isEmpty(ingredientBasesModel)){
                for(int i=0;i<ingredientBasesModel.size();i++){
                    //sheetNo 不能从0开始
                    Sheet sheet = new Sheet(i+1, 2, libraryPurchaseVO.getClass());
                    sheet.setSheetName(ingredientBasesModel.get(i).getMsg());
                    sheet.setColumnWidthMap((Map)ingredientBasesModel.get(i).getObj());
                    Table table1 = new Table(1);
                    table1.setClazz(LibraryPurchaseVO.class);
                    table1.setHead((List<List<String>>)ingredientBasesModel.get(i).getRows());
                    List<LibraryPurchaseVO> ingredientBaseVOS = (List<LibraryPurchaseVO>)ingredientBasesModel.get(i).getResult();
                    if(!IsEmptyUtils.isEmpty(ingredientBaseVOS)){
                        writer.write(ingredientBaseVOS, sheet, table1);
                    }
                }
            }
            writer.finish();
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("导出采购单Excel异常！", e);
        }
    }
}
