package cn.zhixiangsingle.entity.libraryChange.dto;

import cn.zhixiangsingle.entity.base.common.dto.BaseDTO;
import lombok.Data;
import lombok.ToString;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangpingtai
 * @Package cn.zhixiangsingle.entity.libraryChange.dto
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019/10/22 13:50
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Data
@ToString
public class LibraryChangeDTO extends BaseDTO {
    private Integer id;
    //食材基础id
    private Integer ingredientBaseId;
    //变更方式: 1批次增仓  2批次减仓
    private String changeMode;
    //变更数量
    private Double changeNumber;
    //变更批次的记录id ， 食材入库记录表id
    private Integer changeBatch;
    //变更备注,必填字段
    private String changeRemarks;
    //变更操作者名称
    private String changePeople;
    //变更操作者id
    private Integer changePeopleId;
    //变更操作执行时间
    private String changeTime;
    private Double changePrice;

    //参数
    private Double bitchCount;
}
