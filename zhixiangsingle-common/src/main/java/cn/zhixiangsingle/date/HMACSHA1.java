package cn.zhixiangsingle.date;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangpingtai
 * @Package cn.zhixiangsingle.date
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019/10/10 17:03
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
public class HMACSHA1 {
    private static final String HMAC_SHA1 = "HmacSHA1";
    public static byte[] getSignature(String data, String key) throws Exception {
        Mac mac = Mac.getInstance(HMAC_SHA1);
        SecretKeySpec signingKey = new SecretKeySpec(key.getBytes(),
                mac.getAlgorithm());
        mac.init(signingKey);
        return mac.doFinal(data.getBytes());
    }
}
