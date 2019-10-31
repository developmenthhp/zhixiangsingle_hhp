package cn.zhixiangsingle.dao.role;

import cn.zhixiangsingle.entity.base.role.Role;
import cn.zhixiangsingle.entity.base.role.dto.RoleDTO;
import cn.zhixiangsingle.entity.base.role.vo.RoleVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangpingtai
 * @Package cn.zhixiangsingle.dao.role
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019/8/23 11:42
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
public interface RoleMapper {
    int deleteByPrimaryKey(Integer id);

    int insertSelective(Role record);

    Role selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Role record);

    /**
     * 分页查询所有的角色列表
     * @return
     */
    List<Map<String,Object>> findList(RoleDTO roleDTO);

    /**
     * 获取角色相关的数据
     * @param id
     * @return
     */
    RoleVO findRoleAndPerms(Integer id);

    /**
     * 根据用户id获取角色数据
     * @param userId
     * @return
     */
    List<Role> getRoleByUserId(Integer userId);

    List<Role> getRoles(Role role);
}
