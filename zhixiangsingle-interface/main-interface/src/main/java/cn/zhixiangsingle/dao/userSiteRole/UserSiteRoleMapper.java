package cn.zhixiangsingle.dao.userSiteRole;

import cn.zhixiangsingle.entity.base.userSiteRole.UserSiteRoleKey;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangpingtai
 * @Package cn.zhixiangsingle.dao.userSiteRole
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019/8/23 11:49
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
public interface UserSiteRoleMapper {
    List<UserSiteRoleKey> findByUserId(@Param("userId") int userId);

    int deleteByPrimaryKey(UserSiteRoleKey ur);

    int insert(UserSiteRoleKey usrk);
}
