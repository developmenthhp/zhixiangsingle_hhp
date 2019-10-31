package cn.zhixiangsingle.entity.fromWall.fromWallBase.dto;

import cn.zhixiangsingle.entity.base.common.dto.BaseDTO;
import lombok.Data;
import lombok.ToString;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangpingtai
 * @Package cn.zhixiangsingle.entity.fromWall.fromWallBase.dto
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019/10/29 16:51
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Data
@ToString
public class FromWallBaseDTO extends BaseDTO {
    //
    private Integer id;
    //区域名称
    private String fromwallArea;
    //传感器编hoa
    private String fromwallSensor;
    //最小限距离,超过距离将报警
    private String maxImumDistance;
    //可用状态 1 禁用  2.正常 3.删除
    private String status;
}
