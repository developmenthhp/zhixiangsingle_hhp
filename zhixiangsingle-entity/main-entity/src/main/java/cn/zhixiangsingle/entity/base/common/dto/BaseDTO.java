package cn.zhixiangsingle.entity.base.common.dto;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangpingtai
 * @Package cn.zhixiangsingle.base.common.dto
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019/8/23 10:15
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Data
@ToString
public class BaseDTO implements Serializable {
    private Integer userId;
    private Integer sdId;
    private Integer page;
    private Integer limit;
    private String sdIds;
    private Integer start;
    private Integer end;
    private String siteName;
    private String sitePhoto;
    private String picturePrefix;
    private String startTime;
    private String endTime;

}
