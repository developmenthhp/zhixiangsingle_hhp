package cn.zhixiangsingle.web.service.deviceManager;

import com.alibaba.fastjson.JSONObject;

import java.util.HashMap;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangpingtai
 * @Package cn.zhixiangsingle.web.service.deviceManager
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019/10/29 13:11
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
public interface DeviceManagerService {
    //抓图
    public JSONObject setDeviceSnap(HashMap<String, Object> paramMap);
    //获取警报信息
    public JSONObject getAlarmMessage(HashMap<String, Object> paramMap);
    //获取直播状态
    public JSONObject queryLiveStatus(HashMap<String, Object> paramMap);
    //修改直播状态
    public JSONObject modifyLiveStatus(HashMap<String, Object> paramMap);
    //设置直播计划开关
    public JSONObject modifyLivePlanStatus(HashMap<String, Object> paramMap);
    //修改直播计划时间
    public JSONObject modifyLivePlan(HashMap<String, Object> paramMap);
}
