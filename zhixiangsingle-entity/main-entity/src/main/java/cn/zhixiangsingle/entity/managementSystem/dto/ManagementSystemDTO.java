package cn.zhixiangsingle.entity.managementSystem.dto;

import cn.zhixiangsingle.entity.base.common.dto.BaseDTO;
import lombok.Data;
import lombok.ToString;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangpingtai
 * @Package cn.zhixiangsingle.entity.managementSystem
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019/10/9 12:10
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Data
@ToString
public class ManagementSystemDTO extends BaseDTO {
    private Integer id;
    //添加时间
    private String executionTime;
    //名称
    private String systemName;
    //内容描述
    private String contentDescribe;
    //图片地址
    private String imgPath;
}
