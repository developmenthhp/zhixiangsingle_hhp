package cn.zhixiangsingle.dao.companyInformation;

import cn.zhixiangsingle.entity.companyInformation.dto.CompanyInformationDTO;

import java.util.Map;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangpingtai
 * @Package cn.zhixiangsingle.dao.companyInformation
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019/10/12 16:22
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
public interface CompanyInformationMapper {
    Map<String,Object> findCompanyInformation(CompanyInformationDTO companyInformationDTO);

    Integer updateByPrimaryKey(CompanyInformationDTO companyInformationDTO);

    Integer insertSelective(CompanyInformationDTO companyInformationDTO);

    Integer deleteByPrimaryKey(CompanyInformationDTO companyInformationDTO);
}
