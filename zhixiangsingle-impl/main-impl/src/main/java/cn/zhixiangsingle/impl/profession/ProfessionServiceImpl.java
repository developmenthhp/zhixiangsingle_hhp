package cn.zhixiangsingle.impl.profession;

import cn.zhixiangsingle.annotation.TargetDataSource;
import cn.zhixiangsingle.dao.profession.ProfessionMapper;
import cn.zhixiangsingle.entity.base.job.Profession;
import cn.zhixiangsingle.entity.base.job.dto.ProfessionDTO;
import cn.zhixiangsingle.service.profession.ProfessionService;
import cn.zhixiangsingle.web.responsive.IStatusMessage;
import cn.zhixiangsingle.web.responsive.ResultBean;
import cn.zhixiangsingle.web.result.CommonResultMethod;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangpingtai
 * @Package cn.zhixiangsingle.impl.profession
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019/8/30 18:06
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Service(version = "1.0.0", interfaceClass = ProfessionService.class)
public class ProfessionServiceImpl implements ProfessionService {
    private static final org.slf4j.Logger logger = LoggerFactory
            .getLogger(ProfessionServiceImpl.class);
    @Autowired
    private ProfessionMapper professionMapper;
    /**
     * @version V1.0
     * @Title: zhixiangpingtai
     * @Description:
     * @author: hhp
     * @param:  * @param professionDTO
     * @param page
     * @param limit
     * @date: 2019/8/30 18:39
     */
    @Override
    @TargetDataSource(name = "default")
    public ResultBean getProfessions(ProfessionDTO professionDTO, Integer page, Integer limit) {
        ResultBean resultBean = new ResultBean();
        if (null == page) {
            page = 1;
        }
        if (null == limit) {
            limit = 10;
        }
        PageHelper.startPage(page, limit);
        professionDTO.setId(-1);
        professionDTO.setName("n");
        List<Profession> urList = professionMapper.selectByPrimaryKey(professionDTO);
        professionDTO = null;
        PageInfo<Profession> pageInfo = new PageInfo<>(urList);
        resultBean.setTotal(Long.valueOf(pageInfo.getTotal()).intValue());
        resultBean.setSuccess(true);
        resultBean.setResult(urList);
        resultBean.setResultCode(IStatusMessage.SystemStatus.SUCCESS.getCode());
        resultBean.setMsg(IStatusMessage.SystemStatus.SUCCESS.getMessage());
        return resultBean;
    }
    /**
     * @version V1.0
     * @Title: zhixiangpingtai
     * @Description:
     * @author: hhp
     * @param:  * @param profession
     * @date: 2019/10/14 19:04
     */
    @Override
    @TargetDataSource(name = "default")
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, timeout = 30000, rollbackFor = {
            RuntimeException.class, Exception.class })
    public ResultBean setProfessions(Profession profession) {
        ResultBean resultBean = new ResultBean();
        String whichMsg = "";
        if (profession.getId() != null) {
            int updateCount = this.professionMapper.updateByPrimaryKeySelective(profession);
            if(updateCount!=1){
                resultBean.setSuccess(false);
                resultBean.setResultCode(IStatusMessage.SystemStatus.ERROR.getCode());
                resultBean.setMsg(IStatusMessage.SystemStatus.ERROR.getMessage());
                return resultBean;
            }else{
                whichMsg = "更新工种信息成功";
            }
        } else {
            int insertCount = this.professionMapper.insertSelective(profession);
            if(insertCount!=1){
                resultBean.setSuccess(false);
                resultBean.setResultCode(IStatusMessage.SystemStatus.ERROR.getCode());
                resultBean.setMsg(IStatusMessage.SystemStatus.ERROR.getMessage());
                return resultBean;
            }else{
                whichMsg = "创建工种成功";
            }
        }
        resultBean.setSuccess(true);
        resultBean.setResultCode(IStatusMessage.SystemStatus.SUCCESS.getCode());
        resultBean.setMsg(whichMsg);
        return resultBean;
    }
    /**
     * @version V1.0
     * @Title: zhixiangpingtai
     * @Description:
     * @author: hhp
     * @param:  * @param id
     * @date: 2019/10/14 19:04
     */
    @Override
    @TargetDataSource(name = "default")
    public ResultBean delProfession(Integer id) {
        int delCount = this.professionMapper.deleteByPrimaryKey(id);
        return CommonResultMethod.getUpdOrInsertResultBean(delCount);
    }
}
