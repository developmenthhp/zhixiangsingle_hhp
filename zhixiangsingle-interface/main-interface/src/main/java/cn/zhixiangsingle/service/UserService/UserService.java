package cn.zhixiangsingle.service.UserService;

import cn.zhixiangsingle.entity.base.user.User;
import cn.zhixiangsingle.entity.base.userRole.dto.UserSearchDTO;
import cn.zhixiangsingle.entity.base.userRole.vo.UserRolesVO;
import cn.zhixiangsingle.web.responsive.ResultBean;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangpingtai
 * @Package cn.zhixiangsingle.service.UserService
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019/8/23 11:36
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
public interface UserService {
    /**
     * 分页查询用户列表
     * @param page
     * @param limit
     * @return
     */
    ResultBean getUsers(UserSearchDTO userSearch, Integer page, Integer limit);

    /**
     *	设置用户【新增或更新】
     * @param user
     * @param roleIds
     * @return
     */
    ResultBean setUser(User user, String roleIds, User adminUser, String siteRoleIds);

    /**
     * 设置用户是否离职
     * @param id
     * @param isJob
     * @param insertUid
     * @return
     */
    ResultBean setJobUser(Integer id, Integer isJob, Integer insertUid);

    /**
     * 删除用户
     * @param id
     * @return
     */
    ResultBean setDelUser(Integer id);

    /**
     * 查询用户数据
     * @param id
     * @return
     */
    UserRolesVO getUserAndRoles(Integer id);

    /**
     * 修改用户手机号
     * @param id
     * @param password
     * @return
     */
    int updatePwd(Integer id, String password);

    User findUserByName(User user);

    ResultBean selectCommon(User user);
}
