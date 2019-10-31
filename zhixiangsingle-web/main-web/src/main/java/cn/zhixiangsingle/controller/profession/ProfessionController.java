package cn.zhixiangsingle.controller.profession;

import cn.zhixiangsingle.entity.base.IsEmptyUtils;
import cn.zhixiangsingle.entity.base.job.Profession;
import cn.zhixiangsingle.entity.base.job.dto.ProfessionDTO;
import cn.zhixiangsingle.entity.base.user.User;
import cn.zhixiangsingle.service.profession.ProfessionService;
import cn.zhixiangsingle.web.responsive.IStatusMessage;
import cn.zhixiangsingle.web.responsive.ResultBean;
import cn.zhixiangsingle.web.responsive.WebMassage;
import com.alibaba.dubbo.config.annotation.Reference;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangpingtai
 * @Package cn.zhixiangsingle.controller.job
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019/8/30 17:58
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Controller
@RequestMapping("/profession")
public class ProfessionController {
    private static final Logger logger = LoggerFactory
            .getLogger(ProfessionController.class);
    @Reference(version = "1.0.0")
    private ProfessionService professionService;
    /**
     * @version V1.0
     * @Title: zhixiangpingtai
     * @Description:
     * @author: hhp
     * @param:  * @param
     * @date: 2019/8/30 18:21
     */
    @RequestMapping("/professionList")
    public String toUserList() {
        return "/auth/professionList";
    }

    @RequestMapping(value = "/getProfessions", method = RequestMethod.POST)
    @ResponseBody
    @RequiresPermissions(value = "usermanage")
    public ResultBean getProfessions(@RequestParam("page") Integer page,
                               @RequestParam("limit") Integer limit, ProfessionDTO professionDTO) {
        logger.debug("分页查询工种列表！搜索条件：professionDTO：" + professionDTO + ",page:" + page
                + ",每页记录数量limit:" + limit);
        User existUser= (User) SecurityUtils.getSubject().getPrincipal();
        ResultBean resultBean = new ResultBean();
        try {
            if(existUser.getZx()==true||existUser.getSdId()!=22){
                professionDTO.setSdId(existUser.getSdId());
            }else{
                professionDTO.setSdId(-1);
            }
            resultBean = professionService.getProfessions(professionDTO, page, limit);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("查询工种列表异常！", e);
            resultBean.setSuccess(false);
            resultBean.setResultCode(IStatusMessage.SystemStatus.ERROR.getCode());
            resultBean.setMsg(IStatusMessage.SystemStatus.SUCCESS.getMessage());
        }
        return resultBean;
    }
    @PostMapping(value = "/setProfessions")
    @ResponseBody
    public ResultBean setProfessions(Profession profession) {
        logger.debug("设置工种[新增或更新]！profession:" + profession);
        ResultBean rb = new ResultBean();
        try {
            if(IsEmptyUtils.isEmpty(profession.getName())){
                logger.debug("设置工种[新增或更新]，结果=请您填写工种信息");
                rb.setSuccess(false);
                rb.setResultCode(IStatusMessage.SystemStatus.PARAMETERS_LOSE.getCode());
                rb.setMsg(WebMassage.PARAMETERS_REQUIRED_LOSE);
                return rb;
            }
            User existUser = (User) SecurityUtils.getSubject().getPrincipal();
            logger.info("设置工种[新增或更新]！profession=" + profession);
            if(!IsEmptyUtils.isEmpty(profession.getSdId())){
                if(existUser.getZx()==true||existUser.getSdId()!=22){
                    profession.setSdId(existUser.getSdId());
                }
            }else{
                profession.setSdId(existUser.getSdId());
            }
            rb =  professionService.setProfessions(profession);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("设置工种[新增或更新]异常！", e);
            rb.setSuccess(false);
            rb.setResultCode(IStatusMessage.SystemStatus.ERROR.getCode());
            rb.setMsg(IStatusMessage.SystemStatus.ERROR.getMessage());
        }
        return rb;
    }

    @RequestMapping(value = "/delProfession", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean delUser(@RequestParam("id") Integer id) {
        logger.debug("删除工种！id:" + id);
        ResultBean resultBean = new ResultBean();
        try {
            if (null == id) {
                logger.debug("删除工种，结果=请求参数有误，请您稍后再试");
                resultBean.setSuccess(false);
                resultBean.setResultCode(IStatusMessage.SystemStatus.PARAMETERS_LOSE.getCode());
                resultBean.setMsg(IStatusMessage.SystemStatus.PARAMETERS_LOSE.getMessage());
                return resultBean;
            }
            User existUser = (User) SecurityUtils.getSubject().getPrincipal();
            resultBean = professionService.delProfession(id);
            logger.info("删除工种:"  + "！profession=" + id + "，操作用户id:"
                    + existUser.getId());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("删除工种异常！", e);
            resultBean.setSuccess(false);
            resultBean.setResultCode(IStatusMessage.SystemStatus.ERROR.getCode());
            resultBean.setMsg(IStatusMessage.SystemStatus.ERROR.getMessage());
        }
        return resultBean;
    }
}
