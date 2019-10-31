package cn.zhixiangsingle.entity.gas.gasAlarm.dto;

import cn.zhixiangsingle.entity.base.common.dto.BaseDTO;
import lombok.Data;
import lombok.ToString;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangpingtai
 * @Package cn.zhixiangsingle.entity.gas.gasAlarm.dto
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019/10/28 17:01
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Data
@ToString
public class GasAlarmDTO extends BaseDTO {
    //编号
    private Integer id;
    //区域
    private String alarmArea;
    //时间
    private String alarmTime;
    //描述信息
    private String descriptionInfo;
    //1报警中 2已处理 3作废
    private String handleStatus;
}
