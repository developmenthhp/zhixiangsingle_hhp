package cn.zhixiangsingle.impl.companyInformation;

import cn.zhixiangsingle.config.DynamicDataSourceContextHolder;
import cn.zhixiangsingle.dao.companyInformation.CompanyInformationMapper;
import cn.zhixiangsingle.dao.site.SiteMapper;
import cn.zhixiangsingle.entity.base.IsEmptyUtils;
import cn.zhixiangsingle.entity.base.site.SiteData;
import cn.zhixiangsingle.entity.base.site.vo.SiteVO;
import cn.zhixiangsingle.entity.companyInformation.dto.CompanyInformationDTO;
import cn.zhixiangsingle.service.companyInformation.CompanyInformationService;
import cn.zhixiangsingle.web.responsive.IStatusMessage;
import cn.zhixiangsingle.web.responsive.ResultBean;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangpingtai
 * @Package cn.zhixiangsingle.impl.companyInformation
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019/10/12 16:23
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Service(version = "1.0.0", interfaceClass = CompanyInformationService.class)
public class CompanyInformationServiceImpl implements CompanyInformationService {
    private static final Logger logger = LoggerFactory
            .getLogger(CompanyInformationServiceImpl.class);
    @Autowired
    private SiteMapper siteMapper;
    @Autowired
    private CompanyInformationMapper companyInformationMapper;
    /**
     * @version V1.0
     * @Title: zhixiangpingtai
     * @Description:
     * @author: hhp
     * @param:  * @param companyInformationDTO
     * @date: 2019/10/12 17:52
     */
    @Override
    public ResultBean findCompanyInformation(CompanyInformationDTO companyInformationDTO) throws Exception {
        ResultBean resultBean = new ResultBean();
        List<Map<String,Object>> companyLicenses = Lists.newArrayList();
        StringBuffer stringBufferJson = new StringBuffer();
        InputStream stream = getClass().getClassLoader().getResourceAsStream("json/siteData.json");
        BufferedReader br = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
        String line;
        while ((line = br.readLine()) != null) {
            stringBufferJson.append(line);
        }
        JSONObject jobj = JSON.parseObject(stringBufferJson.toString());

        companyInformationDTO.setId(-1);
        companyInformationDTO.setBusinessHours("b");
        companyInformationDTO.setCommitmentFoodSafetyImg("c");
        companyInformationDTO.setCompanyName("c");
        companyInformationDTO.setContactWay("c");
        companyInformationDTO.setDetailedAddress("d");
        companyInformationDTO.setDiningType(-1);
        companyInformationDTO.setFoodsafetyName("f");
        companyInformationDTO.setFoodsafeyImg("f");
        companyInformationDTO.setFoodsafeyPhone("f");
        companyInformationDTO.setOperatingState(-1);
        companyInformationDTO.setPrincipal("p");
        companyInformationDTO.setSupervisorImg("s");
        companyInformationDTO.setSupervisorName("s");
        companyInformationDTO.setSupervisorPhone("s");

        if(!IsEmptyUtils.isEmpty(companyInformationDTO.getSdIds())){
            String[] sdIds = companyInformationDTO.getSdIds().split(",");
            StringBuffer stringBuffer = new StringBuffer();
            for(String sdId:sdIds){
                companyInformationDTO.setSdId(Integer.parseInt(sdId));
                String dataSourceName = "";
                SiteData siteData = null;
                if(!IsEmptyUtils.isEmpty(jobj.get(sdId))){
                    siteData = JSON.parseObject(jobj.get(sdId).toString(),SiteData.class);
                }
                companyInformationDTO.setPicturePrefix(siteData.getPicturePrefix());
                if(!IsEmptyUtils.isEmpty(siteData)){
                    dataSourceName = siteData.getDateSourceName();
                }
                if(!IsEmptyUtils.isEmpty(dataSourceName)){
                    logger.info("--"+siteData.getDateSourceName()+"--");
                    DynamicDataSourceContextHolder.setDataSourceType(dataSourceName);
                    companyInformationDTO.setSiteName(siteData.getName());
                    companyInformationDTO.setSitePhoto(siteData.getPhoto());

                    Map<String,Object> map = companyInformationMapper.findCompanyInformation(companyInformationDTO);
                    companyLicenses.add(map);
                }
            }
            DynamicDataSourceContextHolder.setDataSourceType(null);
        }else{
            List<SiteVO> userSites = siteMapper.findUserSites(companyInformationDTO.getUserId());
            StringBuffer stringBuffer = new StringBuffer();
            for(SiteVO siteVO:userSites){
                companyInformationDTO.setSdId(siteVO.getSdId());
                String dataSourceName = "";
                String sdIdStr = siteVO.getSdId().toString();
                SiteData siteData = null;
                if(!IsEmptyUtils.isEmpty(jobj.get(sdIdStr))){
                    siteData = JSON.parseObject(jobj.get(sdIdStr).toString(),SiteData.class);
                }
                companyInformationDTO.setPicturePrefix(siteData.getPicturePrefix());

                if(!IsEmptyUtils.isEmpty(siteData)){
                    dataSourceName = siteData.getDateSourceName();
                }
                if(!IsEmptyUtils.isEmpty(dataSourceName)){
                    logger.info("--"+siteData.getDateSourceName()+"--null sdIdStr");
                    DynamicDataSourceContextHolder.setDataSourceType(dataSourceName);
                    companyInformationDTO.setSiteName(siteData.getName());
                    companyInformationDTO.setSitePhoto(siteData.getPhoto());

                    Map<String,Object> map = companyInformationMapper.findCompanyInformation(companyInformationDTO);
                    companyLicenses.add(map);
                }
            }
            DynamicDataSourceContextHolder.setDataSourceType(null);
        }
        resultBean.setSuccess(true);
        resultBean.setResult(companyLicenses);
        resultBean.setResultCode(IStatusMessage.SystemStatus.SUCCESS.getCode());
        resultBean.setMsg(IStatusMessage.SystemStatus.SUCCESS.getMessage());
        return resultBean;
    }
    /**
     * @version V1.0
     * @Title: zhixiangpingtai
     * @Description:
     * @author: hhp
     * @param:  * @param companyInformationDTO
     * @date: 2019/10/12 17:53
     */
    @Override
    public ResultBean updateCompanyInformation(CompanyInformationDTO companyInformationDTO) throws Exception {
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
        if(!IsEmptyUtils.isEmpty(jobj.get(companyInformationDTO.getSdId().toString()))){
            siteData = JSON.parseObject(jobj.get(companyInformationDTO.getSdId().toString()).toString(),SiteData.class);
        }
        String dataSourceName = "";
        if(!IsEmptyUtils.isEmpty(siteData)){
            dataSourceName = siteData.getDateSourceName();
        }
        if(!IsEmptyUtils.isEmpty(dataSourceName)) {
            logger.info("----" + dataSourceName + "-");
            DynamicDataSourceContextHolder.setDataSourceType(dataSourceName);

            companyInformationMapper.updateByPrimaryKey(companyInformationDTO);

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
     * @param:  * @param companyInformationDTO
     * @date: 2019/10/12 17:53
     */
    @Override
    public ResultBean addCompanyInformation(CompanyInformationDTO companyInformationDTO) throws Exception {
        ResultBean resultBean = new ResultBean();
        if(!IsEmptyUtils.isEmpty(companyInformationDTO.getSdIds())){
            StringBuffer msgStr = new StringBuffer();
            String[] sdIds = companyInformationDTO.getSdIds().split(",");
            for(String sdId:sdIds){
                companyInformationDTO.setSdId(Integer.parseInt(sdId));
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
                    CompanyInformationDTO companyInformationDTO1 = new CompanyInformationDTO();
                    companyInformationDTO1.setSdId(Integer.parseInt(sdId));
                    if(IsEmptyUtils.isEmpty(companyInformationMapper.findCompanyInformation(companyInformationDTO1))){
                        companyInformationMapper.insertSelective(companyInformationDTO);
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
            if(!IsEmptyUtils.isEmpty(jobj.get(companyInformationDTO.getSdId().toString()))){
                siteData = JSON.parseObject(jobj.get(companyInformationDTO.getSdId().toString()).toString(),SiteData.class);
            }
            String dataSourceName = "";
            if(!IsEmptyUtils.isEmpty(siteData)){
                dataSourceName = siteData.getDateSourceName();
            }
            if(!IsEmptyUtils.isEmpty(dataSourceName)) {
                logger.info("-" + dataSourceName + "--");
                DynamicDataSourceContextHolder.setDataSourceType(dataSourceName);
                CompanyInformationDTO companyInformationDTO1 = new CompanyInformationDTO();
                companyInformationDTO1.setSdId(companyInformationDTO.getSdId());
                if(IsEmptyUtils.isEmpty(companyInformationMapper.findCompanyInformation(companyInformationDTO1))){
                    companyInformationMapper.insertSelective(companyInformationDTO);
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
     * @param:  * @param companyInformationDTO
     * @date: 2019/10/14 18:48
     */
    @Override
    public ResultBean findUpdCompanyInformation(CompanyInformationDTO companyInformationDTO) throws Exception {
        ResultBean resultBean = new ResultBean();
        StringBuffer stringBufferJson = new StringBuffer();
        InputStream stream = getClass().getClassLoader().getResourceAsStream("json/siteData.json");
        BufferedReader br = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
        String line;
        while ((line = br.readLine()) != null) {
            stringBufferJson.append(line);
        }
        companyInformationDTO.setSupervisorPhone("s");
        companyInformationDTO.setSupervisorName("s");
        companyInformationDTO.setSupervisorImg("s");
        companyInformationDTO.setPrincipal("p");
        companyInformationDTO.setOperatingState(-1);
        companyInformationDTO.setFoodsafeyPhone("f");
        companyInformationDTO.setFoodsafeyImg("f");
        companyInformationDTO.setFoodsafetyName("f");
        companyInformationDTO.setDiningType(-1);
        companyInformationDTO.setDetailedAddress("d");
        companyInformationDTO.setContactWay("c");
        companyInformationDTO.setCompanyName("c");
        companyInformationDTO.setCommitmentFoodSafetyImg("c");
        companyInformationDTO.setSupervisorImg("s");
        companyInformationDTO.setBusinessHours("b");
        companyInformationDTO.setId(-1);
        JSONObject jobj = JSON.parseObject(stringBufferJson.toString());
        if(!IsEmptyUtils.isEmpty(companyInformationDTO.getSdId())){
            String dataSourceName = "";
            SiteData siteData = null;
            if(!IsEmptyUtils.isEmpty(jobj.get(companyInformationDTO.getSdId().toString()))){
                siteData = JSON.parseObject(jobj.get(companyInformationDTO.getSdId().toString()).toString(),SiteData.class);
            }
            companyInformationDTO.setPicturePrefix(siteData.getPicturePrefix());
            if(!IsEmptyUtils.isEmpty(siteData)){
                dataSourceName = siteData.getDateSourceName();
            }
            if(!IsEmptyUtils.isEmpty(dataSourceName)){
                logger.info("--"+dataSourceName+"--");
                DynamicDataSourceContextHolder.setDataSourceType(dataSourceName);

                Map<String,Object> ingredientBaseDTOResult = companyInformationMapper.findCompanyInformation(companyInformationDTO);
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
    /**
     * @version V1.0
     * @Title: zhixiangpingtai
     * @Description:
     * @author: hhp
     * @param:  * @param companyInformationDTO
     * @date: 2019/10/14 18:48
     */
    @Override
    public ResultBean delCompanyInformation(CompanyInformationDTO companyInformationDTO) throws Exception {
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
        if(!IsEmptyUtils.isEmpty(jobj.get(companyInformationDTO.getSdId().toString()))){
            siteData = JSON.parseObject(jobj.get(companyInformationDTO.getSdId().toString()).toString(),SiteData.class);
        }
        String dataSourceName = "";
        if(!IsEmptyUtils.isEmpty(siteData)){
            dataSourceName = siteData.getDateSourceName();
        }
        if(!IsEmptyUtils.isEmpty(dataSourceName)) {
            logger.info("----" + dataSourceName + "----");
            DynamicDataSourceContextHolder.setDataSourceType(dataSourceName);

            companyInformationMapper.deleteByPrimaryKey(companyInformationDTO);
            DynamicDataSourceContextHolder.setDataSourceType(null);
        }
        resultBean.setSuccess(true);
        resultBean.setResultCode(IStatusMessage.SystemStatus.SUCCESS.getCode());
        resultBean.setMsg(IStatusMessage.SystemStatus.SUCCESS.getMessage());
        return resultBean;
    }
}
