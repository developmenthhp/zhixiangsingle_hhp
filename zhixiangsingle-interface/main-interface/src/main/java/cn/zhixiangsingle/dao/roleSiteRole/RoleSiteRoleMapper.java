package cn.zhixiangsingle.dao.roleSiteRole;

import cn.zhixiangsingle.entity.base.siteRolePermission.SiteRolePermissionKey;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangpingtai
 * @Package cn.zhixiangsingle.dao.roleSiteRole
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019/8/23 11:44
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
public interface RoleSiteRoleMapper {
    int insert(SiteRolePermissionKey rpk);

    List<SiteRolePermissionKey> findByRole(@Param("siteRoleId") int siteRoleId);

    int deleteByPrimaryKey(SiteRolePermissionKey rpk);

    int deleteBySiteRoleId(@Param("siteRoleId") int siteRoleId);

    int insertBitch(@Param("siteRoleId")int siteRoleId, @Param("siteIds")List<String> siteIds);
}
