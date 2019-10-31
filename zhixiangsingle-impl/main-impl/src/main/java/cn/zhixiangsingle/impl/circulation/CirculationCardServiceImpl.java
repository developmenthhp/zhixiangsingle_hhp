package cn.zhixiangsingle.impl.circulation;

import cn.zhixiangsingle.config.DynamicDataSourceContextHolder;
import cn.zhixiangsingle.dao.circulation.CirculationCardMapper;
import cn.zhixiangsingle.dao.site.SiteMapper;
import cn.zhixiangsingle.entity.base.IsEmptyUtils;
import cn.zhixiangsingle.entity.base.site.SiteData;
import cn.zhixiangsingle.entity.circulation.dto.CirculationCardDTO;
import cn.zhixiangsingle.service.circulation.CirculationCardService;
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
import java.util.Map;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangpingtai
 * @Package cn.zhixiangsingle.impl.circulation
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019/10/9 10:56
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Service(version = "1.0.0", interfaceClass = CirculationCardService.class)
public class CirculationCardServiceImpl implements CirculationCardService {
    private static final Logger logger = LoggerFactory
            .getLogger(CirculationCardServiceImpl.class);
    @Autowired
    private SiteMapper siteMapper;
    @Autowired
    private CirculationCardMapper circulationCardMapper;
    /**
     * @version V1.0
     * @Title: zhixiangpingtai
     * @Description:
     * @author: hhp
     * @param:  * @param id
     * @date: 2019/10/14 19:00
     */
    @Override
    public ResultBean updateCirculationCard(CirculationCardDTO circulationCardDTO) throws Exception {
        ResultBean resultBean = new ResultBean();
        StringBuffer stringBufferJson = new StringBuffer();
        InputStream stream = getClass().getClassLoader().getResourceAsStream("json/siteData.json");
        BufferedReader br = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
        String line;
        while ((line = br.readLine()) != null) {
            stringBufferJson.append(line);
        }
        JSONObject jobj = JSON.parseObject(stringBufferJson.toString());
        SiteData siteData = null;
        if(!IsEmptyUtils.isEmpty(jobj.get(circulationCardDTO.getSdId().toString()))){
            siteData = JSON.parseObject(jobj.get(circulationCardDTO.getSdId().toString()).toString(),SiteData.class);
        }
        String dataSourceName = "";
        if(!IsEmptyUtils.isEmpty(siteData)){
            dataSourceName = siteData.getDateSourceName();
        }
        if(!IsEmptyUtils.isEmpty(dataSourceName)) {
            logger.info("----" + dataSourceName + "---");
            DynamicDataSourceContextHolder.setDataSourceType(dataSourceName);

            circulationCardMapper.updateByPrimaryKey(circulationCardDTO);

            DynamicDataSourceContextHolder.setDataSourceType(null);
        }
        resultBean.setSuccess(true);
        resultBean.setResultCode(IStatusMessage.SystemStatus.SUCCESS.getCode());
        resultBean.setMsg(IStatusMessage.SystemStatus.SUCCESS.getMessage());
        return resultBean;
    }
    /**
     * @version V1.0
     * @Title: zhixiangpingtai
     * @Description:
     * @author: hhp
     * @param:  * @param circulationCardDTO
     * @date: 2019/10/14 19:01
     */
    @Override
    public ResultBean addCirculationCard(CirculationCardDTO circulationCardDTO) throws Exception{
        ResultBean resultBean = new ResultBean();
        if(!IsEmptyUtils.isEmpty(circulationCardDTO.getSdIds())){
            StringBuffer msgStr = new StringBuffer();
            String[] sdIds = circulationCardDTO.getSdIds().split(",");
            for(String sdId:sdIds){
                circulationCardDTO.setSdId(Integer.parseInt(sdId));
                StringBuffer stringBufferJson = new StringBuffer();
                InputStream stream = getClass().getClassLoader().getResourceAsStream("json/siteData.json");
                BufferedReader br = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
                String line;
                while ((line = br.readLine()) != null) {
                    stringBufferJson.append(line);
                }
                JSONObject jobj = JSON.parseObject(stringBufferJson.toString());
                SiteData siteData = null;
                if(!IsEmptyUtils.isEmpty(jobj.get(sdId))){
                    siteData = JSON.parseObject(jobj.get(sdId).toString(),SiteData.class);
                }
                String dataSourceName = "";
                if(!IsEmptyUtils.isEmpty(siteData)){
                    dataSourceName = siteData.getDateSourceName();
                }
                if(!IsEmptyUtils.isEmpty(dataSourceName)) {
                    logger.info("---" + dataSourceName + "--");
                    DynamicDataSourceContextHolder.setDataSourceType(dataSourceName);
                    CirculationCardDTO circulationCardDTO1 = new CirculationCardDTO();
                    circulationCardDTO1.setSdId(Integer.parseInt(sdId));
                    if(IsEmptyUtils.isEmpty(circulationCardMapper.findCirculationCard(circulationCardDTO1))){
                        circulationCardMapper.insertSelective(circulationCardDTO);
                    }else{
                        if(IsEmptyUtils.isEmpty(msgStr)){
                            msgStr.append(siteData.getName());
                        }else{
                            msgStr.append(","+siteData.getName());
                        }
                    }

                }
            }
            DynamicDataSourceContextHolder.setDataSourceType(null);
            resultBean.setSuccess(true);
            resultBean.setResultCode(IStatusMessage.SystemStatus.SUCCESS.getCode());
            if(!IsEmptyUtils.isEmpty(msgStr)){
                resultBean.setMsg(IStatusMessage.SystemStatus.SUCCESS.getMessage()+"(其中"+msgStr+"已提交，请勿重复提交)");
            }else{
                resultBean.setMsg(IStatusMessage.SystemStatus.SUCCESS.getMessage());
            }
        }else{
            StringBuffer stringBufferJson = new StringBuffer();
            InputStream stream = getClass().getClassLoader().getResourceAsStream("json/siteData.json");
            BufferedReader br = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
            String line;
            while ((line = br.readLine()) != null) {
                stringBufferJson.append(line);
            }
            JSONObject jobj = JSON.parseObject(stringBufferJson.toString());
            SiteData siteData = null;
            if(!IsEmptyUtils.isEmpty(jobj.get(circulationCardDTO.getSdId().toString()))){
                siteData = JSON.parseObject(jobj.get(circulationCardDTO.getSdId().toString()).toString(),SiteData.class);
            }
            String dataSourceName = "";
            if(!IsEmptyUtils.isEmpty(siteData)){
                dataSourceName = siteData.getDateSourceName();
            }
            if(!IsEmptyUtils.isEmpty(dataSourceName)) {
                logger.info("---" + dataSourceName + "--");
                DynamicDataSourceContextHolder.setDataSourceType(dataSourceName);
                CirculationCardDTO circulationCardDTO1 = new CirculationCardDTO();
                circulationCardDTO1.setSdId(circulationCardDTO.getSdId());
                if(IsEmptyUtils.isEmpty(circulationCardMapper.findCirculationCard(circulationCardDTO1))){
                    circulationCardMapper.insertSelective(circulationCardDTO);
                    resultBean.setSuccess(true);
                    resultBean.setResultCode(IStatusMessage.SystemStatus.SUCCESS.getCode());
                    resultBean.setMsg(IStatusMessage.SystemStatus.SUCCESS.getMessage());
                }else{
                    resultBean.setSuccess(false);
                    resultBean.setResultCode(IStatusMessage.SystemStatus.DATA_EXITS.getCode());
                    resultBean.setMsg(IStatusMessage.SystemStatus.DATA_EXITS.getMessage());
                }

                DynamicDataSourceContextHolder.setDataSourceType(null);
            }
        }
        return resultBean;
    }
    /**
     * @version V1.0
     * @Title: zhixiangpingtai
     * @Description:
     * @author: hhp
     * @param:  * @param circulationCardDTO
     * @date: 2019/10/14 19:01
     */
    @Override
    public ResultBean delCirculationCard(CirculationCardDTO circulationCardDTO) throws Exception{
        ResultBean resultBean = new ResultBean();
        StringBuffer stringBufferJson = new StringBuffer();
        InputStream stream = getClass().getClassLoader().getResourceAsStream("json/siteData.json");
        BufferedReader br = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
        String line;
        while ((line = br.readLine()) != null) {
            stringBufferJson.append(line);
        }
        JSONObject jobj = JSON.parseObject(stringBufferJson.toString());
        SiteData siteData = null;
        if(!IsEmptyUtils.isEmpty(jobj.get(circulationCardDTO.getSdId().toString()))){
            siteData = JSON.parseObject(jobj.get(circulationCardDTO.getSdId().toString()).toString(),SiteData.class);
        }
        String dataSourceName = "";
        if(!IsEmptyUtils.isEmpty(siteData)){
            dataSourceName = siteData.getDateSourceName();
        }
        if(!IsEmptyUtils.isEmpty(dataSourceName)) {
            logger.info("----" + dataSourceName + "---");
            DynamicDataSourceContextHolder.setDataSourceType(dataSourceName);

            circulationCardMapper.deleteByPrimaryKey(circulationCardDTO);
            DynamicDataSourceContextHolder.setDataSourceType(null);
        }
        resultBean.setSuccess(true);
        resultBean.setResultCode(IStatusMessage.SystemStatus.SUCCESS.getCode());
        resultBean.setMsg(IStatusMessage.SystemStatus.SUCCESS.getMessage());
        return resultBean;
    }
    /**
     * @version V1.0
     * @Title: zhixiangpingtai
     * @Description:
     * @author: hhp
     * @param:  * @param circulationCardDTO
     * @date: 2019/10/14 19:02
     */
    @Override
    public ResultBean findUpdCirculationCard(CirculationCardDTO circulationCardDTO) throws Exception {
        ResultBean resultBean = new ResultBean();
        StringBuffer stringBufferJson = new StringBuffer();
        InputStream stream = getClass().getClassLoader().getResourceAsStream("json/siteData.json");
        BufferedReader br = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
        String line;
        while ((line = br.readLine()) != null) {
            stringBufferJson.append(line);
        }
        circulationCardDTO.setCirculationCardImg("c");
        circulationCardDTO.setSerialNumber("s");
        circulationCardDTO.setWarningDate("w");
        circulationCardDTO.setValidTime("v");
        circulationCardDTO.setRepresentative("r");
        circulationCardDTO.setName("n");
        JSONObject jobj = JSON.parseObject(stringBufferJson.toString());
        if(!IsEmptyUtils.isEmpty(circulationCardDTO.getSdId())){
            String dataSourceName = "";
            SiteData siteData = null;
            if(!IsEmptyUtils.isEmpty(jobj.get(circulationCardDTO.getSdId().toString()))){
                siteData = JSON.parseObject(jobj.get(circulationCardDTO.getSdId().toString()).toString(),SiteData.class);
            }
            circulationCardDTO.setPicturePrefix(siteData.getPicturePrefix());
            if(!IsEmptyUtils.isEmpty(siteData)){
                dataSourceName = siteData.getDateSourceName();
            }
            if(!IsEmptyUtils.isEmpty(dataSourceName)){
                logger.info("--"+dataSourceName+"--");
                DynamicDataSourceContextHolder.setDataSourceType(dataSourceName);

                Map<String,Object> ingredientBaseDTOResult = circulationCardMapper.findCirculationCard(circulationCardDTO);
                resultBean.setResult(ingredientBaseDTOResult);
            }
            resultBean.setSuccess(true);
            resultBean.setResultCode(IStatusMessage.SystemStatus.SUCCESS.getCode());
            resultBean.setMsg(IStatusMessage.SystemStatus.SUCCESS.getMessage());
            DynamicDataSourceContextHolder.setDataSourceType(null);
        }else{
            resultBean.setSuccess(false);
            resultBean.setResultCode(IStatusMessage.SystemStatus.SITE_ID_LOSE.getCode());
            resultBean.setMsg(IStatusMessage.SystemStatus.SITE_ID_LOSE.getMessage());
        }
        return resultBean;
    }
}
