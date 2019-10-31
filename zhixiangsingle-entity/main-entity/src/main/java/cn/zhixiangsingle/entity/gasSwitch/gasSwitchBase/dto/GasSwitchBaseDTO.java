package cn.zhixiangsingle.entity.gasSwitch.gasSwitchBase.dto;

import cn.zhixiangsingle.entity.base.common.dto.BaseDTO;
import lombok.Data;
import lombok.ToString;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangpingtai
 * @Package cn.zhixiangsingle.entity.gasSwitch.gasSwitchBase.dto
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019/10/29 18:32
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Data
@ToString
public class GasSwitchBaseDTO extends BaseDTO {
    private Integer id;
    //存放区域名称
    private String area;
    //传感器编号
    private String sensor;
    //可用状态 0开启，1关闭
    private Integer status;
    //乐橙序列号
    private String lcSequence;
}
