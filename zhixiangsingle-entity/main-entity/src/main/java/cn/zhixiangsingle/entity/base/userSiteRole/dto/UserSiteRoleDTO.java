package cn.zhixiangsingle.entity.base.userSiteRole.dto;

import cn.zhixiangsingle.entity.base.common.dto.BaseDTO;
import lombok.Data;
import lombok.ToString;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangpingtai
 * @Package cn.zhixiangsingle.base.userSiteRole.dto
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019/8/23 11:24
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Data
@ToString
public class UserSiteRoleDTO extends BaseDTO {
    private Integer id;
    private String username;
    private String email;
    private String password;
    private Integer insertUid;
    private String insertTime;
    private String updateTime;
    private Boolean job;
    private String siteRoleNames;
}
