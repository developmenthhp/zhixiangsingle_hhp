package cn.zhixiangsingle.entity.circulation.dto;

import cn.zhixiangsingle.entity.base.common.dto.BaseDTO;
import lombok.Data;
import lombok.ToString;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangpingtai
 * @Package cn.zhixiangsingle.entity.circulation.dto
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019/10/9 11:19
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Data
@ToString
public class CirculationCardDTO extends BaseDTO {
    private Integer id;
    //单位名称
    private String name;
    //法定代表人
    private String representative;
    //有效期限
    private String validTime;
    //预警日期
    private String warningDate;
    //编号
    private String serialNumber;
    //用户的id
    private Integer accountId;
    //餐饮流通证照片
    private String circulationCardImg;
}
