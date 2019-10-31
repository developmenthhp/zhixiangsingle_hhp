package cn.zhixiangsingle.dao.permission;

import cn.zhixiangsingle.entity.base.permission.Permission;
import cn.zhixiangsingle.entity.base.permission.vo.PermissionVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangpingtai
 * @Package cn.zhixiangsingle.dao.permission
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019/8/23 11:42
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
public interface PermissionMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Permission record);

    Permission selectByPrimaryKey(PermissionVO permissionVO);

    List<Permission> findAll(Permission permission);

    List<Permission> findChildPerm(Permission permission);

    List<PermissionVO> findPerms(Permission permission);

    List<Permission> findPermsByRole(PermissionVO permission);

    List<PermissionVO> getUserPerms(PermissionVO permissionVO);

    int updateByPrimaryKeySelective(Permission permission);
}
