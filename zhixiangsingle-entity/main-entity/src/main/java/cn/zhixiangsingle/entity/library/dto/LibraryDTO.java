package cn.zhixiangsingle.entity.library.dto;

import cn.zhixiangsingle.entity.base.common.dto.BaseDTO;
import lombok.Data;
import lombok.ToString;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangpingtai
 * @Package cn.zhixiangsingle.entity.library.dto
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019/10/19 15:44
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Data
@ToString
public class LibraryDTO extends BaseDTO {
    private Integer id;
    //食材id
    private Integer ingredientBaseId;
    //库存结余,库存批次变更需要对该字段进行更新
    private Double librarySurplus;
    //批次数量过期总数
    private Double libraryExpired;
    //补货状态 1.生产进货单(手动)   2.正常
    private String replenishmentStatus;
    //库存状态 1.正常  2.过期  3.即将过期
    private String libraryStatus;

    //大类类别关联id
    private Integer mainCategoryId;
    //小类类别关联id
    private Integer smallCategoryId;
}
