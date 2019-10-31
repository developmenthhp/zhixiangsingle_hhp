package cn.zhixiangsingle.dao.displayArea;

import cn.zhixiangsingle.entity.displayArea.dto.DisplayAreaDTO;

import java.util.List;
import java.util.Map;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangpingtai
 * @Package cn.zhixiangsingle.dao.displayArea
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019/10/15 17:09
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
public interface DisplayAreaMapper {
    List<Map<String,Object>> findDisplayAreaList(DisplayAreaDTO displayAreaDTO);

    Integer updateByPrimaryKey(DisplayAreaDTO displayAreaDTO);

    Integer insertSelective(DisplayAreaDTO displayAreaDTO);

    Map<String,Object> findDisplayArea(DisplayAreaDTO displayAreaDTO);

    Integer deleteByPrimaryKey(DisplayAreaDTO displayAreaDTO);
}
