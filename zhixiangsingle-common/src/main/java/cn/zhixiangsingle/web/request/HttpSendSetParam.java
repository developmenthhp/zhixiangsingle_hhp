package cn.zhixiangsingle.web.request;

import java.util.HashMap;
import java.util.UUID;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangpingtai
 * @Package cn.zhixiangsingle.web.request
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019/10/29 11:18
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
public class HttpSendSetParam extends HttpSend {
    public static Object sent(HashMap<String, Object> paramMap, String method) {
        HttpSend.setHost(paramMap.get("lcHost") + ":" + paramMap.get("lcPort"));
        HttpSend.setAppId(paramMap.get("lcAppId").toString());
        HttpSend.setAppSecret(paramMap.get("lcSecret").toString());
        HttpSend.setPhone(paramMap.get("lcPhone").toString());
        HttpSend.setVer("1.0");
        HttpSend.setNonce(UUID.randomUUID().toString());
        HttpSend.setId("123");
        return HttpSendMethod(paramMap, method);
    }
}
