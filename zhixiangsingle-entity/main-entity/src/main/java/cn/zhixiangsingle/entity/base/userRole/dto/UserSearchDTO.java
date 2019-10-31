package cn.zhixiangsingle.entity.base.userRole.dto;

import cn.zhixiangsingle.entity.base.common.dto.BaseDTO;
import lombok.Data;
import lombok.ToString;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangpingtai
 * @Package cn.zhixiangsingle.base.userRole.dto
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019/8/23 11:20
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Data
@ToString
public class UserSearchDTO extends BaseDTO {
    private Integer page;

    private Integer limit;

    private String uname;

    private String insertTimeStart;

    private String insertTimeEnd;
}
