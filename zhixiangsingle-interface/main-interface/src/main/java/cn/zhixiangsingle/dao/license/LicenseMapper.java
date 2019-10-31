package cn.zhixiangsingle.dao.license;

import cn.zhixiangsingle.entity.license.dto.LicenseDTO;

import java.util.Map;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangpingtai
 * @Package cn.zhixiangsingle.dao.license
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019/10/9 10:49
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
public interface LicenseMapper {
    Map<String,Object> findLicense(LicenseDTO licenseDTO);

    Integer updateByPrimaryKey(LicenseDTO licenseDTO);

    Integer insertSelective(LicenseDTO licenseDTO);

    Integer deleteByPrimaryKey(LicenseDTO licenseDTO);
}
