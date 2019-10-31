package cn.zhixiangsingle.entity.company.dto;

import cn.zhixiangsingle.entity.business.dto.BusinessDTO;
import cn.zhixiangsingle.entity.circulation.dto.CirculationCardDTO;
import cn.zhixiangsingle.entity.license.dto.LicenseDTO;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.Map;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangpingtai
 * @Package cn.zhixiangsingle.entity.company.dto
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019/10/9 15:53
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Data
@ToString
public class Company implements Serializable {
    private Map<String,Object> businessDTO;
    private Map<String,Object> licenseDTO;
    private Map<String,Object> circulationCardDTO;
}
