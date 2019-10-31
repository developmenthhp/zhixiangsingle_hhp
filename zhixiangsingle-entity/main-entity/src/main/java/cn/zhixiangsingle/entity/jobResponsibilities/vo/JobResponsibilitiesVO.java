package cn.zhixiangsingle.entity.jobResponsibilities.vo;

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
 * @Package cn.zhixiangsingle.entity.jobResponsibilities.vo
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019/10/19 11:08
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Data
@ToString
public class JobResponsibilitiesVO extends BaseRowModel implements Serializable {
    @ExcelProperty(value = {"岗位名称"},index = 0)
    private String jobTitle;
    @ExcelProperty(value = {"岗位内容"},index = 1)
    private String jobResponsibilities;
    @ExcelProperty(value = {"创建时间"},index = 2)
    private String   addTime;
}
