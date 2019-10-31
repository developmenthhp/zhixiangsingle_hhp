package cn.zhixiangsingle.dao.ratplateAlert;

import cn.zhixiangsingle.entity.ratplate.ratplateAlert.dto.RatplateAlertDTO;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangpingtai
 * @Package cn.zhixiangsingle.dao.ratplateAlert
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019/10/28 18:28
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
public interface RatplateAlertMapper {
    Integer insertSelective(RatplateAlertDTO ratplateAlertDTO);
}
