package cn.zhixiangsingle.dao.liveShow;

import cn.zhixiangsingle.entity.liveShow.dto.LiveShowDTO;

import java.util.List;
import java.util.Map;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangpingtai
 * @Package cn.zhixiangsingle.dao.liveShow
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019/10/15 11:59
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
public interface LiveShowMapper {
    Integer findLiveShowTotal(LiveShowDTO liveShowDTO);

    List<Map<String,Object>> findLiveShowList(LiveShowDTO liveShowDTO);

    Map<String,Object> findLiveShow(LiveShowDTO liveShowDTO);

    Integer deleteByPrimaryKey(LiveShowDTO liveShowDTO);

    Integer updateByPrimaryKey(LiveShowDTO liveShowDTO);

    Integer insertSelective(LiveShowDTO liveShowDTO);
}
