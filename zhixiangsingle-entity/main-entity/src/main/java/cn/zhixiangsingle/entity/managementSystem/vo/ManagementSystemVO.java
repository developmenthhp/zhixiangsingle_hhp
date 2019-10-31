package cn.zhixiangsingle.entity.managementSystem.vo;

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
 * @Package cn.zhixiangsingle.entity.managementSystem.vo
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019/10/19 10:44
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Data
@ToString
public class ManagementSystemVO extends BaseRowModel implements Serializable {
    @ExcelProperty(value = {"制度名称"},index = 0)
    private String systemName;
    @ExcelProperty(value = {"制度内容"},index = 1)
    private String contentDescribe;
    @ExcelProperty(value = {"创建时间"},index = 2)
    private String executionTime;
}
