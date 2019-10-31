package cn.zhixiangsingle.service.auth;

import cn.zhixiangsingle.entity.base.permission.Permission;
import cn.zhixiangsingle.entity.base.permission.vo.PermissionVO;
import cn.zhixiangsingle.entity.base.role.Role;
import cn.zhixiangsingle.entity.base.role.dto.RoleDTO;
import cn.zhixiangsingle.entity.base.role.vo.RoleVO;
import cn.zhixiangsingle.web.responsive.ResultBean;

import java.util.List;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangpingtai
 * @Package cn.zhixiangsingle.service.auth
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019/8/23 11:33
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
public interface AuthService {
    int addPermission(Permission permission);

    List<Permission> permList();

    int updatePerm(Permission permission);

    ResultBean getPermission(int id);

    String delPermission(int id);

    /**
     * 查询所有角色
     * @return
     */
    ResultBean roleList(RoleDTO roleDTO, Integer page, Integer limit);

    /**
     * 关联查询权限树列表
     * @return
     */
    List<PermissionVO> findPerms(Integer flag, Integer userId);

    /**
     * 添加角色
     * @param role
     * @param permIds
     * @return
     */
    String addRole(Role role, String permIds);

    RoleVO findRoleAndPerms(Integer id);

    /**
     * 更新角色并授权
     * @param role
     * @param permIds
     * @return
     */
    ResultBean updateRole(Role role, String permIds);

    /**
     * 删除角色以及它对应的权限
     * @param id
     * @return
     */
    ResultBean delRole(int id);

    /**
     * 查找所有角色
     * @return
     */
    List<Role> getRoles(Role role);

    /**
     * 根据用户获取角色列表
     * @param userId
     * @return
     */
    List<Role> getRoleByUser(Integer userId);

    /**
     * 根据角色id获取权限数据
     * @param id
     * @return
     */
    List<Permission> findPermsByRoleId(Integer id);

    /**
     * 根据用户id获取权限数据
     * @param id
     * @return
     */
    ResultBean getUserPerms(Integer id);
}
