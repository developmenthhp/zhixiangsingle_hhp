package cn.zhixiangsingle.impl.business;

import cn.zhixiangsingle.config.DynamicDataSourceContextHolder;
import cn.zhixiangsingle.dao.business.BusinessMapper;
import cn.zhixiangsingle.dao.site.SiteMapper;
import cn.zhixiangsingle.date.DateUtils;
import cn.zhixiangsingle.entity.base.IsEmptyUtils;
import cn.zhixiangsingle.entity.base.site.SiteData;
import cn.zhixiangsingle.entity.base.site.vo.SiteVO;
import cn.zhixiangsingle.entity.business.dto.BusinessDTO;
import cn.zhixiangsingle.service.business.BusinessService;
import cn.zhixiangsingle.web.responsive.IStatusMessage;
import cn.zhixiangsingle.web.responsive.ResultBean;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangpingtai
 * @Package cn.zhixiangsingle.impl.business
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019/10/9 9:22
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Service(version = "1.0.0", interfaceClass = BusinessService.class)
public class BusinessServiceImpl implements BusinessService {
    private static final Logger logger = LoggerFactory
            .getLogger(BusinessServiceImpl.class);
    @Autowired
    private SiteMapper siteMapper;
    @Autowired
    private BusinessMapper businessMapper;

    /**
     * @version V1.0
     * @Title: zhixiangpingtai
     * @Description:
     * @author: hhp
     * @param:  * @param businessDTO
     * @date: 2019/10/9 9:49
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED,isolation = Isolation.DEFAULT,timeout=30000,rollbackFor={RuntimeException.class, Exception.class})
    public ResultBean updateBusiness(BusinessDTO businessDTO) throws Exception {
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
        if(!IsEmptyUtils.isEmpty(jobj.get(businessDTO.getSdId().toString()))){
            siteData = JSON.parseObject(jobj.get(businessDTO.getSdId().toString()).toString(),SiteData.class);
        }
        String dataSourceName = "";
        if(!IsEmptyUtils.isEmpty(siteData)){
            dataSourceName = siteData.getDateSourceName();
        }
        if(!IsEmptyUtils.isEmpty(dataSourceName)) {
            logger.info("--" + dataSourceName + "---");
            DynamicDataSourceContextHolder.setDataSourceType(dataSourceName);

            businessMapper.updateByPrimaryKey(businessDTO);

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
     * @param:  * @param businessDTO
     * @date: 2019/10/9 9:49
     */
    @Override
    public ResultBean addBusiness(BusinessDTO businessDTO) throws Exception {
        ResultBean resultBean = new ResultBean();
        if(!IsEmptyUtils.isEmpty(businessDTO.getSdIds())){
            StringBuffer msgStr = new StringBuffer();
            String[] sdIds = businessDTO.getSdIds().split(",");
            for(String sdId:sdIds){
                businessDTO.setSdId(Integer.parseInt(sdId));
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
                    logger.info("---" + dataSourceName + "---");
                    DynamicDataSourceContextHolder.setDataSourceType(dataSourceName);
                    BusinessDTO businessDTO1 = new BusinessDTO();
                    businessDTO1.setSdId(Integer.parseInt(sdId));
                    if(IsEmptyUtils.isEmpty(businessMapper.findBusiness(businessDTO1))){
                        businessMapper.insertSelective(businessDTO);
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
            if(!IsEmptyUtils.isEmpty(jobj.get(businessDTO.getSdId().toString()))){
                siteData = JSON.parseObject(jobj.get(businessDTO.getSdId().toString()).toString(),SiteData.class);
            }
            String dataSourceName = "";
            if(!IsEmptyUtils.isEmpty(siteData)){
                dataSourceName = siteData.getDateSourceName();
            }
            if(!IsEmptyUtils.isEmpty(dataSourceName)) {
                logger.info("--" + dataSourceName + "--");
                DynamicDataSourceContextHolder.setDataSourceType(dataSourceName);

                businessMapper.insertSelective(businessDTO);
                BusinessDTO businessDTO1 = new BusinessDTO();
                businessDTO1.setSdId(businessDTO.getSdId());
                if(IsEmptyUtils.isEmpty(businessMapper.findBusiness(businessDTO1))){
                    businessMapper.insertSelective(businessDTO);
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
     * @param:  * @param businessDTO
     * @date: 2019/10/9 9:49
     */
    @Override
    public ResultBean delBusiness(BusinessDTO businessDTO) throws Exception {
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
        if(!IsEmptyUtils.isEmpty(jobj.get(businessDTO.getSdId().toString()))){
            siteData = JSON.parseObject(jobj.get(businessDTO.getSdId().toString()).toString(),SiteData.class);
        }
        String dataSourceName = "";
        if(!IsEmptyUtils.isEmpty(siteData)){
            dataSourceName = siteData.getDateSourceName();
        }
        if(!IsEmptyUtils.isEmpty(dataSourceName)) {
            logger.info("--" + dataSourceName + "---");
            DynamicDataSourceContextHolder.setDataSourceType(dataSourceName);

            businessMapper.deleteByPrimaryKey(businessDTO);
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
     * @param:  * @param businessDTO
     * @date: 2019/10/9 9:49
     */
    @Override
    public ResultBean findBusinessList(BusinessDTO businessDTO) throws Exception {

        return null;
    }
    /**
     * @version V1.0
     * @Title: zhixiangpingtai
     * @Description:
     * @author: hhp
     * @param:  * @param businessDTO
     * @date: 2019/10/9 9:49
     */
    @Override
    public ResultBean findUpdBusiness(BusinessDTO businessDTO) throws Exception {
        ResultBean resultBean = new ResultBean();
        StringBuffer stringBufferJson = new StringBuffer();
        InputStream stream = getClass().getClassLoader().getResourceAsStream("json/siteData.json");
        BufferedReader br = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
        String line;
        while ((line = br.readLine()) != null) {
            stringBufferJson.append(line);
        }
        businessDTO.setEarlyWarningTime("e");
        businessDTO.setPath("p");
        businessDTO.setMainTypes("m");
        businessDTO.setBusinessScope("b");
        businessDTO.setDeadline("d");
        businessDTO.setAddress("a");
        businessDTO.setCompanyName("c");
        businessDTO.setRegisteredCapital("r");
        businessDTO.setLegalRepresentative("l");
        businessDTO.setRegistration("r");
        JSONObject jobj = JSON.parseObject(stringBufferJson.toString());
        if(!IsEmptyUtils.isEmpty(businessDTO.getSdId())){
            String dataSourceName = "";
            SiteData siteData = null;
            if(!IsEmptyUtils.isEmpty(jobj.get(businessDTO.getSdId().toString()))){
                siteData = JSON.parseObject(jobj.get(businessDTO.getSdId().toString()).toString(),SiteData.class);
            }
            businessDTO.setPicturePrefix(siteData.getPicturePrefix());
            if(!IsEmptyUtils.isEmpty(siteData)){
                dataSourceName = siteData.getDateSourceName();
            }
            if(!IsEmptyUtils.isEmpty(dataSourceName)){
                logger.info("--"+dataSourceName+"--");
                DynamicDataSourceContextHolder.setDataSourceType(dataSourceName);

                Map<String,Object> ingredientBaseDTOResult = businessMapper.findBusiness(businessDTO);
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
