package cn.zhixiangsingle.entity.iwareCount.dto;

import cn.zhixiangsingle.entity.base.common.dto.BaseDTO;
import lombok.Data;
import lombok.ToString;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangpingtai
 * @Package cn.zhixiangsingle.entity.iwareCount.dto
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019/10/25 12:51
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Data
@ToString
public class IwareCountDTO extends BaseDTO {
    private Integer id;
    //采购记录id
    private String wareIds;
    //制单时间
    private String zdTime;
    //采购货品种类/条数
    private String wareCount;
    //报表制单人姓名,直接填写用户姓名
    private String reportPersion;
    //报表制单人id
    private Integer reportPersionId;
    //报表状态 1.审签中  2.已作废  3.审签完成
    private String reportStatus;
    //隐藏签收状态  1 未审签  2 第一审通过 3第二审通过 3第三审通过
    private String hideStatus;
}
