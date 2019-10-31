package cn.zhixiangsingle.web.service.userManager;

import com.alibaba.fastjson.JSONObject;

import java.util.HashMap;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangpingtai
 * @Package cn.zhixiangsingle.web.service.userManager
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019/10/29 12:52
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
public interface UserManagerService {
    public JSONObject accessToken(HashMap<String, Object> paramMap);
    public JSONObject userToken(HashMap<String, Object> paramMap);
    public JSONObject userBindSms(HashMap<String, Object> paramMap);
    public JSONObject userBind(HashMap<String, Object> paramMap);
}
