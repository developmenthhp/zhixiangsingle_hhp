package cn.zhixiangsingle.dao.siteRole;

import cn.zhixiangsingle.entity.base.siteRole.SiteRole;
import cn.zhixiangsingle.entity.base.siteRole.dto.SiteRoleDTO;
import cn.zhixiangsingle.entity.base.siteRole.vo.SiteRoleVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangpingtai
 * @Package cn.zhixiangsingle.dao.siteRole
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019/8/23 11:47
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
public interface SiteRoleMapper {
    List<SiteRole> findSiteRoleList(SiteRoleDTO siteRoleDTO);

    int insertSelective(SiteRole siteRole);

    int updateByPrimaryKeySelective(SiteRole siteRole);

    SiteRoleVO findSiteRoleAndSites(@Param("id") Integer id);

    List<SiteRole> findSiteRoles(SiteRole siteRole);

    int deleteByPrimaryKey(int id);
}
