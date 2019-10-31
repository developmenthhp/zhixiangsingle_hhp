package cn.zhixiangsingle.entity.slippery.slipperyAlert.dto;

import cn.zhixiangsingle.entity.base.common.dto.BaseDTO;
import lombok.Data;
import lombok.ToString;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangpingtai
 * @Package cn.zhixiangsingle.entity.slippery.slipperyAlert.dto
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019/10/29 17:49
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Data
@ToString
public class SlipperyAlertDTO extends BaseDTO {
    //
    private Integer id;
    //存放区域名称
    private String ratplateArea;
    //传感器编号
    private String ratplateSensor;
    //1.警报中  2.已处理  3.已作废
    private String ratplateStatus;
    //描述
    private String ratplateDescription;
    //实际积水水位
    private String ratplateRultionsTime;
    //警报时间
    private String alertTime;
}
