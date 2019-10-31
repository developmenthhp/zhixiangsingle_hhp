package cn.zhixiangsingle.web.service.deviceManager.impl;

import cn.zhixiangsingle.web.request.HttpSendSetParam;
import cn.zhixiangsingle.web.service.deviceManager.DeviceManagerService;
import com.alibaba.fastjson.JSONObject;

import java.util.HashMap;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangpingtai
 * @Package cn.zhixiangsingle.web.service.deviceManager.impl
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019/10/29 13:11
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
public class DeviceManagerServiceImpl implements DeviceManagerService {
    @Override
    public JSONObject setDeviceSnap(HashMap<String, Object> paramMap) {
        String method="setDeviceSnap";
        return (JSONObject) HttpSendSetParam.sent(paramMap,method);
    }

    @Override
    public JSONObject getAlarmMessage(HashMap<String, Object> paramMap) {
        String method="getAlarmMessage";
        return (JSONObject) HttpSendSetParam.sent(paramMap,method);
    }

    @Override
    public JSONObject queryLiveStatus(HashMap<String, Object> paramMap) {
        String	method = "queryLiveStatus";
        return (JSONObject) HttpSendSetParam.sent(paramMap,method);
    }

    @Override
    public JSONObject modifyLiveStatus(HashMap<String, Object> paramMap) {
        String	method = "modifyLiveStatus";
        return (JSONObject) HttpSendSetParam.sent(paramMap,method);
    }

    @Override
    public JSONObject modifyLivePlanStatus(HashMap<String, Object> paramMap) {
        String	method = "modifyLivePlanStatus";
        return (JSONObject) HttpSendSetParam.sent(paramMap,method);
    }

    @Override
    public JSONObject modifyLivePlan(HashMap<String, Object> paramMap) {
        String	method = "modifyLivePlan";
        return (JSONObject) HttpSendSetParam.sent(paramMap,method);
    }
}
