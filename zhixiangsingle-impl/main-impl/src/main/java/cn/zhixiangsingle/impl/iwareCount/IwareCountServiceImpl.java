package cn.zhixiangsingle.impl.iwareCount;

import cn.zhixiangsingle.config.DynamicDataSourceContextHolder;
import cn.zhixiangsingle.dao.iwareCount.IwareCountMapper;
import cn.zhixiangsingle.dao.site.SiteMapper;
import cn.zhixiangsingle.date.DateUtils;
import cn.zhixiangsingle.entity.base.IsEmptyUtils;
import cn.zhixiangsingle.entity.base.site.SiteData;
import cn.zhixiangsingle.entity.iwareCount.dto.IwareCountDTO;
import cn.zhixiangsingle.service.iwareCount.IwareCountService;
import cn.zhixiangsingle.web.responsive.IStatusMessage;
import cn.zhixiangsingle.web.responsive.ResultBean;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.Map;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangpingtai
 * @Package cn.zhixiangsingle.impl.iwareCount
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019/10/25 12:49
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Service(version = "1.0.0", interfaceClass = IwareCountService.class)
public class IwareCountServiceImpl implements IwareCountService {
    private static final Logger logger = LoggerFactory
            .getLogger(IwareCountServiceImpl.class);
    @Autowired
    private SiteMapper siteMapper;
    @Autowired
    private IwareCountMapper iwareCountMapper;

    @Override
    public ResultBean findIwareCountList(IwareCountDTO iwareCountDTO) throws Exception {
        return null;
    }
    /**
     * @version V1.0
     * @Title: zhixiangpingtai
     * @Description:
     * @author: hhp
     * @param:  * @param iwareCountDTO
     * @date: 2019/10/25 16:12
     */
    @Override
    public ResultBean transLPurchase(IwareCountDTO iwareCountDTO) throws Exception {
        ResultBean resultBean = new ResultBean();
        iwareCountDTO.setZdTime(DateUtils.formatDate(new Date(),"yyyy-MM-dd HH:mm:ss"));
        StringBuffer stringBufferJson = new StringBuffer();
        InputStream stream = getClass().getClassLoader().getResourceAsStream("json/siteData.json");
        BufferedReader br = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
        String line;
        while ((line = br.readLine()) != null) {
            stringBufferJson.append(line);
        }
        JSONObject jobj = JSON.parseObject(stringBufferJson.toString());

        JSONObject idsJson = JSON.parseObject(iwareCountDTO.getWareIds());
        for (Map.Entry<String, Object> entry : idsJson.entrySet()) {
            SiteData idSiteData = JSON.parseObject(entry.getValue().toString(),SiteData.class);
            Integer wareCount = idSiteData.getName().split(",").length;
            iwareCountDTO.setWareCount(String.valueOf(wareCount));
            iwareCountDTO.setWareIds(idSiteData.getName());
            iwareCountDTO.setSdId(Integer.parseInt(entry.getKey()));
            iwareCountDTO.setHideStatus("1");
            SiteData siteData = null;
            if(!IsEmptyUtils.isEmpty(jobj.get(entry.getKey()))){
                siteData = JSON.parseObject(jobj.get(entry.getKey()).toString(),SiteData.class);
            }
            String dataSourceName = "";
            if(!IsEmptyUtils.isEmpty(siteData)){
                dataSourceName = siteData.getDateSourceName();
            }
            if(!IsEmptyUtils.isEmpty(dataSourceName)) {
                logger.info("-" + dataSourceName + "");
                DynamicDataSourceContextHolder.setDataSourceType(dataSourceName);
                iwareCountMapper.insertSelective(iwareCountDTO);
                resultBean.setSuccess(true);
                resultBean.setResultCode(IStatusMessage.SystemStatus.SUCCESS.getCode());
                resultBean.setMsg(IStatusMessage.SystemStatus.SUCCESS.getMessage());
            }
        }
        DynamicDataSourceContextHolder.setDataSourceType(null);
        return resultBean;
    }
}
