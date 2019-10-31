package cn.zhixiangsingle.entity.base.mqtt;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangpingtai
 * @Package cn.zhixiangsingle.entity.base.mqtt
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019/10/10 16:09
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Data
@ToString
public class Mqtt implements Serializable {
    private String mqHost;
    private String mqPort;
    private String client;
    private String clientId;
    private String userName;
    private String passWord;
}
