package cn.zhixiangsingle.entity.base.youTu;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangpingtai
 * @Package cn.zhixiangsingle.entity.base.youTu
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019/10/10 16:02
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Data
@ToString
public class YouTu implements Serializable {
    private String appId;
    private String secretId;
    private String secretKey;
    private String endPointUrl;
    private String chargePointUrl;
    private String tencentPointUrl;
    private String handWritePointUrl;
    private String userId;
}
