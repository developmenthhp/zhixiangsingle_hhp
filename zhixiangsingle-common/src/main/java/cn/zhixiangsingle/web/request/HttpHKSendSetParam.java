package cn.zhixiangsingle.web.request;

import com.alibaba.fastjson.JSONObject;

import java.util.Map;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangpingtai
 * @Package cn.zhixiangsingle.web.request
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019/10/29 14:06
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
public class HttpHKSendSetParam extends HttpHKSend {
    public static JSONObject sent(Map<String,String> param, String path){
        HttpHKSend.setHost(param.get("host"));
        HttpHKSend.setAppKey(param.get("appKey"));
        HttpHKSend.setAppSecret(param.get("appSecret"));
        return getHttpResponseJson(param, path);
    }
}
