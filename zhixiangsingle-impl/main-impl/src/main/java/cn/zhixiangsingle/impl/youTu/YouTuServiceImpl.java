package cn.zhixiangsingle.impl.youTu;

import cn.zhixiangsingle.entity.base.IsEmptyUtils;
import cn.zhixiangsingle.entity.base.thirdParty.ThirdParty;
import cn.zhixiangsingle.entity.base.user.User;
import cn.zhixiangsingle.entity.base.youTu.YouTu;
import cn.zhixiangsingle.entity.business.dto.BusinessDTO;
import cn.zhixiangsingle.json.JSONUtils;
import cn.zhixiangsingle.service.youTu.YouTuService;
import cn.zhixiangsingle.web.youTu.YuoTuRequest;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import org.codehaus.jackson.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangpingtai
 * @Package cn.zhixiangsingle.impl.youTu
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019/10/10 16:34
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Service(version = "1.0.0", interfaceClass = YouTuService.class)
public class YouTuServiceImpl implements YouTuService {
    private static final Logger logger = LoggerFactory
            .getLogger(YouTuServiceImpl.class);
    /**
     * @version V1.0
     * @Title: zhixiangpingtai
     * @Description:
     * @author: hhp
     * @param:  * @param imgPath
     * @param user
     * @date: 2019/10/14 19:08
     */
    @Override
    public Map<String,Object> simpleCompanyOCR(String imgPath,User user) throws Exception{
        imgPath = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1570709949940&di=df94c75fd7955926558c16c9bffad4d6&imgtype=0&src=http%3A%2F%2Fimg.mp.itc.cn%2Fq_70%2Cc_zoom%2Cw_640%2Fupload%2F20161221%2F9a0de9462959453e893de45d75e45670_th.jpg";
        Map<String,Object> businessDTO = new HashMap<>();
        StringBuffer stringBuffer = new StringBuffer();
        InputStream stream = getClass().getClassLoader().getResourceAsStream("json/yTuHKLCData.json");
        BufferedReader br = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
        String line;
        while ((line = br.readLine()) != null) {
            stringBuffer.append(line);
        }
        com.alibaba.fastjson.JSONObject jobj = JSON.parseObject(stringBuffer.toString());
        ThirdParty thirdParty = null;
        if(!IsEmptyUtils.isEmpty(jobj.get(user.getSdId().toString()))){
            thirdParty = JSON.parseObject(jobj.get(user.getSdId().toString()).toString(),ThirdParty.class);
        }
        YouTu youTu = thirdParty.getYouTu();
        YuoTuRequest faceYoutu = new YuoTuRequest(youTu.getAppId(), youTu.getSecretId(), youTu.getSecretKey(),youTu.getEndPointUrl(),youTu.getUserId());
        org.json.JSONObject respose=faceYoutu.companyDiscern(imgPath);

        Map<String,Object> c1 = JSONUtils.getObjFromString(respose.toString(),
                new TypeReference<Map<String,Object>>() {
                });

        List<Map<String,Object>> cc=(List<Map<String, Object>>) c1.get("items");

        if(cc!=null && cc.size()>0){
            for(Map<String,Object> v:cc){

                String items = (String) v.get("item");
                String itemstring = (String) v.get("itemstring");

                if(items.equals("注册号")){
                    businessDTO.put("registration",itemstring);
                }else if(items.equals("法定代表人")){
                    businessDTO.put("legalRepresentative",itemstring);
                }
                else if(items.equals("注册资本")){
                    businessDTO.put("registeredCapital",itemstring);
                }
                else if(items.equals("公司名称")){
                    businessDTO.put("companyName",itemstring);
                }
                else if(items.equals("地址")){
                    businessDTO.put("address",itemstring);
                }
                else if(items.equals("营业期限")){
                    businessDTO.put("deadline",itemstring);
                }
                else if(items.equals("经营范围")){
                    businessDTO.put("businessScope",itemstring);
                }
                else if(items.equals("主体类型")){
                    businessDTO.put("mainTypes",itemstring);
                }
            }
        }
        return businessDTO;
    }
}
