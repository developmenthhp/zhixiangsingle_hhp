package cn.zhixiangsingle.service.site;

import cn.zhixiangsingle.entity.base.site.Site;
import cn.zhixiangsingle.entity.base.site.dto.SiteDTO;
import cn.zhixiangsingle.entity.base.site.vo.SiteVO;
import cn.zhixiangsingle.entity.base.siteRole.SiteRole;
import cn.zhixiangsingle.entity.base.siteRole.dto.SiteRoleDTO;
import cn.zhixiangsingle.entity.base.siteRole.vo.SiteRoleVO;
import cn.zhixiangsingle.web.responsive.ResultBean;

import java.util.List;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangpingtai
 * @Package cn.zhixiangsingle.service.site
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019/8/23 11:35
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
public interface SiteService {
    ResultBean siteRoleList(SiteRoleDTO siteRoleDTO, Integer page, Integer limit);
    int addSite(Site site);
    ResultBean siteList(SiteVO siteVO) throws Exception;

    Site getSite(int id);

    ResultBean delSite(int id);

    SiteRoleVO findSiteRoleAndSites(Integer id);

    List<SiteVO> findSites(Integer flag, Integer userId);

    ResultBean addSiteRole(SiteRole siteRole, String permIds);

    ResultBean updateSiteRole(SiteRole siteRole, String permIds);

    ResultBean delSiteRole(int id);

    List<SiteRole> getSiteRoles(SiteRole siteRole);

    ResultBean getUserSites(Integer id) throws Exception;

    int updateSite(Site site);

    SiteVO findSiteBySdId(Integer sdid);

    List<SiteVO> findSitesByPId(Integer id);

    ResultBean getPhotoBySdId(String sdId);

    ResultBean isSiteOverTime(String sdId);

    ResultBean getPageSiteList(SiteDTO siteDTO, Integer page, Integer limit);
}
