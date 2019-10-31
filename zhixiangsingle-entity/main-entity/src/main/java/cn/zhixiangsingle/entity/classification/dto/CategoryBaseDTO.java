package cn.zhixiangsingle.entity.classification.dto;

import cn.zhixiangsingle.entity.base.common.dto.BaseDTO;
import lombok.Data;
import lombok.ToString;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangpingtai
 * @Package cn.zhixiangsingle.entity.classification.dto
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019/9/27 16:14
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Data
@ToString
public class CategoryBaseDTO extends BaseDTO {
    private Integer id;
    private String categoryName;
    private String deleteStatus;
    private Integer pId;
}
