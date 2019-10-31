package cn.zhixiangsingle.entity.morningMeeting.dto;

import cn.zhixiangsingle.entity.base.common.dto.BaseDTO;
import lombok.Data;
import lombok.ToString;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangpingtai
 * @Package cn.zhixiangsingle.entity.morningMeeting.dto
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019/10/18 10:15
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Data
@ToString
public class MorningMeetingDTO extends BaseDTO {
    private Integer id;
    //晨会时间
    private String meetingTime;
    //地址
    private String address;
    //抓拍图片
    private String snapPicture;
}
