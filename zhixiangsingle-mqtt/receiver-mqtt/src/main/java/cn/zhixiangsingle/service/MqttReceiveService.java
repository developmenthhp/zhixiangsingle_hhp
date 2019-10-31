package cn.zhixiangsingle.service;

import cn.zhixiangsingle.web.responsive.ResultBean;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangpingtai
 * @Package cn.zhixiangsingle.service
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019/10/28 10:55
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
public interface MqttReceiveService {
    ResultBean handlerMqttMessage(String topic, String jsonData);
}
