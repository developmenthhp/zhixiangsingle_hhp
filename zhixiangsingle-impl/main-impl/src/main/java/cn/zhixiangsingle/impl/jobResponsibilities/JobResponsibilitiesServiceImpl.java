package cn.zhixiangsingle.impl.jobResponsibilities;

import cn.zhixiangsingle.config.DynamicDataSourceContextHolder;
import cn.zhixiangsingle.dao.jobResponsibilities.JobResponsibilitiesMapper;
import cn.zhixiangsingle.dao.site.SiteMapper;
import cn.zhixiangsingle.date.DateUtils;
import cn.zhixiangsingle.entity.base.IsEmptyUtils;
import cn.zhixiangsingle.entity.base.site.SiteData;
import cn.zhixiangsingle.entity.base.site.vo.SiteVO;
import cn.zhixiangsingle.entity.jobResponsibilities.dto.JobResponsibilitiesDTO;
import cn.zhixiangsingle.entity.jobResponsibilities.vo.JobResponsibilitiesVO;
import cn.zhixiangsingle.pagination.MyStartEndUtil;
import cn.zhixiangsingle.service.jobResponsibilities.JobResponsibilitiesService;
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
import java.util.*;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangpingtai
 * @Package cn.zhixiangsingle.impl.jobResponsibilities
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019/10/17 10:52
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Service(version = "1.0.0", interfaceClass = JobResponsibilitiesService.class)
public class JobResponsibilitiesServiceImpl implements JobResponsibilitiesService {
    private static final Logger logger = LoggerFactory
            .getLogger(JobResponsibilitiesServiceImpl.class);
    @Autowired
    private SiteMapper siteMapper;
    @Autowired
    private JobResponsibilitiesMapper jobResponsibilitiesMapper;
    /**
     * @version V1.0
     * @Title: zhixiangpingtai
     * @Description:
     * @author: hhp
     * @param:  * @param jobResponsibilitiesDTO
     * @date: 2019/10/17 11:24
     */
    @Override
    public ResultBean findJobResponsibilitiesList(JobResponsibilitiesDTO jobResponsibilitiesDTO) throws Exception {
        ResultBean resultBean = new ResultBean();
        if(IsEmptyUtils.isEmpty(jobResponsibilitiesDTO.getPage())){
            jobResponsibilitiesDTO.setPage(1);
        }
        if (IsEmptyUtils.isEmpty(jobResponsibilitiesDTO.getLimit())){
            jobResponsibilitiesDTO.setLimit(10);
        }
        Integer page = jobResponsibilitiesDTO.getPage();
        Integer limit = jobResponsibilitiesDTO.getLimit();
        StringBuffer stringBufferJson = new StringBuffer();
        InputStream stream = getClass().getClassLoader().getResourceAsStream("json/siteData.json");
        BufferedReader br = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
        String line;
        while ((line = br.readLine()) != null) {
            stringBufferJson.append(line);
        }
        JSONObject jobj = JSON.parseObject(stringBufferJson.toString());

        List<Map<String,Object>> mainLPLs = Lists.newArrayList();
        Integer total = 0;
        int startNum = (page-1)*limit;
        int endNum = startNum+limit;
        ArrayList<Integer> totalAll = Lists.newArrayList();
        jobResponsibilitiesDTO.setId(-1);
        jobResponsibilitiesDTO.setAddTime("a");
        if(IsEmptyUtils.isEmpty(jobResponsibilitiesDTO.getJobTitle())){
            jobResponsibilitiesDTO.setJobTitle("j");
        }
        jobResponsibilitiesDTO.setJobResponsibilities("j");
        jobResponsibilitiesDTO.setImgPath("i");
        if(!IsEmptyUtils.isEmpty(jobResponsibilitiesDTO.getSdIds())){
            String[] sdIds = jobResponsibilitiesDTO.getSdIds().split(",");
            StringBuffer stringBuffer = new StringBuffer();
            for(String sdId:sdIds){
                jobResponsibilitiesDTO.setSdId(Integer.parseInt(sdId));
                String dataSourceName = "";
                SiteData siteData = null;
                if(!IsEmptyUtils.isEmpty(jobj.get(sdId))){
                    siteData = JSON.parseObject(jobj.get(sdId).toString(),SiteData.class);
                }
                jobResponsibilitiesDTO.setPicturePrefix(siteData.getPicturePrefix());
                if(!IsEmptyUtils.isEmpty(siteData)){
                    dataSourceName = siteData.getDateSourceName();
                }
                if(!IsEmptyUtils.isEmpty(dataSourceName)){
                    if(IsEmptyUtils.isEmpty(stringBuffer)){
                        stringBuffer.append(sdId);
                    }else{
                        stringBuffer.append(","+sdId);
                    }
                    logger.info("--"+dataSourceName+"--");
                    DynamicDataSourceContextHolder.setDataSourceType(dataSourceName);
                    Integer totalStatus = jobResponsibilitiesMapper.findJobResponsibilitiesTotal(jobResponsibilitiesDTO);
                    total = total + totalStatus;
                    totalAll.add(totalStatus);
                }
            }
            ArrayList<ArrayList<Integer>> startEndNums = MyStartEndUtil.getStartEndArray(totalAll,startNum,endNum);
            String[] sourceNames = stringBuffer.toString().split(",");
            for(int i=0;i<startEndNums.size();i++){
                jobResponsibilitiesDTO.setSdId(Integer.parseInt(sourceNames[i]));
                SiteData siteData = JSON.parseObject(jobj.get(sourceNames[i]).toString(),SiteData.class);
                jobResponsibilitiesDTO.setPicturePrefix(siteData.getPicturePrefix());
                logger.info("--"+siteData.getDateSourceName()+"--");
                DynamicDataSourceContextHolder.setDataSourceType(siteData.getDateSourceName());
                jobResponsibilitiesDTO.setStart(startEndNums.get(i).get(0));
                jobResponsibilitiesDTO.setEnd(startEndNums.get(i).get(1)-startEndNums.get(i).get(0));
                jobResponsibilitiesDTO.setSdId(Integer.parseInt(sourceNames[i]));
                jobResponsibilitiesDTO.setSiteName(siteData.getName());
                jobResponsibilitiesDTO.setSitePhoto(siteData.getPhoto());

                List<Map<String,Object>> mainLPList = jobResponsibilitiesMapper.findJobResponsibilitiesList(jobResponsibilitiesDTO);
                mainLPLs.addAll(mainLPList);
            }
            DynamicDataSourceContextHolder.setDataSourceType(null);
        }else{
            List<SiteVO> userSites = siteMapper.findUserSites(jobResponsibilitiesDTO.getUserId());
            StringBuffer stringBuffer = new StringBuffer();
            for(SiteVO siteVO:userSites){
                jobResponsibilitiesDTO.setSdId(siteVO.getSdId());
                String dataSourceName = "";
                String sdIdStr = siteVO.getSdId().toString();
                SiteData siteData = null;
                if(!IsEmptyUtils.isEmpty(jobj.get(sdIdStr))){
                    siteData = JSON.parseObject(jobj.get(sdIdStr).toString(),SiteData.class);
                }
                jobResponsibilitiesDTO.setPicturePrefix(siteData.getPicturePrefix());
                if(!IsEmptyUtils.isEmpty(siteData)){
                    dataSourceName = siteData.getDateSourceName();
                }
                if(!IsEmptyUtils.isEmpty(dataSourceName)){
                    if(IsEmptyUtils.isEmpty(stringBuffer)){
                        stringBuffer.append(siteVO.getSdId());
                    }else{
                        stringBuffer.append(","+siteVO.getSdId());
                    }
                    logger.info("null sdIds...--"+dataSourceName+"----");
                    DynamicDataSourceContextHolder.setDataSourceType(dataSourceName);
                    Integer totalStatus = jobResponsibilitiesMapper.findJobResponsibilitiesTotal(jobResponsibilitiesDTO);
                    total = total + totalStatus;
                    totalAll.add(totalStatus);
                }
            }
            ArrayList<ArrayList<Integer>> startEndNums = MyStartEndUtil.getStartEndArray(totalAll,startNum,endNum);
            String[] sourceNames = stringBuffer.toString().split(",");
            for(int i=0;i<startEndNums.size();i++){
                SiteData siteData = JSON.parseObject(jobj.get(sourceNames[i]).toString(),SiteData.class);
                logger.info("--"+siteData.getDateSourceName()+"--");
                jobResponsibilitiesDTO.setPicturePrefix(siteData.getPicturePrefix());
                jobResponsibilitiesDTO.setSdId(Integer.parseInt(sourceNames[i]));
                DynamicDataSourceContextHolder.setDataSourceType(siteData.getDateSourceName());
                jobResponsibilitiesDTO.setStart(startEndNums.get(i).get(0));
                jobResponsibilitiesDTO.setEnd(startEndNums.get(i).get(1)-startEndNums.get(i).get(0));
                jobResponsibilitiesDTO.setSdId(Integer.parseInt(sourceNames[i]));
                jobResponsibilitiesDTO.setSiteName(siteData.getName());
                jobResponsibilitiesDTO.setSitePhoto(siteData.getPhoto());

                List<Map<String,Object>> mainLPList = jobResponsibilitiesMapper.findJobResponsibilitiesList(jobResponsibilitiesDTO);
                mainLPLs.addAll(mainLPList);
            }
            DynamicDataSourceContextHolder.setDataSourceType(null);
        }
        resultBean.setSuccess(true);
        resultBean.setResult(mainLPLs);
        resultBean.setTotal(total);
        resultBean.setResultCode(IStatusMessage.SystemStatus.SUCCESS.getCode());
        resultBean.setMsg(IStatusMessage.SystemStatus.SUCCESS.getMessage());
        return resultBean;
    }
    /**
     * @version V1.0
     * @Title: zhixiangpingtai
     * @Description:
     * @author: hhp
     * @param:  * @param jobResponsibilitiesDTO
     * @date: 2019/10/17 11:24
     */
    @Override
    public ResultBean updateJobResponsibilities(JobResponsibilitiesDTO jobResponsibilitiesDTO) throws Exception {
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
        if(!IsEmptyUtils.isEmpty(jobj.get(jobResponsibilitiesDTO.getSdId().toString()))){
            siteData = JSON.parseObject(jobj.get(jobResponsibilitiesDTO.getSdId().toString()).toString(),SiteData.class);
        }
        String dataSourceName = "";
        if(!IsEmptyUtils.isEmpty(siteData)){
            dataSourceName = siteData.getDateSourceName();
        }
        if(!IsEmptyUtils.isEmpty(dataSourceName)) {
            logger.info("-----" + dataSourceName + "--");
            DynamicDataSourceContextHolder.setDataSourceType(dataSourceName);

            jobResponsibilitiesMapper.updateByPrimaryKey(jobResponsibilitiesDTO);

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
     * @param:  * @param jobResponsibilitiesDTO
     * @date: 2019/10/17 11:23
     */
    @Override
    public ResultBean addJobResponsibilities(JobResponsibilitiesDTO jobResponsibilitiesDTO) throws Exception {
        ResultBean resultBean = new ResultBean();
        jobResponsibilitiesDTO.setAddTime(DateUtils.formatDate(new Date(),"yyyy-MM-dd HH:mm:ss"));
        if(!IsEmptyUtils.isEmpty(jobResponsibilitiesDTO.getSdIds())){
            StringBuffer msgStr = new StringBuffer();
            String[] sdIds = jobResponsibilitiesDTO.getSdIds().split(",");
            for(String sdId:sdIds){
                jobResponsibilitiesDTO.setSdId(Integer.parseInt(sdId));
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
                    logger.info("----" + dataSourceName + "--");
                    DynamicDataSourceContextHolder.setDataSourceType(dataSourceName);
                    jobResponsibilitiesMapper.insertSelective(jobResponsibilitiesDTO);
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
            if(!IsEmptyUtils.isEmpty(jobj.get(jobResponsibilitiesDTO.getSdId().toString()))){
                siteData = JSON.parseObject(jobj.get(jobResponsibilitiesDTO.getSdId().toString()).toString(),SiteData.class);
            }
            String dataSourceName = "";
            if(!IsEmptyUtils.isEmpty(siteData)){
                dataSourceName = siteData.getDateSourceName();
            }
            if(!IsEmptyUtils.isEmpty(dataSourceName)) {
                logger.info("---" + dataSourceName + "--");
                DynamicDataSourceContextHolder.setDataSourceType(dataSourceName);
                jobResponsibilitiesMapper.insertSelective(jobResponsibilitiesDTO);
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
     * @param:  * @param jobResponsibilitiesDTO
     * @date: 2019/10/17 11:23
     */
    @Override
    public ResultBean findUpdJobResponsibilities(JobResponsibilitiesDTO jobResponsibilitiesDTO) throws Exception {
        ResultBean resultBean = new ResultBean();
        StringBuffer stringBufferJson = new StringBuffer();
        InputStream stream = getClass().getClassLoader().getResourceAsStream("json/siteData.json");
        BufferedReader br = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
        String line;
        while ((line = br.readLine()) != null) {
            stringBufferJson.append(line);
        }
        jobResponsibilitiesDTO.setAddTime("a");
        jobResponsibilitiesDTO.setJobTitle("j");
        jobResponsibilitiesDTO.setJobResponsibilities("j");
        jobResponsibilitiesDTO.setImgPath("i");
        JSONObject jobj = JSON.parseObject(stringBufferJson.toString());
        if(!IsEmptyUtils.isEmpty(jobResponsibilitiesDTO.getSdId())){
            String dataSourceName = "";
            SiteData siteData = null;
            if(!IsEmptyUtils.isEmpty(jobj.get(jobResponsibilitiesDTO.getSdId().toString()))){
                siteData = JSON.parseObject(jobj.get(jobResponsibilitiesDTO.getSdId().toString()).toString(),SiteData.class);
            }
            jobResponsibilitiesDTO.setPicturePrefix(siteData.getPicturePrefix());
            if(!IsEmptyUtils.isEmpty(siteData)){
                dataSourceName = siteData.getDateSourceName();
            }
            if(!IsEmptyUtils.isEmpty(dataSourceName)){
                logger.info("--"+dataSourceName+"--");
                DynamicDataSourceContextHolder.setDataSourceType(dataSourceName);

                Map<String,Object> ingredientBaseDTOResult = jobResponsibilitiesMapper.findJobResponsibilities(jobResponsibilitiesDTO);

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
     * @param:  * @param jobResponsibilitiesDTO
     * @date: 2019/10/17 11:23
     */
    @Override
    public ResultBean delJobResponsibilities(JobResponsibilitiesDTO jobResponsibilitiesDTO) throws Exception {
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
        if(!IsEmptyUtils.isEmpty(jobj.get(jobResponsibilitiesDTO.getSdId().toString()))){
            siteData = JSON.parseObject(jobj.get(jobResponsibilitiesDTO.getSdId().toString()).toString(),SiteData.class);
        }
        String dataSourceName = "";
        if(!IsEmptyUtils.isEmpty(siteData)){
            dataSourceName = siteData.getDateSourceName();
        }
        if(!IsEmptyUtils.isEmpty(dataSourceName)) {
            logger.info("---" + dataSourceName + "----");
            DynamicDataSourceContextHolder.setDataSourceType(dataSourceName);

            jobResponsibilitiesMapper.deleteByPrimaryKey(jobResponsibilitiesDTO);
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
     * @param:  * @param jobResponsibilitiesDTO
     * @date: 2019/10/19 11:13
     */
    @Override
    public List<ResultBean> getJobResponsibilities(JobResponsibilitiesDTO jobResponsibilitiesDTO) throws Exception {
        StringBuffer stringBufferJson = new StringBuffer();
        InputStream stream = getClass().getClassLoader().getResourceAsStream("json/siteData.json");
        BufferedReader br = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
        String line;
        while ((line = br.readLine()) != null) {
            stringBufferJson.append(line);
        }
        JSONObject jobj = JSON.parseObject(stringBufferJson.toString());

        List<ResultBean> mainLPLs = Lists.newArrayList();

        ArrayList<Integer> totalAll = Lists.newArrayList();
        if(IsEmptyUtils.isEmpty(jobResponsibilitiesDTO.getJobTitle())){
            jobResponsibilitiesDTO.setJobTitle("j");
        }
        jobResponsibilitiesDTO.setAddTime("a");
        jobResponsibilitiesDTO.setJobResponsibilities("j");
        String exportTopTitle = "岗位职责-"+DateUtils.formatDate(new Date(),"yyyy-MM-dd")+"导出";
        if(!IsEmptyUtils.isEmpty(jobResponsibilitiesDTO.getSdIds())){
            String[] sdIds = jobResponsibilitiesDTO.getSdIds().split(",");
            for(String sdId:sdIds){
                jobResponsibilitiesDTO.setSdId(Integer.parseInt(sdId));
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

                    jobResponsibilitiesDTO.setSiteName(siteData.getName());

                    Map columnWidth = new HashMap();
                    columnWidth.put(0,6000);
                    columnWidth.put(1,20000);
                    columnWidth.put(2,5000);
                    List<List<String>> topTitles = Lists.newArrayList();
                    List<String> col0 = Lists.newArrayList();
                    List<String> col1 = Lists.newArrayList();
                    List<String> col2 = Lists.newArrayList();
                    col0.add(exportTopTitle);col0.add("岗位名称");
                    col1.add(exportTopTitle);col1.add("岗位内容");
                    col2.add(exportTopTitle);col2.add("创建时间");
                    topTitles.add(col0);
                    topTitles.add(col1);
                    topTitles.add(col2);
                    ResultBean resultBean = new ResultBean();
                    resultBean.setMsg(siteData.getName());
                    resultBean.setObj(columnWidth);
                    List<JobResponsibilitiesVO> mainLPList = jobResponsibilitiesMapper.findAllJobResponsibilities(jobResponsibilitiesDTO);
                    resultBean.setRows(topTitles);
                    resultBean.setResult(mainLPList);
                    mainLPLs.add(resultBean);

                }
            }
            DynamicDataSourceContextHolder.setDataSourceType(null);
        }else{
            List<SiteVO> userSites = siteMapper.findUserSites(jobResponsibilitiesDTO.getUserId());
            StringBuffer stringBuffer = new StringBuffer();
            for(SiteVO siteVO:userSites){
                jobResponsibilitiesDTO.setSdId(siteVO.getSdId());
                String dataSourceName = "";
                String sdIdStr = siteVO.getSdId().toString();
                SiteData siteData = null;
                if(!IsEmptyUtils.isEmpty(jobj.get(sdIdStr))){
                    siteData = JSON.parseObject(jobj.get(sdIdStr).toString(),SiteData.class);
                }

                if(!IsEmptyUtils.isEmpty(siteData)){
                    dataSourceName = siteData.getDateSourceName();
                }
                if(!IsEmptyUtils.isEmpty(dataSourceName)){

                    logger.info("--"+siteData.getDateSourceName()+"--");

                    DynamicDataSourceContextHolder.setDataSourceType(dataSourceName);

                    jobResponsibilitiesDTO.setSiteName(siteData.getName());

                    Map columnWidth = new HashMap();
                    columnWidth.put(0,6000);
                    columnWidth.put(1,20000);
                    columnWidth.put(2,5000);
                    List<List<String>> topTitles = Lists.newArrayList();
                    List<String> col0 = Lists.newArrayList();
                    List<String> col1 = Lists.newArrayList();
                    List<String> col2 = Lists.newArrayList();
                    col0.add(exportTopTitle);col0.add("岗位名称");
                    col1.add(exportTopTitle);col1.add("岗位内容");
                    col2.add(exportTopTitle);col2.add("创建时间");
                    topTitles.add(col0);
                    topTitles.add(col1);
                    topTitles.add(col2);
                    ResultBean resultBean = new ResultBean();
                    resultBean.setMsg(siteData.getName());
                    resultBean.setObj(columnWidth);
                    List<JobResponsibilitiesVO> mainLPList = jobResponsibilitiesMapper.findAllJobResponsibilities(jobResponsibilitiesDTO);
                    resultBean.setRows(topTitles);
                    resultBean.setResult(mainLPList);
                    mainLPLs.add(resultBean);
                }
            }
            DynamicDataSourceContextHolder.setDataSourceType(null);
        }
        return mainLPLs;
    }
}
