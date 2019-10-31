package cn.zhixiangsingle.entity.base.common;

import lombok.Data;

import java.io.Serializable;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangpingtai
 * @Package cn.zhixiangsingle.base.common
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019/8/23 10:13
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Data
public class BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    /** 主键ID **/
    private Integer id;
}
