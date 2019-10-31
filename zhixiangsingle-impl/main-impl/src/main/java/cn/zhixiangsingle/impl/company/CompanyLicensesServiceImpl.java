package cn.zhixiangsingle.impl.company;

import cn.zhixiangsingle.config.DynamicDataSourceContextHolder;
import cn.zhixiangsingle.dao.business.BusinessMapper;
import cn.zhixiangsingle.dao.circulation.CirculationCardMapper;
import cn.zhixiangsingle.dao.license.LicenseMapper;
import cn.zhixiangsingle.dao.site.SiteMapper;
import cn.zhixiangsingle.entity.base.IsEmptyUtils;
import cn.zhixiangsingle.entity.base.common.dto.BaseDTO;
import cn.zhixiangsingle.entity.base.site.SiteData;
import cn.zhixiangsingle.entity.base.site.vo.SiteVO;
import cn.zhixiangsingle.entity.business.dto.BusinessDTO;
import cn.zhixiangsingle.entity.circulation.dto.CirculationCardDTO;
import cn.zhixiangsingle.entity.company.dto.Company;
import cn.zhixiangsingle.entity.license.dto.LicenseDTO;
import cn.zhixiangsingle.impl.business.BusinessServiceImpl;
import cn.zhixiangsingle.service.company.CompanyLicensesService;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangpingtai
 * @Package cn.zhixiangsingle.impl.company
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019/10/9 10:43
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Service(version = "1.0.0", interfaceClass = CompanyLicensesService.class)
public class CompanyLicensesServiceImpl implements CompanyLicensesService {
    private static final Logger logger = LoggerFactory
            .getLogger(BusinessServiceImpl.class);
    @Autowired
    private SiteMapper siteMapper;
    @Autowired
    private BusinessMapper businessMapper;
    @Autowired
    private LicenseMapper licenseMapper;
    @Autowired
    private CirculationCardMapper circulationCardMapper;
    /**
     * @version V1.0
     * @Title: zhixiangpingtai
     * @Description:
     * @author: hhp
     * @param:  * @param baseDTO
     * @date: 2019/10/14 19:02
     */
    @Override
    public ResultBean findCompanyLicensesList(BaseDTO baseDTO) throws Exception{
        ResultBean resultBean = new ResultBean();
        StringBuffer stringBufferJson = new StringBuffer();
        InputStream stream = getClass().getClassLoader().getResourceAsStream("json/siteData.json");
        BufferedReader br = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
        String line;
        while ((line = br.readLine()) != null) {
            stringBufferJson.append(line);
        }
        JSONObject jobj = JSON.parseObject(stringBufferJson.toString());

        List<Company> mainLPLs = Lists.newArrayList();

        ArrayList<Integer> totalAll = Lists.newArrayList();
        BusinessDTO businessDTO = new BusinessDTO();
        businessDTO.setId(-1);
        businessDTO.setRegistration("r");
        businessDTO.setLegalRepresentative("l");
        businessDTO.setRegisteredCapital("c");
        businessDTO.setCompanyName("c");
        businessDTO.setAddress("a");
        businessDTO.setDeadline("d");
        businessDTO.setBusinessScope("b");
        businessDTO.setMainTypes("m");
        businessDTO.setPath("p");
        businessDTO.setEarlyWarningTime("e");
        LicenseDTO licenseDTO = new LicenseDTO();
        licenseDTO.setId(-1);
        licenseDTO.setName("n");
        licenseDTO.setRepresentative("r");
        licenseDTO.setValidTime("v");
        licenseDTO.setWarningDate("w");
        licenseDTO.setSerialNumber("s");
        licenseDTO.setLicenceImg("l");
        CirculationCardDTO circulationCardDTO = new CirculationCardDTO();
        circulationCardDTO.setId(-1);
        circulationCardDTO.setName("n");
        circulationCardDTO.setRepresentative("r");
        circulationCardDTO.setValidTime("v");
        circulationCardDTO.setWarningDate("w");
        circulationCardDTO.setSerialNumber("s");
        circulationCardDTO.setCirculationCardImg("c");

        if(!IsEmptyUtils.isEmpty(baseDTO.getSdIds())){
            String[] sdIds = baseDTO.getSdIds().split(",");
            StringBuffer stringBuffer = new StringBuffer();
            for(String sdId:sdIds){
                businessDTO.setSdId(Integer.parseInt(sdId));
                licenseDTO.setSdId(Integer.parseInt(sdId));
                circulationCardDTO.setSdId(Integer.parseInt(sdId));
                String dataSourceName = "";
                SiteData siteData = null;
                if(!IsEmptyUtils.isEmpty(jobj.get(sdId))){
                    siteData = JSON.parseObject(jobj.get(sdId).toString(),SiteData.class);
                }
                businessDTO.setPicturePrefix(siteData.getPicturePrefix());
                licenseDTO.setPicturePrefix(siteData.getPicturePrefix());
                circulationCardDTO.setPicturePrefix(siteData.getPicturePrefix());
                if(!IsEmptyUtils.isEmpty(siteData)){
                    dataSourceName = siteData.getDateSourceName();
                }
                if(!IsEmptyUtils.isEmpty(dataSourceName)){
                    logger.info("--"+siteData.getDateSourceName()+"--");
                    DynamicDataSourceContextHolder.setDataSourceType(dataSourceName);
                    businessDTO.setSiteName(siteData.getName());
                    licenseDTO.setSiteName(siteData.getName());
                    circulationCardDTO.setSiteName(siteData.getName());
                    businessDTO.setSitePhoto(siteData.getPhoto());
                    licenseDTO.setSitePhoto(siteData.getPhoto());
                    circulationCardDTO.setSitePhoto(siteData.getPhoto());
                    Company company = new Company();
                    Map<String,Object> busMap = businessMapper.findBusiness(businessDTO);
                    if(IsEmptyUtils.isEmpty(busMap)){
                        busMap = new HashMap<>();
                        busMap.put("picturePrefix",siteData.getPicturePrefix());
                        busMap.put("sdId",sdId);
                        busMap.put("siteName",siteData.getName());
                        busMap.put("sitePhoto",siteData.getPhoto());
                    }
                    busMap.put("licenseType","营业执照");
                    company.setBusinessDTO(busMap);
                    Map<String,Object> licenseMap = licenseMapper.findLicense(licenseDTO);
                    if(IsEmptyUtils.isEmpty(licenseMap)){
                        licenseMap = new HashMap<>();
                        licenseMap.put("picturePrefix",siteData.getPicturePrefix());
                        licenseMap.put("sdId",sdId);
                        licenseMap.put("siteName",siteData.getName());
                        licenseMap.put("sitePhoto",siteData.getPhoto());
                    }
                    licenseMap.put("licenseType","餐饮许可证");
                    company.setLicenseDTO(licenseMap);
                    Map<String,Object> circulationMap = circulationCardMapper.findCirculationCard(circulationCardDTO);
                    if(IsEmptyUtils.isEmpty(circulationMap)){
                        circulationMap = new HashMap<>();
                        circulationMap.put("picturePrefix",siteData.getPicturePrefix());
                        circulationMap.put("sdId",sdId);
                        circulationMap.put("siteName",siteData.getName());
                        circulationMap.put("sitePhoto",siteData.getPhoto());
                    }
                    circulationMap.put("licenseType","食品流通证");
                    company.setCirculationCardDTO(circulationMap);
                    mainLPLs.add(company);
                }
            }
            DynamicDataSourceContextHolder.setDataSourceType(null);
        }else{
            List<SiteVO> userSites = siteMapper.findUserSites(baseDTO.getUserId());
            StringBuffer stringBuffer = new StringBuffer();
            for(SiteVO siteVO:userSites){
                businessDTO.setSdId(siteVO.getSdId());
                licenseDTO.setSdId(siteVO.getSdId());
                circulationCardDTO.setSdId(siteVO.getSdId());
                String dataSourceName = "";
                String sdIdStr = siteVO.getSdId().toString();
                SiteData siteData = null;
                if(!IsEmptyUtils.isEmpty(jobj.get(sdIdStr))){
                    siteData = JSON.parseObject(jobj.get(sdIdStr).toString(),SiteData.class);
                }
                businessDTO.setPicturePrefix(siteData.getPicturePrefix());
                licenseDTO.setPicturePrefix(siteData.getPicturePrefix());
                circulationCardDTO.setPicturePrefix(siteData.getPicturePrefix());

                if(!IsEmptyUtils.isEmpty(siteData)){
                    dataSourceName = siteData.getDateSourceName();
                }
                if(!IsEmptyUtils.isEmpty(dataSourceName)){
                    logger.info("--"+siteData.getDateSourceName()+"--null sdIdStr");
                    DynamicDataSourceContextHolder.setDataSourceType(dataSourceName);
                    businessDTO.setSiteName(siteData.getName());
                    licenseDTO.setSiteName(siteData.getName());
                    circulationCardDTO.setSiteName(siteData.getName());
                    businessDTO.setSitePhoto(siteData.getPhoto());
                    licenseDTO.setSitePhoto(siteData.getPhoto());
                    circulationCardDTO.setSitePhoto(siteData.getPhoto());

                    Company company = new Company();
                    Map<String,Object> busMap = businessMapper.findBusiness(businessDTO);
                    if(IsEmptyUtils.isEmpty(busMap)){
                        busMap = new HashMap<>();
                        busMap.put("picturePrefix",siteData.getPicturePrefix());
                        busMap.put("sdId",sdIdStr);
                        busMap.put("siteName",siteData.getName());
                        busMap.put("sitePhoto",siteData.getPhoto());
                    }
                    busMap.put("licenseType","营业执照");
                    company.setBusinessDTO(busMap);
                    Map<String,Object> licenseMap = licenseMapper.findLicense(licenseDTO);
                    if(IsEmptyUtils.isEmpty(licenseMap)){
                        licenseMap = new HashMap<>();
                        licenseMap.put("picturePrefix",siteData.getPicturePrefix());
                        licenseMap.put("sdId",sdIdStr);
                        licenseMap.put("siteName",siteData.getName());
                        licenseMap.put("sitePhoto",siteData.getPhoto());
                    }
                    licenseMap.put("licenseType","餐饮许可证");
                    company.setLicenseDTO(licenseMap);
                    Map<String,Object> circulationMap = circulationCardMapper.findCirculationCard(circulationCardDTO);
                    if(IsEmptyUtils.isEmpty(circulationMap)){
                        circulationMap = new HashMap<>();
                        circulationMap.put("picturePrefix",siteData.getPicturePrefix());
                        circulationMap.put("sdId",sdIdStr);
                        circulationMap.put("siteName",siteData.getName());
                        circulationMap.put("sitePhoto",siteData.getPhoto());
                    }
                    circulationMap.put("licenseType","食品流通证");
                    company.setCirculationCardDTO(circulationMap);
                    mainLPLs.add(company);
                }
            }
            DynamicDataSourceContextHolder.setDataSourceType(null);
        }
        resultBean.setSuccess(true);
        resultBean.setResult(mainLPLs);
        resultBean.setResultCode(IStatusMessage.SystemStatus.SUCCESS.getCode());
        resultBean.setMsg(IStatusMessage.SystemStatus.SUCCESS.getMessage());
        return resultBean;
    }
}
