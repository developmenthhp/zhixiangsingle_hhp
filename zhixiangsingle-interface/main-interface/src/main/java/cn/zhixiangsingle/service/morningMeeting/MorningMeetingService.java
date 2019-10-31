package cn.zhixiangsingle.service.morningMeeting;

import cn.zhixiangsingle.entity.morningMeeting.dto.MorningMeetingDTO;
import cn.zhixiangsingle.web.responsive.ResultBean;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangpingtai
 * @Package cn.zhixiangsingle.service.morningMeeting
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019/10/18 10:12
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
public interface MorningMeetingService {
    ResultBean findMorningMeetingList(MorningMeetingDTO morningMeetingDTO) throws Exception;

    ResultBean updateMorningMeeting(MorningMeetingDTO morningMeetingDTO) throws Exception;

    ResultBean addMorningMeeting(MorningMeetingDTO morningMeetingDTO) throws Exception;

    ResultBean findUpdMorningMeeting(MorningMeetingDTO morningMeetingDTO) throws Exception;

    ResultBean delMorningMeeting(MorningMeetingDTO morningMeetingDTO) throws Exception;
}
