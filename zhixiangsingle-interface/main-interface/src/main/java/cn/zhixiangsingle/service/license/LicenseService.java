package cn.zhixiangsingle.service.license;

import cn.zhixiangsingle.entity.license.dto.LicenseDTO;
import cn.zhixiangsingle.web.responsive.ResultBean;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangpingtai
 * @Package cn.zhixiangsingle.service.license
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019/10/9 10:49
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
public interface LicenseService {
    ResultBean updateLicense(LicenseDTO licenseDTO) throws Exception;

    ResultBean addLicense(LicenseDTO licenseDTO) throws Exception;

    ResultBean delLicense(LicenseDTO licenseDTO) throws Exception;

    ResultBean findLicense(LicenseDTO licenseDTO) throws Exception;
}
