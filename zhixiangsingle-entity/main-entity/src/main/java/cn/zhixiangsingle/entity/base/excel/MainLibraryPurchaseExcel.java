package cn.zhixiangsingle.entity.base.excel;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangpingtai
 * @Package cn.zhixiangsingle.entity.base.excel
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019/9/30 11:42
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Data
@ToString
public class MainLibraryPurchaseExcel implements Serializable {
    //加工单苏州移动list
    private List<Machining> machiningSYList;
    //加工单绍兴list
    private List<Machining> machiningSXList;
    private String headNameSY;
    private String headNameSX;
    private List<List<String>> syHeads;
    private List<List<String>> sxHeads;
    private String sheetName;

    /*//采购单苏州移动list
    private List<Purchase> purchaseSYList;
    //采购单绍兴list
    private List<Purchase> purchaseSXList;*/
}
