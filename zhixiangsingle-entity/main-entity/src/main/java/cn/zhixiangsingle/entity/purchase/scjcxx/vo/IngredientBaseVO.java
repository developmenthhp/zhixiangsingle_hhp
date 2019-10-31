package cn.zhixiangsingle.entity.purchase.scjcxx.vo;

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
 * @Package cn.zhixiangsingle.entity.purchase.scjcxx.vo
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019/9/30 17:58
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Data
@ToString
public class IngredientBaseVO  extends BaseRowModel implements Serializable {
    //食材药材配料等信息名称
    @ExcelProperty(value = {"食材"},index = 0)
    private String ingredientName;
    //计量单位名称
    @ExcelProperty(value = {"单位"},index = 1)
    private String meteringName;
    @ExcelProperty(value = {"一级分类"},index = 2)
    private String mainCategoryName;

    @ExcelProperty(value = {"二级分类"},index = 3)
    private String smallCategoryName;
    //额定常见存储日期
    @ExcelProperty(value = {"保质日期"},index = 5)
    private String ratedTerm;
    @ExcelProperty(value = {"保质日期单位"},index = 6)
    private String ratedTermDW;
    //检测状态 1检测2不检测
    @ExcelProperty(value = {"检测状态"},index = 8)
    private String checkStatus;
    //基础价格
    @ExcelProperty(value = {"基础价格(元)"},index = 4)
    private String basePrice;
    //存储警戒
    @ExcelProperty(value = {"存储警戒"},index = 7)
    private String inventoryLimit;
}
