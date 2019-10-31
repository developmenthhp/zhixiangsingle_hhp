package cn.zhixiangsingle.service.redis;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangpingtai
 * @Package cn.zhixiangsingle.service.redis
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019/8/23 11:34
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
public interface RedisService {
    void set(String key, Object value);
    Object get(String key);
}
