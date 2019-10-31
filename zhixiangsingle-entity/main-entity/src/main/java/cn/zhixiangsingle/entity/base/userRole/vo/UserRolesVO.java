package cn.zhixiangsingle.entity.base.userRole.vo;

import cn.zhixiangsingle.entity.base.common.vo.BaseVO;
import cn.zhixiangsingle.entity.base.userRole.UserRoleKey;
import cn.zhixiangsingle.entity.base.userSiteRole.UserSiteRoleKey;
import lombok.Data;
import lombok.ToString;

import java.util.List;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangpingtai
 * @Package cn.zhixiangsingle.base.userRole.vo
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019/8/23 11:18
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Data
@ToString
public class UserRolesVO extends BaseVO {
    private Integer id;

    private String username;

    private String email;

    private String password;

    private Integer insertUid;

    private String insertTime;

    private String updateTime;

    private Boolean job;

    private List<UserRoleKey> userRoles;

    private List<UserSiteRoleKey> userSiteRoleKeys;

    private Boolean zx;

    private Integer sdid;

    private Integer profession;
    private Integer qsType;
    private Integer classes;

}
