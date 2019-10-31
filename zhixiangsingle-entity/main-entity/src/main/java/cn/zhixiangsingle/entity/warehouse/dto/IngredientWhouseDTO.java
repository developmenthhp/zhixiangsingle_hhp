package cn.zhixiangsingle.entity.warehouse.dto;

import cn.zhixiangsingle.entity.base.common.dto.BaseDTO;
import lombok.Data;
import lombok.ToString;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangpingtai
 * @Package cn.zhixiangsingle.entity.warehouse.dto
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019/10/5 11:23
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Data
@ToString
public class IngredientWhouseDTO extends BaseDTO {
    private Integer id;
    //仓库基础信息名称
    private String whName;
    //仓库状态信息 1正常使用  2删除禁用状态
    private String whStatus;
}
