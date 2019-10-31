package cn.zhixiangsingle.web.service.userManager.impl;

import cn.zhixiangsingle.web.request.HttpSendSetParam;
import cn.zhixiangsingle.web.service.userManager.UserManagerService;
import com.alibaba.fastjson.JSONObject;

import java.util.HashMap;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangpingtai
 * @Package cn.zhixiangsingle.web.service.userManager.impl
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019/10/29 12:54
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
public class UserManagerServiceImpl implements UserManagerService {
    @Override
    public JSONObject accessToken(HashMap<String, Object> paramMap) {
        String	method = "accessToken";
        return (JSONObject) HttpSendSetParam.sent(paramMap,method);
    }

    @Override
    public JSONObject userToken(HashMap<String, Object> paramMap) {
        String	method = "userToken";
        return (JSONObject) HttpSendSetParam.sent(paramMap,method);
    }

    @Override
    public JSONObject userBindSms(HashMap<String, Object> paramMap) {
        String	method = "userBindSms";
        return (JSONObject) HttpSendSetParam.sent(paramMap,method);
    }

    @Override
    public JSONObject userBind(HashMap<String, Object> paramMap) {
        String	method = "userBind";
        return (JSONObject) HttpSendSetParam.sent(paramMap,method);
    }
}
