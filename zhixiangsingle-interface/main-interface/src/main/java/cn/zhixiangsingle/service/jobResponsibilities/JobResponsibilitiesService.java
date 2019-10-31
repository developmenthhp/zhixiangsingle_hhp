package cn.zhixiangsingle.service.jobResponsibilities;

import cn.zhixiangsingle.entity.jobResponsibilities.dto.JobResponsibilitiesDTO;
import cn.zhixiangsingle.web.responsive.ResultBean;

import java.util.List;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangpingtai
 * @Package cn.zhixiangsingle.service.jobResponsibilities
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019/10/17 10:50
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
public interface JobResponsibilitiesService {
    ResultBean findJobResponsibilitiesList(JobResponsibilitiesDTO jobResponsibilitiesDTO) throws Exception;

    ResultBean updateJobResponsibilities(JobResponsibilitiesDTO jobResponsibilitiesDTO) throws Exception;

    ResultBean addJobResponsibilities(JobResponsibilitiesDTO jobResponsibilitiesDTO) throws Exception;

    ResultBean findUpdJobResponsibilities(JobResponsibilitiesDTO jobResponsibilitiesDTO) throws Exception;

    ResultBean delJobResponsibilities(JobResponsibilitiesDTO jobResponsibilitiesDTO) throws Exception;

    List<ResultBean> getJobResponsibilities(JobResponsibilitiesDTO jobResponsibilitiesDTO) throws Exception;
}
