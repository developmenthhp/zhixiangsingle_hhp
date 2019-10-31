package cn.zhixiangsingle.dao.rolePermission;

import cn.zhixiangsingle.entity.base.rolePermission.RolePermissionKey;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangpingtai
 * @Package cn.zhixiangsingle.dao.rolePermission
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019/8/23 11:43
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
public interface RolePermissionMapper {
    int deleteByPrimaryKey(RolePermissionKey key);

    int insert(RolePermissionKey record);

    int insertSelective(RolePermissionKey record);

    List<RolePermissionKey> findByRole(int roleId);

    int insertBitch(@Param("roleId")int roleId, @Param("permissionIds")List<String> arrays);
}
