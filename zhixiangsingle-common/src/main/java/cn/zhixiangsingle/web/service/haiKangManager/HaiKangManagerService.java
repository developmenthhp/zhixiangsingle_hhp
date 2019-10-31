package cn.zhixiangsingle.web.service.haiKangManager;

import com.alibaba.fastjson.JSONObject;

import java.util.HashMap;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangpingtai
 * @Package cn.zhixiangsingle.web.service.haiKangManager
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019/10/29 14:01
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
public interface HaiKangManagerService {
    //获取管理员token
    public JSONObject tokenGet(HashMap<String, String> paramMap);
    //设备抓拍图片
    public JSONObject deviceCapture(HashMap<String, String> paramMap);
    //获取直播地址
    public JSONObject liveAddressGet(HashMap<String, String> paramMap);
    //关闭设备视频加密
    public JSONObject deviceEncryptOff(HashMap<String, String> paramMap);
    //获取用户下直播视频列表
    public JSONObject liveVideoList(HashMap<String, String> paramMap);
    //创建消费者
    public JSONObject mqV1Consumer(HashMap<String, String> paramMap);
}
