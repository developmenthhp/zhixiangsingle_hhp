package cn.zhixiangsingle.entity.base.permission.vo;

import cn.zhixiangsingle.entity.base.common.vo.BaseVO;
import lombok.Data;
import lombok.ToString;

import java.util.Date;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangpingtai
 * @Package cn.zhixiangsingle.base.permission.vo
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019/8/23 10:25
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Data
@ToString
public class PermissionVO extends BaseVO {
    private static final long serialVersionUID = -2783081162690878303L;
    private Integer id;
    private String name;
    private Integer pid;
    private Integer isType;
    private String code;
    private String page;
    private String icon;
    private Integer zindex;
    private boolean checked;
    private boolean open;
    private String descpt;
    private Date insertTime;
    private Date updateTime;
}
