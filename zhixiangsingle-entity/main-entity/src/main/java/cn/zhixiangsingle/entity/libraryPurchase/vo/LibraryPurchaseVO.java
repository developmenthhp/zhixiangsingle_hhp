package cn.zhixiangsingle.entity.libraryPurchase.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangpingtai
 * @Package cn.zhixiangsingle.entity.libraryPurchase.vo
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019/10/25 15:59
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Data
@ToString
public class LibraryPurchaseVO  extends BaseRowModel implements Serializable {
    @ExcelProperty(value = {"编号"},index = 0)
    private String id;
    @ExcelProperty(value = {"分类"},index = 1)
    private String categoryName;
    @ExcelProperty(value = {"食材名称"},index = 2)
    private String ingredientName;
    @ExcelProperty(value = {"采购数量"},index = 3)
    private String purchCount;
    @ExcelProperty(value = {"预算最低单价"},index = 4)
    private String lowestUnitPrice;
    @ExcelProperty(value = {"预算最高单价"},index = 5)
    private String highestUnitPrice;
    @ExcelProperty(value = {"核准单价"},index = 6)
    private String authorizedPrice;
    @ExcelProperty(value = {"单据状态"},index = 7)
    private String purchStatus;
    @ExcelProperty(value = {"制单人"},index = 8)
    private String purchPeople;
    @ExcelProperty(value = {"制单时间"},index = 9)
    private String purchTime;
}
