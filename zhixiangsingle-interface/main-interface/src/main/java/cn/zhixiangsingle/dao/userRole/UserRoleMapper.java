package cn.zhixiangsingle.dao.userRole;

import cn.zhixiangsingle.entity.base.userRole.UserRoleKey;

import java.util.List;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangpingtai
 * @Package cn.zhixiangsingle.dao.userRole
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019/8/23 11:49
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
public interface UserRoleMapper {
    int deleteByPrimaryKey(UserRoleKey key);

    int insert(UserRoleKey record);

    int insertSelective(UserRoleKey record);

    /**
     * 根据用户获取用户角色中间表数据
     * @param userId
     * @return
     */
    List<UserRoleKey> findByUserId(int userId);
}
