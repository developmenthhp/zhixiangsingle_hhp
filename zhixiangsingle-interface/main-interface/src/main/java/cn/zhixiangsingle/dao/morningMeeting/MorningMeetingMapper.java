package cn.zhixiangsingle.dao.morningMeeting;

import cn.zhixiangsingle.entity.morningMeeting.dto.MorningMeetingDTO;

import java.util.List;
import java.util.Map;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangpingtai
 * @Package cn.zhixiangsingle.dao.morningMeeting
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019/10/18 10:12
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
public interface MorningMeetingMapper {
    List<Map<String,Object>> findMorningMeetingList(MorningMeetingDTO morningMeetingDTO);

    Integer findMorningMeetingTotal(MorningMeetingDTO morningMeetingDTO);

    Integer updateByPrimaryKey(MorningMeetingDTO morningMeetingDTO);

    Integer insertSelective(MorningMeetingDTO morningMeetingDTO);

    Map<String,Object> findMorningMeeting(MorningMeetingDTO morningMeetingDTO);

    Integer deleteByPrimaryKey(MorningMeetingDTO morningMeetingDTO);
}
