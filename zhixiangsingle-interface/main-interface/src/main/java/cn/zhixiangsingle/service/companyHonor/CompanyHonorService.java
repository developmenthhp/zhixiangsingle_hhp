package cn.zhixiangsingle.service.companyHonor;

import cn.zhixiangsingle.entity.companyHonor.dto.CompanyHonorDTO;
import cn.zhixiangsingle.web.responsive.ResultBean;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangpingtai
 * @Package cn.zhixiangsingle.service.companyHonor
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019/10/14 18:44
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
public interface CompanyHonorService {
    ResultBean delCompanyHonor(CompanyHonorDTO companyHonorDTO) throws Exception;

    ResultBean findUpdCompanyHonor(CompanyHonorDTO companyHonorDTO) throws Exception;

    ResultBean addCompanyHonor(CompanyHonorDTO companyHonorDTO) throws Exception;

    ResultBean updateCompanyHonor(CompanyHonorDTO companyHonorDTO) throws Exception;

    ResultBean findCompanyHonor(CompanyHonorDTO companyHonorDTO) throws Exception;
}
