package cn.zhixiangsingle.entity.base.permission;

import cn.zhixiangsingle.entity.base.common.BaseEntity;
import lombok.Data;
import lombok.ToString;

import javax.persistence.Table;
import java.util.Date;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangpingtai
 * @Package cn.zhixiangsingle.base.permission
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019/8/23 10:22
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Data
@ToString
@Table(name="permission")
public class Permission extends BaseEntity {
    private String name;
    private Integer pid;
    private Integer zindex;
    private Integer isType;
    private String descpt;
    private String code;
    private String icon;
    private String page;
    private Date insertTime;
    private Date updateTime;
}
