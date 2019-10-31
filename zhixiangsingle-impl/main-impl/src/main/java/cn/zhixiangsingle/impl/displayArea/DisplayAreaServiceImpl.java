package cn.zhixiangsingle.impl.displayArea;

import cn.zhixiangsingle.config.DynamicDataSourceContextHolder;
import cn.zhixiangsingle.dao.displayArea.DisplayAreaMapper;
import cn.zhixiangsingle.dao.site.SiteMapper;
import cn.zhixiangsingle.date.DateUtils;
import cn.zhixiangsingle.entity.base.IsEmptyUtils;
import cn.zhixiangsingle.entity.base.manyResult.ManyResult;
import cn.zhixiangsingle.entity.base.site.SiteData;
import cn.zhixiangsingle.entity.base.site.vo.SiteVO;
import cn.zhixiangsingle.entity.displayArea.dto.DisplayAreaDTO;
import cn.zhixiangsingle.service.displayArea.DisplayAreaService;
import cn.zhixiangsingle.web.responsive.IStatusMessage;
import cn.zhixiangsingle.web.responsive.ResultBean;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangpingtai
 * @Package cn.zhixiangsingle.impl.displayArea
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019/10/15 17:09
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Service(version = "1.0.0", interfaceClass = DisplayAreaService.class)
public class DisplayAreaServiceImpl implements DisplayAreaService {
    private static final Logger logger = LoggerFactory
            .getLogger(DisplayAreaServiceImpl.class);
    @Autowired
    private SiteMapper siteMapper;
    @Autowired
    private DisplayAreaMapper displayAreaMapper;
    /**
     * @version V1.0
     * @Title: zhixiangpingtai
     * @Description:
     * @author: hhp
     * @param:  * @param displayAreaDTO
     * @date: 2019/10/17 14:14
     */
    @Override
    public ResultBean findAll(DisplayAreaDTO displayAreaDTO) throws Exception {
        ResultBean resultBean = new ResultBean();
        StringBuffer stringBufferJson = new StringBuffer();
        InputStream stream = getClass().getClassLoader().getResourceAsStream("json/siteData.json");
        BufferedReader br = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
        String line;
        while ((line = br.readLine()) != null) {
            stringBufferJson.append(line);
        }
        JSONObject jobj = JSON.parseObject(stringBufferJson.toString());

        List<Map<String,Object>> mainLPLs = Lists.newArrayList();

        ArrayList<Integer> totalAll = Lists.newArrayList();
        displayAreaDTO.setId(-1);
        displayAreaDTO.setArea("a");
        if(!IsEmptyUtils.isEmpty(displayAreaDTO.getSdIds())){
            String[] sdIds = displayAreaDTO.getSdIds().split(",");
            for(String sdId:sdIds){
                displayAreaDTO.setSdId(Integer.parseInt(sdId));
                String dataSourceName = "";
                SiteData siteData = null;
                if(!IsEmptyUtils.isEmpty(jobj.get(sdId))){
                    siteData = JSON.parseObject(jobj.get(sdId).toString(),SiteData.class);
                }
                displayAreaDTO.setPicturePrefix(siteData.getPicturePrefix());
                if(!IsEmptyUtils.isEmpty(siteData)){
                    dataSourceName = siteData.getDateSourceName();
                }
                if(!IsEmptyUtils.isEmpty(dataSourceName)){
                    logger.info("--"+dataSourceName+"--");
                    DynamicDataSourceContextHolder.setDataSourceType(dataSourceName);

                    displayAreaDTO.setPicturePrefix(siteData.getPicturePrefix());
                    logger.info("--"+siteData.getDateSourceName()+"--");
                    DynamicDataSourceContextHolder.setDataSourceType(siteData.getDateSourceName());

                    displayAreaDTO.setSiteName(siteData.getName());
                    displayAreaDTO.setSitePhoto(siteData.getPhoto());

                    List<Map<String,Object>> mainLPList = displayAreaMapper.findDisplayAreaList(displayAreaDTO);
                    mainLPLs.addAll(mainLPList);

                }
            }
            DynamicDataSourceContextHolder.setDataSourceType(null);
        }else{
            List<SiteVO> userSites = siteMapper.findUserSites(displayAreaDTO.getUserId());
            StringBuffer stringBuffer = new StringBuffer();
            for(SiteVO siteVO:userSites){
                String dataSourceName = "";
                String sdIdStr = siteVO.getSdId().toString();
                SiteData siteData = null;
                if(!IsEmptyUtils.isEmpty(jobj.get(sdIdStr))){
                    siteData = JSON.parseObject(jobj.get(sdIdStr).toString(),SiteData.class);
                }
                displayAreaDTO.setSdId(siteVO.getSdId());
                displayAreaDTO.setPicturePrefix(siteData.getPicturePrefix());
                if(!IsEmptyUtils.isEmpty(siteData)){
                    dataSourceName = siteData.getDateSourceName();
                }
                if(!IsEmptyUtils.isEmpty(dataSourceName)){

                    logger.info("--"+siteData.getDateSourceName()+"--");
                    displayAreaDTO.setPicturePrefix(siteData.getPicturePrefix());

                    DynamicDataSourceContextHolder.setDataSourceType(siteData.getDateSourceName());

                    displayAreaDTO.setSiteName(siteData.getName());
                    displayAreaDTO.setSitePhoto(siteData.getPhoto());

                    List<Map<String,Object>> mainLPList = displayAreaMapper.findDisplayAreaList(displayAreaDTO);
                    mainLPLs.addAll(mainLPList);
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
    /**
     * @version V1.0
     * @Title: zhixiangpingtai
     * @Description:
     * @author: hhp
     * @param:  * @param displayAreaDTO
     * @date: 2019/10/17 14:13
     */
    @Override
    public ResultBean findDisplayAreaList(DisplayAreaDTO displayAreaDTO) throws Exception {
        ResultBean resultBean = new ResultBean();
        List<ManyResult> companyLicenses = Lists.newArrayList();
        StringBuffer stringBufferJson = new StringBuffer();
        InputStream stream = getClass().getClassLoader().getResourceAsStream("json/siteData.json");
        BufferedReader br = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
        String line;
        while ((line = br.readLine()) != null) {
            stringBufferJson.append(line);
        }
        JSONObject jobj = JSON.parseObject(stringBufferJson.toString());

        displayAreaDTO.setId(-1);
        displayAreaDTO.setArea("a");
        displayAreaDTO.setPid(-1);

        if(!IsEmptyUtils.isEmpty(displayAreaDTO.getSdIds())){
            String[] sdIds = displayAreaDTO.getSdIds().split(",");
            StringBuffer stringBuffer = new StringBuffer();
            for(String sdId:sdIds){
                displayAreaDTO.setSdId(Integer.parseInt(sdId));
                String dataSourceName = "";
                SiteData siteData = null;
                if(!IsEmptyUtils.isEmpty(jobj.get(sdId))){
                    siteData = JSON.parseObject(jobj.get(sdId).toString(),SiteData.class);
                }
                displayAreaDTO.setPicturePrefix(siteData.getPicturePrefix());
                if(!IsEmptyUtils.isEmpty(siteData)){
                    dataSourceName = siteData.getDateSourceName();
                }
                if(!IsEmptyUtils.isEmpty(dataSourceName)){
                    logger.info("--"+siteData.getDateSourceName()+"--");
                    DynamicDataSourceContextHolder.setDataSourceType(dataSourceName);
                    displayAreaDTO.setSiteName(siteData.getName());
                    displayAreaDTO.setSitePhoto(siteData.getPhoto());

                    List<Map<String,Object>> mainLPList = displayAreaMapper.findDisplayAreaList(displayAreaDTO);
                    ManyResult manyResult = new ManyResult();
                    manyResult.setRows(mainLPList);
                    companyLicenses.add(manyResult);
                }
            }
            DynamicDataSourceContextHolder.setDataSourceType(null);
        }else{
            List<SiteVO> userSites = siteMapper.findUserSites(displayAreaDTO.getUserId());
            StringBuffer stringBuffer = new StringBuffer();
            for(SiteVO siteVO:userSites){
                displayAreaDTO.setSdId(siteVO.getSdId());
                String dataSourceName = "";
                String sdIdStr = siteVO.getSdId().toString();
                SiteData siteData = null;
                if(!IsEmptyUtils.isEmpty(jobj.get(sdIdStr))){
                    siteData = JSON.parseObject(jobj.get(sdIdStr).toString(),SiteData.class);
                }
                displayAreaDTO.setPicturePrefix(siteData.getPicturePrefix());

                if(!IsEmptyUtils.isEmpty(siteData)){
                    dataSourceName = siteData.getDateSourceName();
                }
                if(!IsEmptyUtils.isEmpty(dataSourceName)){
                    logger.info("--"+siteData.getDateSourceName()+"--null sdIdStr");
                    DynamicDataSourceContextHolder.setDataSourceType(dataSourceName);
                    displayAreaDTO.setSiteName(siteData.getName());
                    displayAreaDTO.setSitePhoto(siteData.getPhoto());

                    List<Map<String,Object>> mainLPList = displayAreaMapper.findDisplayAreaList(displayAreaDTO);
                    ManyResult manyResult = new ManyResult();
                    manyResult.setRows(mainLPList);
                    companyLicenses.add(manyResult);
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
     * @param:  * @param displayAreaDTO
     * @date: 2019/10/17 14:14
     */
    @Override
    public ResultBean updateDisplayArea(DisplayAreaDTO displayAreaDTO) throws Exception {
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
        if(!IsEmptyUtils.isEmpty(jobj.get(displayAreaDTO.getSdId().toString()))){
            siteData = JSON.parseObject(jobj.get(displayAreaDTO.getSdId().toString()).toString(),SiteData.class);
        }
        String dataSourceName = "";
        if(!IsEmptyUtils.isEmpty(siteData)){
            dataSourceName = siteData.getDateSourceName();
        }
        if(!IsEmptyUtils.isEmpty(dataSourceName)) {
            logger.info("-----" + dataSourceName + "---");
            DynamicDataSourceContextHolder.setDataSourceType(dataSourceName);

            displayAreaMapper.updateByPrimaryKey(displayAreaDTO);

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
     * @param:  * @param displayAreaDTO
     * @date: 2019/10/17 14:17
     */
    @Override
    public ResultBean addDisplayArea(DisplayAreaDTO displayAreaDTO) throws Exception {
        ResultBean resultBean = new ResultBean();
        if(!IsEmptyUtils.isEmpty(displayAreaDTO.getSdIds())){
            StringBuffer msgStr = new StringBuffer();
            String[] sdIds = displayAreaDTO.getSdIds().split(",");
            for(String sdId:sdIds){
                displayAreaDTO.setSdId(Integer.parseInt(sdId));
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
                    logger.info("----" + dataSourceName + "---");
                    DynamicDataSourceContextHolder.setDataSourceType(dataSourceName);
                    displayAreaMapper.insertSelective(displayAreaDTO);
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
            if(!IsEmptyUtils.isEmpty(jobj.get(displayAreaDTO.getSdId().toString()))){
                siteData = JSON.parseObject(jobj.get(displayAreaDTO.getSdId().toString()).toString(),SiteData.class);
            }
            String dataSourceName = "";
            if(!IsEmptyUtils.isEmpty(siteData)){
                dataSourceName = siteData.getDateSourceName();
            }
            if(!IsEmptyUtils.isEmpty(dataSourceName)) {
                logger.info("---" + dataSourceName + "---");
                DynamicDataSourceContextHolder.setDataSourceType(dataSourceName);
                displayAreaMapper.insertSelective(displayAreaDTO);
                resultBean.setSuccess(true);
                resultBean.setResultCode(IStatusMessage.SystemStatus.SUCCESS.getCode());
                resultBean.setMsg(IStatusMessage.SystemStatus.SUCCESS.getMessage());
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
     * @param:  * @param displayAreaDTO
     * @date: 2019/10/17 14:22
     */
    @Override
    public ResultBean findUpdDisplayArea(DisplayAreaDTO displayAreaDTO) throws Exception {
        ResultBean resultBean = new ResultBean();
        StringBuffer stringBufferJson = new StringBuffer();
        InputStream stream = getClass().getClassLoader().getResourceAsStream("json/siteData.json");
        BufferedReader br = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
        String line;
        while ((line = br.readLine()) != null) {
            stringBufferJson.append(line);
        }
        displayAreaDTO.setArea("a");
        displayAreaDTO.setPid(-1);
        JSONObject jobj = JSON.parseObject(stringBufferJson.toString());
        if(!IsEmptyUtils.isEmpty(displayAreaDTO.getSdId())){
            String dataSourceName = "";
            SiteData siteData = null;
            if(!IsEmptyUtils.isEmpty(jobj.get(displayAreaDTO.getSdId().toString()))){
                siteData = JSON.parseObject(jobj.get(displayAreaDTO.getSdId().toString()).toString(),SiteData.class);
            }
            displayAreaDTO.setPicturePrefix(siteData.getPicturePrefix());
            if(!IsEmptyUtils.isEmpty(siteData)){
                dataSourceName = siteData.getDateSourceName();
            }
            if(!IsEmptyUtils.isEmpty(dataSourceName)){
                logger.info("--"+dataSourceName+"--");
                DynamicDataSourceContextHolder.setDataSourceType(dataSourceName);

                Map<String,Object> ingredientBaseDTOResult = displayAreaMapper.findDisplayArea(displayAreaDTO);

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
     * @param:  * @param displayAreaDTO
     * @date: 2019/10/17 14:23
     */
    @Override
    public ResultBean delDisplayArea(DisplayAreaDTO displayAreaDTO) throws Exception {
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
        if(!IsEmptyUtils.isEmpty(jobj.get(displayAreaDTO.getSdId().toString()))){
            siteData = JSON.parseObject(jobj.get(displayAreaDTO.getSdId().toString()).toString(),SiteData.class);
        }
        String dataSourceName = "";
        if(!IsEmptyUtils.isEmpty(siteData)){
            dataSourceName = siteData.getDateSourceName();
        }
        if(!IsEmptyUtils.isEmpty(dataSourceName)) {
            logger.info("---" + dataSourceName + "-----");
            DynamicDataSourceContextHolder.setDataSourceType(dataSourceName);
            //赋值 param1(temperatureDTO)的属性至 param2(temperatureDTO2)中相同属性,不相同属性不处理
            DisplayAreaDTO displayAreaDTO1 = new DisplayAreaDTO();
            BeanUtils.copyProperties(displayAreaDTO, displayAreaDTO1);
            displayAreaDTO1.setPid(displayAreaDTO1.getId());
            displayAreaDTO1.setId(null);
            List<Map<String,Object>> childPerm = displayAreaMapper.findDisplayAreaList(displayAreaDTO1);
            if(!IsEmptyUtils.isEmpty(childPerm)){
                resultBean.setSuccess(false);
                resultBean.setResultCode(IStatusMessage.SystemStatus.SUN_PERMISSION_NOT_DELETE.getCode());
                resultBean.setMsg(IStatusMessage.SystemStatus.SUN_PERMISSION_NOT_DELETE.getMessage());
            }else{
                displayAreaMapper.deleteByPrimaryKey(displayAreaDTO);
                resultBean.setSuccess(true);
                resultBean.setResultCode(IStatusMessage.SystemStatus.SUCCESS.getCode());
                resultBean.setMsg(IStatusMessage.SystemStatus.SUCCESS.getMessage());
            }

            DynamicDataSourceContextHolder.setDataSourceType(null);
        }
        return resultBean;
    }
}
