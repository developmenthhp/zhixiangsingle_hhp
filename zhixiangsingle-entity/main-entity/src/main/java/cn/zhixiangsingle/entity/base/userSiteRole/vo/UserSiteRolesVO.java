package cn.zhixiangsingle.entity.base.userSiteRole.vo;

import cn.zhixiangsingle.entity.base.common.vo.BaseVO;
import cn.zhixiangsingle.entity.base.userSiteRole.UserSiteRoleKey;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangpingtai
 * @Package cn.zhixiangsingle.base.userSiteRole.vo
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019/8/23 11:23
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Data
@ToString
public class UserSiteRolesVO extends BaseVO {
    private Integer id;
    private String username;
    private String email;
    private String password;
    private Integer insertUid;
    private String insertTime;
    private String updateTime;
    private Boolean job;
    private List<UserSiteRoleKey> userSiteRoles;
    private Boolean zx;
    private Integer sdId;
}
