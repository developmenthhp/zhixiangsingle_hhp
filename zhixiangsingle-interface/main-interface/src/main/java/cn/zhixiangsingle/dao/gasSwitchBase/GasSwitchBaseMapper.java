package cn.zhixiangsingle.dao.gasSwitchBase;

import cn.zhixiangsingle.entity.gasSwitch.gasSwitchBase.dto.GasSwitchBaseDTO;

import java.util.List;
import java.util.Map;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangpingtai
 * @Package cn.zhixiangsingle.dao.gasSwitchBase
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019/10/29 18:29
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
public interface GasSwitchBaseMapper {
    List<Map<String,Object>> findGasSwitchBaseList(GasSwitchBaseDTO gasSwitchBaseDTO);

    Integer updateByPrimaryKey(GasSwitchBaseDTO updGasSwitchBase);
}
