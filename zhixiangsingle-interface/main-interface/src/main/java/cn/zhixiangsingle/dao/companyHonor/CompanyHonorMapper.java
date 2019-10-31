package cn.zhixiangsingle.dao.companyHonor;

import cn.zhixiangsingle.entity.companyHonor.dto.CompanyHonorDTO;

import java.util.Map;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangpingtai
 * @Package cn.zhixiangsingle.dao.companyHonor
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019/10/14 18:43
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
public interface CompanyHonorMapper {
    Integer deleteByPrimaryKey(CompanyHonorDTO companyHonorDTO);

    Map<String,Object> findCompanyHonor(CompanyHonorDTO companyHonorDTO);

    Integer insertSelective(CompanyHonorDTO companyHonorDTO);

    Integer updateByPrimaryKey(CompanyHonorDTO companyHonorDTO);
}
