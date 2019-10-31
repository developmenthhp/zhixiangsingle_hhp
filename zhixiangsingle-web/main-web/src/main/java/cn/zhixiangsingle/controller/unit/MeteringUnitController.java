package cn.zhixiangsingle.controller.unit;

import cn.zhixiangsingle.entity.base.IsEmptyUtils;
import cn.zhixiangsingle.entity.base.user.User;
import cn.zhixiangsingle.entity.unit.dto.MeteringUnitDTO;
import cn.zhixiangsingle.service.site.SiteService;
import cn.zhixiangsingle.service.unit.MeteringUnitService;
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
 * @Package cn.zhixiangsingle.controller.unit
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019/10/4 17:17
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Controller
@RequestMapping("/meteringUnit")
public class MeteringUnitController {
    private static final Logger logger = LoggerFactory
            .getLogger(MeteringUnitController.class);
    @Reference(version = "1.0.0")
    private SiteService siteService;
    @Reference(version = "1.0.0")
    private MeteringUnitService meteringUnitService;
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
        logger.debug("跳转计量单位信息列表！");
        ModelAndView mav = new ModelAndView("/cgcc/jldwgl/jldwgl");
        try {
            mav.addObject("msg", "ok");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("跳转计量单位信息异常！", e);
        }
        return mav;
    }
    /**
     * @version V1.0
     * @Title: zhixiangpingtai
     * @Description:
     * @author: hhp
     * @param:  * @param meteringUnitDTO
     * @date: 2019/9/27 17:39
     */
    @RequestMapping(value = "/setMeteringUnit", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean setMeteringUnit(MeteringUnitDTO meteringUnitDTO) {
        logger.debug("添加或修改计量单位信息 --meteringUnitDTO-" + meteringUnitDTO);
        ResultBean resultBean = new ResultBean();
        try {
            User existUser = (User) SecurityUtils.getSubject().getPrincipal();
            if (!IsEmptyUtils.isEmpty(meteringUnitDTO)) {
                meteringUnitDTO.setUserId(existUser.getId());
                if(!IsEmptyUtils.isEmpty(meteringUnitDTO.getId())){
                    resultBean = meteringUnitService.updateMeteringUnit(meteringUnitDTO);
                }else{
                    resultBean = meteringUnitService.addMeteringUnit(meteringUnitDTO);
                }
            }else{
                resultBean.setSuccess(false);
                resultBean.setResultCode(IStatusMessage.SystemStatus.PARAMETERS_LOSE.getCode());
                resultBean.setMsg(IStatusMessage.SystemStatus.PARAMETERS_LOSE.getMessage());
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("添加或修改计量单位信息异常！", e);
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
     * @param:  * @param meteringUnitDTO
     * @date: 2019/9/27 17:40
     */
    @PostMapping(value = "/getMeteringUnitList")
    @ResponseBody
    public ResultBean getMeteringUnitList(MeteringUnitDTO meteringUnitDTO) {
        logger.debug("获取计量单位信息列表！ meteringUnitDTO = "+meteringUnitDTO);
        ResultBean resultBean = new ResultBean();
        try {
            User existUser = (User) SecurityUtils.getSubject().getPrincipal();
            meteringUnitDTO.setUserId(existUser.getId());
            if(existUser.getZx()==true||existUser.getSdId()!=22){
                meteringUnitDTO.setSdId(existUser.getSdId());
            }
            resultBean = meteringUnitService.findMeteringUnitList(meteringUnitDTO);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("获取计量单位信息列表异常！", e);
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
     * @param:  * @param meteringUnitDTO
     * @date: 2019/9/27 17:39
     */
    @PostMapping(value = "/getAll")
    @ResponseBody
    public ResultBean getAll(MeteringUnitDTO meteringUnitDTO) {
        logger.debug("获取所有计量单位信息列表！ meteringUnitDTO = "+meteringUnitDTO);
        ResultBean resultBean = new ResultBean();
        try {
            User existUser = (User) SecurityUtils.getSubject().getPrincipal();
            meteringUnitDTO.setUserId(existUser.getId());
            if(existUser.getZx()==true||existUser.getSdId()!=22){
                meteringUnitDTO.setSdId(existUser.getSdId());
            }
            resultBean = meteringUnitService.findAll(meteringUnitDTO);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("获取所有计量单位信息列表异常！", e);
            resultBean.setSuccess(false);
            resultBean.setResultCode(IStatusMessage.SystemStatus.ERROR.getCode());
            resultBean.setMsg(IStatusMessage.SystemStatus.ERROR.getMessage());
        }
        return resultBean;
    }
}
