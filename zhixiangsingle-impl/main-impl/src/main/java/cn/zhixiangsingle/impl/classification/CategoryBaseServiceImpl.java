package cn.zhixiangsingle.impl.classification;

import cn.zhixiangsingle.config.DynamicDataSourceContextHolder;
import cn.zhixiangsingle.dao.classification.CategoryBaseMapper;
import cn.zhixiangsingle.dao.site.SiteMapper;
import cn.zhixiangsingle.date.DateUtils;
import cn.zhixiangsingle.entity.base.IsEmptyUtils;
import cn.zhixiangsingle.entity.base.manyResult.ManyResult;
import cn.zhixiangsingle.entity.base.site.SiteData;
import cn.zhixiangsingle.entity.base.site.vo.SiteVO;
import cn.zhixiangsingle.entity.classification.dto.CategoryBaseDTO;
import cn.zhixiangsingle.pagination.MyStartEndUtil;
import cn.zhixiangsingle.service.classification.CategoryBaseService;
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
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangpingtai
 * @Package cn.zhixiangsingle.impl.classification
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019/9/23 18:13
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Service(version = "1.0.0", interfaceClass = CategoryBaseService.class)
public class CategoryBaseServiceImpl implements CategoryBaseService {
    private static final Logger logger = LoggerFactory
            .getLogger(CategoryBaseServiceImpl.class);
    @Autowired
    private SiteMapper siteMapper;
    @Autowired
    private CategoryBaseMapper categoryBaseMapper;
    /**
     * @version V1.0
     * @Title: zhixiangpingtai
     * @Description:
     * @author: hhp
     * @param:  * @param categoryBaseDTO
     * @date: 2019/10/19 14:27
     */
    @Override
    public ResultBean updateCategoryBase(CategoryBaseDTO categoryBaseDTO) throws Exception {
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
        if(!IsEmptyUtils.isEmpty(jobj.get(categoryBaseDTO.getSdId().toString()))){
            siteData = JSON.parseObject(jobj.get(categoryBaseDTO.getSdId().toString()).toString(),SiteData.class);
        }
        String dataSourceName = "";
        if(!IsEmptyUtils.isEmpty(siteData)){
            dataSourceName = siteData.getDateSourceName();
        }
        if(!IsEmptyUtils.isEmpty(dataSourceName)) {
            logger.info("-" + dataSourceName + "----");
            DynamicDataSourceContextHolder.setDataSourceType(dataSourceName);

            categoryBaseMapper.updateByPrimaryKey(categoryBaseDTO);

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
     * @param:  * @param categoryBaseDTO
     * @date: 2019/10/19 14:28
     */
    @Override
    public ResultBean addCategoryBase(CategoryBaseDTO categoryBaseDTO) throws Exception {
        ResultBean resultBean = new ResultBean();
        categoryBaseDTO.setDeleteStatus("1");
        if(!IsEmptyUtils.isEmpty(categoryBaseDTO.getSdIds())){
            StringBuffer msgStr = new StringBuffer();
            String[] sdIds = categoryBaseDTO.getSdIds().split(",");
            for(String sdId:sdIds){
                categoryBaseDTO.setSdId(Integer.parseInt(sdId));
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
                    logger.info("-" + dataSourceName + "----");
                    DynamicDataSourceContextHolder.setDataSourceType(dataSourceName);
                    categoryBaseMapper.insertSelective(categoryBaseDTO);
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
            if(!IsEmptyUtils.isEmpty(jobj.get(categoryBaseDTO.getSdId().toString()))){
                siteData = JSON.parseObject(jobj.get(categoryBaseDTO.getSdId().toString()).toString(),SiteData.class);
            }
            String dataSourceName = "";
            if(!IsEmptyUtils.isEmpty(siteData)){
                dataSourceName = siteData.getDateSourceName();
            }
            if(!IsEmptyUtils.isEmpty(dataSourceName)) {
                logger.info("-" + dataSourceName + "---");
                DynamicDataSourceContextHolder.setDataSourceType(dataSourceName);
                categoryBaseMapper.insertSelective(categoryBaseDTO);
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
     * @param:  * @param categoryBaseDTO
     * @date: 2019/9/27 17:17
     */
    @Override
    public ResultBean findCategoryBaseList(CategoryBaseDTO categoryBaseDTO) throws Exception {
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

        categoryBaseDTO.setId(-1);
        if(IsEmptyUtils.isEmpty(categoryBaseDTO.getCategoryName())){
            categoryBaseDTO.setCategoryName("c");
        }
        categoryBaseDTO.setDeleteStatus("1");
        categoryBaseDTO.setPId(-1);

        if(!IsEmptyUtils.isEmpty(categoryBaseDTO.getSdIds())){
            String[] sdIds = categoryBaseDTO.getSdIds().split(",");
            StringBuffer stringBuffer = new StringBuffer();
            for(String sdId:sdIds){
                categoryBaseDTO.setSdId(Integer.parseInt(sdId));
                String dataSourceName = "";
                SiteData siteData = null;
                if(!IsEmptyUtils.isEmpty(jobj.get(sdId))){
                    siteData = JSON.parseObject(jobj.get(sdId).toString(),SiteData.class);
                }
                categoryBaseDTO.setPicturePrefix(siteData.getPicturePrefix());
                if(!IsEmptyUtils.isEmpty(siteData)){
                    dataSourceName = siteData.getDateSourceName();
                }
                if(!IsEmptyUtils.isEmpty(dataSourceName)){
                    logger.info("--"+siteData.getDateSourceName()+"--");
                    DynamicDataSourceContextHolder.setDataSourceType(dataSourceName);
                    categoryBaseDTO.setSiteName(siteData.getName());
                    categoryBaseDTO.setSitePhoto(siteData.getPhoto());

                    List<Map<String,Object>> mainLPList = categoryBaseMapper.findCategoryBaseList(categoryBaseDTO);
                    ManyResult manyResult = new ManyResult();
                    manyResult.setRows(mainLPList);
                    companyLicenses.add(manyResult);
                }
            }
            DynamicDataSourceContextHolder.setDataSourceType(null);
        }else{
            List<SiteVO> userSites = siteMapper.findUserSites(categoryBaseDTO.getUserId());
            StringBuffer stringBuffer = new StringBuffer();
            for(SiteVO siteVO:userSites){
                categoryBaseDTO.setSdId(siteVO.getSdId());
                String dataSourceName = "";
                String sdIdStr = siteVO.getSdId().toString();
                SiteData siteData = null;
                if(!IsEmptyUtils.isEmpty(jobj.get(sdIdStr))){
                    siteData = JSON.parseObject(jobj.get(sdIdStr).toString(),SiteData.class);
                }
                categoryBaseDTO.setPicturePrefix(siteData.getPicturePrefix());

                if(!IsEmptyUtils.isEmpty(siteData)){
                    dataSourceName = siteData.getDateSourceName();
                }
                if(!IsEmptyUtils.isEmpty(dataSourceName)){
                    logger.info("--"+siteData.getDateSourceName()+"-null sdIdStr");
                    DynamicDataSourceContextHolder.setDataSourceType(dataSourceName);
                    categoryBaseDTO.setSiteName(siteData.getName());
                    categoryBaseDTO.setSitePhoto(siteData.getPhoto());

                    List<Map<String,Object>> mainLPList = categoryBaseMapper.findCategoryBaseList(categoryBaseDTO);
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
     * @param:  * @param categoryBaseDTO
     * @date: 2019/9/27 17:41
     */
    @Override
    public ResultBean findAll(CategoryBaseDTO categoryBaseDTO) throws Exception {
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
        categoryBaseDTO.setId(-1);
        categoryBaseDTO.setPId(-1);
        if(IsEmptyUtils.isEmpty(categoryBaseDTO.getCategoryName())){
            categoryBaseDTO.setCategoryName("c");
        }
        if(IsEmptyUtils.isEmpty(categoryBaseDTO.getDeleteStatus())){
            categoryBaseDTO.setDeleteStatus("1");
        }

        if(!IsEmptyUtils.isEmpty(categoryBaseDTO.getSdIds())){
            String[] sdIds = categoryBaseDTO.getSdIds().split(",");
            for(String sdId:sdIds){
                categoryBaseDTO.setSdId(Integer.parseInt(sdId));
                String dataSourceName = "";
                SiteData siteData = null;
                if(!IsEmptyUtils.isEmpty(jobj.get(sdId))){
                    siteData = JSON.parseObject(jobj.get(sdId).toString(),SiteData.class);
                }
                if(!IsEmptyUtils.isEmpty(siteData)){
                    dataSourceName = siteData.getDateSourceName();
                }
                if(!IsEmptyUtils.isEmpty(dataSourceName)){
                    logger.info("--"+dataSourceName+"--");
                    DynamicDataSourceContextHolder.setDataSourceType(dataSourceName);

                    categoryBaseDTO.setPicturePrefix(siteData.getPicturePrefix());
                    logger.info("--"+siteData.getDateSourceName()+"--");
                    DynamicDataSourceContextHolder.setDataSourceType(siteData.getDateSourceName());

                    categoryBaseDTO.setSiteName(siteData.getName());
                    categoryBaseDTO.setSitePhoto(siteData.getPhoto());

                    List<Map<String,Object>> mainLPList = categoryBaseMapper.findCategoryBaseList(categoryBaseDTO);
                    mainLPLs.addAll(mainLPList);

                }
            }
            DynamicDataSourceContextHolder.setDataSourceType(null);
        }else{
            List<SiteVO> userSites = siteMapper.findUserSites(categoryBaseDTO.getUserId());
            StringBuffer stringBuffer = new StringBuffer();
            for(SiteVO siteVO:userSites){
                categoryBaseDTO.setSdId(siteVO.getSdId());
                categoryBaseDTO.setPId(-1);
                String dataSourceName = "";
                String sdIdStr = siteVO.getSdId().toString();
                SiteData siteData = null;
                if(!IsEmptyUtils.isEmpty(jobj.get(sdIdStr))){
                    siteData = JSON.parseObject(jobj.get(sdIdStr).toString(),SiteData.class);
                }
                categoryBaseDTO.setPicturePrefix(siteData.getPicturePrefix());
                if(!IsEmptyUtils.isEmpty(siteData)){
                    dataSourceName = siteData.getDateSourceName();
                }
                if(!IsEmptyUtils.isEmpty(dataSourceName)){

                    logger.info("--"+siteData.getDateSourceName()+"--");

                    DynamicDataSourceContextHolder.setDataSourceType(siteData.getDateSourceName());

                    categoryBaseDTO.setSiteName(siteData.getName());
                    categoryBaseDTO.setSitePhoto(siteData.getPhoto());

                    List<Map<String,Object>> mainLPList = categoryBaseMapper.findCategoryBaseList(categoryBaseDTO);
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
     * @param:  * @param categoryBaseDTO
     * @date: 2019/10/19 14:36
     */
    @Override
    public ResultBean findUpdCategoryBase(CategoryBaseDTO categoryBaseDTO) throws Exception {
        ResultBean resultBean = new ResultBean();
        StringBuffer stringBufferJson = new StringBuffer();
        InputStream stream = getClass().getClassLoader().getResourceAsStream("json/siteData.json");
        BufferedReader br = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
        String line;
        while ((line = br.readLine()) != null) {
            stringBufferJson.append(line);
        }
        categoryBaseDTO.setDeleteStatus("1");
        categoryBaseDTO.setPId(-1);
        categoryBaseDTO.setCategoryName("c");
        JSONObject jobj = JSON.parseObject(stringBufferJson.toString());
        if(!IsEmptyUtils.isEmpty(categoryBaseDTO.getSdId())){
            String dataSourceName = "";
            SiteData siteData = null;
            if(!IsEmptyUtils.isEmpty(jobj.get(categoryBaseDTO.getSdId().toString()))){
                siteData = JSON.parseObject(jobj.get(categoryBaseDTO.getSdId().toString()).toString(),SiteData.class);
            }
            categoryBaseDTO.setPicturePrefix(siteData.getPicturePrefix());
            if(!IsEmptyUtils.isEmpty(siteData)){
                dataSourceName = siteData.getDateSourceName();
            }
            if(!IsEmptyUtils.isEmpty(dataSourceName)){
                logger.info("--"+dataSourceName+"--");
                DynamicDataSourceContextHolder.setDataSourceType(dataSourceName);

                Map<String,Object> morningMeetingResult = categoryBaseMapper.findCategoryBase(categoryBaseDTO);

                resultBean.setResult(morningMeetingResult);
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
     * @param:  * @param categoryBaseDTO
     * @date: 2019/10/19 14:38
     */
    @Override
    public ResultBean delCategoryBase(CategoryBaseDTO categoryBaseDTO) throws Exception {
        ResultBean resultBean = new ResultBean();
        categoryBaseDTO.setDeleteStatus("0");
        StringBuffer stringBufferJson = new StringBuffer();
        InputStream stream = getClass().getClassLoader().getResourceAsStream("json/siteData.json");
        BufferedReader br = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
        String line;
        while ((line = br.readLine()) != null) {
            stringBufferJson.append(line);
        }
        JSONObject jobj = JSON.parseObject(stringBufferJson.toString());
        SiteData siteData = null;
        if(!IsEmptyUtils.isEmpty(jobj.get(categoryBaseDTO.getSdId().toString()))){
            siteData = JSON.parseObject(jobj.get(categoryBaseDTO.getSdId().toString()).toString(),SiteData.class);
        }
        String dataSourceName = "";
        if(!IsEmptyUtils.isEmpty(siteData)){
            dataSourceName = siteData.getDateSourceName();
        }
        if(!IsEmptyUtils.isEmpty(dataSourceName)) {
            logger.info("--" + dataSourceName + "----");
            DynamicDataSourceContextHolder.setDataSourceType(dataSourceName);

            categoryBaseMapper.updateByPrimaryKey(categoryBaseDTO);
            DynamicDataSourceContextHolder.setDataSourceType(null);
        }
        resultBean.setSuccess(true);
        resultBean.setResultCode(IStatusMessage.SystemStatus.SUCCESS.getCode());
        resultBean.setMsg(IStatusMessage.SystemStatus.SUCCESS.getMessage());
        return resultBean;
    }
}
