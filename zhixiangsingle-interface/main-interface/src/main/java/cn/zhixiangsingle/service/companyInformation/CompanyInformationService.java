package cn.zhixiangsingle.service.companyInformation;

import cn.zhixiangsingle.entity.companyInformation.dto.CompanyInformationDTO;
import cn.zhixiangsingle.web.responsive.ResultBean;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangpingtai
 * @Package cn.zhixiangsingle.service.companyInformation
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019/10/12 16:21
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
public interface CompanyInformationService {
    ResultBean findCompanyInformation(CompanyInformationDTO companyInformationDTO) throws Exception;

    ResultBean updateCompanyInformation(CompanyInformationDTO companyInformationDTO) throws Exception;

    ResultBean addCompanyInformation(CompanyInformationDTO companyInformationDTO) throws Exception;

    ResultBean findUpdCompanyInformation(CompanyInformationDTO companyInformationDTO) throws Exception;

    ResultBean delCompanyInformation(CompanyInformationDTO companyInformationDTO) throws Exception;
}
