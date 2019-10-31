package cn.zhixiangsingle.entity.base.excel;

import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangpingtai
 * @Package cn.zhixiangsingle.entity.base.excel
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019/9/30 11:38
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Data
public class MachiningExcelResult implements Serializable {
    /** 导出excel名称 */
    private String fileName;
    /** sheet名称数组 */
    private String[] sheetNames;
    /** 结果数据对象集合 */
    List<List<Machining>> machinings;
    /** 结果数据对象集合 */
    List<Machining> morningMachinings;
    /** 结果数据对象集合 */
    List<Machining> afterNoonMachinings;
    /** 结果数据对象集合 */
    List<Machining> nightMachinings;
    /** 结果数据对象集合 */
    List<Machining> midleNightMachinings;
    /** 表头集合 */
    List<List<String>> heads;
    /** 多个表头做准备 */
    List<Map<String,List<String>>> manyHeads;
    /** 多个的加工采购返回model */
    List<MainLibraryPurchaseExcel> mlpReturnExcel;



    public MachiningExcelResult() {

    }

    public MachiningExcelResult(String fileName, String[] sheetNames, List<List<Machining>> machinings, List<List<String>> heads) {
        this.fileName = fileName;
        this.sheetNames = sheetNames;
        this.machinings = machinings;
        this.heads = heads;
    }

    public MachiningExcelResult(String fileName, String[] sheetNames, List<Machining> morningMachinings, List<Machining> afterNoonMachinings, List<Machining> nightMachinings, List<Machining> midleNightMachinings, List<List<String>> heads) {
        this.fileName = fileName;
        this.sheetNames = sheetNames;
        this.morningMachinings = morningMachinings;
        this.afterNoonMachinings = afterNoonMachinings;
        this.nightMachinings = nightMachinings;
        this.midleNightMachinings = midleNightMachinings;
        this.heads = heads;
    }
}
