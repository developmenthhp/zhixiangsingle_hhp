package cn.zhixiangsingle.web.request;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import com.alibaba.fastjson.JSONObject;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangpingtai
 * @Package cn.zhixiangsingle.web.request
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019/10/29 14:07
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
public class HttpHKSend {
    private static String host;
    private static String appKey;
    private static String appSecret;

    public static JSONObject getHttpResponseJson(Map<String,String> param, String path){
        CloseableHttpClient httpclient = null;
        CloseableHttpResponse response = null;
        JSONObject jsonObject = null;
        try {
            //请求发起客户端
            httpclient = HttpClients.createDefault();
            //参数集合
            List postParams = (List) new ArrayList();
            //遍历参数并添加到集合
            for(Map.Entry<String, String> entry:param.entrySet()){

                postParams.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
            }
            //通过post方式访问
            HttpPost post = new HttpPost(host+"/api/lapp/"+path);
            HttpEntity paramEntity = new UrlEncodedFormEntity(postParams,"UTF-8");
            post.setEntity(paramEntity);
            response = httpclient.execute(post);
            HttpEntity valueEntity = response.getEntity();
            String content = EntityUtils.toString(valueEntity);
            jsonObject = JSONObject.parseObject(content);

            return jsonObject;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            if(httpclient != null){
                try {
                    httpclient.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(response != null){
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return jsonObject;
    }
    public static String getHost() {
        return host;
    }
    public static void setHost(String host) {
        HttpHKSend.host = host;
    }
    public static String getAppKey() {
        return appKey;
    }
    public static void setAppKey(String appKey) {
        HttpHKSend.appKey = appKey;
    }
    public static String getAppSecret() {
        return appSecret;
    }
    public static void setAppSecret(String appSecret) {
        HttpHKSend.appSecret = appSecret;
    }
}
