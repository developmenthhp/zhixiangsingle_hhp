package cn.zhixiangsingle.service.business;

import cn.zhixiangsingle.entity.business.dto.BusinessDTO;
import cn.zhixiangsingle.web.responsive.ResultBean;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangpingtai
 * @Package cn.zhixiangsingle.service.business
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019/10/9 9:21
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
public interface BusinessService {
    ResultBean updateBusiness(BusinessDTO businessDTO) throws Exception;

    ResultBean addBusiness(BusinessDTO businessDTO) throws Exception;

    ResultBean delBusiness(BusinessDTO businessDTO) throws Exception;

    ResultBean findBusinessList(BusinessDTO businessDTO) throws Exception;

    ResultBean findUpdBusiness(BusinessDTO businessDTO) throws Exception;
}
