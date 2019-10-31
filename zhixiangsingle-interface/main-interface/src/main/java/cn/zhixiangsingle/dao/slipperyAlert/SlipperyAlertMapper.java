package cn.zhixiangsingle.dao.slipperyAlert;

import cn.zhixiangsingle.entity.slippery.slipperyAlert.dto.SlipperyAlertDTO;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangpingtai
 * @Package cn.zhixiangsingle.dao.slipperyAlert
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019/10/29 17:46
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
public interface SlipperyAlertMapper {
    Integer insertSelective(SlipperyAlertDTO slipperyAlertDTO);
}
