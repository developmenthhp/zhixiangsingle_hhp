package cn.zhixiangsingle.web.service.haiKangManager.impl;

import cn.zhixiangsingle.web.request.HttpHKSendSetParam;
import cn.zhixiangsingle.web.service.haiKangManager.HaiKangManagerService;
import com.alibaba.fastjson.JSONObject;

import java.util.HashMap;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangpingtai
 * @Package cn.zhixiangsingle.web.service.haiKangManager.impl
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019/10/29 14:01
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
public class HaiKangManagerServiceImpl implements HaiKangManagerService {
    /**
     * @version V1.0
     * @Title: zhixiangpingtai
     * @Description:获取管理员token
     * @author: hhp
     * @param:  * @param paramMap
     * @date: 2019/10/29 14:14
     */
    @Override
    public JSONObject tokenGet(HashMap<String, String> paramMap) {
        String path="token/get";
        return  HttpHKSendSetParam.sent(paramMap, path);
    }
    /**
     * @version V1.0
     * @Title: zhixiangpingtai
     * @Description:设备抓拍图片
     * @author: hhp
     * @param:  * @param paramMap
     * @date: 2019/10/29 14:14
     */
    @Override
    public JSONObject deviceCapture(HashMap<String, String> paramMap) {
        String path="device/capture";
        return  HttpHKSendSetParam.sent(paramMap, path);
    }
    /**
     * @version V1.0
     * @Title: zhixiangpingtai
     * @Description:获取直播地址
     * @author: hhp
     * @param:  * @param paramMap
     * @date: 2019/10/29 14:14
     */
    @Override
    public JSONObject liveAddressGet(HashMap<String, String> paramMap) {
        String path="live/address/get";
        return  HttpHKSendSetParam.sent(paramMap, path);
    }
    /**
     * @version V1.0
     * @Title: zhixiangpingtai
     * @Description:关闭设备视频加密
     * @author: hhp
     * @param:  * @param paramMap
     * @date: 2019/10/29 14:14
     */
    @Override
    public JSONObject deviceEncryptOff(HashMap<String, String> paramMap) {
        String path="device/encrypt/off";
        return  HttpHKSendSetParam.sent(paramMap, path);
    }
    /**
     * @version V1.0
     * @Title: zhixiangpingtai
     * @Description:获取用户下直播视频列表
     * @author: hhp
     * @param:  * @param paramMap
     * @date: 2019/10/29 14:14
     */
    @Override
    public JSONObject liveVideoList(HashMap<String, String> paramMap) {
        String path="live/video/list";
        return  HttpHKSendSetParam.sent(paramMap, path);
    }
    /**
     * @version V1.0
     * @Title: zhixiangpingtai
     * @Description:获取用户下直播视频列表
     * @author: hhp
     * @param:  * @param paramMap
     * @date: 2019/10/29 14:13
     */
    @Override
    public JSONObject mqV1Consumer(HashMap<String, String> paramMap) {
        String path="mq/v1/consumer/group1";
        return  HttpHKSendSetParam.sent(paramMap, path);
    }
}
