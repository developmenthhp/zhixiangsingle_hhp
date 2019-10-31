package cn.zhixiangsingle.entity.ratplate.ratplateBase.dto;

import cn.zhixiangsingle.entity.base.common.dto.BaseDTO;
import lombok.Data;
import lombok.ToString;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangpingtai
 * @Package cn.zhixiangsingle.entity.ratplate.ratplateBase.dto
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019/10/28 18:24
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Data
@ToString
public class RatplateBaseDTO extends BaseDTO {
    //
    private Integer id;
    //存放区域名称
    private String ratplateArea;
    //传感器编号
    private String ratplateSensor;
    //可用状态 1 禁用  2.正常 3.删除
    private String status;
    //最小限距离,超过距离将报警
    private String ratplateRultionsTime;
}
