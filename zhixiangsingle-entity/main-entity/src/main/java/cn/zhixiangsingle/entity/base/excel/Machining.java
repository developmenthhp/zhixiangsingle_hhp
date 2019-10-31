package cn.zhixiangsingle.entity.base.excel;

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
 * @Package cn.zhixiangsingle.entity.base.excel
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019/9/30 11:40
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Data
@ToString
public class Machining extends BaseRowModel implements Serializable {
    @ExcelProperty(value = {"加工单","序号"},index = 0)
    private Integer foodId;

    @ExcelProperty(value = {"加工单","菜品"},index = 1)
    private String foodName;

    @ExcelProperty(value = {"加工单","份数"},index = 2)
    private Double foodCount;

    @ExcelProperty(value = {"加工单","每份净重"},index = 3)
    private String weight;

    @ExcelProperty(value = {"加工单","单价"},index = 4)
    private String price;

    @ExcelProperty(value = {"加工单","食材需求"},index = 5)
    private String ingredientBases;

    @ExcelProperty(value = {"加工单","食材需求汇总"},index = 6)
    private String allIngredientBase;
}
