package cn.zhixiangsingle.web.request;

import cn.zhixiangsingle.entity.base.PrintUtil;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangpingtai
 * @Package cn.zhixiangsingle.web.request
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019/8/21 18:40
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
public class RequestDataConfig {
    public static final String PRINT_STR = "requestData";
    /**
     * Title: getWebRequestData
     * Description: 将请求参数配置成map数组
     * @param request
     * @return Map<String,String> 返回一个键值对map
     */
    @SuppressWarnings("unchecked")
    public static Map<String, String> getWebRequestData(HttpServletRequest request) {
        Map<String,String> map=new HashMap<>();
        try {
            Enumeration<String> names = request.getParameterNames();
            if (names != null) {
                while (names.hasMoreElements()) {
                    String name = (String) names.nextElement();
                    String value = request.getParameter(name);
                    value = java.net.URLDecoder.decode(value, "UTF-8");
                    map.put(name, value);
                }
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        PrintUtil.outln(PRINT_STR, map.toString());
        return map;
    }
}
