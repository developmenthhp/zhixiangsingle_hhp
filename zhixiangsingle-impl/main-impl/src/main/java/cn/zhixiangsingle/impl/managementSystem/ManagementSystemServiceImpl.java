package cn.zhixiangsingle.impl.managementSystem;

import cn.zhixiangsingle.config.DynamicDataSourceContextHolder;
import cn.zhixiangsingle.dao.managementSystem.ManagementSystemMapper;
import cn.zhixiangsingle.dao.site.SiteMapper;
import cn.zhixiangsingle.date.DateUtils;
import cn.zhixiangsingle.entity.base.IsEmptyUtils;
import cn.zhixiangsingle.entity.base.site.SiteData;
import cn.zhixiangsingle.entity.base.site.vo.SiteVO;
import cn.zhixiangsingle.entity.managementSystem.dto.ManagementSystemDTO;
import cn.zhixiangsingle.entity.managementSystem.vo.ManagementSystemVO;
import cn.zhixiangsingle.pagination.MyStartEndUtil;
import cn.zhixiangsingle.service.managementSystem.ManagementSystemService;
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
 * @Package cn.zhixiangsingle.impl.managementSystem
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019/10/16 14:23
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Service(version = "1.0.0", interfaceClass = ManagementSystemService.class)
public class ManagementSystemServiceImpl implements ManagementSystemService {
    private static final Logger logger = LoggerFactory
            .getLogger(ManagementSystemServiceImpl.class);
    @Autowired
    private SiteMapper siteMapper;
    @Autowired
    private ManagementSystemMapper managementSystemMapper;
    /**
     * @version V1.0
     * @Title: zhixiangpingtai
     * @Description:
     * @author: hhp
     * @param:  * @param managementSystemDTO
     * @date: 2019/10/16 15:23
     */
    @Override
    public ResultBean findManagementSystemList(ManagementSystemDTO managementSystemDTO) throws Exception {
        ResultBean resultBean = new ResultBean();
        if(IsEmptyUtils.isEmpty(managementSystemDTO.getPage())){
            managementSystemDTO.setPage(1);
        }
        if (IsEmptyUtils.isEmpty(managementSystemDTO.getLimit())){
            managementSystemDTO.setLimit(10);
        }
        Integer page = managementSystemDTO.getPage();
        Integer limit = managementSystemDTO.getLimit();
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
        managementSystemDTO.setId(-1);
        managementSystemDTO.setContentDescribe("c");
        managementSystemDTO.setExecutionTime("e");
        managementSystemDTO.setImgPath("i");
        if(IsEmptyUtils.isEmpty(managementSystemDTO.getSystemName())){
            managementSystemDTO.setSystemName("s");
        }
        if(!IsEmptyUtils.isEmpty(managementSystemDTO.getSdIds())){
            String[] sdIds = managementSystemDTO.getSdIds().split(",");
            StringBuffer stringBuffer = new StringBuffer();
            for(String sdId:sdIds){
                managementSystemDTO.setSdId(Integer.parseInt(sdId));
                String dataSourceName = "";
                SiteData siteData = null;
                if(!IsEmptyUtils.isEmpty(jobj.get(sdId))){
                    siteData = JSON.parseObject(jobj.get(sdId).toString(),SiteData.class);
                }
                managementSystemDTO.setPicturePrefix(siteData.getPicturePrefix());
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
                    Integer totalStatus = managementSystemMapper.findManagementSystemTotal(managementSystemDTO);
                    total = total + totalStatus;
                    totalAll.add(totalStatus);
                }
            }
            ArrayList<ArrayList<Integer>> startEndNums = MyStartEndUtil.getStartEndArray(totalAll,startNum,endNum);
            String[] sourceNames = stringBuffer.toString().split(",");
            for(int i=0;i<startEndNums.size();i++){
                managementSystemDTO.setSdId(Integer.parseInt(sourceNames[i]));
                SiteData siteData = JSON.parseObject(jobj.get(sourceNames[i]).toString(),SiteData.class);
                managementSystemDTO.setPicturePrefix(siteData.getPicturePrefix());
                logger.info("--"+siteData.getDateSourceName()+"--");
                DynamicDataSourceContextHolder.setDataSourceType(siteData.getDateSourceName());
                managementSystemDTO.setStart(startEndNums.get(i).get(0));
                managementSystemDTO.setEnd(startEndNums.get(i).get(1)-startEndNums.get(i).get(0));
                managementSystemDTO.setSdId(Integer.parseInt(sourceNames[i]));
                managementSystemDTO.setSiteName(siteData.getName());
                managementSystemDTO.setSitePhoto(siteData.getPhoto());

                List<Map<String,Object>> mainLPList = managementSystemMapper.findManagementSystemList(managementSystemDTO);
                mainLPLs.addAll(mainLPList);
            }
            DynamicDataSourceContextHolder.setDataSourceType(null);
        }else{
            List<SiteVO> userSites = siteMapper.findUserSites(managementSystemDTO.getUserId());
            StringBuffer stringBuffer = new StringBuffer();
            for(SiteVO siteVO:userSites){
                managementSystemDTO.setSdId(siteVO.getSdId());
                String dataSourceName = "";
                String sdIdStr = siteVO.getSdId().toString();
                SiteData siteData = null;
                if(!IsEmptyUtils.isEmpty(jobj.get(sdIdStr))){
                    siteData = JSON.parseObject(jobj.get(sdIdStr).toString(),SiteData.class);
                }
                managementSystemDTO.setPicturePrefix(siteData.getPicturePrefix());
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
                    Integer totalStatus = managementSystemMapper.findManagementSystemTotal(managementSystemDTO);
                    total = total + totalStatus;
                    totalAll.add(totalStatus);
                }
            }
            ArrayList<ArrayList<Integer>> startEndNums = MyStartEndUtil.getStartEndArray(totalAll,startNum,endNum);
            String[] sourceNames = stringBuffer.toString().split(",");
            for(int i=0;i<startEndNums.size();i++){
                SiteData siteData = JSON.parseObject(jobj.get(sourceNames[i]).toString(),SiteData.class);
                logger.info("--"+siteData.getDateSourceName()+"--");
                managementSystemDTO.setPicturePrefix(siteData.getPicturePrefix());
                managementSystemDTO.setSdId(Integer.parseInt(sourceNames[i]));
                DynamicDataSourceContextHolder.setDataSourceType(siteData.getDateSourceName());
                managementSystemDTO.setStart(startEndNums.get(i).get(0));
                managementSystemDTO.setEnd(startEndNums.get(i).get(1)-startEndNums.get(i).get(0));
                managementSystemDTO.setSdId(Integer.parseInt(sourceNames[i]));
                managementSystemDTO.setSiteName(siteData.getName());
                managementSystemDTO.setSitePhoto(siteData.getPhoto());

                List<Map<String,Object>> mainLPList = managementSystemMapper.findManagementSystemList(managementSystemDTO);
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
     * @param:  * @param managementSystemDTO
     * @date: 2019/10/16 15:24
     */
    @Override
    public ResultBean updateManagementSystem(ManagementSystemDTO managementSystemDTO) throws Exception {
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
        if(!IsEmptyUtils.isEmpty(jobj.get(managementSystemDTO.getSdId().toString()))){
            siteData = JSON.parseObject(jobj.get(managementSystemDTO.getSdId().toString()).toString(),SiteData.class);
        }
        String dataSourceName = "";
        if(!IsEmptyUtils.isEmpty(siteData)){
            dataSourceName = siteData.getDateSourceName();
        }
        if(!IsEmptyUtils.isEmpty(dataSourceName)) {
            logger.info("-" + dataSourceName + "--");
            DynamicDataSourceContextHolder.setDataSourceType(dataSourceName);

            managementSystemMapper.updateByPrimaryKey(managementSystemDTO);

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
     * @param:  * @param managementSystemDTO
     * @date: 2019/10/16 15:32
     */
    @Override
    public ResultBean addManagementSystem(ManagementSystemDTO managementSystemDTO) throws Exception {
        ResultBean resultBean = new ResultBean();
        managementSystemDTO.setExecutionTime(DateUtils.formatDate(new Date(),"yyyy-MM-dd HH:mm:ss"));
        if(!IsEmptyUtils.isEmpty(managementSystemDTO.getSdIds())){
            StringBuffer msgStr = new StringBuffer();
            String[] sdIds = managementSystemDTO.getSdIds().split(",");
            for(String sdId:sdIds){
                managementSystemDTO.setSdId(Integer.parseInt(sdId));
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
                    managementSystemMapper.insertSelective(managementSystemDTO);
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
            if(!IsEmptyUtils.isEmpty(jobj.get(managementSystemDTO.getSdId().toString()))){
                siteData = JSON.parseObject(jobj.get(managementSystemDTO.getSdId().toString()).toString(),SiteData.class);
            }
            String dataSourceName = "";
            if(!IsEmptyUtils.isEmpty(siteData)){
                dataSourceName = siteData.getDateSourceName();
            }
            if(!IsEmptyUtils.isEmpty(dataSourceName)) {
                logger.info("--" + dataSourceName + "--");
                DynamicDataSourceContextHolder.setDataSourceType(dataSourceName);
                managementSystemMapper.insertSelective(managementSystemDTO);
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
     * @param:  * @param managementSystemDTO
     * @date: 2019/10/16 15:32
     */
    @Override
    public ResultBean findUpdManagementSystem(ManagementSystemDTO managementSystemDTO) throws Exception {
        ResultBean resultBean = new ResultBean();
        StringBuffer stringBufferJson = new StringBuffer();
        InputStream stream = getClass().getClassLoader().getResourceAsStream("json/siteData.json");
        BufferedReader br = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
        String line;
        while ((line = br.readLine()) != null) {
            stringBufferJson.append(line);
        }
        managementSystemDTO.setContentDescribe("c");
        managementSystemDTO.setExecutionTime("e");
        managementSystemDTO.setImgPath("i");
        managementSystemDTO.setSystemName("s");
        JSONObject jobj = JSON.parseObject(stringBufferJson.toString());
        if(!IsEmptyUtils.isEmpty(managementSystemDTO.getSdId())){
            String dataSourceName = "";
            SiteData siteData = null;
            if(!IsEmptyUtils.isEmpty(jobj.get(managementSystemDTO.getSdId().toString()))){
                siteData = JSON.parseObject(jobj.get(managementSystemDTO.getSdId().toString()).toString(),SiteData.class);
            }
            managementSystemDTO.setPicturePrefix(siteData.getPicturePrefix());
            if(!IsEmptyUtils.isEmpty(siteData)){
                dataSourceName = siteData.getDateSourceName();
            }
            if(!IsEmptyUtils.isEmpty(dataSourceName)){
                logger.info("--"+dataSourceName+"--");
                DynamicDataSourceContextHolder.setDataSourceType(dataSourceName);

                Map<String,Object> ingredientBaseDTOResult = managementSystemMapper.findManagementSystem(managementSystemDTO);

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
     * @param:  * @param managementSystemDTO
     * @date: 2019/10/16 15:33
     */
    @Override
    public ResultBean delManagementSystem(ManagementSystemDTO managementSystemDTO) throws Exception {
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
        if(!IsEmptyUtils.isEmpty(jobj.get(managementSystemDTO.getSdId().toString()))){
            siteData = JSON.parseObject(jobj.get(managementSystemDTO.getSdId().toString()).toString(),SiteData.class);
        }
        String dataSourceName = "";
        if(!IsEmptyUtils.isEmpty(siteData)){
            dataSourceName = siteData.getDateSourceName();
        }
        if(!IsEmptyUtils.isEmpty(dataSourceName)) {
            logger.info("-" + dataSourceName + "----");
            DynamicDataSourceContextHolder.setDataSourceType(dataSourceName);

            managementSystemMapper.deleteByPrimaryKey(managementSystemDTO);
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
     * @param:  * @param managementSystemDTO
     * @date: 2019/10/19 11:13
     */
    @Override
    public List<ResultBean> getManagementSystems(ManagementSystemDTO managementSystemDTO) throws Exception {
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
        if(IsEmptyUtils.isEmpty(managementSystemDTO.getSystemName())){
            managementSystemDTO.setSystemName("s");
        }
        managementSystemDTO.setExecutionTime("e");
        managementSystemDTO.setContentDescribe("c");
        String exportTopTitle = "管理制度-"+DateUtils.formatDate(new Date(),"yyyy-MM-dd")+"导出";
        if(!IsEmptyUtils.isEmpty(managementSystemDTO.getSdIds())){
            String[] sdIds = managementSystemDTO.getSdIds().split(",");
            for(String sdId:sdIds){
                managementSystemDTO.setSdId(Integer.parseInt(sdId));
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

                    managementSystemDTO.setSiteName(siteData.getName());

                    Map columnWidth = new HashMap();
                    columnWidth.put(0,6000);
                    columnWidth.put(1,20000);
                    columnWidth.put(2,5000);
                    List<List<String>> topTitles = Lists.newArrayList();
                    List<String> col0 = Lists.newArrayList();
                    List<String> col1 = Lists.newArrayList();
                    List<String> col2 = Lists.newArrayList();
                    col0.add(exportTopTitle);col0.add("制度名称");
                    col1.add(exportTopTitle);col1.add("制度内容");
                    col2.add(exportTopTitle);col2.add("创建时间");
                    topTitles.add(col0);
                    topTitles.add(col1);
                    topTitles.add(col2);
                    ResultBean resultBean = new ResultBean();
                    resultBean.setMsg(siteData.getName());
                    resultBean.setObj(columnWidth);
                    List<ManagementSystemVO> mainLPList = managementSystemMapper.findAllManagementSystem(managementSystemDTO);
                    resultBean.setRows(topTitles);
                    resultBean.setResult(mainLPList);
                    mainLPLs.add(resultBean);

                }
            }
            DynamicDataSourceContextHolder.setDataSourceType(null);
        }else{
            List<SiteVO> userSites = siteMapper.findUserSites(managementSystemDTO.getUserId());
            StringBuffer stringBuffer = new StringBuffer();
            for(SiteVO siteVO:userSites){
                managementSystemDTO.setSdId(siteVO.getSdId());
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

                    managementSystemDTO.setSiteName(siteData.getName());

                    Map columnWidth = new HashMap();
                    columnWidth.put(0,6000);
                    columnWidth.put(1,20000);
                    columnWidth.put(2,5000);
                    List<List<String>> topTitles = Lists.newArrayList();
                    List<String> col0 = Lists.newArrayList();
                    List<String> col1 = Lists.newArrayList();
                    List<String> col2 = Lists.newArrayList();
                    col0.add(exportTopTitle);col0.add("制度名称");
                    col1.add(exportTopTitle);col1.add("制度内容");
                    col2.add(exportTopTitle);col2.add("创建时间");
                    topTitles.add(col0);
                    topTitles.add(col1);
                    topTitles.add(col2);
                    ResultBean resultBean = new ResultBean();
                    resultBean.setMsg(siteData.getName());
                    resultBean.setObj(columnWidth);
                    List<ManagementSystemVO> mainLPList = managementSystemMapper.findAllManagementSystem(managementSystemDTO);
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
