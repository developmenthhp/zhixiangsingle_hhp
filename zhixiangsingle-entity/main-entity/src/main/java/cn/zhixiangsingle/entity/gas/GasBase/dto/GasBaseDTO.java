package cn.zhixiangsingle.entity.gas.GasBase.dto;

import cn.zhixiangsingle.entity.base.common.dto.BaseDTO;
import lombok.Data;
import lombok.ToString;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangpingtai
 * @Package cn.zhixiangsingle.entity.gas.GasBase.dto
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019/10/28 17:01
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Data
@ToString
public class GasBaseDTO extends BaseDTO {
    //
    private Integer id;
    //区域名称
    private String gasArea;
    //煤气值
    private String gasVal;
    //空气液化气值
    private String gasYhVal;
    //传感器编号
    private String gasSensor;
    //时间 精确到分钟
    private String time;
}
