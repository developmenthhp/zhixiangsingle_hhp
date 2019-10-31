package cn.zhixiangsingle.impl.companyHonor;

import cn.zhixiangsingle.config.DynamicDataSourceContextHolder;
import cn.zhixiangsingle.dao.companyHonor.CompanyHonorMapper;
import cn.zhixiangsingle.dao.site.SiteMapper;
import cn.zhixiangsingle.entity.base.IsEmptyUtils;
import cn.zhixiangsingle.entity.base.site.SiteData;
import cn.zhixiangsingle.entity.base.site.vo.SiteVO;
import cn.zhixiangsingle.entity.companyHonor.dto.CompanyHonorDTO;
import cn.zhixiangsingle.service.companyHonor.CompanyHonorService;
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
 * @Package cn.zhixiangsingle.impl.companyHonor
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019/10/14 18:44
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Service(version = "1.0.0", interfaceClass = CompanyHonorService.class)
public class CompanyHonorServiceImpl implements CompanyHonorService {
    private static final Logger logger = LoggerFactory
            .getLogger(CompanyHonorServiceImpl.class);
    @Autowired
    private SiteMapper siteMapper;
    @Autowired
    private CompanyHonorMapper companyHonorMapper;
    /**
     * @version V1.0
     * @Title: zhixiangpingtai
     * @Description:
     * @author: hhp
     * @param:  * @param companyHonorDTO
     * @date: 2019/10/14 18:59
     */
    @Override
    public ResultBean delCompanyHonor(CompanyHonorDTO companyHonorDTO) throws Exception {
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
        if(!IsEmptyUtils.isEmpty(jobj.get(companyHonorDTO.getSdId().toString()))){
            siteData = JSON.parseObject(jobj.get(companyHonorDTO.getSdId().toString()).toString(),SiteData.class);
        }
        String dataSourceName = "";
        if(!IsEmptyUtils.isEmpty(siteData)){
            dataSourceName = siteData.getDateSourceName();
        }
        if(!IsEmptyUtils.isEmpty(dataSourceName)) {
            logger.info("--" + dataSourceName + "----");
            DynamicDataSourceContextHolder.setDataSourceType(dataSourceName);

            companyHonorMapper.deleteByPrimaryKey(companyHonorDTO);
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
     * @param:  * @param companyHonorDTO
     * @date: 2019/10/14 18:59
     */
    @Override
    public ResultBean findUpdCompanyHonor(CompanyHonorDTO companyHonorDTO) throws Exception {
        ResultBean resultBean = new ResultBean();
        StringBuffer stringBufferJson = new StringBuffer();
        InputStream stream = getClass().getClassLoader().getResourceAsStream("json/siteData.json");
        BufferedReader br = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
        String line;
        while ((line = br.readLine()) != null) {
            stringBufferJson.append(line);
        }
        companyHonorDTO.setCertificateImg("c");
        companyHonorDTO.setZhengPaiImg("z");
        companyHonorDTO.setId(-1);
        JSONObject jobj = JSON.parseObject(stringBufferJson.toString());
        if(!IsEmptyUtils.isEmpty(companyHonorDTO.getSdId())){
            String dataSourceName = "";
            SiteData siteData = null;
            if(!IsEmptyUtils.isEmpty(jobj.get(companyHonorDTO.getSdId().toString()))){
                siteData = JSON.parseObject(jobj.get(companyHonorDTO.getSdId().toString()).toString(),SiteData.class);
            }
            companyHonorDTO.setPicturePrefix(siteData.getPicturePrefix());
            if(!IsEmptyUtils.isEmpty(siteData)){
                dataSourceName = siteData.getDateSourceName();
            }
            if(!IsEmptyUtils.isEmpty(dataSourceName)){
                logger.info("--"+dataSourceName+"--");
                DynamicDataSourceContextHolder.setDataSourceType(dataSourceName);

                Map<String,Object> ingredientBaseDTOResult = companyHonorMapper.findCompanyHonor(companyHonorDTO);
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
     * @param:  * @param companyHonorDTO
     * @date: 2019/10/14 18:59
     */
    @Override
    public ResultBean addCompanyHonor(CompanyHonorDTO companyHonorDTO) throws Exception {
        ResultBean resultBean = new ResultBean();
        if(!IsEmptyUtils.isEmpty(companyHonorDTO.getSdIds())){
            StringBuffer msgStr = new StringBuffer();
            String[] sdIds = companyHonorDTO.getSdIds().split(",");
            for(String sdId:sdIds){
                companyHonorDTO.setSdId(Integer.parseInt(sdId));
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
                    CompanyHonorDTO companyHonorDTO1 = new CompanyHonorDTO();
                    companyHonorDTO1.setSdId(Integer.parseInt(sdId));
                    if(IsEmptyUtils.isEmpty(companyHonorMapper.findCompanyHonor(companyHonorDTO1))){
                        companyHonorMapper.insertSelective(companyHonorDTO);
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
            if(!IsEmptyUtils.isEmpty(jobj.get(companyHonorDTO.getSdId().toString()))){
                siteData = JSON.parseObject(jobj.get(companyHonorDTO.getSdId().toString()).toString(),SiteData.class);
            }
            String dataSourceName = "";
            if(!IsEmptyUtils.isEmpty(siteData)){
                dataSourceName = siteData.getDateSourceName();
            }
            if(!IsEmptyUtils.isEmpty(dataSourceName)) {
                logger.info("-" + dataSourceName + "--");
                DynamicDataSourceContextHolder.setDataSourceType(dataSourceName);
                CompanyHonorDTO companyHonorDTO1 = new CompanyHonorDTO();
                companyHonorDTO1.setSdId(companyHonorDTO.getSdId());
                if(IsEmptyUtils.isEmpty(companyHonorMapper.findCompanyHonor(companyHonorDTO1))){
                    companyHonorMapper.insertSelective(companyHonorDTO);
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
     * @param:  * @param companyHonorDTO
     * @date: 2019/10/14 18:59
     */
    @Override
    public ResultBean updateCompanyHonor(CompanyHonorDTO companyHonorDTO) throws Exception {
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
        if(!IsEmptyUtils.isEmpty(jobj.get(companyHonorDTO.getSdId().toString()))){
            siteData = JSON.parseObject(jobj.get(companyHonorDTO.getSdId().toString()).toString(),SiteData.class);
        }
        String dataSourceName = "";
        if(!IsEmptyUtils.isEmpty(siteData)){
            dataSourceName = siteData.getDateSourceName();
        }
        if(!IsEmptyUtils.isEmpty(dataSourceName)) {
            logger.info("----" + dataSourceName + "--");
            DynamicDataSourceContextHolder.setDataSourceType(dataSourceName);

            companyHonorMapper.updateByPrimaryKey(companyHonorDTO);

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
     * @param:  * @param companyHonorDTO
     * @date: 2019/10/14 18:59
     */
    @Override
    public ResultBean findCompanyHonor(CompanyHonorDTO companyHonorDTO) throws Exception {
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

        companyHonorDTO.setCertificateImg("c");
        companyHonorDTO.setZhengPaiImg("z");
        companyHonorDTO.setId(-1);

        if(!IsEmptyUtils.isEmpty(companyHonorDTO.getSdIds())){
            String[] sdIds = companyHonorDTO.getSdIds().split(",");
            StringBuffer stringBuffer = new StringBuffer();
            for(String sdId:sdIds){
                companyHonorDTO.setSdId(Integer.parseInt(sdId));
                String dataSourceName = "";
                SiteData siteData = null;
                if(!IsEmptyUtils.isEmpty(jobj.get(sdId))){
                    siteData = JSON.parseObject(jobj.get(sdId).toString(),SiteData.class);
                }
                companyHonorDTO.setPicturePrefix(siteData.getPicturePrefix());
                if(!IsEmptyUtils.isEmpty(siteData)){
                    dataSourceName = siteData.getDateSourceName();
                }
                if(!IsEmptyUtils.isEmpty(dataSourceName)){
                    logger.info("--"+siteData.getDateSourceName()+"--");
                    DynamicDataSourceContextHolder.setDataSourceType(dataSourceName);
                    companyHonorDTO.setSiteName(siteData.getName());
                    companyHonorDTO.setSitePhoto(siteData.getPhoto());

                    Map<String,Object> map = companyHonorMapper.findCompanyHonor(companyHonorDTO);
                    companyLicenses.add(map);
                }
            }
            DynamicDataSourceContextHolder.setDataSourceType(null);
        }else{
            List<SiteVO> userSites = siteMapper.findUserSites(companyHonorDTO.getUserId());
            StringBuffer stringBuffer = new StringBuffer();
            for(SiteVO siteVO:userSites){
                companyHonorDTO.setSdId(siteVO.getSdId());
                String dataSourceName = "";
                String sdIdStr = siteVO.getSdId().toString();
                SiteData siteData = null;
                if(!IsEmptyUtils.isEmpty(jobj.get(sdIdStr))){
                    siteData = JSON.parseObject(jobj.get(sdIdStr).toString(),SiteData.class);
                }
                companyHonorDTO.setPicturePrefix(siteData.getPicturePrefix());

                if(!IsEmptyUtils.isEmpty(siteData)){
                    dataSourceName = siteData.getDateSourceName();
                }
                if(!IsEmptyUtils.isEmpty(dataSourceName)){
                    logger.info("--"+siteData.getDateSourceName()+"--null sdIdStr");
                    DynamicDataSourceContextHolder.setDataSourceType(dataSourceName);
                    companyHonorDTO.setSiteName(siteData.getName());
                    companyHonorDTO.setSitePhoto(siteData.getPhoto());

                    Map<String,Object> map = companyHonorMapper.findCompanyHonor(companyHonorDTO);
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
}
