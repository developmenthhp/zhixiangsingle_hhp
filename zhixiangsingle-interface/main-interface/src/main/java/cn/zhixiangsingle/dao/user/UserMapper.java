package cn.zhixiangsingle.dao.user;

import cn.zhixiangsingle.entity.base.user.User;
import cn.zhixiangsingle.entity.base.userRole.dto.UserRoleDTO;
import cn.zhixiangsingle.entity.base.userRole.dto.UserSearchDTO;
import cn.zhixiangsingle.entity.base.userRole.vo.UserRolesVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangpingtai
 * @Package cn.zhixiangsingle.dao.user
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019/8/23 11:48
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
public interface UserMapper {
    int deleteByPrimaryKey(@Param("id")Integer id);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKey(User record);

    /**
     * 分页查询用户数据
     * @return
     */
    List<UserRoleDTO> getUsers(@Param("userSearch") UserSearchDTO userSearch);

    /**
     * 删除用户
     * @param id
     * @param isDel
     * @return
     */
    int setDelUser(@Param("id") Integer id, @Param("isDel") Integer isDel,
                   @Param("insertUid") Integer insertUid);

    /**
     * 设置用户是否离职
     * @param id
     * @param isJob
     * @return
     */
    int setJobUser(@Param("id") Integer id, @Param("job") Integer isJob,
                   @Param("insertUid") Integer insertUid);

    /**
     * 查询用户及对应的角色
     * @param id
     * @return
     */
    UserRolesVO getUserAndRoles(Integer id);

    /**
     * 根据用户名和密码查找用户
     * @param username
     * @param password
     * @return
     */
    User findUser(@Param("username") String username,
                  @Param("password") String password);

    /**
     *	根据手机号获取用户数据
     * @param user
     * @return
     */
    User findUserByName(User user);

    /**
     * 修改用户密码
     * @param id
     * @param password
     * @return
     */
    int updatePwd(@Param("id") Integer id, @Param("password") String password);

    User selectCommon(User findUser);

    int setUserLockNum(@Param("id")Integer id, @Param("isLock")int isLock);
}
