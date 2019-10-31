package cn.zhixiangsingle.dao.circulation;

import cn.zhixiangsingle.entity.circulation.dto.CirculationCardDTO;

import java.util.Map;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangpingtai
 * @Package cn.zhixiangsingle.dao.circulation
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019/10/9 10:55
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
public interface CirculationCardMapper {
    Map<String,Object> findCirculationCard(CirculationCardDTO circulationCardDTO);

    Integer updateByPrimaryKey(CirculationCardDTO circulationCardDTO);

    Integer insertSelective(CirculationCardDTO circulationCardDTO);

    Integer deleteByPrimaryKey(CirculationCardDTO circulationCardDTO);
}
