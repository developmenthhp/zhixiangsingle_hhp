package cn.zhixiangsingle.entity.base.siteRolePermission;

import cn.zhixiangsingle.entity.base.common.BaseEntity;
import lombok.Data;
import lombok.ToString;

import javax.persistence.Table;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangpingtai
 * @Package cn.zhixiangsingle.base.siteRolePermission
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019/8/23 11:05
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Data
@ToString
@Table(name="siteSiteRole")
public class SiteRolePermissionKey extends BaseEntity {
    private Integer siteId;
    private Integer siteRoleId;
}
