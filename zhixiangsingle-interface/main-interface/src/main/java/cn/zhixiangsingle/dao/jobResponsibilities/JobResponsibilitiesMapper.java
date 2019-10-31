package cn.zhixiangsingle.dao.jobResponsibilities;

import cn.zhixiangsingle.entity.jobResponsibilities.dto.JobResponsibilitiesDTO;
import cn.zhixiangsingle.entity.jobResponsibilities.vo.JobResponsibilitiesVO;

import java.util.List;
import java.util.Map;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangpingtai
 * @Package cn.zhixiangsingle.dao.jobResponsibilities
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019/10/17 10:50
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
public interface JobResponsibilitiesMapper {
    Integer findJobResponsibilitiesTotal(JobResponsibilitiesDTO jobResponsibilitiesDTO);

    List<Map<String,Object>> findJobResponsibilitiesList(JobResponsibilitiesDTO jobResponsibilitiesDTO);

    Integer updateByPrimaryKey(JobResponsibilitiesDTO jobResponsibilitiesDTO);

    Integer insertSelective(JobResponsibilitiesDTO jobResponsibilitiesDTO);

    Map<String,Object> findJobResponsibilities(JobResponsibilitiesDTO jobResponsibilitiesDTO);

    Integer deleteByPrimaryKey(JobResponsibilitiesDTO jobResponsibilitiesDTO);

    List<JobResponsibilitiesVO> findAllJobResponsibilities(JobResponsibilitiesDTO jobResponsibilitiesDTO);
}
