package cn.zhixiangsingle.dao.site;

import cn.zhixiangsingle.entity.base.site.Site;
import cn.zhixiangsingle.entity.base.site.dto.SiteDTO;
import cn.zhixiangsingle.entity.base.site.vo.SiteVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangpingtai
 * @Package cn.zhixiangsingle.dao.site
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019/8/23 11:46
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
public interface SiteMapper {
    int insert(Site site);

    List<SiteVO> findSites();

    List<Site> findAll(SiteVO siteVO);

    int updateByPrimaryKeySelective(Site site);

    Site selectByPrimaryKey(int id);

    List<Site> findChildSite(@Param("pid") int id);

    int deleteByPrimaryKey(int id);

    SiteVO findSiteBySdId(@Param("sdId") Integer sdId);

    List<SiteVO> findSitesByPId(@Param("id") Integer id);

    List<SiteVO> findUserSites(Integer id);

    List<Integer> findSdIdByUserSites(Integer userId);

    String findPhotoBySdId(String sdId);

    List<SiteVO> findSitesBySdIdArray(String[] sdIds);

    Map<String,Object> findOverTimeSiteId(@Param("sdId") String sdId);

    List<Site> findPageSiteList(SiteDTO siteDTO);
}
