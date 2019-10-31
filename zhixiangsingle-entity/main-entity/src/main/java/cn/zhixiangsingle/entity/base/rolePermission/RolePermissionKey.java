package cn.zhixiangsingle.entity.base.rolePermission;

import cn.zhixiangsingle.entity.base.common.BaseEntity;
import lombok.Data;
import lombok.ToString;

import javax.persistence.Table;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangpingtai
 * @Package cn.zhixiangsingle.base.rolePermission
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019/8/23 10:32
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Data
@ToString
@Table(name="rolePermission")
public class RolePermissionKey extends BaseEntity {
    private Integer permitId;
    private Integer roleId;
}
