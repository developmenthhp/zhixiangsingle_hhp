package cn.zhixiangsingle.entity.base.role.dto;

import cn.zhixiangsingle.entity.base.common.dto.BaseDTO;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.ToString;

import java.util.Date;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangpingtai
 * @Package cn.zhixiangsingle.base.role.dto
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019/8/23 10:31
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Data
@ToString
public class RoleDTO extends BaseDTO {
    private Integer id;
    private String roleName;
    private String code;
    private String descpt;
    private Date insertTime;
    private Integer insertUid;
    private Date updateTime;
}
