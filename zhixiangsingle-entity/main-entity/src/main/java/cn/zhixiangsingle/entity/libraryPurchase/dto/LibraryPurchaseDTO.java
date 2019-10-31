package cn.zhixiangsingle.entity.libraryPurchase.dto;

import cn.zhixiangsingle.entity.base.common.dto.BaseDTO;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangpingtai
 * @Package cn.zhixiangsingle.entity.libraryPurchase.dto
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019/10/24 10:23
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Data
@ToString
public class LibraryPurchaseDTO extends BaseDTO {
    //b编号
    private Integer id;
    //食材基础id
    private Integer ingredientBaseId;
    //采购数量
    private Double purchCount;
    //预算最低单价-选择货品后 先默认页面填充 基础价格
    private Double lowestUnitPrice;
    //预算最高单价
    private Double highestUnitPrice;
    //核准单价
    private Double authorizedPrice;
    //1.已制单 2.已配送 3.已入库 4.已作废
    private String purchStatus;
    //制单人，默认当前人 不可选择
    private String purchPeople;
    //制单时间
    private String purchTime;
    //建议采购数量  这里的单位是公斤
    private Double adviceCount;
    //需求采购数量  这里的单位是公斤
    private Double needPurchCount;
    //库存量 这里的单位是公斤
    private Double stockCount;
    //表头信息总信息
    private Integer lchId;

    private Integer mainCategoryId;
    private Integer smallCategoryId;
    private Integer ingSdId;

    private List<Integer> ids;
}
